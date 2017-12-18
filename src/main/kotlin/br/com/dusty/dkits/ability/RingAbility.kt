package br.com.dusty.dkits.ability

import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.rename
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object RingAbility: Ability() {

	val LEATHER_BOOTS = ItemStack(Material.LEATHER_BOOTS).rename("Botas Comuns")

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		if (event.isShiftClick) {
			val player = event.whoClicked as Player
			val gamer = player.gamer()

			if (hasAbility(gamer)) {
				val item = event.currentItem

				if (item != null && (item.type == Material.LEATHER_BOOTS || item.type == Material.GOLD_BOOTS)) event.isCancelled = true
			}
		}
	}

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR) {
			val item = event.item

			if (item != null && (item.type == Material.LEATHER_BOOTS || item.type == Material.GOLD_BOOTS)) {
				val player = event.player
				val gamer = player.gamer()

				if (hasAbility(gamer) && canUse(gamer)) {
					if (gamer.isOnKitCooldown()) {
						sendKitCooldownMessage(gamer)
					} else {
						player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 200, 0), true)
						player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0), true)

						val inventory = player.inventory
						inventory.itemInMainHand = LEATHER_BOOTS

						val chestplate = inventory.chestplate

						inventory.chestplate = null

						Tasks.sync(Runnable {
							val index = player.inventory.indexOfFirst { it != null && it.type == Material.LEATHER_BOOTS }

							if (index != -1 && gamer.kit == Kits.RING) {
								inventory.setItem(index, Kits.RING.items[0])
								inventory.chestplate = chestplate
							}
						}, 600L)

						gamer.kitCooldown = 30000L
					}
				}
			}
		}
	}
}
