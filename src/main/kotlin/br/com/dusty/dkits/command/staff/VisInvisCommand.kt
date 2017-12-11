package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player

object VisInvisCommand: PlayerCustomCommand(EnumRank.MOD, "vis", "invis") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		if (gamer.mode != EnumMode.ADMIN) sender.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" está no modo ").negative("ADMIN").basic("!").toString())
		else when (alias) {
			"vis"   -> when (args.size) {
				0 -> {
					gamer.visibleTo = EnumRank.DEFAULT
				}
				1 -> {
					val rank = EnumRank[args[0]]

					when {
						rank == EnumRank.NONE         -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um rank com o nome \"").negative(args[0]).basic("\"!").toString())
						rank.isHigherThan(gamer.rank) -> sender.sendMessage(Text.negativePrefix().basic("Você ").negative("somente").basic(" pode ficar ").negative("invisível").basic(" para jogadores que tenham um rank ").negative(
								"menor").basic(" do que o seu!").toString())
						else                          -> gamer.visibleTo = rank
					}
				}
			}
			"invis" -> when (args.size) {
				0 -> {
					gamer.visibleTo = gamer.rank
				}
				1 -> {
					val rank = EnumRank[args[0]]

					when {
						rank == EnumRank.NONE                 -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um rank com o nome \"").negative(args[0]).basic("\"!").toString())
						rank.isHigherThanOrEquals(gamer.rank) -> sender.sendMessage(Text.negativePrefix().basic("Você ").negative("somente").basic(" pode ficar ").negative("invisível").basic(" para jogadores que tenham um rank ").negative(
								"menor").basic(" do que o seu!").toString())
						else                                  -> gamer.visibleTo = if (rank.hasNext()) rank.next() else EnumRank.ADMIN
					}
				}
			}
		}

		return false
	}
}