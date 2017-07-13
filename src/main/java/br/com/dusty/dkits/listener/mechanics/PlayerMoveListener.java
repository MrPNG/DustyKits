package br.com.dusty.dkits.listener.mechanics;

import br.com.dusty.dkits.gamer.Gamer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		Gamer gamer = Gamer.of(player);
		
		Location from = event.getFrom();
		Location to = event.getTo();
		
		if(gamer.isFrozen() && (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()))
			event.setCancelled(true);
	}
}
