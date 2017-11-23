package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.gamer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EntityDamageByEntityListener: Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onEntityDamage(event: EntityDamageByEntityEvent) {
		if (event.entity is Player && event.damager is Player) {
			val gamer = (event.entity as Player).gamer()
			val damager = (event.damager as Player).gamer()

			if (!gamer.player.canSee(damager.player) || damager.mode == EnumMode.SPECTATE) {
				event.isCancelled = true

				return
			}

			gamer.combatTag = 10000
			gamer.combatPartner = damager

			damager.combatTag = 10000
			damager.combatPartner = gamer
		}
	}
}
