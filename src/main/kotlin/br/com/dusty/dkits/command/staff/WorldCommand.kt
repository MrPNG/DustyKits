package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.Worlds
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object WorldCommand: PlayerCustomCommand(EnumRank.ADMIN, "world") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.size < 2) sender.sendMessage(Text.negativePrefix().basic("Uso: /world ").negative("<load>/<unload> <nomeDoMundo>").basic(" [edit]").toString())
		else when (args[0].toLowerCase()) {
			"load"   -> {
				when {
					!Worlds.exists(args[1])  -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um ").negative("mundo").basic(" com o nome ").negative("\"${args[1]}\"").basic(
							"!").toString())
					Worlds.isLoaded(args[1]) -> {
						sender.sendMessage(Text.positivePrefix().basic("Este mundo ").positive("já").basic(" foi ").positive("carregado").basic(", você será ").positive("teleportado").basic(" ao spawn dele...").toString())

						if (sender.teleport(Bukkit.getWorld(args[1]).spawnLocation)) sender.sendMessage(Text.positivePrefix().basic("Você foi ").positive("teleportado").basic("!").toString())
						else sender.sendMessage(Text.positivePrefix().basic("Algo deu ").positive("errado").basic("!").toString())
					}
					else                     -> {
						sender.sendMessage(Text.positivePrefix().basic("Este mundo será ").positive("carregado").basic(" agora e você será ").positive("teleportado").basic(" ao spawn dele em seguida...").toString())

						if (sender.teleport(Worlds.load(args[1],
						                                args.size > 2 && args[2] == "edit")?.spawnLocation)) sender.sendMessage(Text.positivePrefix().basic("Você foi ").positive("teleportado").basic(
								"!").toString())
						else sender.sendMessage(Text.positivePrefix().basic("Algo deu ").positive("errado").basic("!").toString())
					}
				}
			}
			"unload" -> {
				val name = args[1].toLowerCase()

				when {
					!Worlds.exists(name)   -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um ").negative("mundo").basic(" com o nome ").negative("\"${args[1]}\"").basic(
							"!").toString())
					!Worlds.isLoaded(name) -> sender.sendMessage(Text.negativePrefix().basic("Esse mundo ").negative("não").basic(" foi ").negative("carregado").basic("!").toString())
					else                   -> {
						sender.sendMessage(Text.positivePrefix().basic("Esse ").positive("mundo").basic(" será ").positive("fechado").basic(" agora...").toString())

						Worlds.rollback(name)
					}
				}
			}
		}

		return false
	}
}