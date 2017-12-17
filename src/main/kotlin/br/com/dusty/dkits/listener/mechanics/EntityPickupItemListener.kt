package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.util.gamer.gamer
import org.bukkit.GameMode
import org.bukkit.Material.MUSHROOM_SOUP
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

object EntityPickupItemListener: Listener {

	val ALLOWED_DROPS = arrayOf(MUSHROOM_SOUP)

	@EventHandler
	fun onEntityPickupItem(event: EntityPickupItemEvent) {
		if (event.entity is Player) {
			val player = event.entity as Player
			val gamer = player.gamer()

			if (gamer.warp.overrides(event)) return

			if (player.gameMode != GameMode.CREATIVE && event.item.itemStack.type !in ALLOWED_DROPS) event.isCancelled = true
		}
	}
}
