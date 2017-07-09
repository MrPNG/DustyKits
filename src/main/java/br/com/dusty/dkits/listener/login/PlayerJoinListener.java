package br.com.dusty.dkits.listener.login;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.protocol.BossBar;
import br.com.dusty.dkits.util.scoreboard.ScoreboardUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		Gamer gamer = Gamer.of(player);
		
		ScoreboardUtils.create(player);
		ScoreboardUtils.updateAll();
	}
}
