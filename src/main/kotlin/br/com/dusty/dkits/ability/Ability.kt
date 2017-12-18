package br.com.dusty.dkits.ability

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.millisToPeriod
import br.com.dusty.dkits.util.text.Text
import com.sk89q.worldguard.protection.flags.DefaultFlag
import org.bukkit.event.Listener

open class Ability: Listener {

	fun sendKitCooldownMessage(gamer: Gamer) {
		val period = gamer.kitCooldown.millisToPeriod().toInt()

		gamer.player.sendMessage(Text.negativePrefix().basic("Você deve ").negative("esperar").basic(" mais ").negative(period).basic((if (period == 1) " segundo" else " segundos") + " antes de usar essa ").negative(
				"habilidade").basic(" novamente!").toString())
	}

	fun hasAbility(gamer: Gamer) = gamer.kit.ability == this

	fun canUse(gamer: Gamer): Boolean = gamer.kit.ability == this && gamer.mode == EnumMode.PLAY && !Main.REGION_MANAGER!!.getApplicableRegions(gamer.player.location).allows(DefaultFlag.INVINCIBILITY)

	fun canUse(actor: Gamer,
	           receptor: Gamer): Boolean = actor != receptor && canUse(actor) && receptor.mode == EnumMode.PLAY && receptor.player.canSee(actor.player) && !Main.REGION_MANAGER!!.getApplicableRegions(
			receptor.player.location).allows(DefaultFlag.INVINCIBILITY)
}
