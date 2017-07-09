package br.com.dusty.dkits.listener;

import br.com.dusty.dkits.util.protocol.BossBar;
import br.com.dusty.dkits.util.scoreboard.ScoreboardUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GenericListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		BossBar.create("Hello, World!",
		               0.5f,
		               BossBar.EnumBarColor.BLUE,
		               BossBar.EnumBarStyle.NOTCHED_6,
		               BossBar.EnumFlags.NONE).send(player);
		Bukkit.broadcastMessage("Sent BossBar!");
		
		ScoreboardUtils.create(player);
	}
}
