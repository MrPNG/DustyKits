package br.com.dusty.dkits.kit

import org.bukkit.inventory.ItemStack

object Kits {

	val NONE = NoneKit
	val PVP = PvpKit
	val BUBBLE = BubbleKit
	val CHEMIST = ChemistKit
	val CHICKENMAN = ChickenmanKit
	val ELFO = ElfoKit
	val ENDERMAGE = EndermageKit
	val GLADIATOR = GladiatorKit
	val GRANDPA = GrandpaKit
	val FISHERMAN = FishermanKit
	val KANGAROO = KangarooKit
	val NINJA = NinjaKit
	val POSEIDON = PoseidonKit
	val RING = RingKit
	val SNAIL = SnailKit
	val STOMPER = StomperKit
	val THOR = ThorKit
	val TURTLE = TurtleKit
	val VIPER = ViperKit
	val VIKING = VikingKit

	val KITS = arrayListOf<Kit>()

	lateinit var enabledKitsNames: Array<String>

	fun registerAll() {
		//Usage: KITS.add(FOO);

		KITS.add(NONE)
		KITS.add(PVP)
		KITS.add(BUBBLE)
		KITS.add(CHEMIST)
		KITS.add(CHICKENMAN)
		KITS.add(ELFO)
		KITS.add(ENDERMAGE)
		KITS.add(FISHERMAN)
		KITS.add(GLADIATOR)
		KITS.add(GRANDPA)
		KITS.add(KANGAROO)
		KITS.add(NINJA)
		KITS.add(POSEIDON)
		KITS.add(RING)
		KITS.add(SNAIL)
		KITS.add(STOMPER)
		KITS.add(THOR)
		KITS.add(TURTLE)
		KITS.add(VIPER)
		KITS.add(VIKING)

		enabledKitsNames = KITS.filter { it.data.isEnabled }.map { it.name.toLowerCase() }.toTypedArray()
	}

	operator fun get(name: String) = KITS.firstOrNull { it.name.toLowerCase() == name.toLowerCase().replace("_", " ") } ?: NONE

	operator fun get(icon: ItemStack) = KITS.firstOrNull { it.icon == icon } ?: NONE
}
