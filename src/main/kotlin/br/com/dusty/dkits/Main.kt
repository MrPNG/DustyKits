package br.com.dusty.dkits

import br.com.dusty.dkits.ability.Abilities
import br.com.dusty.dkits.command.Commands
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.listener.Listeners
import br.com.dusty.dkits.warp.Warps
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.google.gson.Gson
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class Main: JavaPlugin() {

	init {
		INSTANCE = this

		CONFIG_DIR.mkdirs()
	}

	override fun onLoad() {}

	override fun onEnable() {
		Kits.registerAll()
		Warps.registerAll()
		Commands.registerAll()
		Listeners.registerAll()
		Abilities.registerAll()

		serverStatus = EnumServerStatus.ONLINE
	}

	override fun onDisable() {}

	companion object {

		/**
		 * **Singleton** deste [JavaPlugin].
		 */
		lateinit var INSTANCE: Main

		//TODO: public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

		val PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager()

		val RANDOM = Random()
		val GSON = Gson()

		val CONFIG_DIR = File(Bukkit.getWorldContainer(), "config")

		val MAX_PLAYERS = 150

		var serverStatus = EnumServerStatus.OFFLINE
	}
}
