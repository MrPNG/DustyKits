package br.com.dusty.dkits.listener.mechanics;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class LeavesDecayListener implements Listener {
	
	@EventHandler
	public static void onLeavesDecay(LeavesDecayEvent event){
		event.setCancelled(true);
	}
}
