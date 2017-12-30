package br.com.dusty.dkits.listener.quit

import br.com.dusty.dkits.Config
import br.com.dusty.dkits.clan.ClanRegistry
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.scoreboard.Scoreboards
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.stdlib.clearFormatting
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import br.com.dusty.dkits.warp.Warps
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object PlayerQuitListener: Listener {

	private val QUIT_MESSAGE_PREFIX = Text.basicOf("[").negative("-").basic("] ").toString()

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player
		val gamer = player.gamer()

		GamerRegistry.unregister(player.uniqueId)?.run {
			if (isCombatTagged()) {
				Bukkit.broadcastMessage(Text.negativePrefix().negative(player.displayName.clearFormatting()).basic(" deslogou em ").negative("combate").basic("!").toString())

				combatPartner?.kill(this)
			}

			warp.dispatchGamer(this, Warps.NONE)

			if (Config.data.serverStatus == Config.EnumServerStatus.ONLINE) Tasks.async(Runnable { WebAPI.saveProfiles(gamer) })

			if (clan != null) {
				val clan = clan!!

				clan.onlineMembers.remove(this)

				if (clan.leader == this) clan.leader = null

				if (clan.onlineMembers.isEmpty()) ClanRegistry.CLAN_BY_STRING.remove(clan.uuid)
			}

			if (rank.isLowerThan(EnumRank.MOD)) event.quitMessage = QUIT_MESSAGE_PREFIX + player.displayName.clearFormatting()
			else event.quitMessage = null
		}

		Scoreboards.update()
	}
}
