package br.com.dusty.dkits.listener.login

import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.ScoreboardUtils
import br.com.dusty.dkits.util.bossbar.BossBarUtils
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warps
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinListener: Listener {

	private val KICK_NOT_READY = Text.negativeOf("O servidor ainda não está pronto!\n\nVolte em alguns segundos...").toString()

	private val JOIN_MESSAGE_PREFIX = Text.basicOf("[").positive("+").basic("] ").toString()

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player

		val gamer = Gamer.of(player)

		if (gamer.protocolVersion == null) {
			player.kickPlayer(KICK_NOT_READY)
			return
		}

		if (gamer.rank.isLowerThan(EnumRank.MOD))
			event.joinMessage = JOIN_MESSAGE_PREFIX + player.name
		else
			event.joinMessage = null

		ScoreboardUtils.create(player)
		ScoreboardUtils.updateAll()

		gamer.sendToWarp(Warps.LOBBY)

		if (gamer.protocolVersion.isLowerThan(EnumProtocolVersion.RELEASE_1_8))
			BossBarUtils.MAIN.send(player)
	}
}
