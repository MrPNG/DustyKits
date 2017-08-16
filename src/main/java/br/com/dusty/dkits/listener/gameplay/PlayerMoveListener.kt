package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object PlayerMoveListener: Listener {

	@EventHandler
	fun onPlayerMove(event: PlayerMoveEvent) {
		val player = event.player

		val gamer = Gamer.of(player)

		if (gamer.warpTask != null) {
			gamer.warpTask.cancel()
			gamer.warpTask = null

			player.sendMessage(Text.negativePrefix()
					                   .basic("Você se ")
					                   .negative("moveu")
					                   .basic(", teleporte ")
					                   .negative("cancelado")
					                   .basic("!")
					                   .toString())
		}
	}
}
