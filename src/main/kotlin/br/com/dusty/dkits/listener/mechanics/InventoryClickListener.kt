package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.inventory.InventoryUtils.BUTTON_BACK
import br.com.dusty.dkits.util.inventory.WarpMenu
import br.com.dusty.dkits.warp.Warps
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

object InventoryClickListener: Listener {

	val TITLE_CONTAINER_INVENTORY = "container.inventory"

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as Player
		val gamer = player.gamer()

		if (gamer.kit.isDummy && gamer.mode != EnumMode.ADMIN) event.isCancelled = true

		val inventory = event.clickedInventory

		val itemStack = event.currentItem

		if (inventory != null && inventory.title != null && inventory.title != TITLE_CONTAINER_INVENTORY) {

			when (inventory.title) {
				WarpMenu.TITLE_MAIN                       -> {
					when (itemStack) {
						WarpMenu.BUTTON_GAME  -> player.openInventory(WarpMenu.menuWarpGame(player))
						WarpMenu.BUTTON_EVENT -> player.openInventory(WarpMenu.menuWarpEvent(player))
					}
				}
				WarpMenu.TITLE_GAME, WarpMenu.TITLE_EVENT -> {
					when (itemStack) {
						BUTTON_BACK -> player.openInventory(WarpMenu.menuWarpMain(player))
						else        -> Warps.WARPS.filter { it.icon == itemStack }.forEach {
							gamer.sendToWarp(it, true)
							return
						}
					}
				}
			}
		}
	}
}
