package br.com.dusty.dkits.listener.login

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.Scoreboards
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.bossbar.BossBars
import br.com.dusty.dkits.util.gamer.Tags
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.protocol.Protocols
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warps
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinListener: Listener {

	private val KICK_NOT_READY = Text.negativeOf("O servidor ainda não está aberto!\n\nVolte em alguns instantes...").toString()

	private val JOIN_MESSAGE_PREFIX = Text.basicOf("[").positive("+").basic("] ").toString()

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		val gamer = player.gamer()

		val protocolVersion = EnumProtocolVersion[Protocols.protocolVersion(player)]

		if (protocolVersion == EnumProtocolVersion.UNKNOWN) {
			player.kickPlayer(KICK_NOT_READY)
		} else {
			gamer.protocolVersion = protocolVersion

			if (protocolVersion.isLowerThan(EnumProtocolVersion.RELEASE_1_8)) BossBars.MAIN.send(player)
			//TODO: Titles/subtitles for 1.8+ players

			if (gamer.rank.isLowerThan(EnumRank.MOD)) event.joinMessage = JOIN_MESSAGE_PREFIX + player.displayName
			else event.joinMessage = null

			Tags.applyTag(gamer)

			gamer.createScoreboard()
			Scoreboards.update()

			gamer.sendToWarp(Warps.LOBBY, false)

			Tasks.sync(Runnable {
				when {
					gamer.rank.isLowerThan(EnumRank.MOD) -> gamer.mode = EnumMode.PLAY
					else                                 -> gamer.mode = EnumMode.ADMIN
				}

				GamerRegistry.onlineGamers().filter { gamer.rank.isLowerThan(it.visibleTo) }.forEach { player.hidePlayer(it.player) }
			})
		}
	}
}
