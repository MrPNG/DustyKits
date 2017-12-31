package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.stdlib.clearFormatting
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.pow

object EndermageAbility: Ability() {

	val STAINED_GLASS_PANE = ItemStack(Material.STAINED_GLASS_PANE, 1, 1.toShort(), 10.toByte()).rename(" ")

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		if (event.action == Action.RIGHT_CLICK_BLOCK) {
			val player = event.player

			val item = player.itemInHand

			if (item != null && ((item.type == Material.STAINED_GLASS_PANE && item == STAINED_GLASS_PANE) || (item.type == Material.ENDER_PORTAL_FRAME && item == Kits.ENDERMAGE.items[0]))) {
				val gamer = player.gamer()

				if (gamer.isOnKitCooldown()) sendKitCooldownMessage(gamer)
				else if (hasAbility(gamer) && gamer.canUse()) {
					val block = event.clickedBlock
					val blockAbove = block.getRelative(BlockFace.UP)
					val blockAboveTheAboveBlock = blockAbove.getRelative(BlockFace.UP)

					if (!block.type.isSolid || block.type == Material.GLASS || block.type == Material.STAINED_GLASS || blockAbove.type.isSolid || blockAboveTheAboveBlock.type.isSolid) event.player.sendMessage(
							Text.negativePrefix().basic("Você ").negative("não").basic(" pode colocar um ").negative("portal").basic(" nesse local!").toString())
					else {
						val type = block.type
						val data = block.data

						block.type = Material.ENDER_PORTAL_FRAME
						block.data = 0

						val location = event.clickedBlock.location
						location.y += 1.5

						player.itemInHand = STAINED_GLASS_PANE

						fun undo() {
							block.type = type
							block.data = data

							if (gamer.kit == Kits.ENDERMAGE) {
								val index = player.inventory.indexOfFirst { it != null && it.type == Material.STAINED_GLASS_PANE && it == STAINED_GLASS_PANE }

								if (index != -1) player.inventory.setItem(index, Kits.ENDERMAGE.items[0])
							}
						}

						Tasks.sync(object: BukkitRunnable() {
							var i = 100

							override fun run() {
								if (--i == 0) {
									cancel()
									undo()
								} else {
									val gamers = arrayListOf<Gamer>()

									GamerRegistry.onlineGamers().filter { gamer.canUse(it) }.forEach {
										val otherLocation = it.player.location

										if (Math.sqrt((location.x - otherLocation.x).pow(2) + (location.z - otherLocation.z).pow(2)) < 3) gamers.add(it)
									}

									if (gamers.isNotEmpty()) {
										val message = Text.negativePrefix().basic("Você foi ").negative("teleportado").basic(" pelo endermage ").negative(player.displayName.clearFormatting()).basic(
												" e tem ").negative("5 segundos").basic(" de invencibilidade!").toString()

										gamers.forEach {
											it.player.run {
												teleport(location)
												sendMessage(message)
											}

											it.invincibility = 5000L
										}

										player.teleport(location)
										gamer.invincibility = 5000L

										cancel()
										undo()
									}
								}
							}
						}, 0L, 1L)

						gamer.kitCooldown = 10000L
					}
				}
			}
		}
	}
}
