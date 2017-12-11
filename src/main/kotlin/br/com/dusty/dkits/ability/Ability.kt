package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.Gamer
import org.bukkit.event.Listener

open class Ability: Listener {

	fun canUse(gamer: Gamer): Boolean = this == gamer.kit.ability

	fun canUse(damager: Gamer, victim: Gamer): Boolean = this == damager.kit.ability && victim.mode == EnumMode.PLAY
}
