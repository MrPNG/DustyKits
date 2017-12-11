package br.com.dusty.dkits.ability

import br.com.dusty.dkits.Main
import org.bukkit.Bukkit

object Abilities {

	val ABILITIES = arrayListOf<Ability>()

	fun registerAll() {
		//Usage: ABILITIES.add(FOO);

		ABILITIES.add(SnailAbility)

		val pluginManager = Bukkit.getPluginManager()
		ABILITIES.forEach { ability -> pluginManager.registerEvents(ability, Main.INSTANCE) }
	}
}
