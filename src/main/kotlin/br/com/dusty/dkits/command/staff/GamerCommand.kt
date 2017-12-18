package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.clearFormatting
import org.bukkit.entity.Player

object GamerCommand: PlayerCustomCommand(EnumRank.ADMIN, "gamers") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val list = GamerRegistry.onlineGamers()

		sender.sendMessage("Gamers (" + list.size + "): " + list.toString().clearFormatting())

		return false
	}
}
