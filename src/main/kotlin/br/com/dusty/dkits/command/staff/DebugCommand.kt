package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.ability.GladiatorAbility
import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.OneVsOneWarp
import org.bukkit.entity.Player

object DebugCommand: PlayerCustomCommand(EnumRank.ADMIN, "debug") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) {
			sender.sendMessage(Text.negativePrefix().negative("Uso:").basic(" /debug ").negative("<gladiator>/<1v1>").toString())
		} else {
			when (args[0].toLowerCase()) {
				"gladiator" -> {
					sender.sendMessage(GladiatorAbility.FIGHTS.toString())
				}
				"1v1"       -> {
					sender.sendMessage(OneVsOneWarp.FIGHTS.toString())
				}
			}
		}

		return false
	}
}
