package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.Gamer
import org.bukkit.event.Listener

open class Ability: Listener {

	fun canUse(gamer: Gamer): Boolean = this.javaClass.isAssignableFrom(gamer.kit.ability.javaClass)

	fun canUse(damager: Gamer, victim: Gamer): Boolean = this.javaClass.isAssignableFrom(damager.kit.ability.javaClass) && victim.mode == EnumMode.PLAY
}
