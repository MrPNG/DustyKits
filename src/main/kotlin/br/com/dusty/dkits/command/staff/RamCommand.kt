package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player
import java.text.DecimalFormat

object RamCommand: PlayerCustomCommand(EnumRank.ADMIN, "ram") {

	val MB = 1024 * 1024
	val runtime = Runtime.getRuntime()
	val decimalFormat = DecimalFormat("#.##")

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val totalMemory = runtime.totalMemory() / MB
		val freeMemory = runtime.freeMemory() / MB
		val usedMemory = totalMemory - freeMemory
		val maxMemory = runtime.maxMemory() / MB

		sender.sendMessage(Text.neutralPrefix().basic("Memória RAM:").neutral("\nEm uso: ").basic(usedMemory.toString() + "MB (" + decimalFormat.format(usedMemory * 100 / totalMemory) + "% do total)").neutral(
				"\nLivre: ").basic(freeMemory.toString() + "MB (" + decimalFormat.format(freeMemory * 100 / totalMemory) + "% do total)").neutral("\nTotal: ").basic(totalMemory.toString() + "MB (" + decimalFormat.format(
				totalMemory * 100 / maxMemory) + "% do máximo)").neutral("\nMáximo: ").basic(maxMemory.toString() + "MB").toString())

		return false
	}
}
