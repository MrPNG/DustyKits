package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.block.interact
import br.com.dusty.dkits.util.block.isSpecial
import br.com.dusty.dkits.util.inventory.KitMenu
import br.com.dusty.dkits.util.inventory.ShopMenu
import br.com.dusty.dkits.util.inventory.WarpMenu
import br.com.dusty.dkits.warp.Warp
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object PlayerInteractListener: Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.mode != EnumMode.ADMIN) event.isCancelled = true

		if (event.action == Action.RIGHT_CLICK_BLOCK) {
			val block = event.clickedBlock

			when (block.type) {
				Material.WALL_SIGN, Material.SIGN_POST -> {
					val sign = block.state as Sign

					if (sign.isSpecial()) sign.interact(player)
				}
			}
		}

		if (event.item != null) {
			val itemStack = event.item

			Warp.GameWarpKit.items.forEach { Bukkit.broadcastMessage("" + (itemStack == it)) }

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
