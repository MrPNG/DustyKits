package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.inventory.KitMenu
import br.com.dusty.dkits.util.text.Text
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object KitCommand: PlayerCustomCommand(EnumRank.DEFAULT, "kit") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		when (alias) {
			"kit" -> {
				if (gamer.warp.enabledKits.isEmpty()) {
					sender.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" pode escolher ").negative("kits").basic(" nessa warp!").toString())
				} else {
					if (args.isEmpty()) {
						sender.openInventory(KitMenu.menuKitOwned(sender))
					} else {
						val kit = Kits[args[0]]

						if (kit == Kits.NONE || (!kit.data.isEnabled && gamer.mode != EnumMode.ADMIN)) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um kit com o nome \"").negative(
								args[0]).basic("\"!").toString())
						else if (gamer.warp.isAllowed(kit, gamer, true)) gamer.setKitAndApply(kit, true)
					}
				}
			}
			else  -> {
				val kit = Kits[alias]

				if (kit == Kits.NONE || (!kit.data.isEnabled && gamer.mode != EnumMode.ADMIN)) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um kit com o nome \"").negative(
						args[0]).basic("\"!").toString())
				else if (gamer.warp.isAllowed(kit, gamer, true)) gamer.setKitAndApply(kit, true)
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
