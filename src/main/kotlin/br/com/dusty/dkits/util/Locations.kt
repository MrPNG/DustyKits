package br.com.dusty.dkits.util

import br.com.dusty.dkits.Main
import org.bukkit.Location

fun Location.spread(radius: Float): Location {
	val distance = Main.RANDOM.nextDouble() * radius
	val theta = Main.RANDOM.nextDouble() * 2 * Math.PI

	return this.clone().add(distance * Math.cos(theta), 0.0, distance * Math.sin(theta))
}
