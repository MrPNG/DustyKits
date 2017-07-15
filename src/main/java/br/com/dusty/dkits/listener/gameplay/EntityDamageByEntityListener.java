package br.com.dusty.dkits.listener.gameplay;

import br.com.dusty.dkits.gamer.Gamer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageByEntityListener implements Listener {
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
			Gamer gamer = Gamer.of((Player) event.getEntity());
			Gamer damager = Gamer.of((Player) event.getDamager());
			
			gamer.setCombatTag(10000);
			gamer.setCombatPartner(damager);
			
			damager.setCombatTag(10000);
			damager.setCombatPartner(gamer);
		}
	}
}
