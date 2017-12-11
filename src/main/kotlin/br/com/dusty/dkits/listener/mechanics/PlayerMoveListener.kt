package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material.SPONGE
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.Vector

object PlayerMoveListener: Listener {

	@EventHandler
	fun onPlayerMove(event: PlayerMoveEvent) {
		val player = event.player

		val gamer = player.gamer()

		if (gamer.warpTask != null) {
			gamer.warpTask!!.cancel()
			gamer.warpTask = null

			player.sendMessage(Text.negativePrefix().basic("Você se ").negative("moveu").basic(", teleporte ").negative("cancelado").basic("!").toString())
		}

		val from = event.from
		val to = event.to

		if (gamer.isFrozen() && isWalk(from, to)) event.to = Location(from.world, from.x, to.y, from.z, to.yaw, to.pitch)
		else when (to.block.getRelative(BlockFace.DOWN).type) {
			SPONGE -> boost(gamer, to)
		}
	}

	private fun isWalk(from: Location, to: Location): Boolean = from.x != to.x || from.z != to.z

	private fun boost(gamer: Gamer, location: Location) {
		val player = gamer.player

		player.velocity = Vector(0.0, 1.5, 0.0)

		gamer.isNoFall = true
	}
}
