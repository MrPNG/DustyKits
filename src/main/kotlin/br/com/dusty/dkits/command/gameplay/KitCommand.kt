package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.text.Text
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object KitCommand: PlayerCustomCommand(EnumRank.DEFAULT, "kit") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		if (args.isEmpty()) {
			//TODO: Open kit menu
		} else {
			val kit = Kits[args[0]]

			if (kit == Kits.NONE || !kit.data.isEnabled) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um kit com o nome \"").negative(args[0]).basic("\"!").toString())
			else kit.setAndApplyIfAllowed(gamer)
		}

		return false
	}

	override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String>? {
		return if (sender !is Player) arrayListOf()
		else Kits.KITS.filter {
			it.data.isEnabled && sender.gamer().warp.enabledKits.contains(it) && (args.isEmpty() || it.name.startsWith(args[0], true))
		}.map { it.name.toLowerCase() }.toMutableList()
	}
}
