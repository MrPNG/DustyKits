package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

object ReportCommand: PlayerCustomCommand(EnumRank.DEFAULT, "report") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.size < 2) {
			sender.sendMessage(Text.negativePrefix().basic("Uso: /report ").negative("<jogador> <motivo>").toString())
		} else {
			val player = Bukkit.getPlayerExact(args[0])

			if (player == null) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online com o nome \"").negative(args[0]).basic("\"!").toString())
			else GamerRegistry.onlineGamers().forEach {
				if (it.rank.isHigherThanOrEquals(EnumRank.MOD)) {
					val player = it.player

					player.playSound(player.location, Sound.ENTITY_CHICKEN_HURT, 10F, 1F)
					player.sendMessage(Text.negativePrefix().basic("[").negative("Report").basic("]: \n").negative("Jogador").basic(": " + player.name + "\n").negative("Motivo").basic(": " + args.copyOfRange(
							0,
							args.lastIndex).joinToString(separator = " ")).toString())
				}
			}
		}

		return false
	}
}
