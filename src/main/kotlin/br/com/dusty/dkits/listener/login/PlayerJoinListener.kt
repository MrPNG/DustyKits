package br.com.dusty.dkits.listener.login

import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.Scoreboards
import br.com.dusty.dkits.util.bossbar.BossBars
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

		val protocolVersion = Protocols.protocolVersion(player)

		if (protocolVersion == null) {
			player.kickPlayer(KICK_NOT_READY)
			return
		} else {
			gamer.protocolVersion = EnumProtocolVersion[Protocols.protocolVersion(player)]
		}

		if (gamer.rank.isLowerThan(EnumRank.YOUTUBER)) event.joinMessage = JOIN_MESSAGE_PREFIX + player.name
		else event.joinMessage = null

		gamer.createScoreboard()
		Scoreboards.update()

		gamer.sendToWarp(Warps.LOBBY)

		if (gamer.protocolVersion.isLowerThan(EnumProtocolVersion.RELEASE_1_8)) BossBars.MAIN.send(player)
		//TODO: Titles/subtitles for 1.8+ players
	}
}
