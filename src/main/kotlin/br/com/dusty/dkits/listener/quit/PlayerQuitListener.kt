package br.com.dusty.dkits.listener.quit

import br.com.dusty.dkits.clan.ClanRegistry
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.Scoreboards
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object PlayerQuitListener: Listener {

	private val QUIT_MESSAGE_PREFIX = Text.basicOf("[").negative("-").basic("] ").toString()

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player

		GamerRegistry.GAMER_BY_PLAYER.remove(player)?.run {
			if (isCombatTagged()) {
				combatPartner?.kill(this)

				Bukkit.broadcastMessage(Text.negativeOf(player.displayName.clearFormatting()).basic(" deslogou em ").negative("combate").basic("!").toString())
			}

			//TODO: Reactivate Web API
//			if(Main.serverStatus == EnumServerStatus.ONLINE) Tasks.async(Runnable { WebAPI.saveProfiles(gamer) })

			if (clan?.onlineMembers?.size == 1) ClanRegistry.CLAN_BY_STRING.remove(clan!!.uuid)

			if (rank.isLowerThan(EnumRank.MOD)) event.quitMessage = QUIT_MESSAGE_PREFIX + player.displayName
			else event.quitMessage = null
		}

		Scoreboards.update()
	}
}
