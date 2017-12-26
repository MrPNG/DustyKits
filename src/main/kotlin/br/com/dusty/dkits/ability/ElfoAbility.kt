package br.com.dusty.dkits.ability

import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

object ElfoAbility: Ability() {

	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		if (event.damager is Arrow) {
			val arrow = event.damager as Arrow

			if (event.entity is Player && arrow.shooter is Player) {
				val player = event.entity as Player
				val damagerPlayer = arrow.shooter as Player

				val gamer = player.gamer()
				val damager = damagerPlayer.gamer()

				if (hasAbility(damager) && damager.canUse(gamer)) {
					val distance = damagerPlayer.location.distance(player.location)

					if (distance >= 60 && arrow.location.y >= player.location.y + 1.62) {
						event.damage = 1024.0

						player.sendMessage(Text.negativePrefix().basic("Você levou um ").negative("headshot").basic(" do jogador ").negative(damagerPlayer.displayName.clearFormatting()).basic(
								" de ").negative(distance.toInt()).basic(" blocos de distância!").toString())
						damagerPlayer.sendMessage(Text.positivePrefix().basic("Você deu um ").positive("headshot").basic(" no jogador ").positive(player.displayName.clearFormatting()).basic(" a ").positive(
								distance.toInt()).basic(" blocos de distância!").toString())
					}
				}
			}
		}
	}
}
