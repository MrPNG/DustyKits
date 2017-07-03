package br.com.dusty.dkits.listener;

import br.com.dusty.dkits.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

public class Listeners {
	
	public static final ArrayList<Listener> LISTENERS = new ArrayList<>();
	
	public static void registerAll() {
		//Usage: LISTENERS.add(new FooListener());
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		for(Listener listener : LISTENERS){
			pluginManager.registerEvents(listener, Main.INSTANCE);
		}
	}
}
