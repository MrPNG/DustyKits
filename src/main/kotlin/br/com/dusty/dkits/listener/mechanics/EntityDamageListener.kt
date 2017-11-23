package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.gamer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

object EntityDamageListener: Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onEntityDamage(event: EntityDamageEvent) {
		if (event.entity is Player) {
			val player = event.entity as Player

			val gamer = player.gamer()

			if (gamer.isInvincible) event.isCancelled = true

			if (event.cause == EntityDamageEvent.DamageCause.FALL && gamer.isNoFall) {
				gamer.isNoFall = false

				event.isCancelled = true
			}
		}
	}
}
