package br.com.dusty.dkits.util;

import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;

public class MOTDUtils {
	
	private static final String MOTD_MAIN = Text.of("Dusty")
	                                            .color(TextColor.GOLD)
	                                            .append(" - ")
	                                            .color(TextColor.GRAY)
	                                            .toString();
	
	private static final String[] MOTD_ALTERNATE = {Text.of("Venha jogar!").color(TextColor.DARK_AQUA).toString(),
	                                                Text.of("Divirta-se com seus amigos!")
	                                                    .color(TextColor.DARK_AQUA).toString()};
	
	private static final String[] MOTD_SECONDARY = {Text.of("Visite loja.dusty.com.br!").color(TextColor.GREEN).toString(),
	                                                Text.of("Veja seu perfil em dusty.com.br/perfil!")
	                                                    .color(TextColor.GREEN).toString()};
	
	private static final String MOTD_OFFLINE = Text.of("O servidor ainda não está aberto...").color(TextColor.RED).toString();
	
	private static final String MOTD_MAINTENANCE = Text.of("Em manutenção...").color(TextColor.RED).toString();
	
	public static String randomMOTD() {
		return MOTD_MAIN + MOTD_ALTERNATE[Main.RANDOM.nextInt(MOTD_ALTERNATE.length)] + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(
				MOTD_ALTERNATE.length)];
	}
	
	public static String offlineMOTD() {
		return MOTD_MAIN + MOTD_OFFLINE + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(MOTD_ALTERNATE.length)];
	}
	
	public static String maintenanceMOTD() {
		return MOTD_MAIN + MOTD_MAINTENANCE + "\n" + MOTD_SECONDARY[Main.RANDOM.nextInt(MOTD_ALTERNATE.length)];
	}
}
