package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player
import java.text.DecimalFormat

object ProfileCommand: PlayerCustomCommand(EnumRank.DEFAULT, "perfil") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		if (args.isEmpty()) {
			sender.sendMessage(Text.positivePrefix().basic("Seu perfil:").positive("\nKills: ").basic(gamer.kills).positive("\nDeaths: ").basic(gamer.deaths).positive("\nKDR: ").basic(
					DecimalFormat("#.##").format(gamer.kills / gamer.deaths)).positive("\nKillstreak Máximo: ").basic(gamer.maxKillStreak).positive("\nXP: ").basic(gamer.xp.toInt()).positive("\nCréditos: ").basic(
					gamer.money.toInt()).positive("\nVitórias no Evento HG: ").basic(gamer.hgWins).positive("\nPerdas no Evento HG: ").basic(gamer.hgLosses).toString())
		} else {
			//TODO: Profile for another player
		}

		return false
	}
}
