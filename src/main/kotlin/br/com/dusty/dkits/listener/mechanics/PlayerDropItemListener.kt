package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.entity.gamer
import org.bukkit.Material.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

object PlayerDropItemListener: Listener {

	val SWORD_MATERIALS = arrayOf(WOOD_SWORD, GOLD_SWORD, STONE_SWORD, IRON_SWORD, DIAMOND_SWORD)

	@EventHandler
	fun onPlayerDropItem(event: PlayerDropItemEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp.overrides(event)) return

		val itemDrop = event.itemDrop
		val itemStack = itemDrop.itemStack

		when {
			gamer.mode == EnumMode.ADMIN                                                                       -> return //TODO: Logging
			itemStack.type in SWORD_MATERIALS || itemStack == gamer.kit.weapon || itemStack in gamer.kit.items -> event.isCancelled = true
			itemStack.type != MUSHROOM_SOUP                                                                    -> Tasks.sync(Runnable { itemDrop.remove() })
		}
	}
}
