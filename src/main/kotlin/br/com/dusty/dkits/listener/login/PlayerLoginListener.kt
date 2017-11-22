package br.com.dusty.dkits.listener.login

import br.com.dusty.dkits.EnumServerStatus
import br.com.dusty.dkits.Main
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

object PlayerLoginListener: Listener {

	private val KICK_NOT_READY = Text.negativeOf("O servidor ainda não está aberto!\n\nVolte em alguns instantes...").toString()

	private val KICK_FULL_MESSAGE = Text.negativeOf("O servidor está cheio!\n\n").basic("Compre ").append("PRO").color(TextColor.GOLD).basic(" ou um ").append("Slot Reservado").color(TextColor.GOLD).basic(
			" no site ").append("loja.dusty.com.br").color(TextColor.GOLD).basic(" e entre agora!").toString()

	@EventHandler
	fun onPlayerLogin(event: PlayerLoginEvent) {
		val player = event.player

		if (GamerRegistry.PRIMITIVE_GAMER_BY_UUID[player.uniqueId] == null) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, KICK_NOT_READY)

			return
		}

		val gamer = player.gamer()

		if (Main.serverStatus != EnumServerStatus.ONLINE && gamer.rank.isLowerThan(EnumRank.MOD)) {
			GamerRegistry.PRIMITIVE_GAMER_BY_UUID.remove(player.uniqueId)

			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, KICK_NOT_READY)

			return
		}

		if (event.result == PlayerLoginEvent.Result.KICK_FULL) {
			if (gamer.canLogin()) event.allow()
			else event.disallow(PlayerLoginEvent.Result.KICK_FULL, KICK_FULL_MESSAGE)
		}
	}
}
