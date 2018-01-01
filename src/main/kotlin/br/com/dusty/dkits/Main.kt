package br.com.dusty.dkits

import br.com.dusty.dkits.ability.Abilities
import br.com.dusty.dkits.command.Commands
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.leaderboard.Leaderboards
import br.com.dusty.dkits.listener.Listeners
import br.com.dusty.dkits.util.Tags
import br.com.dusty.dkits.warp.Warps
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Main: JavaPlugin() {

	init {
		INSTANCE = this
	}

	override fun onLoad() {}

	override fun onEnable() {
		Config.loadData()

		Kits.registerAll()
		Warps.registerAll()
		Commands.registerAll()
		Listeners.registerAll()
		Abilities.registerAll()
		Leaderboards.registerAll()

		Tags.registerPacketListener()

		Config.data.serverStatus = Config.EnumServerStatus.ONLINE
	}

	override fun onDisable() {
		Config.saveData()

		Warps.WARPS.forEach { it.finalize() }
	}

	companion object {

		/**
		 * **Singleton** deste [JavaPlugin].
		 */
		lateinit var INSTANCE: Main

		//TODO: public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

		val RANDOM = Random()
	}
}
