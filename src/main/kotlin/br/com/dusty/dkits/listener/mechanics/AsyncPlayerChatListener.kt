package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.EnumChat.*
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.util.text.TextStyle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

object AsyncPlayerChatListener: Listener {

	val STAFF_CHAT_PREFIX_POSITIVE = Text.basicOf("[").positive("Staff Chat").basic("] ")
	val STAFF_CHAT_PREFIX_NEGATIVE = Text.basicOf("[").negative("Staff Chat").basic("] ")

	val CLAN_CHAT_PREFIX_POSITIVE = Text.basicOf("[").positive("Clan Chat").basic("] ")
	val CLAN_CHAT_PREFIX_NEGATIVE = Text.basicOf("[").negative("Clan Chat").basic("] ")

	var rank = EnumRank.DEFAULT

	@EventHandler
	fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp.overrides(event)) return

		when (gamer.chat) {
			NORMAL -> {
				if (gamer.rank.isLowerThan(rank)) {
					event.isCancelled = true

					player.sendMessage(Text.negativePrefix().basic("O chat está ").negative("restrito").basic(" apenas a ").negative(rank.string).basic(" e acima!").toString())
				}

				event.format = "" + Text.basicOf(if (gamer.clan != null) gamer.clan!!.tag + " " else "").toString() + "%s" + TextColor.GRAY + ":" + TextStyle.RESET + " %s"
			}
			CLAN   -> {
				event.recipients.clear()

				val messagePositive = CLAN_CHAT_PREFIX_POSITIVE.basic(if (gamer.clan != null) gamer.clan!!.tag + " " else "").append(gamer.rank.format(player.name) + TextStyle.RESET).basic(": ").neutral(
						event.message).toString()
				val messageNegative = CLAN_CHAT_PREFIX_NEGATIVE.basic(if (gamer.clan != null) gamer.clan!!.tag + " " else "").append(gamer.rank.format(player.name) + TextStyle.RESET).basic(": ").negative(
						event.message).toString()

				GamerRegistry.onlineGamers().filter { it.clan == gamer.clan }.forEach {
					it.player.sendMessage(if (it.chat == CLAN) messagePositive else messageNegative)
				}
			}
			STAFF  -> {
				event.recipients.clear()

				val messagePositive = STAFF_CHAT_PREFIX_POSITIVE.basic(if (gamer.clan != null) gamer.clan!!.tag + " " else "").append(gamer.rank.format(player.name) + TextStyle.RESET).basic(": ").neutral(
						event.message).toString()
				val messageNegative = STAFF_CHAT_PREFIX_NEGATIVE.basic(if (gamer.clan != null) gamer.clan!!.tag + " " else "").append(gamer.rank.format(player.name) + TextStyle.RESET).basic(": ").negative(
						event.message).toString()

				GamerRegistry.onlineGamers().filter { it.rank.isHigherThanOrEquals(EnumRank.MOD) }.forEach {
					it.player.sendMessage(if (it.chat == STAFF) messagePositive else messageNegative)
				}
			}
		}
	}
}
