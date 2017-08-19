package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.CustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.text.Text
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class KitCommand(rank: EnumRank, alias: String): CustomCommand(rank, alias) {

	override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
		if (!testPermission(sender))
			return true

		if (sender !is Player)
			return true

		val gamer = Gamer.of(sender)

		if (args.isEmpty()) {
			//TODO: Open kit menu
		} else {
			val kit = Kits.byName(args[0])

			if (kit == null)
				sender.sendMessage(Text.negativePrefix()
						                   .basic("Não")
						                   .basic(" há um kit com o nome \"")
						                   .negative(args[0])
						                   .basic("\"!")
						                   .toString())
			else
				kit.applyIfAllowed(gamer)
		}

		return false
	}

	override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String>? {
		val tabCompletions = ArrayList<String>()

		if (sender !is Player)
			return tabCompletions

		val gamer = Gamer.of(sender)

		Kits.KITS
				.filter {
					it.data.isEnabled && gamer.warp.enabledKits.contains(it) && (args.isEmpty() || it.name.startsWith(
							args[0],
							true))
				}
				.mapTo(tabCompletions) { it.name.toLowerCase() }

		return tabCompletions
	}
}
