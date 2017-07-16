package br.com.dusty.dkits.listener.login;

import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.ScoreboardUtils;
import br.com.dusty.dkits.util.bossbar.BossBarUtils;
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.warp.Warps;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	
	private static final String KICK_NOT_READY = Text.negativeOf(
			"O servidor ainda não está pronto!\n\nVolte em alguns segundos...").toString();
	
	private static final String JOIN_MESSAGE_PREFIX = Text.neutralOf("[").positive("+").neutral("] ").toString();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		Gamer gamer = Gamer.of(player);
		
		if(gamer.getProtocolVersion() == null){
			player.kickPlayer(KICK_NOT_READY);
			return;
		}
		
		if(gamer.getRank().isLowerThan(EnumRank.MOD))
			event.setJoinMessage(JOIN_MESSAGE_PREFIX + player.getName());
		else
			event.setJoinMessage(null);
		
		ScoreboardUtils.create(player);
		ScoreboardUtils.updateAll();
		
		gamer.sendToWarp(Warps.LOBBY);
		
		if(gamer.getProtocolVersion().isLowerThan(EnumProtocolVersion.RELEASE_1_8))
			BossBarUtils.MAIN.send(player);
	}
}
