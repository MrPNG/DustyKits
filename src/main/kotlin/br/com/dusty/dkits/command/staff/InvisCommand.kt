package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.CustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.Exception

object InvisCommand: CustomCommand(EnumRank.MOD, "invis") {

	override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean = when {
		!testPermission(sender) || sender !is Player || sender.gamer().mode != EnumMode.ADMIN -> true
		else                                                                                  -> {
			when (args.size) {
				0    -> {
					sender.sendMessage(Text.negativePrefix().negative("Uso:").basic(" /invis ").negative("<rank>").toString())

					true
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

					false
				}
			}
		}
	}
}