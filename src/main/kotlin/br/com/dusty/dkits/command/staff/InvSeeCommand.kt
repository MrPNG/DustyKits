package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.inventory.openInventory
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object InvSeeCommand: PlayerCustomCommand(EnumRank.MOD, "invsee") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (sender.gamer().mode != EnumMode.ADMIN) sender.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" está no modo ").negative("ADMIN").basic("!").toString())
		else when (args.size) {
			0    -> {
				sender.sendMessage(Text.negativePrefix().basic("Uso: /invsee ").negative("<nomeDoJogador>").toString())
			}
			else -> {
				val player = Bukkit.getPlayerExact(args[0])

				if (player == null) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online com o nome ").negative(args[0]).toString())
				else sender.openInventory(player)
			}
		}

		return false
	}
}