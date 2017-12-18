package br.com.dusty.dkits

import br.com.dusty.dkits.ability.Abilities
import br.com.dusty.dkits.command.Commands
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.listener.Listeners
import br.com.dusty.dkits.warp.Warps
import com.google.gson.GsonBuilder
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

		data.serverStatus = EnumServerStatus.ONLINE
	}

	override fun onDisable() {
		Warps.WARPS.forEach { it.finalize() }
	}

	companion object {

		/**
		 * **Singleton** deste [JavaPlugin].
		 */
		lateinit var INSTANCE: Main

		//TODO: public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

//		val PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager()

		/*var REGION_MANAGER: RegionManager? = null
			get() {
				if (field == null) field = WGBukkit.getPlugin().getRegionManager(Bukkit.getWorlds()[0])

				return field
			}*/ //TODO: 1.8 switch

		val RANDOM = Random()
		val GSON = GsonBuilder().setPrettyPrinting().create()

		val CONFIG_DIR = File(Bukkit.getWorldContainer(), "config")

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

	data class Data(var slots: Int = 120, var serverStatus: EnumServerStatus = EnumServerStatus.OFFLINE)
}
