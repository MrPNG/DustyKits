package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.block.interact
import br.com.dusty.dkits.util.block.isSpecial
import br.com.dusty.dkits.util.inventory.KitMenu
import br.com.dusty.dkits.util.inventory.ShopMenu
import br.com.dusty.dkits.util.inventory.WarpMenu
import br.com.dusty.dkits.warp.Warp
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object PlayerInteractListener: Listener {

	@EventHandler(priority = EventPriority.HIGH)
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.player

		if (player.gameMode != GameMode.CREATIVE) event.isCancelled = true

		if (event.action == Action.RIGHT_CLICK_BLOCK) {
			val block = event.clickedBlock

			when (block.type) {
				Material.CHEST                         -> {
					player.openInventory((block as Chest).blockInventory)
				}
				Material.WALL_SIGN, Material.SIGN_POST -> {
					val sign = block.state as Sign

					if (sign.isSpecial()) sign.interact(player)
				}
			}
		}

		if (event.item != null) {
			val itemStack = event.item

			when (itemStack.type) {
				Material.CHEST     -> if (itemStack == Warp.GameWarpKit.items[0]) {
					player.openInventory(KitMenu.menuKitOwned(player))
				}
				Material.EMERALD   -> if (itemStack == Warp.GameWarpKit.items[4]) {
					player.openInventory(ShopMenu.menuShopMain(player))
				}
				Material.EMPTY_MAP -> if (itemStack == Warp.GameWarpKit.items[8]) {
					player.openInventory(WarpMenu.menuWarpMain(player))
				}
			}
		}
	}
}
