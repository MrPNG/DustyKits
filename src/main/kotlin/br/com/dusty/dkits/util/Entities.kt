package br.com.dusty.dkits.util

import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework

fun Location.spawnFirework(power: Int, vararg effects: FireworkEffect) {
	val firework = world.spawnEntity(this, EntityType.FIREWORK) as Firework

	val fireworkMeta = firework.fireworkMeta
	fireworkMeta.power = power
	fireworkMeta.addEffects(*effects)

	firework.fireworkMeta = fireworkMeta
}
