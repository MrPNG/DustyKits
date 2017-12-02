package br.com.dusty.dkits.util

import br.com.dusty.dkits.Main

object Maths {

	fun probability(chances: Double) = Main.RANDOM.nextFloat() < chances
}
