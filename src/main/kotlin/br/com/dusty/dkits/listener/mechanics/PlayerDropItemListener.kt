package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.Tasks
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

object PlayerDropItemListener: Listener {

	val ALLOWED_DROPS = arrayOf(Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.BOWL, Material.MUSHROOM_SOUP)

	@EventHandler(priority = EventPriority.HIGH)
	fun onPlayerDropItem(event: PlayerDropItemEvent) {
		val player = event.player

		val item = event.itemDrop

		if (player.gameMode != GameMode.CREATIVE) {
			if (item.itemStack.type !in ALLOWED_DROPS) event.isCancelled = true else if (item.itemStack.type != Material.MUSHROOM_SOUP) Tasks.sync(Runnable { item.remove() })
		}
	}
}
