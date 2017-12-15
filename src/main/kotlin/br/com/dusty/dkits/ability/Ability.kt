package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.millisToPeriod
import br.com.dusty.dkits.util.text.Text
import org.bukkit.event.Listener

open class Ability: Listener {

	fun sendKitCooldownMessage(gamer: Gamer) {
		val period = gamer.kitCooldown.millisToPeriod().toInt()

		gamer.player.sendMessage(Text.negativePrefix().basic("Você deve ").negative("esperar").basic(" mais ").negative(period).basic((if (period == 1) " segundo" else " segundos") + " antes de usar esse ").negative(
				"kit").basic(" novamente!").toString())
	}

	fun hasAbility(gamer: Gamer) = gamer.kit.ability == this

	fun canUse(gamer: Gamer): Boolean = gamer.kit.ability == this && gamer.mode == EnumMode.PLAY

	fun canUse(actor: Gamer, receptor: Gamer): Boolean = actor.kit.ability == this && actor.mode == EnumMode.PLAY && receptor.mode == EnumMode.PLAY && receptor.player.canSee(actor.player)
}
