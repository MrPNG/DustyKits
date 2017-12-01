package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.gamer
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

object EntityPickupItemListener: Listener {

	val ALLOWED_DROPS = arrayOf(Material.MUSHROOM_SOUP)

	@EventHandler
	fun onPlayerDropItem(event: EntityPickupItemEvent) {
		val player = event.entity as? Player

		if (player != null) {
			val gamer = player.gamer()

			if (player.gameMode != GameMode.CREATIVE && event.item.itemStack.type !in ALLOWED_DROPS) event.isCancelled = true
		}
	}
}
