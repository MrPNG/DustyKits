package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun Player.openInventory(player: Player) {
	openInventory(player.inventory)
	sendMessage(Text.positivePrefix().basic("Você está ").positive("vendo").basic(" o inventário de ").positive(player.name).toString())
}

object InvSeeCommand: PlayerCustomCommand(EnumRank.MOD, "invsee") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean = when {
		sender.gamer().mode != EnumMode.ADMIN -> {
			sender.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" está no modo ").negative("ADMIN").basic("!").toString())

			true
		}
		else                                  -> {
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
						sender.openInventory(player)

						false
					}
				}
			}
		}
	}
}