package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.gamer.gamer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPickupItemEvent

object PlayerPickupItemListener: Listener {

	@EventHandler
	fun onEntityPickupItem(event: PlayerPickupItemEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.visibleTo != EnumRank.DEFAULT) event.isCancelled = true
	}
}
