package br.com.dusty.dkits.listener.quit;

import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.gamer.GamerRegistry;
import br.com.dusty.dkits.util.ScoreboardUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.web.WebAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
	
	private static final String QUIT_MESSAGE_PREFIX = Text.neutralOf("[").negative("-").neutral("] ").toString();
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		Gamer gamer = Gamer.of(player);
		GamerRegistry.unregister(gamer);
		
		WebAPI.saveProfiles(gamer);
		
		ScoreboardUtils.updateAll();
		
		if(gamer.getRank().isLowerThan(EnumRank.MOD))
			event.setQuitMessage(QUIT_MESSAGE_PREFIX + player.getName());
		else
			event.setQuitMessage(null);
	}
}
