package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.warp.Warp
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent

object PlayerItemDamageListener: Listener {

	@EventHandler
	fun onPlayerItemDamage(event: PlayerItemDamageEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp.overrides(event)) return

		if (gamer.warp.durabilityBehavior == Warp.EnumDurabilityBehavior.REGEN) event.isCancelled = true
	}
}
