package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Locations
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.inventory.addItemStacks
import br.com.dusty.dkits.util.inventory.fillRecraft
import br.com.dusty.dkits.util.inventory.fillSoups
import br.com.dusty.dkits.util.inventory.setArmor
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.spread
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
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

	var spawn = Locations.GENERIC
		get() {
			if (field == Locations.GENERIC) field = Location(Bukkit.getWorlds()[0], data.spawn[0].toDouble(), data.spawn[1].toDouble(), data.spawn[2].toDouble())

			return field
		}
		set(location) {
			field = location

			data.spawn[0] = location.x.toFloat()
			data.spawn[1] = location.y.toFloat()
			data.spawn[2] = location.z.toFloat()

			saveData()
		}

	var entryKit: Kit = Kits.NONE
	var enabledKits = HashSet<Kit>()

	var hasShop = false

	var durabilityBehavior = EnumDurabilityBehavior.REGEN

	var data = Data()

	fun enable(enabled: Boolean): Boolean {
		if (data.isEnabled == enabled) return false

		data.isEnabled = enabled

		Tasks.async(Runnable { this.saveData() })

		return true
	}

	fun enableKit(kit: Kit, enable: Boolean): Boolean {
		val b: Boolean = if (enable) enabledKits.add(kit) else enabledKits.remove(kit)

		if (data.isListEnabledKits) data.kits = enabledKits.map { it.name }.toTypedArray()
		else data.kits = Kits.KITS.filter { !enabledKits.contains(it) }.map { it.name }.toTypedArray()

		Tasks.async(Runnable { this.saveData() })

		return b
	}

	fun loadData() {
		val dir = File(Main.CONFIG_DIR, "warp")
		val file = File(dir, name.toLowerCase().replace(" ", "_") + ".json")

		if (file.exists()) data = Main.GSON.fromJson(FileReader(file), data.javaClass)

		if (data.isListEnabledKits) data.kits.mapNotNull { Kits[it] }.filter { it.data.isEnabled }.forEach { enabledKits.add(it) }
		else Kits.KITS.filter { it.data.isEnabled && !data.kits.contains(it.name) }.forEach { enabledKits.add(it) }

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

	open fun applyKit(gamer: Gamer, kit: Kit) {
		gamer.clear()
		gamer.player.run {
			inventory.setItem(0, kit.weapon)
			inventory.addItemStacks(kit.items)
			setArmor(kit.armor)

			if (!kit.isDummy) {
				fillRecraft()
				fillSoups()
			}
		}
	}

	fun receiveGamer(gamer: Gamer, announce: Boolean) {
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

	override fun equals(other: Any?) = when {
		this === other                -> true
		javaClass != other?.javaClass -> false
		else                          -> true
	}

	override fun toString(): String {
		return "Warp(name='$name')"
	}

	object GameWarpKit: Kit() {

		init {
			items = arrayOf(ItemStack(Material.CHEST).rename(Text.of("Kits").color(TextColor.GOLD).toString()),
			                null,
			                null,
			                null,
			                ItemStack(Material.EMERALD).rename(Text.of("Shop").color(TextColor.GOLD).toString()),
			                null,
			                null,
			                null,
			                ItemStack(Material.EMPTY_MAP).rename(Text.of("Warps").color(TextColor.GOLD).toString()))
		}
	}

	object SimpleGameWarpKit: Kit() {

		init {
			items = arrayOf(GameWarpKit.items[0], null, null, null, null, null, null, null, GameWarpKit.items[8])
		}
	}

	object EventWarpKit: Kit() {

		init {
			items = arrayOf(null, null, null, null, null, null, null, null, ItemStack(Material.EMPTY_MAP).rename(Text.of("Warps").color(TextColor.GOLD).toString()))
		}
	}

	enum class EnumWarpType {
		GAME,
		EVENT
	}

	enum class EnumDurabilityBehavior {
		REGEN,
		REGEN_ON_KILL,
		BREAK
	}

	open class Data(var isEnabled: Boolean = false,
	                var isListEnabledKits: Boolean = true,
	                var kits: Array<String> = arrayOf(),
	                var spawn: Array<Float> = arrayOf(0F, 0F, 0F),
	                var spawnRadius: Float = 0F,
	                var spreadRange: Float = 0F)
}
