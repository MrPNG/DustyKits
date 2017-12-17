package br.com.dusty.dkits.ability

import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.isWalk
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object PoseidonAbility: Ability() {

	@EventHandler
	fun onPlayerMove(event: PlayerMoveEvent) {
		val type = event.to.block.type

		if (type == Material.WATER || type == Material.STATIONARY_WATER) {
			val player = event.player
			val gamer = player.gamer()

			if (hasAbility(gamer) && canUse(gamer) && event.isWalk()) {
				player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 60, 1))
				player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0))
			}
		}
	}
}
