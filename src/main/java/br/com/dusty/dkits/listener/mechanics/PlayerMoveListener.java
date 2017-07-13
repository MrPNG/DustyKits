package br.com.dusty.dkits.listener.mechanics;

import br.com.dusty.dkits.gamer.Gamer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerMoveListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		Gamer gamer = Gamer.of(player);
		
		Location from = event.getFrom();
		Location to = event.getTo();
		
		if(gamer.isFrozen() && walk(from, to))
			event.setCancelled(true);
		else if(to.clone().add(0, -0.5, 0).getBlock().getType() == Material.SPONGE)
			boost(gamer, to);
	}
	
	private static boolean walk(Location from, Location to) {
		return from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ();
	}
	
	private static void boost(Gamer gamer, Location location){
		Player player = gamer.getPlayer();
		
		player.setVelocity(new Vector(0, 2, 0));
		
		gamer.setNoFall(true);
	}
}
