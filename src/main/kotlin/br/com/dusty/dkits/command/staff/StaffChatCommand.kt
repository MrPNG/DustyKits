package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumChat
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player

object StaffChatCommand: PlayerCustomCommand(EnumRank.MOD, "staffchat", "sc") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		if (gamer.chat != EnumChat.STAFF) {
			gamer.chat = EnumChat.STAFF

			sender.sendMessage(Text.positivePrefix().basic("Agora você ").positive("está").basic(" no chat da ").positive("staff").basic("!").toString())
		} else {
			gamer.chat = EnumChat.NORMAL

			sender.sendMessage(Text.negativePrefix().basic("Agora você ").negative("não").basic(" está mais no chat da ").negative("staff").basic("!").toString())
		}

		return false
	}
}
