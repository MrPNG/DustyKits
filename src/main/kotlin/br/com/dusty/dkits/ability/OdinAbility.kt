package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.kit.OdinKit
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.entity.gamer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.pow

object OdinAbility: Ability() {

	@EventHandler
	fun onEntityDamage(event: EntityDamageEvent) {
		if (event.cause == EntityDamageEvent.DamageCause.LIGHTNING && event.entity is Player) {
			val player = event.entity as Player
			val gamer = player.gamer()

			if (hasAbility(gamer) && gamer.canUse()) event.isCancelled = true
		}
	}

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		if (event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_BLOCK) {
			val player = event.player

			val item = player.itemInHand

			if (item != null && item.type == Material.DIAMOND_AXE && item == OdinKit.items[0]) {

				val gamer = player.gamer()

				if (gamer.isOnKitCooldown()) sendKitCooldownMessage(gamer)
				else if (hasAbility(gamer) && gamer.canUse()) {
					val location = event.clickedBlock.location
					val world = location.world

					val gamers = arrayListOf<Gamer>()

					GamerRegistry.onlineGamers().filter { gamer.canUse(it) }.forEach {
						val otherLocation = it.player.location

						if (Math.sqrt((location.x - otherLocation.x).pow(2) + (location.z - otherLocation.z).pow(2)) < 5) gamers.add(it)
					}

					gamers.forEach {
						val otherPlayer = it.player

						val vector = otherPlayer.location.subtract(location).toVector()
						val vectorLength = Math.sqrt(vector.x.pow(2) + vector.z.pow(2))

						val normalizationFactor = 1.0 / vectorLength
						vector.x *= normalizationFactor
						vector.y = 1.0
						vector.z *= normalizationFactor

						otherPlayer.velocity = vector
					}

					Tasks.sync(object: BukkitRunnable() {
						val increment = Math.PI / 4

						var i = 0

						override fun run() {
							if (++i == 7) cancel()

							val angle = increment * i

							val x = Math.cos(angle) * 5
							val z = Math.cos(angle) * 5

							world.strikeLightning(location.clone().add(x, 0.0, z))
						}
					}, 0L, 1L)

					gamer.kitCooldown = 30000L
				}
			}
		}
	}
}
