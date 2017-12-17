package br.com.dusty.dkits.listener.mechanics

import org.bukkit.Material.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

object PlayerCommandPreProcessListener: Listener {

	val PROHIBITED_BLOCKS = arrayOf(GLASS, STAINED_GLASS, QUARTZ_BLOCK)

	@EventHandler
	fun onPlayerCommandPreProcess(event: PlayerCommandPreprocessEvent) {
		if (event.message.startsWith("/report ")) {
			event.isCancelled = true

			event.player.chat(event.message.replace("/report", "/dustyreport"))
		}
	}
}
