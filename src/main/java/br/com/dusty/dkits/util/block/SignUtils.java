package br.com.dusty.dkits.util.block;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.InventoryUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class SignUtils {
	
	public static boolean isSpecialSign(Sign sign) {
		return sign.getLine(0).endsWith("=");
	}
	
	public static void doStuff(Sign sign, Player player) {
		Gamer gamer = Gamer.of(player);
		
		switch(Text.clearFormatting(sign.getLine(1))){
			case "[Grátis]":
				if(gamer.isOnSignCooldown()){
					player.sendMessage(Text.of("Você ainda deve ")
					                       .color(TextColor.GRAY)
					                       .append("esperar")
					                       .color(TextColor.RED)
					                       .append(" mais ")
					                       .color(TextColor.GRAY)
					                       .append(gamer.getSignCooldown())
					                       .color(TextColor.RED)
					                       .append(" segundo(s) para usar essa placa novamente!")
					                       .color(TextColor.GRAY)
					                       .toString());
				}else{
					switch(Text.clearFormatting(sign.getLine(2))){
						case "Sopa":
							player.openInventory(InventoryUtils.soups(player));
							
							gamer.setSignCooldown(10000);
							
							break;
						case "Recraft":
							player.openInventory(InventoryUtils.recraft(player));
							
							gamer.setSignCooldown(10000);
							
							break;
					}
				}
				
				break;
			case "Créditos":
				//TODO: Send player back to where they came from
				int amount = Integer.parseInt(sign.getLine(2).substring(3));
				gamer.addMoney(amount);
				
				player.sendMessage(Text.of("Você ")
				                       .color(TextColor.GRAY)
				                       .append("ganhou " + amount)
				                       .color(TextColor.GREEN)
				                       .append(" créditos!")
				                       .color(TextColor.GRAY)
				                       .toString());
				
				break;
			case "XP":
				//TODO: Send player back to where they came from
				amount = Integer.parseInt(sign.getLine(2).substring(3));
				gamer.addXp(amount);
				
				player.sendMessage(Text.of("Você ")
				                       .color(TextColor.GRAY)
				                       .append("ganhou " + amount)
				                       .color(TextColor.GREEN)
				                       .append(" de XP!")
				                       .color(TextColor.GRAY)
				                       .toString());
				
				break;
		}
	}
}
