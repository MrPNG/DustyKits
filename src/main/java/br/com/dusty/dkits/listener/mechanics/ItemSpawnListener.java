package br.com.dusty.dkits.listener.mechanics;

import br.com.dusty.dkits.util.TaskUtils;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class ItemSpawnListener implements Listener {
	
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		Item item = event.getEntity();
		
		if(item.isValid())
			TaskUtils.sync(item::remove, 300);
	}
}
