package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.inventory.InventoryUtils
import br.com.dusty.dkits.util.inventory.WarpMenu
import br.com.dusty.dkits.warp.Warp
import br.com.dusty.dkits.warp.Warps
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class InventoryClickListener: Listener {

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as Player

		val gamer = Gamer.of(player)

		if (gamer.kit.isDummy)
			event.isCancelled = true

		val inventory = event.clickedInventory

		val itemStack = event.currentItem

		if (inventory.title != null && inventory.title != TITLE_CONTAINER_INVENTORY) {

			//Warp menu - main
			if (inventory.title == WarpMenu.TITLE_MAIN) {
				if (itemStack == WarpMenu.BUTTON_GAME)
					player.openInventory(WarpMenu.menuWarpGame(player))
				else if (itemStack == WarpMenu.BUTTON_EVENT)
					player.openInventory(WarpMenu.menuWarpEvent(player))

				//Warp menu - game/event
			} else if (inventory.title == WarpMenu.TITLE_GAME || inventory.title == WarpMenu.TITLE_EVENT) {
				if (itemStack == InventoryUtils.BUTTON_BACK)
					player.openInventory(WarpMenu.menuWarpMain(player))
				else if (itemStack != InventoryUtils.BACKGROUND)
					for (warp in Warps.WARPS)
						if (warp.icon == itemStack) {
							gamer.sendToWarp(warp)
							return
						}
			}
		}
	}

	companion object {

		private val TITLE_CONTAINER_INVENTORY = "container.inventory"
	}
}
