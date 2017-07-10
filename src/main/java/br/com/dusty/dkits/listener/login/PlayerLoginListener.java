package br.com.dusty.dkits.listener.login;

import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {
	
	private static final String KICK_FULL_MESSAGE = Text.of("O servidor está cheio!\n\n")
	                                                    .color(TextColor.RED)
	                                                    .append("Compre ")
	                                                    .color(TextColor.GRAY)
	                                                    .append("PRO")
	                                                    .color(TextColor.GOLD)
	                                                    .append(" ou um ")
	                                                    .color(TextColor.GRAY)
	                                                    .append("Slot Reservado")
	                                                    .color(TextColor.GRAY)
	                                                    .append(" no site ")
	                                                    .color(TextColor.GRAY)
	                                                    .append("loja.dusty.com.br")
	                                                    .color(TextColor.GOLD)
	                                                    .append(" e entre agora!")
	                                                    .color(TextColor.GRAY)
	                                                    .toString();
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		if(e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)){
			Player player = e.getPlayer();
			
			if(canLogin(player))
				e.allow();
			else
				e.disallow(PlayerLoginEvent.Result.KICK_FULL, KICK_FULL_MESSAGE);
			
		}
	}
	
	private boolean canLogin(Player player) {
		//TODO: Login on full
		
		return false;
	}
}
