package br.com.dusty.dkits.listener.quit

import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.Scoreboards
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object PlayerQuitListener: Listener {

	private val QUIT_MESSAGE_PREFIX = Text.basicOf("[").negative("-").basic("] ").toString()

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player

		val gamer = GamerRegistry.GAMER_BY_PLAYER.remove(player)

		if (gamer != null) {
			if (gamer.isCombatTagged()) {
				gamer.combatPartner?.kill(gamer)

				Bukkit.broadcastMessage(Text.negativeOf(player.displayName.clearFormatting()).basic(" deslogou em ").negative("combate").basic("!").toString())
			}

			//TODO: Reactivate Web API
//			Tasks.async(Runnable { WebAPI.saveProfiles(gamer) })

			if (gamer.rank.isLowerThan(EnumRank.MOD)) event.quitMessage = QUIT_MESSAGE_PREFIX + player.displayName
			else event.quitMessage = null
		}

		Scoreboards.update()
	}
}
