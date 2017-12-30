package br.com.dusty.dkits.listener.login

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.scoreboard.Scoreboards
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.bossbar.BossBars
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.protocol.Protocols
import br.com.dusty.dkits.util.stdlib.clearFormatting
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.warp.Warps
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinListener: Listener {

	private val COULDNT_CHECK_VERSION = Text.negativeOf("Não foi possível identificar a versão de seu jogo!\n\nVerifique se você está usando mods que alterem sua versão...").toString()

	private val JOIN_MESSAGE_PREFIX = Text.basicOf("[").positive("+").basic("] ").toString()

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		val gamer = player.gamer()

		val protocolVersion = EnumProtocolVersion[Protocols.VIA_VERSION_API.getPlayerVersion(player)]

		if (protocolVersion == EnumProtocolVersion.UNKNOWN) player.kickPlayer(COULDNT_CHECK_VERSION)
		else {
			gamer.protocolVersion = protocolVersion

//		if (protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) {
//				player.sendTitle(Text.of("Bem vindo ao ").color(TextColor.RED).append("Dusty").color(TextColor.GOLD).append("!").color(TextColor.RED).toString(),
//				                 Text.basicOf("Escolha uma warp e divirta-se, ").append(player.name).color(TextColor.GOLD).basic("!").toString(),
//				                 10,
//				                 80,
//				                 10) //TODO: 1.8 switch
//		} else {
			BossBars.MAIN.addPlayer(player)

			player.sendMessage(" \n" + Text.of("Bem vindo ao ").color(TextColor.RED).append("Dusty").color(TextColor.GOLD).append("!").color(TextColor.RED).toString() + "\n\n" + Text.basicOf("Escolha uma warp e divirta-se, ").append(
					player.name).color(TextColor.GOLD).basic("!").toString() + "\n ")
		}

		if (gamer.rank.isLowerThan(EnumRank.MOD)) event.joinMessage = JOIN_MESSAGE_PREFIX + player.displayName.clearFormatting()
		else event.joinMessage = null

		gamer.sendToWarp(Warps.LOBBY, true, false)

		Tasks.sync(Runnable {
			gamer.run {
				tag = rank

				when {
					rank.isLowerThan(EnumRank.MOD) -> player.gameMode = GameMode.ADVENTURE
					else                           -> mode = EnumMode.ADMIN
				}

				hidePlayers()
				createScoreboard()
			}

			Scoreboards.update()
		})
//		}
	}
}
