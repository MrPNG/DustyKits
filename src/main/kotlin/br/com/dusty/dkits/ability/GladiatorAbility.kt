package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.destroyGlassArena
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.generateGlassArena
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
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
	val FIGHTS = hashMapOf<Gamer, Fight>()

	val WITHER_EFFECT = PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 1)

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
					if (FIGHTS.containsKey(gamer) || FIGHTS.containsKey(rightClicked)) {
						player.sendMessage(ALREADY_FIGHTING)
					} else {
						val location = player.location
						val locations = location.generateGlassArena(15, 10, 15, true, true)

						player.teleport(locations.second)
						rightClickedPlayer.teleport(locations.third)

						gamer.invincibility = 5000L
						rightClicked.invincibility = 5000L

						gamer.freeze = 5000L
						rightClicked.freeze = 5000L

						if (gamer.isCombatTagged()) gamer.combatPartner!!.combatPartner = null
						if (rightClicked.isCombatTagged()) rightClicked.combatPartner!!.combatPartner = null

						gamer.combatTag = Int.MAX_VALUE.toLong()
						rightClicked.combatTag = Int.MAX_VALUE.toLong()

						gamer.combatPartner = rightClicked
						rightClicked.combatPartner = gamer

						player.sendMessage(Text.positivePrefix().basic("Você ").positive("puxou").basic(" o jogador ").positive(rightClickedPlayer.displayName.clearFormatting()).basic(" para uma ").positive(
								"luta").basic("!").toString())
						rightClickedPlayer.sendMessage(Text.negativePrefix().basic("Você foi ").negative("puxado").basic(" pelo \'gladiator\' ").negative(player.displayName.clearFormatting()).basic(
								" para uma ").negative("luta").basic("!").toString())

						val task = Tasks.sync(object: BukkitRunnable() {

							override fun run() {
								player.addPotionEffect(WITHER_EFFECT)
								rightClickedPlayer.addPotionEffect(WITHER_EFFECT)
							}
						}, 1200L)

						FIGHTS.put(gamer, Fight(task, gamer, rightClicked, location, locations.first))
					}
				}
			}
		}
	}

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		if (event.action == Action.LEFT_CLICK_BLOCK) {
			val clickedBlock = event.clickedBlock

			if (clickedBlock.type == Material.GLASS || clickedBlock.type == Material.STAINED_GLASS) {
				val player = event.player
				val gamer = player.gamer()

				if (FIGHTS.values.any { it.host == gamer || it.guest == gamer }) player.sendBlockChange(clickedBlock.location, Material.BEDROCK, 0)
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val player = event.entity
		val gamer = player.gamer()

		val fight = FIGHTS.values.firstOrNull { it.host == gamer || it.guest == gamer }

		if (fight != null) {
			val alive = if (gamer == fight.host) fight.guest else fight.host
			val alivePlayer = alive.player

			alivePlayer.removePotionEffect(PotionEffectType.WITHER)
			alivePlayer.teleport(fight.startLocation)

			FIGHTS.remove(fight.host)

			fight.arenaLocation.destroyGlassArena(15, 10, 15)
			fight.task.cancel()
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player
		val gamer = player.gamer()

		val fight = FIGHTS.values.firstOrNull { it.host == gamer || it.guest == gamer }

		if (fight != null) {
			val alive = if (gamer == fight.host) fight.guest else fight.host
			val alivePlayer = alive.player

			alivePlayer.removePotionEffect(PotionEffectType.WITHER)
			alivePlayer.teleport(fight.startLocation)

			FIGHTS.remove(fight.host)

			fight.arenaLocation.destroyGlassArena(15, 10, 15)
			fight.task.cancel()
		}
	}

	data class Fight(val task: BukkitTask, val host: Gamer, val guest: Gamer, val startLocation: Location, val arenaLocation: Location)
}
