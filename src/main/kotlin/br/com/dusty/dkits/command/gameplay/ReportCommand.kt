package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

object ReportCommand: PlayerCustomCommand(EnumRank.DEFAULT, "dustyreport") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.size < 2) {
			sender.sendMessage(Text.negativePrefix().basic("Uso: /report ").negative("<jogador> <motivo>").toString())
		} else {
			val player = Bukkit.getPlayerExact(args[0])

			if (player == null) {
				sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online com o nome \"").negative(args[0]).basic("\"!").toString())
			} else {
				val reason = args.copyOfRange(1, args.size).joinToString(separator = " ")

				GamerRegistry.onlineGamers().forEach {
					if (it.rank.isHigherThanOrEquals(EnumRank.MOD)) {
						val staffPlayer = it.player

						staffPlayer.playSound(staffPlayer.location, Sound.CHICKEN_HURT, 1F, 1F)
						staffPlayer.sendMessage(Text.negativePrefix().basic("[").negative("Report").basic("]: \n").negative("  Jogador").basic(": " + player.name + "\n").negative("  Motivo").basic(
								": " + reason + "\n").negative("  Por").basic(": " + sender.name).toString())
					}
				}

				Tasks.async(Runnable { println(WebAPI.report(player.name, sender.name, reason)) })
			}
		}

		return false
	}
}
