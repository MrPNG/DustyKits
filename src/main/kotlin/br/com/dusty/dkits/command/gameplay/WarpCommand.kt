package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.inventory.WarpMenu
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warps
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object WarpCommand: PlayerCustomCommand(EnumRank.DEFAULT, "warp", "spawn", "lobby", "goto", *Warps.enabledWarpsNames) {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		when (alias) {
			"warp"  -> {
				if (args.isEmpty()) {
					sender.openInventory(WarpMenu.menuWarpMain(sender))
				} else {
					val warp = Warps[args[0]]

					if (warp == Warps.NONE || (!warp.data.isEnabled && gamer.mode != EnumMode.ADMIN)) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há uma warp com o nome \"").negative(
							args[0]).basic("\"!").toString())
					else gamer.sendToWarp(warp, true)
				}
			}
			"spawn" -> {
				gamer.sendToWarp(gamer.warp, false)
			}
			"lobby" -> {
				gamer.sendToWarp(Warps.LOBBY, true)
			}
			else    -> {
				val warp = Warps[alias]

				if (warp == Warps.NONE || (!warp.data.isEnabled && gamer.mode != EnumMode.ADMIN)) sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há uma warp com o nome \"").negative(
						args[0]).basic("\"!").toString())
				else gamer.sendToWarp(warp, true)
			}
		}

		return false
	}

	override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String>? = when {
		sender !is Player || alias == "spawn" || alias == "lobby" || Warps.enabledWarpsNames.contains(alias.toLowerCase()) -> arrayListOf()
		else                                                                                                               -> Warps.WARPS.filter {
			(it.data.isEnabled || (alias == "goto" && sender.gamer().mode == EnumMode.ADMIN)) && (args.isEmpty() || it.name.startsWith(args[0], true))
		}.map { it.name.toLowerCase() }.toMutableList()
	}
}
