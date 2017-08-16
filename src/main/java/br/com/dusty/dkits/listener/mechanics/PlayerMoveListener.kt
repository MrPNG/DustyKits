package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.Gamer
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.Vector

object PlayerMoveListener: Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onPlayerMove(event: PlayerMoveEvent) {
		val player = event.player

		val gamer = Gamer.of(player)

		val from = event.from
		val to = event.to

		if (gamer.isFrozen && walk(from, to))
			event.isCancelled = true
		else when (to.clone().add(0.0, -0.5, 0.0).block.type) {
			Material.SPONGE -> boost(gamer, to)
		}
	}

	private fun walk(from: Location, to: Location): Boolean {
		return from.x != to.x || from.y != to.y || from.z != to.z
	}

	private fun boost(gamer: Gamer, location: Location) {
		val player = gamer.player

		player.velocity = Vector(0.0, 1.5, 0.0)

		gamer.setNoFall(true)
	}
}
