package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Tasks
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
	var warpType = EnumWarpType.GAME

	var spawn: Location? = null
		get() {
			if (field == null) field = Location(Bukkit.getWorlds()[0], data.spawn[0].toDouble(), data.spawn[1].toDouble(), data.spawn[2].toDouble())

			return field
		}
		set(location) {
			if (location != null) {
				field = location

				data.spawn[0] = location.x.toFloat()
				data.spawn[1] = location.y.toFloat()
				data.spawn[2] = location.z.toFloat()
			}
		}

	var entryKit: Kit = Kits.NONE
	var enabledKits = HashSet<Kit>()

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
		val file = File(dir, name.toLowerCase() + ".json")

		if (file.exists()) data = Main.GSON.fromJson(FileReader(file), Warp.Data::class.java)
		else saveData()

		if (data.isListEnabledKits) data.kits.mapNotNull { Kits[it] }.filter { it.data.isEnabled }.forEach { enabledKits.add(it) }
		else Kits.KITS.filter { it.data.isEnabled && !data.kits.contains(it.name) }.forEach { enabledKits.add(it) }
	}

	fun saveData() {
		val dir = File(Main.CONFIG_DIR, "warp")
		val file = File(dir, name.toLowerCase() + ".json")

		dir.mkdirs()
		file.createNewFile()

		val printWriter = PrintWriter(file)
		printWriter.println(Main.GSON.toJson(data))
		printWriter.close()
	}

	fun receiveGamer(gamer: Gamer) {
		val player = gamer.player

		player.teleport(spawn!!.spread(data.spreadRange))
		player.sendMessage(Text.positivePrefix().basic("Você foi ").positive("teleportado").basic(" para a warp ").positive(name).basic("!").toString())
		//TODO: Titles/subtitles for 1.8+ players

		gamer.kit = entryKit
		entryKit.apply(gamer)
	}

	private class GameWarpKit: Kit() {

		init {
			items = arrayOf(ItemStack(Material.CHEST).rename(Text.of("Kits").color(TextColor.GOLD).toString()),
			                null,
			                null,
			                null,
			                ItemStack(Material.EMERALD).rename(Text.of("Shop").color(TextColor.GOLD).toString()),
			                null,
			                null,
			                null,
			                ItemStack(Material.MAP).rename(Text.of("Warps").color(TextColor.GOLD).toString()))
		}
	}

	private class EventWarpKit: Kit() {

		init {
			items = arrayOf(null, null, null, null, null, null, null, null, ItemStack(Material.MAP).rename(Text.of("Warps").color(TextColor.GOLD).toString()))
		}
	}

	enum class EnumWarpType {
		GAME,
		EVENT
	}

	data class Data(var isEnabled: Boolean = false,
	                var isListEnabledKits: Boolean = true,
	                var kits: Array<String> = arrayOf(),
	                var spawn: Array<Float> = arrayOf(0f, 0f, 0f),
	                var spawnRadius: Float = 0f,
	                var spreadRange: Float = 0f)

	companion object {

		internal val GAME_WARP_KIT: Kit = GameWarpKit()
		internal val EVENT_WARP_KIT: Kit = EventWarpKit()
	}
}
