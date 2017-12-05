package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.util.block.interact
import br.com.dusty.dkits.util.block.isSpecial
import br.com.dusty.dkits.util.inventory.Inventories
import br.com.dusty.dkits.util.inventory.KitMenu
import br.com.dusty.dkits.util.inventory.ShopMenu
import br.com.dusty.dkits.util.inventory.WarpMenu
import br.com.dusty.dkits.warp.Warp
import org.bukkit.GameMode
import org.bukkit.Material.*
import org.bukkit.attribute.Attribute
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object PlayerInteractListener: Listener {

	val ALLOWED_ITEMS = arrayOf(WOOD_SWORD, STONE_SWORD, IRON_SWORD, DIAMOND_SWORD)
	val ALLOWED_BLOCKS = arrayOf(ACACIA_DOOR, BIRCH_DOOR, DARK_OAK_DOOR, JUNGLE_DOOR, SPRUCE_DOOR, WOOD_DOOR, WOOD_BUTTON, STONE_BUTTON, TRAP_DOOR)

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.player

		if (player.gameMode != GameMode.CREATIVE) event.isCancelled = true

		if (event.action == Action.RIGHT_CLICK_BLOCK) {
			val block = event.clickedBlock

			if (block.type in ALLOWED_BLOCKS) event.isCancelled = false

			when (block.type) {
				WALL_SIGN, SIGN_POST -> {
					val sign = block.state as Sign

					if (sign.isSpecial()) sign.interact(player)
				}
			}
		}

		event.item?.run {
			if (event.action == Action.RIGHT_CLICK_AIR && type in ALLOWED_ITEMS) event.isCancelled = false

			when (type) {
				MUSHROOM_SOUP -> {
					if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
						val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).value

						if (player.health < maxHealth) {
							if (player.health < maxHealth - 7) player.health += 7.0
							else player.health = maxHealth

							player.inventory.itemInMainHand = Inventories.BOWL
						}
					}
				}
				CHEST         -> if (this == Warp.GameWarpKit.items[0]) player.openInventory(KitMenu.menuKitOwned(player))
				EMERALD       -> if (this == Warp.GameWarpKit.items[4]) player.openInventory(ShopMenu.menuShopMain(player))
				EMPTY_MAP     -> if (this == Warp.GameWarpKit.items[8]) player.openInventory(WarpMenu.menuWarpMain(player))
			}
		}
	}
}
