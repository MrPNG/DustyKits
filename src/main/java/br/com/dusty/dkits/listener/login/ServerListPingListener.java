package br.com.dusty.dkits.listener.login;

import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.util.login.MOTDUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener {
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent e) {
		//TODO: Remove mods/admins from count
		e.setMaxPlayers(Main.MAX_PLAYERS);
		
		switch(Main.serverStatus){
			case ONLINE:
				e.setMotd(MOTDUtils.randomMOTD());
				break;
			case OFFLINE:
				e.setMotd(MOTDUtils.offlineMOTD());
				break;
			case MAINTENANCE:
				e.setMotd(MOTDUtils.maintenanceMOTD());
				break;
		}
	}
}
