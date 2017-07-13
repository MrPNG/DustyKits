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
	
	private static final String KICK_NOT_READY = Text.of("O servidor ainda não está aberto!\n\nVolte em alguns segundos...")
	                                                 .color(TextColor.RED)
	                                                 .toString();
	
	private static final String KICK_FULL_MESSAGE = Text.of("O servidor está cheio!\n\n")
	                                                    .color(TextColor.RED)
	                                                    .append("Compre ")
	                                                    .color(TextColor.GRAY)
	                                                    .append("PRO")
	                                                    .color(TextColor.GOLD)
	                                                    .append(" ou um ")
	                                                    .color(TextColor.GRAY)
	                                                    .append("Slot Reservado")
	                                                    .color(TextColor.GOLD)
	                                                    .append(" no site ")
	                                                    .color(TextColor.GRAY)
	                                                    .append("loja.dusty.com.br")
	                                                    .color(TextColor.GOLD)
	                                                    .append(" e entre agora!")
	                                                    .color(TextColor.GRAY)
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
