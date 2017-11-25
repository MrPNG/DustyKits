package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object SpyCommand: PlayerCustomCommand(EnumRank.ADMIN, "spy") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) {
			sender.sendMessage(Text.negativePrefix().basic("Uso: /spy ").negative("<jogador>").toString())
		} else {
			val player = Bukkit.getPlayerExact(args[0])

			if (player == null) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online com o nome \"").negative(args[0]).basic("\"!").toString())
			else player.gamer().chatSpies.add(sender)
		}

		return false
	}
}
