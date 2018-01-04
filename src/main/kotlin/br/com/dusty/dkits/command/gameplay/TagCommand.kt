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

			when {
				tag == gamer.tag             -> sender.sendMessage(Text.negativePrefix().basic("Sua ").negative("tag").basic(" já é ").negative(tag.string).basic("!").toString())
				tag.isHigherThan(gamer.rank) -> sender.sendMessage(Text.negativePrefix().basic("Essa ").negative("tag").basic(" é mais ").negative("alta").basic(" do que você pode usar!").toString())
				else                         -> {
					gamer.tag = tag

					sender.sendMessage(Text.positivePrefix().basic("Agora sua ").positive("tag").basic(" é ").positive(tag.string).basic("!").toString())
				}
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = EnumRank.values().filter {
		it.isLowerThanOrEquals(sender.gamer().rank) && it.name.startsWith(args[0], true)
	}.map { it.name.toLowerCase() }.toMutableList()
}
