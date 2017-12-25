package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.leaderboard.Leaderboard
import br.com.dusty.dkits.util.leaderboard.Leaderboards
import br.com.dusty.dkits.util.normalize
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player

object LeaderboardCommand: PlayerCustomCommand(EnumRank.ADMIN, "leaderboard") {

	val USAGE_ADD = Text.negativePrefix().negative("Uso:").basic(" /leaderboard ").negative("<tipo> <quantidade> <desc>/<asc>").toString()
	val USAGE_REMOVE = Text.negativePrefix().negative("Uso:").basic(" /leaderboard ").negative("<remove> <raio>").toString()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isNotEmpty()) {
			when {
				args[0] == "remove" -> {
					if (args.size < 2) sender.sendMessage(USAGE_REMOVE)
					else {
						val radius = args[1].toIntOrNull()

						if (radius == null) sender.sendMessage(Text.negativePrefix().basic("\"").negative(args[1]).basic("\" não é um ").negative("número").basic(" válido!").toString())
						else {
							val removed = arrayListOf<Leaderboard>()

							Leaderboards.leaderboards.filter { it.location.distance(sender.location) < radius }.forEach {
								it.remove()

								removed.add(it)
							}

							if (removed.isEmpty()) sender.sendMessage(Text.negativePrefix().basic("Não há ").negative("nenhuma").basic(" leaderboard nesse ").negative("raio").basic("!").toString())
							else sender.sendMessage(Text.positivePrefix().basic("Você removeu ").positive(removed.size).basic(" leaderboards!").toString())

							Tasks.async(Runnable {
								Leaderboards.leaderboards.removeAll(removed)
								Leaderboards.saveData()
							})
						}
					}
				}
				args.size < 3       -> sender.sendMessage(USAGE_ADD)
				else                -> {
					val type = Leaderboards.EnumLeaderboardType.values().firstOrNull { it.name.toLowerCase() == args[0].toLowerCase() }

					if (type == null) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um tipo de ").negative("leaderboard").basic(" com o nome ").negative(args[0]).basic("!").toString())
					else {
						val amount = args[1].toIntOrNull()

						if (amount == null) sender.sendMessage(Text.negativePrefix().basic("\"").negative(args[1]).basic("\" não é um ").negative("número").basic(" válido!").toString())
						else {
							val descending = args[2] == "desc"

							Leaderboards.leaderboards.add(Leaderboard(sender.location.normalize().add(0.0, -1.75, 0.0), type, amount, descending))
							Leaderboards.saveData()

							sender.sendMessage(Text.positivePrefix().basic("Você ").positive("criou").basic(" uma ").positive("leaderboard").basic(" do tipo ").positive(type.name).basic("!").toString())
						}
					}
				}
			}
		}

		return false
	}
}
