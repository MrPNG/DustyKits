package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.util.gamer.gamer
import org.bukkit.GameMode
import org.bukkit.Material.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

object BlockBreakListener: Listener {

	val PROHIBITED_BLOCKS = arrayOf(GLASS, STAINED_GLASS, QUARTZ_BLOCK)

	@EventHandler
	fun onFoodLevelChange(event: BlockBreakEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp.overrides(event)) return

		if (player.gameMode != GameMode.CREATIVE && event.block.type in PROHIBITED_BLOCKS) event.isCancelled = true
	}
}
