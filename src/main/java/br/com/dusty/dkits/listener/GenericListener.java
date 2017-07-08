package br.com.dusty.dkits.listener;

import br.com.dusty.dkits.util.protocol.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GenericListener implements Listener {
	
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		BossBar.create("Hello, World!",
		               0.5f,
		               BossBar.EnumBarColor.BLUE,
		               BossBar.EnumBarStyle.NOTCHED_6,
		               BossBar.EnumFlags.NONE).send(player);
	}
}
