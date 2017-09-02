package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.CustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.inventory.WarpMenu
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warps
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object WarpCommand: CustomCommand(EnumRank.DEFAULT, "warp") {

	override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
		return when (sender) {
			!is Player -> true
			else       -> {
				val gamer = Gamer.of(sender)

				if (args.isEmpty()) {
					sender.openInventory(WarpMenu.menuWarpMain(sender))
				} else {
					val warp = Warps.byName(args[0])

					if (warp == null || !warp.data.isEnabled && gamer.mode !== EnumMode.ADMIN)
						sender.sendMessage(Text.negativePrefix()
								                   .basic("Não")
								                   .basic(" há uma warp com o nome \"")
								                   .negative(args[0])
								                   .basic("\"!")
								                   .toString())
					else
						gamer.sendToWarp(warp)
				}

				false
			}
		}
	}

	override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String>? {
		return if (sender !is Player)
			arrayListOf()
		else
			Warps.WARPS
					.filter { it.data.isEnabled && (args.isEmpty() || it.name.startsWith(args[0], true)) }
					.map { it.name.toLowerCase() }.toMutableList()
	}
}
