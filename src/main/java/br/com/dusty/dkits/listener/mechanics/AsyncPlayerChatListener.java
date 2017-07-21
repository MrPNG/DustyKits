package br.com.dusty.dkits.listener.mechanics;

import br.com.dusty.dkits.gamer.EnumChat;
import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.gamer.GamerRegistry;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {
	
	private static final Text STAFF_CHAT_PREFIX_NEUTRAL = Text.basicOf("[").neutral("Staff Chat").basic("] ");
	private static final Text STAFF_CHAT_PREFIX_NEGATIVE = Text.basicOf("[").neutral("Staff Chat").basic("] ");
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		Gamer gamer = Gamer.of(player);
		
		switch(gamer.getChat()){
			case NORMAL:
				event.setFormat("<%s" + TextStyle.RESET + "> %s");
				
				break;
			case STAFF:
				event.getRecipients().clear();
				
				String message_neutral = STAFF_CHAT_PREFIX_NEUTRAL.append("<")
				                                                  .append(player.getDisplayName())
				                                                  .append("> ")
				                                                  .neutral(event.getMessage())
				                                                  .toString();
				
				String message_negative = STAFF_CHAT_PREFIX_NEGATIVE.append("<")
				                                                    .append(player.getDisplayName())
				                                                    .append("> ")
				                                                    .negative(event.getMessage())
				                                                    .toString();
				
				for(Gamer otherGamer : GamerRegistry.getOnlineGamers())
					if(otherGamer.getRank().isHigherThanOrEquals(EnumRank.MOD))
						otherGamer.getPlayer()
						          .sendMessage(otherGamer.getChat() == EnumChat.STAFF ? message_neutral : message_negative);
				
				break;
		}
	}
}
