package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.CustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.inventory.WarpMenu
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warps
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object WarpCommand: CustomCommand(EnumRank.DEFAULT, "warp", "spawn", "lobby", "goto") {

	override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean = when (sender) {
		!is Player -> true
		else       -> {
			val gamer = sender.gamer()

			when (alias) {
				"warp"  -> {
					if (args.isEmpty()) {
						sender.openInventory(WarpMenu.menuWarpMain(sender))
					} else {
						val warp = Warps[args[0]]

						if (warp == Warps.NONE || !warp.data.isEnabled && gamer.mode != EnumMode.ADMIN) sender.sendMessage(Text.negativePrefix().basic("Não").basic(" há uma warp com o nome \"").negative(
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
				"goto"  -> {
					if (gamer.mode == EnumMode.ADMIN) {
						val warp = Warps[args[0]]

						if (warp == Warps.NONE || !warp.data.isEnabled && gamer.mode != EnumMode.ADMIN) sender.sendMessage(Text.negativePrefix().basic("Não").basic(" há uma warp com o nome \"").negative(
								args[0]).basic("\"!").toString())
						else sender.teleport(warp.spawn)
					} else if (gamer.rank.isLowerThan(EnumRank.MOD)) {
						sender.sendMessage(UNKNOWN)
					}
				}
			}

			false
		}
	}

	override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String>? = when {
		sender !is Player || alias == "spawn" || alias == "lobby" -> arrayListOf()
		else                                                      -> Warps.WARPS.filter {
			(it.data.isEnabled || (alias == "goto" && sender.gamer().mode == EnumMode.ADMIN)) && (args.isEmpty() || it.name.startsWith(args[0], true))
		}.map { it.name.toLowerCase() }.toMutableList()
	}
}
