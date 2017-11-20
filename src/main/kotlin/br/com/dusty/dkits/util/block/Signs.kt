package br.com.dusty.dkits.util.block

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.inventory.InventoryUtils
import br.com.dusty.dkits.util.text.Text
import org.bukkit.block.Sign
import org.bukkit.entity.Player

object Signs {

	fun interact(sign: Sign, player: Player) {
		val gamer = Gamer[player]

		when (Text.clearFormatting(sign.getLine(1))) {
			"[Grátis]" -> if (gamer.isOnSignCooldown()) player.sendMessage(Text.negativePrefix().basic("Você ainda deve ").negative("esperar").basic(" mais ").negative(gamer.signCooldown.toInt()).basic(
					" segundo(s) para usar essa placa novamente!").toString())
			else when (Text.clearFormatting(sign.getLine(2))) {
				"Sopa"    -> {
					player.openInventory(InventoryUtils.soups(player))

					gamer.signCooldown = 10000
				}
				"Recraft" -> {
					player.openInventory(InventoryUtils.recraft(player))

					gamer.signCooldown = 10000
				}
			}
			"Créditos" -> {
				//TODO: Send player back to where they came from
				val amount = try {
					Integer.parseInt(sign.getLine(2).substring(3))
				} catch (e: Exception) {
					0
				}
				gamer.addMoney(amount.toFloat())

				player.sendMessage(Text.positivePrefix().basic("Você ").positive("ganhou " + amount).basic(" créditos!").toString())
			}
			"XP"       -> {
				//TODO: Send player back to where they came from
				val amount = try {
					Integer.parseInt(sign.getLine(2).substring(3))
				} catch (e: Exception) {
					0
				}
				gamer.addXp(amount.toFloat())

				player.sendMessage(Text.positivePrefix().basic("Você ").positive("ganhou " + amount).basic(" XP!").toString())
			}
		}
	}

	fun isSpecialSign(sign: Sign): Boolean = sign.getLine(0).endsWith("=")
}
