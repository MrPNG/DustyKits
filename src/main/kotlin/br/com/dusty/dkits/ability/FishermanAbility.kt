package br.com.dusty.dkits.ability

import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerFishEvent

object FishermanAbility: Ability() {

	@EventHandler
	fun onPlayerFish(event: PlayerFishEvent) {
		if (event.caught is Player) {
			val player = event.caught as Player
			val damagerPlayer = event.player

			val gamer = player.gamer()
			val damager = damagerPlayer.gamer()

			if (hasAbility(damager) && canUse(damager, gamer) && damagerPlayer.itemInHand == Kits.FISHERMAN.items[0]) {
				player.teleport(damagerPlayer)
				player.sendMessage(Text.negativePrefix().basic("Você foi ").negative("capturado").basic(" pelo fisherman ").negative(damagerPlayer.displayName.clearFormatting()).basic("!").toString())
			}
		}
	}
}
