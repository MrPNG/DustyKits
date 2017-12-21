package br.com.dusty.dkits.ability

import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.gamer.gamer
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent

object ChemistAbility: Ability() {

	@EventHandler
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val player = event.entity
		val gamer = player.gamer()

		if (gamer.isCombatTagged()) {
			val combatPartner = gamer.combatPartner!!

			if (hasAbility(combatPartner) && combatPartner.canUse()) combatPartner.player.inventory.addItem(*Kits.CHEMIST.items)
		}
	}
}
