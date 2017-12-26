package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player

object TagCommand: PlayerCustomCommand(EnumRank.DEFAULT, "tag") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		if (args.isEmpty()) sender.sendMessage(Text.negativePrefix().basic("Uso: /tag ").negative("<tag>").toString())
		else {
			val tag = EnumRank.values().firstOrNull { it.name.equals(args[0], true) } ?: gamer.tag

			gamer.tag = tag

			sender.sendMessage(Text.positivePrefix().basic("Agora sua ").positive("tag").basic(" é ").positive(tag.string).basic("!").toString())
		}

		return false
	}
}
