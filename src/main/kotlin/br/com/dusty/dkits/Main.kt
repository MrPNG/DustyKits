package br.com.dusty.dkits

import br.com.dusty.dkits.ability.Abilities
import br.com.dusty.dkits.command.Commands
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.listener.Listeners
import br.com.dusty.dkits.util.leaderboard.Leaderboards
import br.com.dusty.dkits.warp.Warps
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.google.gson.GsonBuilder
import com.sk89q.worldguard.bukkit.WGBukkit
import com.sk89q.worldguard.protection.managers.RegionManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

class Main: JavaPlugin() {

	init {
		INSTANCE = this

		CONFIG_DIR.mkdirs()
	}

	override fun onLoad() {

	}

	override fun onEnable() {
		Kits.registerAll()
		Warps.registerAll()
		Commands.registerAll()
		Listeners.registerAll()
		Abilities.registerAll()
		Leaderboards.registerAll()

		data.serverStatus = EnumServerStatus.ONLINE
	}

	override fun onDisable() {
		Warps.WARPS.forEach { it.finalize() }

		saveData()
	}

	companion object {

		/**
		 * **Singleton** deste [JavaPlugin].
		 */
		lateinit var INSTANCE: Main

		//TODO: public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

		var PROTOCOL_MANAGER: ProtocolManager? = null
			get() {
				if (field == null) field = ProtocolLibrary.getProtocolManager()

				return field
			}

		var REGION_MANAGER: RegionManager? = null
			get() {
				if (field == null) field = WGBukkit.getPlugin().getRegionManager(Main.WORLD)

				return field
			}

		val WORLD
			get() = Bukkit.getWorlds()[0]

		val RANDOM = Random()
		val GSON = GsonBuilder().setPrettyPrinting().create()

		val CONFIG_DIR = File(Bukkit.getWorldContainer(), "config").apply { if (!exists()) mkdirs() }

		var data = Data()

		fun loadData() {
			val file = File(CONFIG_DIR, "config.json")

			if (file.exists()) data = Main.GSON.fromJson(FileReader(file), data.javaClass)

			saveData()
		}

		fun saveData() {
			val file = File(CONFIG_DIR, "config.json")
			file.createNewFile()

			PrintWriter(file).use { it.println(Main.GSON.toJson(data)) }
		}
	}

	data class Data(var slots: Int = 120, var serverStatus: EnumServerStatus = EnumServerStatus.OFFLINE, var soups: Int = 0)
}
