package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player

object ShopCommand: PlayerCustomCommand(EnumRank.DEFAULT, "shop") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) {
			//TODO: Shop
		} else if (sender.gamer().rank.isHigherThanOrEquals(EnumRank.ADMIN)) {
			if (args[0] == "set" && args.size > 2) {
				val kit = Kits[args[0]]

				if (kit == Kits.NONE || !kit.data.isEnabled) {
					sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um kit com o nome \"").negative(args[0]).basic("\"!").toString())
				} else {
					val price = args[2].toIntOrNull()

					if (price != null) {
						if (kit.setPrice(price)) sender.sendMessage(Text.positivePrefix().basic("O preço do kit ").positive(kit.name).basic(" foi ").positive("alterado").basic("!").toString())
						else sender.sendMessage(Text.positivePrefix().basic("O preço do kit ").negative(kit.name).basic(" já era ").negative(price).basic(" créditos!").toString())
					} else {
						sender.sendMessage(Text.negativePrefix().basic("O valor ").negative("\"" + args[2] + "\"").basic(" não é um ").negative("número inteiro e positivo").basic("!").toString())
					}
				}
			} else {
				sender.sendMessage(Text.negativePrefix().basic("Uso: /shop ").negative("set").basic(" <kit> <preço>").toString())
			}
		}

		return false
	}
}
