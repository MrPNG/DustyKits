package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player
import java.lang.Exception

object InvisCommand: PlayerCustomCommand(EnumRank.MOD, "invis") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (sender.gamer().mode != EnumMode.ADMIN) sender.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" está no modo ").negative("ADMIN").basic("!").toString())
		else when (args.size) {
			0    -> {
				sender.sendMessage(Text.negativePrefix().negative("Uso:").basic(" /invis ").negative("<rank>").toString())
			}
			else -> {
				val rank: EnumRank = try {
					EnumRank.valueOf(args[0])
				} catch (e: Exception) {
					EnumRank.NONE
				}

				if (rank == EnumRank.NONE) {
					//TODO: Invis command finishing
				}
			}
		}

		return false
	}
}