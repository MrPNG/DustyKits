package br.com.dusty.dkits;

import br.com.dusty.dkits.command.Commands;
import br.com.dusty.dkits.listener.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Main extends JavaPlugin {
	
	//TODO: public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	public static final Random RANDOM = new Random();
	
	public static final int MAX_PLAYERS = 150;
	
	public static EnumServerStatus serverStatus = EnumServerStatus.ONLINE;
	
	/**
	 * Instância deste {@link JavaPlugin}.
	 */
	public static Main INSTANCE;
	
	public Main() {
		INSTANCE = this;
	}
	
	@Override
	public void onLoad() {
	
	}
	
	@Override
	public void onEnable() {
		Commands.registerAll();
		Listeners.registerAll();
	}
	
	@Override
	public void onDisable() {
	
	}
}
