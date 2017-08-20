package br.com.dusty.dkits.kit

import java.util.*

object Kits {

	val NONE = NoneKit
	val PVP = PvpKit
	val SNAIL = SnailKit

	val KITS = ArrayList<Kit>()

	fun registerAll() {
		//Usage: KITS.add(FOO);

		KITS.add(NONE)
		KITS.add(PVP)
		KITS.add(SNAIL)
	}

	fun byName(name: String): Kit? {
		return KITS.firstOrNull { it.name.startsWith(name, true) }
	}
}
