package br.com.dusty.dkits.warp;

import br.com.dusty.dkits.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

public class Warps {
	
	public static final LobbyWarp LOBBY = new LobbyWarp();
	
	public static final ArrayList<Warp> WARPS = new ArrayList<>();
	
	public static void registerAll() {
		//Usage: WARPS.add(FOO_WARP);
		
		WARPS.add(LOBBY);
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		WARPS.forEach(warp -> pluginManager.registerEvents(warp, Main.INSTANCE));
	}
	
	public static Warp byName(String name) {
		for(Warp warp : WARPS)
			if(warp.NAME.toLowerCase().startsWith(name.toLowerCase()))
				return warp;
		
		return null;
	}
}
