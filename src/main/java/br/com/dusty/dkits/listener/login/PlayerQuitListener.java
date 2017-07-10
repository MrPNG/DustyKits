package br.com.dusty.dkits.listener.login;

import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.gamer.GamerRegistry;
import br.com.dusty.dkits.util.scoreboard.ScoreboardUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
	
	private static final String QUIT_MESSAGE_PREFIX = Text.of("[")
	                                                      .color(TextColor.GRAY)
	                                                      .append("-")
	                                                      .color(TextColor.RED)
	                                                      .append("] ")
	                                                      .color(TextColor.GRAY)
	                                                      .toString();
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		Gamer gamer = Gamer.of(player);
		GamerRegistry.unregister(gamer);
		
		ScoreboardUtils.updateAll();
		
		if(gamer.getRank().isBelow(EnumRank.MOD))
			e.setQuitMessage(QUIT_MESSAGE_PREFIX + player.getName());
		else
			e.setQuitMessage(null);
	}
}
