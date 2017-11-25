package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.EnumChat
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

object AsyncPlayerChatListener: Listener {

	val STAFF_CHAT_PREFIX_NEUTRAL = Text.basicOf("[").neutral("Staff Chat").basic("] ")
	val STAFF_CHAT_PREFIX_NEGATIVE = Text.basicOf("[").negative("Staff Chat").basic("] ")

	var rank = EnumRank.DEFAULT

	@EventHandler
	fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
		val player = event.player
		val gamer = player.gamer()

		when (gamer.chat) {
			EnumChat.NORMAL -> {
				if (gamer.rank.isLowerThan(rank)) {
					event.isCancelled = true

					player.sendMessage(Text.negativePrefix().basic("O chat está ").negative("restrito").basic(" apenas a ").negative(rank.string).basic(" e acima!").toString())
				}
			}
			EnumChat.STAFF  -> {
				event.recipients.clear()

				val messageNeutral = STAFF_CHAT_PREFIX_NEUTRAL.append("<").append(gamer.rank.format(player.name)).append("> ").neutral(event.message).toString()
				val messageNegative = STAFF_CHAT_PREFIX_NEGATIVE.append("<").append(gamer.rank.format(player.name)).append("> ").negative(event.message).toString()

				GamerRegistry.onlineGamers().filter { it.rank.isHigherThanOrEquals(EnumRank.MOD) }.forEach {
					it.player.sendMessage(if (it.chat == EnumChat.STAFF) messageNeutral else messageNegative)
				}
			}
		}
	}
}
