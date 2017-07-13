package br.com.dusty.dkits.util.block;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.InventoryUtils;
import br.com.dusty.dkits.util.text.Text;
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
					player.sendMessage(Text.neutralOf("Você ainda deve ")
					                       .negative("esperar")
					                       .neutral(" mais ")
					                       .negative(gamer.getSignCooldown())
					                       .neutral(" segundo(s) para usar essa placa novamente!")
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
				
				player.sendMessage(Text.neutralOf("Você ").positive("ganhou " + amount).neutral(" créditos!").toString());
				
				break;
			case "XP":
				//TODO: Send player back to where they came from
				amount = Integer.parseInt(sign.getLine(2).substring(3));
				gamer.addXp(amount);
				
				player.sendMessage(Text.neutralOf("Você ").positive("ganhou " + amount).neutral(" XP!").toString());
				
				break;
		}
	}
}
