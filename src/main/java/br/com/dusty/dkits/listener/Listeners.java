package br.com.dusty.dkits.listener;

import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.listener.gameplay.PlayerInteractListener;
import br.com.dusty.dkits.listener.login.AsyncPlayerPreLoginListener;
import br.com.dusty.dkits.listener.login.PlayerJoinListener;
import br.com.dusty.dkits.listener.login.PlayerLoginListener;
import br.com.dusty.dkits.listener.mechanics.*;
import br.com.dusty.dkits.listener.quit.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
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
		LISTENERS.add(new AsyncPlayerPreLoginListener());
		LISTENERS.add(new PlayerJoinListener());
		LISTENERS.add(new PlayerLoginListener());
		
		//Gameplay
		LISTENERS.add(new PlayerInteractListener());
		
		//Mechanincs
		LISTENERS.add(new EntityDamageListener());
		LISTENERS.add(new FoodLevelChangeListener());
		LISTENERS.add(new ItemSpawnListener());
		LISTENERS.add(new LeavesDecayListener());
		LISTENERS.add(new PlayerCommandPreprocessListener());
		LISTENERS.add(new PlayerMoveListener());
		LISTENERS.add(new ServerListPingListener());
		LISTENERS.add(new SignChangeListener());
		LISTENERS.add(new WeatherChangeListener());
		
		//Quit
		LISTENERS.add(new PlayerQuitListener());
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		LISTENERS.forEach(listener -> pluginManager.registerEvents(listener, Main.INSTANCE));
	}
}
