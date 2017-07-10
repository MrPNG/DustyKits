package br.com.dusty.dkits.listener.mechanics;

import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.util.MOTDUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener {
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		//TODO: Remove mods/admins from count
		event.setMaxPlayers(Main.MAX_PLAYERS);
		
		switch(Main.serverStatus){
			case ONLINE:
				event.setMotd(MOTDUtils.randomMOTD());
				break;
			case OFFLINE:
				event.setMotd(MOTDUtils.offlineMOTD());
				break;
			case MAINTENANCE:
				event.setMotd(MOTDUtils.maintenanceMOTD());
				break;
		}
	}
}
