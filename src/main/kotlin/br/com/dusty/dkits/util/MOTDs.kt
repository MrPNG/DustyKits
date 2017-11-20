package br.com.dusty.dkits.util

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor

object MOTDs {

	val MOTD_MAIN = Text.of("Dusty").color(TextColor.GOLD).append(" - ").color(TextColor.GRAY).toString()
	val MOTD_ALTERNATE = arrayOf(Text.of("Venha jogar!").color(TextColor.DARK_AQUA).toString(), Text.of("Divirta-se com seus amigos!").color(TextColor.DARK_AQUA).toString())
	val MOTD_SECONDARY = arrayOf(Text.of("Visite loja.dusty.com.br!").color(TextColor.GREEN).toString(), Text.of("Veja seu perfil em dusty.com.br/perfil!").color(TextColor.GREEN).toString())

	val MOTD_OFFLINE = Text.negativeOf("O servidor ainda não está aberto...").toString()
	val MOTD_MAINTENANCE = Text.negativeOf("Em manutenção...").toString()

	fun randomMOTD(): String = MOTD_MAIN + MOTD_ALTERNATE[Main.RANDOM.nextInt(MOTD_ALTERNATE.size)] + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(MOTD_ALTERNATE.size)]

	fun offlineMOTD(): String = MOTD_MAIN + MOTD_OFFLINE + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(MOTD_ALTERNATE.size)]

	fun maintenanceMOTD(): String = MOTD_MAIN + MOTD_MAINTENANCE + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(MOTD_ALTERNATE.size)]
}
