package br.com.dusty.dkits.listener.mechanics

import org.bukkit.Material.MUSHROOM_SOUP
import org.bukkit.event.Listener

object EntityPickupItemListener: Listener {

	val ALLOWED_DROPS = arrayOf(MUSHROOM_SOUP)

	/*@EventHandler
	fun onEntityPickupItem(event: EntityPickupItemEvent) {
		if (event.entity is Player) {
			val player = event.entity as Player
			val gamer = player.gamer()

			if (gamer.warp.overrides(event)) return

			if (player.gameMode != GameMode.CREATIVE && event.item.itemStack.type !in ALLOWED_DROPS) event.isCancelled = true
		}
	}*/ //TODO: 1.8 switch
}
