package br.com.dusty.dkits.listener.mechanics;

import br.com.dusty.dkits.gamer.Gamer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			
			Gamer gamer = Gamer.of(player);
			
			if(gamer.isInvincible())
				event.setCancelled(true);
			
			if(event.getCause() == EntityDamageEvent.DamageCause.FALL && gamer.hasNoFall()){
				gamer.setNoFall(false);
				
				event.setCancelled(true);
			}
		}
	}
}
