package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.inventory.openInventory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

object PlayerInteractEntityListener: Listener {

	@EventHandler
	fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
		if (event.rightClicked is Player) {
			val rightClicked = event.rightClicked as Player

			val player = event.player
			val gamer = player.gamer()

			if (gamer.warp.overrides(event)) return

			if (gamer.mode == EnumMode.ADMIN) player.openInventory(rightClicked)
		}
	}
}
