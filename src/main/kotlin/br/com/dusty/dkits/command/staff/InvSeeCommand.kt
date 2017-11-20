package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.CustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object InvSeeCommand: CustomCommand(EnumRank.MOD, "invsee") {

	override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean = when {
		!testPermission(sender) || sender !is Player || Gamer[sender].mode != EnumMode.ADMIN -> true
		else                                                                                 -> {
			when (args.size) {
				0    -> {
					sender.sendMessage(Text.negativePrefix().negative("Uso:").basic(" /invsee ").negative("<nomeDoJogador>").toString())

					true
				}
				else -> {
					val player = Bukkit.getPlayerExact(args[0])

					if (player == null) {
						sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um player online com o nome ").negative(args[0]).toString())

						true
					} else {
						sender.sendMessage(Text.positivePrefix().basic("Você está ").positive("vendo").basic(" o inventário de ").positive(player.name).toString())

						false
					}
				}
			}
		}
	}
}