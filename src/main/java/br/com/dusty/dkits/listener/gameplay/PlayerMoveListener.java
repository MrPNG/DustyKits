package br.com.dusty.dkits.listener.gameplay;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.text.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		Gamer gamer = Gamer.of(player);
		
		if(gamer.getWarpTask() != null){
			gamer.getWarpTask().cancel();
			gamer.setWarpTask(null);
			
			player.sendMessage(Text.neutralOf("Você se ")
			                       .negative("moveu")
			                       .neutral(", teleporte ")
			                       .negative("cancelado")
			                       .neutral("!")
			                       .toString());
		}
	}
}
