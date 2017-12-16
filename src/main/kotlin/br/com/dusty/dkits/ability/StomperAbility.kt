package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.gamer.gamer
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.math.pow

object StomperAbility: Ability() {

	@EventHandler(priority = EventPriority.HIGH)
	fun onEntityDamage(event: EntityDamageEvent) {
		if (event.cause == EntityDamageEvent.DamageCause.FALL && event.entity is Player && !event.isCancelled) {
			val player = event.entity as Player
			val gamer = player.gamer()

			if (hasAbility(gamer) && canUse(gamer)) {
				val damage = event.damage

				if (damage > 4.0) event.damage = 4.0

				GamerRegistry.onlineGamers().forEach {
					if (canUse(gamer, it)) {
						val distance = player.location.distance(it.player.location)

						if (distance < 5) {
							val transferredDamage = damage * (1 - (distance / 5.0).pow(2))
							it.player.damage(transferredDamage, player)
						}
					}
				}

				val location = player.location
				location.world.playSound(location, Sound.BLOCK_ANVIL_LAND, 1F, 1F)
			}
		}
	}
}
