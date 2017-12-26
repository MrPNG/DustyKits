package br.com.dusty.dkits.command.overwrite

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object TellCommand: PlayerCustomCommand(EnumRank.DEFAULT, "tell", "msg", "w", "r") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		when (alias) {
			"tell", "msg", "w" -> {
				if (args.size < 2) {
					sender.sendMessage(Text.negativePrefix().basic("Uso: /tell ").negative("<jogador> <mensagem>").toString())
				} else {
					val player = Bukkit.getPlayerExact(args[0])

					if (player == null) {
						sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online com o nome \"").negative(args[0]).basic("\"!").toString())
					} else {
						val partner = player.gamer()

						//TODO: Ignore implementation
						gamer.chatPartner = partner
						partner.chatPartner = gamer

						val message = args.copyOfRange(1, args.size).joinToString(separator = " ")

						sender.sendMessage(Text.basicOf("[" + sender.displayName.clearFormatting() + " -> " + player.displayName.clearFormatting() + "] " + message).toString())
						player.sendMessage(Text.basicOf("[" + player.displayName.clearFormatting() + " <- " + sender.displayName.clearFormatting() + "] " + message).toString())

						gamer.chatSpies.forEach {
							it.sendMessage(Text.basicOf("[").negative("Spy").basic("] (").basic(sender.displayName.clearFormatting() + " -> " + player.displayName.clearFormatting() + "): " + message).toString())
						}
					}
				}
			}
			"r"                -> {
				when {
					args.isEmpty()                                                    -> sender.sendMessage(Text.negativePrefix().basic("Uso: /r ").negative("<mensagem>").toString())
					gamer.chatPartner == null || !gamer.chatPartner!!.player.isOnline -> {
						gamer.chatPartner = null

						sender.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" está conversando com ").negative("ninguém").basic("!").toString())
					}
					else                                                              -> {
						val player = gamer.chatPartner!!.player

						val message = args.copyOfRange(0, args.size).joinToString(separator = " ")

						//TODO: Ignore implementation
						sender.sendMessage(Text.basicOf("[" + sender.displayName.clearFormatting() + " -> " + player.displayName.clearFormatting() + "] " + message).toString())
						player.sendMessage(Text.basicOf("[" + player.displayName.clearFormatting() + " <- " + sender.displayName.clearFormatting() + "] " + message).toString())

						gamer.chatSpies.forEach {
							it.sendMessage(Text.basicOf("[").negative("Spy").basic("]: ").basic(sender.displayName.clearFormatting() + " -> " + player.displayName.clearFormatting() + ": " + message).toString())
						}
					}
				}
			}
		}

		return false
	}
}
