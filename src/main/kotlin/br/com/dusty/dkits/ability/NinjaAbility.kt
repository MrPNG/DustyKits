package br.com.dusty.dkits.ability

import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

object NinjaAbility: Ability() {

	val NINJA_MESSAGE = Text.negativePrefix().basic("O ninja ").negative("%s").basic(" se teleportou até ").negative("você").basic("!").toString()

	val PLAYER_BY_NINJA = hashMapOf<Player, Player>()
	val TASK_BY_NINJA = hashMapOf<Player, BukkitTask>()

	@EventHandler
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val player = event.entity as Player

		if (player in PLAYER_BY_NINJA) PLAYER_BY_NINJA.remove(player)
		else PLAYER_BY_NINJA.values.remove(player)
	}

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player as Player

		if (player in PLAYER_BY_NINJA) PLAYER_BY_NINJA.remove(player)
		else PLAYER_BY_NINJA.values.remove(player)
	}

	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		if (event.entity is Player && event.damager is Player) {
			val player = event.entity as Player
			val gamer = player.gamer()

			if (hasAbility(gamer)) {
				val damagerPlayer = event.damager as Player
				val damager = damagerPlayer.gamer()

				if (damager.canUse(gamer)) {
					PLAYER_BY_NINJA.put(damagerPlayer, player)

					TASK_BY_NINJA.put(damagerPlayer, Tasks.sync(object: BukkitRunnable() {

						override fun run() {
							PLAYER_BY_NINJA.remove(damagerPlayer, player)
							TASK_BY_NINJA.remove(damagerPlayer)
						}
					}, 20L * 15L))
				}
			}
		}
	}

	@EventHandler
	fun onPlayerToggleSneak(event: PlayerToggleSneakEvent) {
		if (event.isSneaking) {
			val player = event.player
			val gamer = player.gamer()

			if (hasAbility(gamer)) {
				if (gamer.isOnKitCooldown()) sendKitCooldownMessage(gamer)
				else {
					val otherPlayer = PLAYER_BY_NINJA[player] ?: return
					val otherGamer = otherPlayer.gamer()

					if (gamer.canUse(otherGamer)) {
						player.teleport(otherPlayer)

						otherPlayer.sendMessage(NINJA_MESSAGE.format(gamer.displayName))

						gamer.kitCooldown = 10000L
					}

					PLAYER_BY_NINJA.remove(player)
					TASK_BY_NINJA.remove(player)?.cancel()
				}
			}
		}
	}
}
