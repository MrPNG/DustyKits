package br.com.dusty.dkits.listener.login;

import br.com.dusty.dkits.EnumServerStatus;
import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.gamer.GamerRegistry;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {
	
	private static final String KICK_NOT_READY = Text.negativeOf(
			"O servidor ainda não está aberto!\n\nVolte em alguns segundos...").toString();
	
	private static final String KICK_FULL_MESSAGE = Text.negativeOf("O servidor está cheio!\n\n")
	                                                    .neutral("Compre ")
	                                                    .append("PRO")
	                                                    .color(TextColor.GOLD)
	                                                    .neutral(" ou um ")
	                                                    .append("Slot Reservado")
	                                                    .color(TextColor.GOLD)
	                                                    .neutral(" no site ")
	                                                    .append("loja.dusty.com.br")
	                                                    .color(TextColor.GOLD)
	                                                    .neutral(" e entre agora!")
	                                                    .toString();
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		
		if(GamerRegistry.getPrimitiveGamerbyUUID(player.getUniqueId()) == null || (Main.serverStatus != EnumServerStatus.ONLINE && Gamer
				.of(player)
				.getRank()
				.isLowerThan(EnumRank.MOD))){
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, KICK_NOT_READY);
		}else if(event.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)){
			if(canLogin(player))
				event.allow();
			else
				event.disallow(PlayerLoginEvent.Result.KICK_FULL, KICK_FULL_MESSAGE);
		}
	}
	
	private boolean canLogin(Player player) {
		//TODO: Login on full
		
		return false;
	}
}
