package br.com.dusty.dkits.ability

import br.com.dusty.dkits.Main
import org.bukkit.Bukkit

object Abilities {

	val ABILITIES = hashSetOf<Ability>()

	fun registerAll() {
		//Usage: ABILITIES.add(FOO);

		ABILITIES.add(ChemistAbility)
		ABILITIES.add(ChickenmanAbility)
		ABILITIES.add(ElfoAbility)
		ABILITIES.add(FishermanAbility)
		ABILITIES.add(KangarooAbility)
		ABILITIES.add(NinjaAbility)
		ABILITIES.add(PoseidonAbility)
		ABILITIES.add(RingAbility)
		ABILITIES.add(SnailAbility)
		ABILITIES.add(StomperAbility)
		ABILITIES.add(ThorAbility)
		ABILITIES.add(TurtleAbility)
		ABILITIES.add(ViperAbility)

		val pluginManager = Bukkit.getPluginManager()
		ABILITIES.forEach { ability -> pluginManager.registerEvents(ability, Main.INSTANCE) }
	}
}
