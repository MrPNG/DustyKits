package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import org.bukkit.entity.Player

object AdminCommand: PlayerCustomCommand(EnumRank.MOD, "admin") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()
		gamer.mode = if (gamer.mode == EnumMode.ADMIN) EnumMode.PLAY else EnumMode.ADMIN

		return false
	}
}
