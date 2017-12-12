package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

object Warps {

	val NONE = NoneWarp
	val ARENA = ArenaWarp
	val FEAST = FeastWarp
	val FPS = FpsWarp
	val HG = HGWarp
	val LAVA_CHALLENGE = LavaChallengeWarp
	val LOBBY = LobbyWarp
	val ONE_VS_ONE = OneVsOneWarp
	val VOLCANO = VolcanoWarp

	val WARPS = arrayListOf<Warp>()

	lateinit var enabledWarpsNames: Array<String>

	fun registerAll() {
		//Usage: WARPS.add(FOO);

		//Game
		WARPS.add(ARENA)
		WARPS.add(FEAST)
		WARPS.add(VOLCANO)
		WARPS.add(ONE_VS_ONE)
		WARPS.add(FPS)
		WARPS.add(LAVA_CHALLENGE)
		WARPS.add(LOBBY)

		//Event
		WARPS.add(HG)

		enabledWarpsNames = Warps.WARPS.filter { it.data.isEnabled }.map { it.name.toLowerCase().replace(" ", "") }.toTypedArray()

		val pluginManager = Bukkit.getPluginManager()
		WARPS.forEach { warp -> pluginManager.registerEvents(warp, Main.INSTANCE) }
	}

	operator fun get(name: String): Warp = WARPS.firstOrNull { it.name.toLowerCase().replace(" ", "") == name.toLowerCase() } ?: NONE

	operator fun get(icon: ItemStack): Warp = WARPS.firstOrNull { it.icon == icon } ?: NONE
}
