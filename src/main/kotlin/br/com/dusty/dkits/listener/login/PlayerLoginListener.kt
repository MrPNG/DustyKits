package br.com.dusty.dkits.listener.login

import br.com.dusty.dkits.EnumServerStatus
import br.com.dusty.dkits.Main
import br.com.dusty.dkits.clan.ClanRegistry
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.store.EnumAdvantage
import br.com.dusty.dkits.store.Store
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.util.web.WebAPI
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

		val purchases = Store.parsePurchases(WebAPI.loadPurchases(player.uniqueId.toString()))

		purchases.run {
			loadRank(gamer)
			loadKits(gamer)
			loadAvantages(gamer)
		}

		gamer.purchases = purchases

		if (Main.data.serverStatus != EnumServerStatus.ONLINE && gamer.rank.isLowerThan(EnumRank.MOD)) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, KICK_NOT_READY)

			GamerRegistry.unregister(player.uniqueId)

			return
		}

		if (event.result == PlayerLoginEvent.Result.KICK_FULL) {
			if (gamer.rank.isLowerThan(EnumRank.MOD) && !gamer.hasAdvantage(EnumAdvantage.SLOT) && GamerRegistry.onlineGamers().filterNot { it.mode == EnumMode.ADMIN }.size >= Main.data.slots) {
				event.disallow(PlayerLoginEvent.Result.KICK_FULL, KICK_FULL_MESSAGE)

				GamerRegistry.unregister(player.uniqueId)

				return
			} else {
				event.allow()
			}
		}

		GamerRegistry.GAMER_BY_PLAYER.put(player.uniqueId, gamer)

		ClanRegistry.clan(gamer.primitiveGamer.clan)?.run {
			if (player.uniqueId.toString() in rawMembers) {
				gamer.clan = this

				onlineMembers.add(gamer)

				if (primitiveClan.leader == player.uniqueId.toString()) leader = gamer
			} else {
				gamer.primitiveGamer.clan = ""
			}
		}
	}
}
