package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.leaderboard.Leaderboard
import br.com.dusty.dkits.util.leaderboard.Leaderboards
import br.com.dusty.dkits.util.normalize
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player

object LeaderboardCommand: PlayerCustomCommand(EnumRank.ADMIN, "leaderboard") {

	val USAGE = Text.negativePrefix().negative("Uso:").basic(" /leaderboard ").negative("<tipo> <quantidade> <desc>/<asc>").toString()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.size < 3) sender.sendMessage(USAGE)
		else {
			val type = Leaderboards.EnumLeaderboardType.values().firstOrNull { it.name.toLowerCase() == args[0].toLowerCase() }

			if (type == null) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um tipo de ").negative("leaderboard").basic(" com o nome ").negative(args[0]).basic("!").toString())
			else {
				val amount = args[1].toIntOrNull()

				if (amount == null) sender.sendMessage(Text.negativePrefix().basic("\"").negative(args[1]).basic("\" não é um ").negative("número").basic(" válido!").toString())
				else {
					val descending = args[2] == "desc"

					Leaderboard(sender.location.normalize().add(0.0, -1.75, 0.0), type, amount, descending)

					sender.sendMessage(Text.positivePrefix().basic("Você ").positive("criou").basic(" uma ").positive("leaderboard").basic(" do tipo ").positive(type.name).basic("!").toString())
				}
			}
		}

		return false
	}
}
