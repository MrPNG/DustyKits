package br.com.dusty.dkits.ability

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.gamer.gamer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

open class PotionEffectAbility(val type: PotionEffectType, val duration: Int, val amplifier: Int, val chance: Float): Ability() {

	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		if (event.entity !is Player || event.damager !is Player) return

		val victim = event.entity as Player
		val damager = event.damager as Player

		if (!canUse(damager.gamer(), victim.gamer())) return

		if (Main.RANDOM.nextFloat() < chance) victim.addPotionEffect(PotionEffect(type, duration, amplifier))
	}
}
