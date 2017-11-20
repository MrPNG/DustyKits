package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.util.block.Signs
import br.com.dusty.dkits.util.inventory.WarpMenu
import br.com.dusty.dkits.warp.Warps
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object PlayerInteractListener: Listener {

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.player

		if (event.action == Action.RIGHT_CLICK_BLOCK) {
			val block = event.clickedBlock

			when (block.type) {
				Material.WALL_SIGN -> {
					val sign = block.state as Sign

					if (Signs.isSpecialSign(sign)) Signs.interact(sign, player)
				}
			}
		}

		if (event.item != null) {
			val itemStack = event.item

			when (itemStack.type) {
				Material.EMPTY_MAP -> if (itemStack == Warps.LOBBY.entryKit.items[0]) {
					player.openInventory(WarpMenu.menuWarpMain(player))

					event.isCancelled = true
				}
			}
		}
	}
}
