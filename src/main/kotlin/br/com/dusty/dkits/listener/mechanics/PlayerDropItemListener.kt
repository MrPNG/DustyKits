package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.gamer.gamer
import org.bukkit.Material.MUSHROOM_SOUP
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

object PlayerDropItemListener: Listener {

	@EventHandler
	fun onPlayerDropItem(event: PlayerDropItemEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp.overrides(event)) return

		val itemDrop = event.itemDrop
		val itemStack = itemDrop.itemStack

		when {
			gamer.mode == EnumMode.ADMIN                                  -> return //TODO: Logging
			itemStack == gamer.kit.weapon || itemStack in gamer.kit.items -> event.isCancelled = true
			itemStack.type != MUSHROOM_SOUP                               -> Tasks.sync(Runnable { itemDrop.remove() })
		}
	}
}
