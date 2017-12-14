package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.Gamer
import org.bukkit.event.Listener

open class Ability: Listener {

	fun canUse(gamer: Gamer): Boolean = gamer.kit.ability == this && gamer.mode == EnumMode.PLAY

	fun canUse(actor: Gamer, receptor: Gamer): Boolean = actor.kit.ability == this && actor.mode == EnumMode.PLAY && receptor.mode == EnumMode.PLAY && receptor.player.canSee(actor.player)
}
