package br.com.dusty.dkits.ability

import br.com.dusty.dkits.Main
import org.bukkit.Bukkit

object Abilities {

	val ABILITIES = hashSetOf<Ability>()

	fun registerAll() {
		//Usage: ABILITIES.add(FOO);

		ABILITIES.add(BubbleAbility)
		ABILITIES.add(ChemistAbility)
		ABILITIES.add(ChickenmanAbility)
		ABILITIES.add(ElfoAbility)
		ABILITIES.add(EndermageAbility)
		ABILITIES.add(FishermanAbility)
		ABILITIES.add(GladiatorAbility)
		ABILITIES.add(KangarooAbility)
		ABILITIES.add(NinjaAbility)
		ABILITIES.add(OdinAbility)
		ABILITIES.add(PoseidonAbility)
		ABILITIES.add(RingAbility)
		ABILITIES.add(SnailAbility)
		ABILITIES.add(StomperAbility)
		ABILITIES.add(ThorAbility)
		ABILITIES.add(TurtleAbility)
		ABILITIES.add(ViperAbility)

		ABILITIES.forEach { ability -> Bukkit.getPluginManager().registerEvents(ability, Main.INSTANCE) }
	}
}
