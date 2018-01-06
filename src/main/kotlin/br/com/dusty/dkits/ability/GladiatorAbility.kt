package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.world.destroyArena
import br.com.dusty.dkits.util.world.generateGlassArena
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

object GladiatorAbility: Ability() {

	val ALREADY_FIGHTING = Text.negativePrefix().basic("Você ").negative("não").basic(" pode lutar com esse ").negative("jogador").basic(" agora!").toString()
	val YOU_GOT_SOMEONE = Text.positivePrefix().basic("Você ").positive("puxou").basic(" o jogador ").positive("%s").basic(" para uma ").positive("luta").basic("!").toString()
	val SOMEONE_GOT_YOU = Text.negativePrefix().basic("Você foi ").negative("puxado").basic(" pelo \'gladiator\' ").negative("%s").basic(" para uma ").negative("luta").basic("!").toString()

	val WITHER_EFFECT = PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 1)

	val FIGHTS = hashMapOf<Gamer, Fight>()

	@EventHandler(priority = EventPriority.LOW)
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val player = event.entity
		val gamer = player.gamer()

		FIGHTS.values.firstOrNull { it.host == gamer || it.guest == gamer }?.run {
			(if (gamer == host) guest else host).player.run {
				removePotionEffect(PotionEffectType.WITHER)
				teleport(startLocation)
			}

			FIGHTS.remove(host)

			arenaLocation.destroyArena(15, 10, 15)
			task.cancel()
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player
		val gamer = player.gamer()

		FIGHTS.values.firstOrNull { it.host == gamer || it.guest == gamer }?.run {
			(if (gamer == host) guest else host).player.run {
				removePotionEffect(PotionEffectType.WITHER)
				teleport(startLocation)
			}

			FIGHTS.remove(host)

			arenaLocation.destroyArena(15, 10, 15)
			task.cancel()
		}
	}

	@EventHandler
	fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
		if (event.rightClicked is Player) {
			val player = event.player
			val item = player.itemInHand

			if (item != null && item.type == Material.IRON_FENCE && item == Kits.GLADIATOR.items[0]) {
				val gamer = player.gamer()

				val rightClickedPlayer = event.rightClicked as Player
				val rightClicked = rightClickedPlayer.gamer()

				if (hasAbility(gamer) && gamer.canUse(rightClicked)) {
					if (FIGHTS.containsKey(gamer) || FIGHTS.containsKey(rightClicked)) player.sendMessage(ALREADY_FIGHTING)
					else {
						val location = player.location
						val locations = location.generateGlassArena(15, 10, 15, true, true)

						player.teleport(locations.second)
						rightClickedPlayer.teleport(locations.third)

						gamer.invincibility = 5000L
						rightClicked.invincibility = 5000L

						gamer.freeze = 5000L
						rightClicked.freeze = 5000L

						gamer.combatPartner?.combatPartner = null
						rightClicked.combatPartner?.combatPartner = null

						gamer.combatTag = Int.MAX_VALUE.toLong()
						rightClicked.combatTag = Int.MAX_VALUE.toLong()

						gamer.combatPartner = rightClicked
						rightClicked.combatPartner = gamer

						player.sendMessage(YOU_GOT_SOMEONE.format(rightClicked.displayName))
						rightClickedPlayer.sendMessage(SOMEONE_GOT_YOU.format(gamer.displayName))

						val task = Tasks.sync(object: BukkitRunnable() {

							override fun run() {
								player.addPotionEffect(WITHER_EFFECT)
								rightClickedPlayer.addPotionEffect(WITHER_EFFECT)
							}
						}, 20L * 60L)

						FIGHTS.put(gamer, Fight(task, gamer, rightClicked, location, locations.first))
					}
				}
			}
		}
	}

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val clickedBlock = event.clickedBlock ?: return

		if (clickedBlock.type == Material.GLASS || clickedBlock.type == Material.STAINED_GLASS) {
			val player = event.player
			val gamer = player.gamer()

			if (FIGHTS.values.any { it.host == gamer || it.guest == gamer }) player.sendBlockChange(clickedBlock.location, Material.BEDROCK, 0)
		}
	}

	data class Fight(val task: BukkitTask, val host: Gamer, val guest: Gamer, val startLocation: Location, val arenaLocation: Location)
}
