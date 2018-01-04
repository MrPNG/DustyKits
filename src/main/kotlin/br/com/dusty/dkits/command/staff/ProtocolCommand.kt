package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ProtocolCommand: PlayerCustomCommand(EnumRank.ADMIN, "protocols") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) {
			var text = Text.neutralPrefix()
			var first = true

			GamerRegistry.onlineGamers().sortedBy { it.protocolVersion }.forEach {
				val protocolVersion = it.protocolVersion

				text = text.basic((if (first) {
					first = false
					"Protocols: "
				} else {
					", "
				}) + it.player.name + " (" + (if (protocolVersion.min == protocolVersion.max) protocolVersion.min else (protocolVersion.min.toString() + "-" + protocolVersion.max)) + ": ").append(
						protocolVersion.string).basic(")")
			}

			sender.sendMessage(text.toString())
		} else {
			val player = Bukkit.getPlayerExact(args[0])

			if (player == null) {
				sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online com o nome \"").negative(args[0]).basic("\"!").toString())
			} else {
				val protocolVersion = player.gamer().protocolVersion

				sender.sendMessage(Text.neutralPrefix().basic("Protocol: " + player.name + " (" + (if (protocolVersion.min == protocolVersion.max) protocolVersion.min else (protocolVersion.min.toString() + "-" + protocolVersion.max)) + ": ").append(
						protocolVersion.string).basic(")").toString())
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = Bukkit.getOnlinePlayers().filter {
		sender.canSee(it) && it.name.startsWith(args[0], true)
	}.map { it.name }.toMutableList()
}
