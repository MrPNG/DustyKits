package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import java.util.*

object PlayerCommandPreprocessListener: Listener {

	private val KICK_SHUTDOWN = Text.negativeOf("O servidor está reiniciando!\n\nVolte em alguns segundos...").toString()

	@EventHandler
	fun onPlayerCommandPreProcess(event: PlayerCommandPreprocessEvent) {
		val player = event.player

		when (event.message.split(" ".toRegex())[0]) {
			"/stop" -> {
				val gamers = HashSet(GamerRegistry.onlineGamers())
				gamers.forEach { gamer -> gamer.player.kickPlayer(KICK_SHUTDOWN) }

				WebAPI.saveProfiles(*gamers.toTypedArray())
			}
		}
	}
}
