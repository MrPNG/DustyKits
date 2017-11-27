package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import java.util.*

object Warps {

	val NONE = NoneWarp
	val ARENA = ArenaWarp
	val LOBBY = LobbyWarp

	val WARPS = ArrayList<Warp>()

	lateinit var enabledWarpsNames: Array<String>

	fun registerAll() {
		//Usage: WARPS.add(FOO);

		WARPS.add(ARENA)
		WARPS.add(LOBBY)

		enabledWarpsNames = Warps.WARPS.filter { it.data.isEnabled }.map { it.name.toLowerCase() }.toTypedArray()

		val pluginManager = Bukkit.getPluginManager()
		WARPS.forEach { warp -> pluginManager.registerEvents(warp, Main.INSTANCE) }
	}

	operator fun get(name: String): Warp = WARPS.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } ?: NONE

	operator fun get(icon: ItemStack): Warp = WARPS.firstOrNull { it.icon == icon } ?: NONE
}
