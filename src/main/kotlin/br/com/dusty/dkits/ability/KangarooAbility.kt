package br.com.dusty.dkits.ability

import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.gamer.gamer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.math.pow

object KangarooAbility: Ability() {

	val USING = linkedMapOf<Player, Int>()

	@EventHandler
	fun onEntityDamage(event: EntityDamageEvent) {
		if (event.cause == EntityDamageEvent.DamageCause.FALL && event.damage > 7.0 && event.entity is Player) {
			val player = event.entity as Player
			val gamer = player.gamer()

			if (hasAbility(gamer) && canUse(gamer)) event.damage = 7.0
		}
	}

	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		if (event.entity is Player && event.damager is Player) {
			val player = event.entity as Player
			val gamer = player.gamer()

			if (hasAbility(gamer) && canUse(gamer)) gamer.kitCooldown = 5000L
		}
	}

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val item = event.item

		if (item != null && item.type == Material.FIREWORK && item == Kits.KANGAROO.items[0]) {
			val player = event.player
			val gamer = player.gamer()

			if (gamer.isOnKitCooldown()) {
				sendKitCooldownMessage(gamer)
			} else {
				var count = USING[player]

				if (count == null) count = 0

				if (count < 2 && hasAbility(gamer) && canUse(gamer)) {
					val velocity = player.velocity
					val velocityLength = Math.sqrt(velocity.x.pow(2) + velocity.z.pow(2))

					val vector = player.eyeLocation.direction
					val vectorLength = Math.sqrt(vector.x.pow(2) + vector.z.pow(2))

					if (player.isSneaking) {
						val normalizationFactor = 1.6 / vectorLength
						vector.x *= normalizationFactor
						vector.y = 0.6
						vector.z *= normalizationFactor
					} else {
						val normalizationFactor = velocityLength / vectorLength
						vector.x *= normalizationFactor
						vector.y = 0.9
						vector.z *= normalizationFactor
					}

					player.fallDistance = -1.5F
					player.velocity = vector

					USING.put(player, count + 1)
				}
			}
		}
	}

	@EventHandler
	fun onPlayerMove(event: PlayerMoveEvent) {
		if (USING.containsKey(event.player) && event.to.clone().add(0.0, -0.0625, 0.0).block.type.isSolid) {
			USING.remove(event.player)
		}
	}
}
