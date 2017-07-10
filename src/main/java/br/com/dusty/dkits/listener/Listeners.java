package br.com.dusty.dkits.listener;

import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.listener.login.PlayerJoinListener;
import br.com.dusty.dkits.listener.login.PlayerLoginListener;
import br.com.dusty.dkits.listener.login.PlayerQuitListener;
import br.com.dusty.dkits.listener.login.ServerListPingListener;
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
		
		//Login
		LISTENERS.add(new PlayerLoginListener());
		LISTENERS.add(new PlayerJoinListener());
		LISTENERS.add(new ServerListPingListener());
		
		//Quit
		LISTENERS.add(new PlayerQuitListener());
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		LISTENERS.forEach(listener -> pluginManager.registerEvents(listener, Main.INSTANCE));
	}
}
