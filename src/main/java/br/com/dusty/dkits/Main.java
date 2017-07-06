package br.com.dusty.dkits;

import br.com.dusty.dkits.command.Commands;
import br.com.dusty.dkits.listener.Listeners;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	//public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
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
