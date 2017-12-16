package br.com.dusty.dkits.ability

import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerToggleSneakEvent

object NinjaAbility: Ability() {

	@EventHandler
	fun onPlayerToggleSneak(event: PlayerToggleSneakEvent) {
		if (event.isSneaking) {
			val player = event.player
			val gamer = player.gamer()

			if (hasAbility(gamer) && gamer.isCombatTagged()) {
				val combatPartner = gamer.combatPartner!!
				val combatPartnerPlayer = combatPartner.player

				if (gamer.isOnKitCooldown()) {
					sendKitCooldownMessage(gamer)
				} else if (canUse(gamer, combatPartner)) {
					player.teleport(combatPartnerPlayer)

					combatPartnerPlayer.sendMessage(Text.negativePrefix().basic("O ninja ").negative(player.displayName.clearFormatting()).basic(" se teleportou até ").negative("você").basic("!").toString())

					gamer.kitCooldown = 10000L
				}
			}
		}
	}
}
