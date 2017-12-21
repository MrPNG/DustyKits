package br.com.dusty.dkits.ability

import br.com.dusty.dkits.util.chances
import br.com.dusty.dkits.util.gamer.gamer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

open class PotionEffectAbility(val type: PotionEffectType, val duration: Int, val amplifier: Int, val chances: Double): Ability() {

	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		if (event.entity is Player && event.damager is Player) {
			val player = event.entity as Player
			val damagerPlayer = event.damager as Player

			val gamer = player.gamer()
			val damager = damagerPlayer.gamer()

			if (hasAbility(damager) && damager.canUse(gamer) && chances.chances()) player.addPotionEffect(PotionEffect(type, duration, amplifier), true)
		}
	}
}
