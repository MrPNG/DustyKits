package br.com.dusty.dkits.util.text

import br.com.dusty.dkits.Main

object MOTDs {

	val MOTD_MAIN = Text.of("Dusty").color(TextColor.GOLD).append(" - ").color(TextColor.GRAY).toString()
	val MOTD_ALTERNATE = arrayOf(Text.of("Venha jogar!").color(TextColor.DARK_AQUA).toString(), Text.of("Divirta-se com seus amigos!").color(TextColor.DARK_AQUA).toString())
	val MOTD_SECONDARY = arrayOf(Text.of("Visite nossa loja em dusty.com.br").color(TextColor.GREEN).toString(),
	                             Text.of("Veja seu perfil em dusty.com.br/perfil").color(TextColor.GREEN).toString())

	val MOTD_OFFLINE = Text.negativeOf("O servidor está iniciando...").toString()
	val MOTD_MAINTENANCE = Text.negativeOf("Em manutenção...").toString()

	val NEW_MOTD_MAIN = "        §e§l§m---§7§l§m[-§6§l Dusty §c» §aVenha jogar! §7§l§m-]§e§l§m---§f"
	val NEW_MOTD_SECONDARY = "     §a§l§m-§e§l§m-§6§l§m-§c§l§m-§d§l§m-§b§l§m-§b§l[ §7Site: §ewww.dusty.com.br §b§l]§b§l§m-§d§l§m-§c§l§m-§6§l§m-§e§l§m-§a§l§m-"

	//fun randomMOTD() = MOTD_MAIN + MOTD_ALTERNATE[Main.RANDOM.nextInt(MOTD_ALTERNATE.size)] + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(MOTD_ALTERNATE.size)]
	fun randomMOTD() = NEW_MOTD_MAIN + "\n" + NEW_MOTD_SECONDARY

	fun offlineMOTD() = MOTD_MAIN + MOTD_OFFLINE + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(MOTD_ALTERNATE.size)]

	fun maintenanceMOTD() = MOTD_MAIN + MOTD_MAINTENANCE + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(MOTD_ALTERNATE.size)]
}
