package br.com.dusty.dkits.listener.mechanics

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

object PlayerCommandPreProcessListener: Listener {

	@EventHandler
	fun onPlayerCommandPreProcess(event: PlayerCommandPreprocessEvent) {
		when {
			event.message.startsWith("/report", true) -> {
				event.isCancelled = true

				event.player.chat(event.message.replace("/report", "/dustyreport", true))
			}
			event.message.startsWith("/sc", true)     -> {
				event.isCancelled = true

				event.player.chat("/staffchat")
			}
		}
	}
}
