package br.com.dusty.dkits.ability

import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.gamer.instaKill
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

				if (hasAbility(damager) && canUse(damager, gamer)) {
					val distance = damagerPlayer.location.distance(player.location)

					if (distance >= 40 && arrow.location.y >= player.location.y + 1.62) {
						player.instaKill(damagerPlayer)

						player.sendMessage(Text.negativePrefix().basic("Você levou um ").negative("headshot").basic(" do jogador ").negative(damagerPlayer.displayName.clearFormatting()).basic(
								" de ").negative(distance.toInt()).negative(" blocos de distância!").toString())
						damagerPlayer.sendMessage(Text.positivePrefix().basic("Você deu um ").positive("headshot").basic(" no jogador ").positive(player.displayName.clearFormatting()).basic(" de ").positive(
								distance.toInt()).negative(" blocos de distância!").toString())
					}
				}
			}
		}
	}
}
