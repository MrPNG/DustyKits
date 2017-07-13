package br.com.dusty.dkits.listener.mechanics;

import br.com.dusty.dkits.gamer.EnumMode;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {
	
	private static final String[] SOUP_SIGN = {Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString(),
	                                           Text.neutralOf("[").positive("Grátis").neutral("]").toString(),
	                                           Text.of("Sopa").color(TextColor.YELLOW).toString(),
	                                           Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString()};
	
	private static final String[] RECRAFT_SIGN = {Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString(),
	                                              Text.neutralOf("[").positive("Grátis").neutral("]").toString(),
	                                              Text.of("Recraft").color(TextColor.YELLOW).toString(),
	                                              Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString()};
	
	private static final String[] MONEY_SIGN = {Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString(),
	                                            Text.of("Créditos").color(TextColor.YELLOW).toString(),
	                                            Text.positiveOf("+").toString(),
	                                            Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString()};
	
	private static final String[] XP_SIGN = {Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString(),
	                                         Text.of("XP").color(TextColor.YELLOW).toString(),
	                                         Text.positiveOf("+").toString(),
	                                         Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString()};
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		
		Gamer gamer = Gamer.of(player);
		
		if(gamer.getMode() != EnumMode.ADMIN)
			return;
		
		switch(event.getLine(0)){
			case "soup":
				for(int i = 0; i < 4; i++)
					event.setLine(i, SOUP_SIGN[i]);
				
				break;
			case "recraft":
				for(int i = 0; i < 4; i++)
					event.setLine(i, RECRAFT_SIGN[i]);
				
				break;
			case "money":
				int amount = 0;
				
				try{
					amount = Integer.parseInt(event.getLine(1));
				}catch(NumberFormatException e){}
				
				for(int i = 0; i < 4; i++)
					event.setLine(i, MONEY_SIGN[i]);
				
				event.setLine(2, event.getLine(2) + amount);
				
				break;
			case "xp":
				amount = 0;
				
				try{
					amount = Integer.parseInt(event.getLine(1));
				}catch(NumberFormatException e){}
				
				for(int i = 0; i < 4; i++)
					event.setLine(i, XP_SIGN[i]);
				
				event.setLine(2, event.getLine(2) + amount);
				
				break;
			default:
				for(int i = 0; i < 4; i++)
					event.setLine(i, event.getLine(i).replace("&", "§"));
				
				break;
		}
	}
}
