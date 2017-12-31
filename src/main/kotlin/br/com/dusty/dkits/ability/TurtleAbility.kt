package br.com.dusty.dkits.ability

import br.com.dusty.dkits.util.entity.gamer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent

object TurtleAbility: Ability() {

	@EventHandler
	fun onEntityDamage(event: EntityDamageEvent) {
		if (event.entity is Player) {
			val player = event.entity as Player

			if (player.isSneaking) {
				val gamer = player.gamer()

				if (hasAbility(gamer) && gamer.canUse()) event.damage = 1.0
			}
		}
	}
}
