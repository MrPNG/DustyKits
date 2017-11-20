package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.CustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warps
import org.bukkit.command.CommandSender

object DisableCommand: CustomCommand(EnumRank.ADMIN, "disable") {

	override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
		if (!testPermission(sender)) return true

		if (args.size > 1) when (args[0].toLowerCase()) {
			"kit"  -> {
				val kit = Kits[args[1]]

				if (kit == Kits.NONE) {
					sender.sendMessage(Text.negativePrefix().basic("Não").basic(" há um kit com o nome \"").negative(args[0]).basic("\"!").toString())
				} else if (args.size > 2) {
					val warp = Warps[args[2]]

					if (warp == Warps.NONE) {
						sender.sendMessage(Text.negativePrefix().basic("Não").basic(" há uma warp com o nome \"").negative(args[0]).basic("\"!").toString())
					} else {
						if (warp.enableKit(kit,
						                   false)) sender.sendMessage(Text.negativePrefix().basic("O kit ").negative(kit.name).basic(" foi ").negative("desabilitado").basic(" na warp ").negative(
								warp.name).basic("!").toString())
						else sender.sendMessage(Text.negativePrefix().basic("O kit ").negative(kit.name).basic(" já está ").negative("desabilitado").basic(" na warp ").negative(warp.name).basic(
								"!").toString())
					}
				} else {
					if (kit.enabled(false)) sender.sendMessage(Text.negativePrefix().basic("O kit ").negative(kit.name).basic(" foi ").negative("desabilitado").basic("!").toString())
					else sender.sendMessage(Text.negativePrefix().basic("O kit ").negative(kit.name).basic(" já está ").negative("desabilitado").basic("!").toString())
				}
			}
			"warp" -> {
				val warp = Warps[args[1]]

				if (warp == Warps.NONE) {
					sender.sendMessage(Text.negativePrefix().basic("Não").basic(" há uma warp com o nome \"").negative(args[0]).basic("\"!").toString())
				} else {
					if (warp.enabled(false)) sender.sendMessage(Text.negativePrefix().basic("A warp ").negative(warp.name).basic(" foi ").negative("desabilitada").basic("!").toString())
					else sender.sendMessage(Text.negativePrefix().basic("A warp ").negative(warp.name).basic(" já está ").negative("desabilitada").basic("!").toString())
				}
			}
		}
		else sender.sendMessage(Text.negativePrefix().basic("Uso: /disable ").negative("<kit>/<warp> <nome>").basic(" [warp]").toString())

		return false
	}

	override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String>? = when {
		args.isEmpty()   -> arrayListOf("kit", "warp")
		args[0] == "kit" -> Warps.WARPS.filter { it.data.isEnabled }.map { it.name.toLowerCase() }.toMutableList()
		else             -> Kits.KITS.filter { it.data.isEnabled }.map { it.name.toLowerCase() }.toMutableList()
	}
}
