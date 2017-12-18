package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player

object ConfigCommand: PlayerCustomCommand(EnumRank.ADMIN, "config") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		when {
			args.size < 2 -> sender.sendMessage(Text.negativePrefix().negative("Uso:").basic(" /config ").negative("<slots> <númeroDeSlots>").toString())
			else          -> when (args[0].toLowerCase()) {
				"slots" -> {
					val slots = args[1].toIntOrNull() ?: 0

					sender.sendMessage(Text.positivePrefix().basic("Agora o número de ").positive("\'slots\'").basic(" do servidor é ").positive(slots).basic("!").toString())

					Main.data.slots
					Main.saveData()
				}
			}
		}

		return false
	}
}
