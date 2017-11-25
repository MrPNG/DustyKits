package br.com.dusty.dkits.kit

import java.util.*

object Kits {

	val NONE = NoneKit
	val PVP = PvpKit
	val SNAIL = SnailKit

	val KITS = ArrayList<Kit>()

	lateinit var enabledKitsNames: Array<String>

	fun registerAll() {
		//Usage: KITS.add(FOO);

		KITS.add(NONE)
		KITS.add(PVP)
		KITS.add(SNAIL)

		enabledKitsNames = KITS.filter { it.data.isEnabled }.map { it.name.toLowerCase() }.toTypedArray()
	}

	operator fun get(name: String): Kit = KITS.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } ?: NONE
}
