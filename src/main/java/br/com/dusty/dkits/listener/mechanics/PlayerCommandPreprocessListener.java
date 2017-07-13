package br.com.dusty.dkits.listener.mechanics;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.gamer.GamerRegistry;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.web.WebAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Collection;
import java.util.HashSet;

public class PlayerCommandPreprocessListener implements Listener {
	
	private static final String KICK_SHUTDOWN = Text.negativeOf("O servidor está reiniciando!\n\nVolte em alguns segundos...")
	                                                .toString();
	
	@EventHandler
	public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		
		switch(event.getMessage().split(" ")[0]){
			case "/stop":
				Collection<Gamer> gamers = new HashSet<>(GamerRegistry.getOnlineGamers());
				gamers.forEach(gamer -> gamer.getPlayer().kickPlayer(KICK_SHUTDOWN));
				
				WebAPI.saveProfiles(gamers.toArray(new Gamer[0]));
				
				break;
		}
	}
}
