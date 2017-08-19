package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.Gamer
import org.bukkit.event.Listener

class Ability: Listener {

	fun isUsing(gamer: Gamer): Boolean {
		return this.javaClass.isAssignableFrom(gamer.kit.ability.javaClass)
	}
}
