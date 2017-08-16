package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.inventory.InventoryUtils.BUTTON_BACK
import br.com.dusty.dkits.util.inventory.WarpMenu.*
import br.com.dusty.dkits.warp.Warps
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

object InventoryClickListener: Listener {

	private val TITLE_CONTAINER_INVENTORY = "container.inventory"

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as Player

		val gamer = Gamer.of(player)

		if (gamer.kit.isDummy)
			event.isCancelled = true

		val inventory = event.clickedInventory

		val itemStack = event.currentItem

		if (inventory.title != null && inventory.title != TITLE_CONTAINER_INVENTORY) {

			when (inventory.title) {
				TITLE_MAIN              -> {
					when (itemStack) {
						BUTTON_GAME  -> player.openInventory(menuWarpGame(player))
						BUTTON_EVENT -> player.openInventory(menuWarpEvent(player))
					}
				}
				TITLE_GAME, TITLE_EVENT -> {
					when (itemStack) {
						BUTTON_BACK -> player.openInventory(menuWarpMain(player))
						else        -> Warps.WARPS.filter { it.icon == itemStack }.forEach {
							gamer.sendToWarp(it)
							return
						}
					}
				}
			}
		}
	}
}
