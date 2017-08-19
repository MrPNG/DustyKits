package br.com.dusty.dkits.ability

import br.com.dusty.dkits.Main
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager

import java.util.ArrayList

object Abilities {

	val ABILITIES = ArrayList<Ability>()

	fun registerAll() {
		//Usage: ABILITIES.add(FOO);

		val pluginManager = Bukkit.getPluginManager()
		ABILITIES.forEach { ability -> pluginManager.registerEvents(ability, Main.INSTANCE) }
	}
}
