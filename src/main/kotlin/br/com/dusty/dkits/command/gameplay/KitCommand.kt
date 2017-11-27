package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.inventory.KitMenu
import br.com.dusty.dkits.util.text.Text
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object KitCommand: PlayerCustomCommand(EnumRank.DEFAULT, "kit", *Kits.enabledKitsNames) {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		when (alias) {
			"kit" -> {
				if (args.isEmpty()) {
					sender.openInventory(KitMenu.menuKitOwned(sender))
				} else {
					val kit = Kits[args[0]]

					if (kit == Kits.NONE || (!kit.data.isEnabled && gamer.mode != EnumMode.ADMIN)) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um kit com o nome \"").negative(
							args[0]).basic("\"!").toString())
					else if (kit.isAllowed(gamer, true)) kit.setAndApply(gamer, true)
				}
			}
			else  -> {
				val kit = Kits[alias]

				if (kit == Kits.NONE || (!kit.data.isEnabled && gamer.mode != EnumMode.ADMIN)) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um kit com o nome \"").negative(
						args[0]).basic("\"!").toString())
				else if (kit.isAllowed(gamer, true)) kit.setAndApply(gamer, true)
			}
		}

		return false
	}

	override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String>? {
		return if (sender !is Player) arrayListOf()
		else Kits.KITS.filter {
			it.data.isEnabled && it in sender.gamer().warp.enabledKits && (args.isEmpty() || it.name.startsWith(args[0], true))
		}.map { it.name.toLowerCase() }.toMutableList()
	}
}
