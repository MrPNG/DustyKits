package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import org.bukkit.Bukkit

import java.util.ArrayList

object Warps {

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

	fun byName(name: String): Warp? {
		return WARPS.firstOrNull { it.name.toLowerCase().startsWith(name.toLowerCase()) }
	}
}
