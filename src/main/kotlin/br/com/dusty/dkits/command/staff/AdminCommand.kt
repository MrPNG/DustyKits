package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.CustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.Gamer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object AdminCommand: CustomCommand(EnumRank.MOD, "admin") {

	override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean = when {
		!testPermission(sender) || sender !is Player -> true
		else                                         -> {
			val gamer = Gamer[sender]
			gamer.mode = if (gamer.mode === EnumMode.ADMIN) EnumMode.PLAY else EnumMode.ADMIN

			false
		}
	}
}
