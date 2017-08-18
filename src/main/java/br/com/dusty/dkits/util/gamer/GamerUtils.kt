package br.com.dusty.dkits.util.gamer

import br.com.dusty.dkits.gamer.Gamer
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect

object GamerUtils {

	fun clear(gamer: Gamer) {
		val player = gamer.player

		player.health = 20.0
		player.foodLevel = 20
		player.exp = 0f
		player.level = 0

		player.inventory.clear()

		for (potionEffect in player.activePotionEffects)
			player.removePotionEffect(potionEffect.type)
	}

	fun fly(gamer: Gamer, fly: Boolean) {
		val player = gamer.player

		player.allowFlight = fly
		player.isFlying = fly
	}
}
