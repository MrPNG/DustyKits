package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object SpyCommand: PlayerCustomCommand(EnumRank.ADMIN, "spy") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) {
			sender.sendMessage(Text.negativePrefix().basic("Uso: /spy ").negative("<jogador>").toString())
		} else {
			val player = Bukkit.getPlayerExact(args[0])

			if (player == null) {
				sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online com o nome \"").negative(args[0]).basic("\"!").toString())
			} else {
				val gamer = player.gamer()

				if (gamer.chatSpies.contains(sender)) {
					sender.sendMessage(Text.negativePrefix().basic("Agora você ").negative("não").basic(" está mais ").negative("espionando").basic(" as conversas privadas do jogador ").negative(
							player.name).basic("!").toString())

					gamer.chatSpies.remove(sender)
				} else {
					sender.sendMessage(Text.positivePrefix().basic("Agora você está ").positive("espionando").basic(" as conversas privadas do jogador ").positive(player.name).basic("!").toString())

					gamer.chatSpies.add(sender)
				}
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = Bukkit.getOnlinePlayers().filter {
		sender.canSee(it) && it.name.startsWith(args[0], true)
	}.map { it.name }.toMutableList()
}
