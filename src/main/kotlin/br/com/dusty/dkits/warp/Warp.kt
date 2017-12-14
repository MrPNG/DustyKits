package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.*
import br.com.dusty.dkits.util.inventory.addItemStacks
import br.com.dusty.dkits.util.inventory.fillRecraft
import br.com.dusty.dkits.util.inventory.fillSoups
import br.com.dusty.dkits.util.inventory.setArmor
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

open class Warp: Listener {

	var name = "None"
	var description = ""
	var icon = ItemStack(Material.STONE_SWORD)
	var type = EnumWarpType.GAME

	var aliases = arrayOf<String>()

	var overriddenEvents = arrayOf<Class<out Event>>()

	var entryKit: Kit = GAME_WARP_KIT
	var enabledKits = HashSet<Kit>()

	var hasShop = false

	var durabilityBehavior = EnumDurabilityBehavior.REGEN

	var spawn = Locations.GENERIC
		get() {
			if (field == Locations.GENERIC) field = data.spawn.toLocation(Bukkit.getWorlds()[0])

			return field
		}
		set(location) {
			field = location

			data.spawn = location.toSimpleLocation()

			saveData()
		}

	var data = Data()

	fun overrides(event: Event): Boolean = event.javaClass in overriddenEvents

	fun enable(enabled: Boolean): Boolean {
		if (data.isEnabled == enabled) return false

		data.isEnabled = enabled

		Tasks.async(Runnable { this.saveData() })

		return true
	}

	fun enableKit(kit: Kit, enable: Boolean): Boolean {
		val b: Boolean = if (enable) enabledKits.add(kit) else enabledKits.remove(kit)

		data.kits = (if (data.isListEnabledKits) enabledKits else Kits.KITS.filter { it !in enabledKits }).map { it.name }.toTypedArray()

		Tasks.async(Runnable { this.saveData() })

		return b
	}

	fun loadData() {
		val dir = File(Main.CONFIG_DIR, "warp")
		val file = File(dir, name.toLowerCase().replace(" ", "_") + ".json")

		if (file.exists()) data = Main.GSON.fromJson(FileReader(file), data.javaClass)

		enabledKits.addAll(Kits.KITS.filter { it.data.isEnabled && if (data.isListEnabledKits) it.name in data.kits else it.name !in data.kits })

		saveData()
	}

	fun saveData() {
		val dir = File(Main.CONFIG_DIR, "warp")
		val file = File(dir, name.toLowerCase().replace(" ", "_") + ".json")

		dir.mkdirs()
		file.createNewFile()

		val printWriter = PrintWriter(file)
		printWriter.println(Main.GSON.toJson(data))
		printWriter.close()
	}

	fun loadLocation(world: World, coordinates: Array<Double>) = Location(world, coordinates[0], coordinates[1], coordinates[2])

	fun saveLocation(location: Location, coordinates: Array<Double>) = location.normalize().apply {
		coordinates[0] = x
		coordinates[1] = y
		coordinates[2] = z

		saveData()
	}

	open fun isAllowed(kit: Kit, gamer: Gamer, announce: Boolean): Boolean = when {
		gamer.mode != EnumMode.ADMIN && !gamer.kit.isDummy         -> {
			if (announce) gamer.player.sendMessage(Text.negativePrefix().basic("Você ").negative("já").basic(" está ").negative("usando").basic(" um kit!").toString())

			false
		}
		gamer.mode != EnumMode.ADMIN && !enabledKits.contains(kit) -> {
			if (announce) gamer.player.sendMessage(Text.negativePrefix().basic("Você ").negative("não pode").basic(" usar o kit ").negative(kit.name).basic(" nesta warp!").toString())

			false
		}
		gamer.mode != EnumMode.ADMIN && !gamer.hasKit(kit)         -> {
			if (announce) gamer.player.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" possui o kit ").negative(kit.name).basic("!").toString())

			false
		}
		else                                                       -> true
	}

	open fun applyKit(gamer: Gamer, kit: Kit) {
		gamer.clear()
		gamer.player.run {
			inventory.setItem(0, kit.weapon)
			inventory.addItemStacks(kit.items)
			setArmor(kit.armor)

			if (!kit.isDummy) {
				fillRecraft()
				fillSoups(true)
			}
		}
	}

	open fun execute(gamer: Gamer, alias: String, args: Array<String>) {}

	open fun receiveGamer(gamer: Gamer, announce: Boolean) {
		val player = gamer.player

		player.teleport(spawn.spread(data.spreadRange))

		if (announce) {
			player.sendMessage(Text.positivePrefix().basic("Você foi ").positive("teleportado").basic(" para a warp ").positive(name).basic("!").toString())

			if (gamer.protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) player.sendTitle(Text.basicOf("Você está na warp ").positive(name).basic("!").toString(),
			                                                                                                   if (enabledKits.isNotEmpty()) Text.basicOf("Escolha um ").positive("kit").basic(" e ").positive(
					                                                                                                   "divirta-se").basic("!").toString() else null,
			                                                                                                   10,
			                                                                                                   80,
			                                                                                                   10)
		}

		gamer.setKitAndApply(entryKit, false)
	}

	open fun dispatchGamer(gamer: Gamer, newWarp: Warp) {}

	open fun finalize(){}

	override fun equals(other: Any?) = when {
		this === other                -> true
		javaClass == other?.javaClass -> true
		else                          -> false
	}

	override fun toString(): String {
		return "Warp(name='$name')"
	}

	enum class EnumWarpType {
		GAME,
		EVENT,
		BOTH
	}

	enum class EnumDurabilityBehavior {
		REGEN,
		REGEN_ON_KILL,
		BREAK
	}

	data class SimpleLocation(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0, var yaw: Float = 0F, var pitch: Float = 0F) {

		fun toLocation(world: World) = Location(world, x, y, z, yaw, pitch)
	}

	open class Data(var isEnabled: Boolean = false,
	                var isListEnabledKits: Boolean = true,
	                var kits: Array<String> = arrayOf(),
	                var spawn: SimpleLocation = SimpleLocation(),
	                var spawnRadius: Double = 0.0,
	                var spreadRange: Double = 0.0)

	companion object {

		val GAME_WARP_KIT = Kit(items = arrayOf(ItemStack(Material.CHEST).rename(Text.of("Kits").color(TextColor.GOLD).toString()),
		                                        null,
		                                        null,
		                                        null,
		                                        ItemStack(Material.EMERALD).rename(Text.of("Shop").color(TextColor.GOLD).toString()),
		                                        null,
		                                        null,
		                                        null,
		                                        ItemStack(Material.EMPTY_MAP).rename(Text.of("Warps").color(TextColor.GOLD).toString())))

		val SIMPLE_GAME_WARP_KIT = Kit(items = arrayOf(GAME_WARP_KIT.items[0], null, null, null, null, null, null, null, GAME_WARP_KIT.items[8]))

		val EVENT_WARP_KIT = Kit(items = arrayOf(null, null, null, null, null, null, null, null, GAME_WARP_KIT.items[8]))
	}
}
