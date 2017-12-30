package br.com.dusty.dkits.util.text

import br.com.dusty.dkits.Main

object MOTDs {

	val MOTD_MAIN = Text.of("Dusty").color(TextColor.GOLD).append(" - ").color(TextColor.GRAY).toString()
	val MOTD_ALTERNATE = arrayOf(Text.of("Venha jogar!").color(TextColor.DARK_AQUA).toString(), Text.of("Divirta-se com seus amigos!").color(TextColor.DARK_AQUA).toString())
	val MOTD_SECONDARY = arrayOf(Text.of("Visite nossa loja em dusty.com.br").color(TextColor.GREEN).toString(),
	                             Text.of("Veja seu perfil em dusty.com.br/perfil").color(TextColor.GREEN).toString())

	val MOTD_OFFLINE = Text.negativeOf("O servidor está iniciando...").toString()
	val MOTD_MAINTENANCE = Text.negativeOf("Em manutenção...").toString()

	val NEW_MOTD_MAIN = "        §e━━━━ §6§lDusty PvP §d» §3Venha jogar! §e━━━━"
	val NEW_MOTD_SECONDARY = "        §aI-§c-§d-§e-§b-[ §fSite: §ewww.dusty.com.br §b]-§e-§d-§c-§a-I"

	//fun randomMOTD() = MOTD_MAIN + MOTD_ALTERNATE[Main.RANDOM.nextInt(MOTD_ALTERNATE.size)] + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(MOTD_ALTERNATE.size)]
	fun randomMOTD() = NEW_MOTD_MAIN + "\n" + NEW_MOTD_SECONDARY

	fun offlineMOTD() = MOTD_MAIN + MOTD_OFFLINE + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(
			MOTD_ALTERNATE.size)]

	fun maintenanceMOTD() = MOTD_MAIN + MOTD_MAINTENANCE + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(
			MOTD_ALTERNATE.size)]
}
