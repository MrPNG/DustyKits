package br.com.dusty.dkits.listener;

import br.com.dusty.dkits.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

public class Listeners {
	
	/**
	 * {@link ArrayList} que contém todos os {@link Listener} a serem/já registrados pelo plugin.
	 */
	private static final ArrayList<Listener> LISTENERS = new ArrayList<>();
	
	/**
	 * Registra todos os {@link Listener} da {@link ArrayList} LISTENERS.
	 */
	public static void registerAll() {
		//Usage: LISTENERS.add(new FooListener());
		LISTENERS.add(new GenericListener());
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		for(Listener listener : LISTENERS){
			pluginManager.registerEvents(listener, Main.INSTANCE);
		}
	}
}
