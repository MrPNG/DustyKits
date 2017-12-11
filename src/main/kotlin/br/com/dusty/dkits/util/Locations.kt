package br.com.dusty.dkits.util

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.warp.Warp
import org.bukkit.Bukkit
import org.bukkit.Location
import kotlin.math.roundToInt

fun Location.toSimpleLocation() = Warp.SimpleLocation(x, y, z, yaw, pitch)

fun Location.normalize() = Location(world, Math.floor(x) + 0.5, Math.floor(y) + 0.5, Math.floor(z) + 0.5, yaw.roundToInt().round(90.0).toFloat(), pitch.roundToInt().round(90.0).toFloat())

fun Location.spread(radius: Double): Location {
	if (radius == 0.0) return this

	val distance = Main.RANDOM.nextDouble() * radius
	val theta = Main.RANDOM.nextDouble() * 2 * Math.PI

	return this.clone().add(distance * Math.cos(theta), 0.0, distance * Math.sin(theta))
}

object Locations {

	val GENERIC = Location(Bukkit.getWorlds()[0], 0.0, 0.0, 0.0)
}
