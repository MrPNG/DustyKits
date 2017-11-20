package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import org.bukkit.Bukkit
import java.util.*

object Warps {

	val NONE = NoneWarp
	val ARENA = ArenaWarp
	val LOBBY = LobbyWarp

	val WARPS = ArrayList<Warp>()

	fun registerAll() {
		//Usage: WARPS.add(FOO);

		WARPS.add(ARENA)
		WARPS.add(LOBBY)

		val pluginManager = Bukkit.getPluginManager()
		WARPS.forEach { warp -> pluginManager.registerEvents(warp, Main.INSTANCE) }
	}

	operator fun get(name: String): Warp = WARPS.firstOrNull { it.name.toLowerCase().startsWith(name.toLowerCase()) } ?: NONE
}
