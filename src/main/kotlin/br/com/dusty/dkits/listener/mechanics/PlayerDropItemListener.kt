package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.Tasks
import org.bukkit.GameMode
import org.bukkit.Material.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

object PlayerDropItemListener: Listener {

	val ALLOWED_DROPS = arrayOf(RED_MUSHROOM, BROWN_MUSHROOM, BOWL, MUSHROOM_SOUP)

	@EventHandler
	fun onPlayerDropItem(event: PlayerDropItemEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp.overrides(event)) return

		val item = event.itemDrop
		val itemStack = event.itemDrop.itemStack

		if (player.gameMode != GameMode.CREATIVE) {
			if (itemStack.type !in ALLOWED_DROPS || itemStack in gamer.kit.items) event.isCancelled = true else if (itemStack.type != MUSHROOM_SOUP) Tasks.sync(Runnable { item.remove() })
		}
	}
}
