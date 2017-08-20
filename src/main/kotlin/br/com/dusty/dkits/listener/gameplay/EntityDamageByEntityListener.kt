package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.gamer.Gamer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EntityDamageByEntityListener: Listener {

	@EventHandler
	fun onEntityDamage(event: EntityDamageByEntityEvent) {
		if (event.entity is Player && event.damager is Player) {
			val gamer = Gamer.of(event.entity as Player)
			val damager = Gamer.of(event.damager as Player)

			gamer.combatTag = 10000
			gamer.combatPartner = damager

			damager.combatTag = 10000
			damager.combatPartner = gamer
		}
	}
}
