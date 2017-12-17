package br.com.dusty.dkits.ability

import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.cosmetic.playEffect
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.rename
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ChickenmanAbility: Ability() {

	val RAW_CHICKEN = ItemStack(Material.RAW_CHICKEN).rename("Frango Comum")

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR) {
			val item = event.item

			if (item != null && (item.type == Material.RAW_CHICKEN || item.type == Material.COOKED_CHICKEN)) {
				val player = event.player
				val gamer = player.gamer()

				if (hasAbility(gamer) && canUse(gamer)) {
					if (gamer.isOnKitCooldown()) {
						sendKitCooldownMessage(gamer)
					} else if (item == Kits.CHICKENMAN.items[0]) {
						player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 200, 1), true)
						player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0), true)

						player.location.playEffect(Effect.POTION_BREAK, 0x400B, 16)

						player.inventory.itemInMainHand = RAW_CHICKEN

						Tasks.sync(Runnable {
							val index = player.inventory.indexOfFirst { it != null && it.type == Material.RAW_CHICKEN && it == RAW_CHICKEN }

							if (index != -1 && gamer.kit == Kits.CHICKENMAN) player.inventory.setItem(index, Kits.CHICKENMAN.items[0])
						}, 600L)

						gamer.kitCooldown = 30000L
					}
				}
			}
		}
	}
}
