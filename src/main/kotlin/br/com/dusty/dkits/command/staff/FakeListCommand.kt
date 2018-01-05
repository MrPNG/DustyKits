package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player

object FakeListCommand: PlayerCustomCommand(EnumRank.MOD, "fakelist") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		var text = Text.neutralPrefix().basic("Jogadores usando nicks ").neutral("falsos").basic(":")

		GamerRegistry.onlineGamers().forEach {
			if (gamer.rank.isHigherThanOrEquals(it.rank) && it.displayName != it.player.name) text = text.append("\n  ").basic("- ").neutral(it.player.name).basic(" » ").neutral(it.displayName)
		}

		sender.sendMessage(text.toString())

		return false
	}
}
