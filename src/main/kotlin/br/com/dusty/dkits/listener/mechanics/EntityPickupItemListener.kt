package br.com.dusty.dkits.listener.mechanics

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
		(event.entity as? Player)?.run { if (gameMode != GameMode.CREATIVE && event.item.itemStack.type !in ALLOWED_DROPS) event.isCancelled = true }
	}
}
