package br.com.dusty.dkits.listener.login;

import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.ScoreboardUtils;
import br.com.dusty.dkits.util.bossbar.BossBarUtils;
import br.com.dusty.dkits.util.text.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	
	private static final String JOIN_MESSAGE_PREFIX = Text.neutralOf("[").positive("+").neutral("] ").toString();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		Gamer gamer = Gamer.of(player);
		
		ScoreboardUtils.create(player);
		ScoreboardUtils.updateAll();
		
		BossBarUtils.MAIN.send(player);
		
		if(gamer.getRank().isLowerThan(EnumRank.MOD))
			event.setJoinMessage(JOIN_MESSAGE_PREFIX + player.getName());
		else
			event.setJoinMessage(null);
	}
}
