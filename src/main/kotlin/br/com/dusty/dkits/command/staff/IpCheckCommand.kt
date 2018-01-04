package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object IpCheckCommand: PlayerCustomCommand(EnumRank.ADMIN, "ipcheck") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		if (args.isEmpty()) {
			sender.sendMessage(Text.negativePrefix().basic("Uso: /ipcheck ").negative("<jogador>").toString())
		} else {
			val player = Bukkit.getPlayerExact(args[0])

			if (player == null) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online com o nome \"").negative(args[0]).basic("\"!").toString())
			else sender.sendMessage(Text.positivePrefix().basic("O ").positive("ip").basic(" do jogador ").positive(player.name).basic(" é ").positive(player.address.address.hostAddress).toString())
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = Bukkit.getOnlinePlayers().filter {
		sender.canSee(it) && it.name.startsWith(args[0], true)
	}.map { it.name }.toMutableList()
}
