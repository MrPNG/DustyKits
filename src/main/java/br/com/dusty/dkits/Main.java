package br.com.dusty.dkits;

import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends JavaPlugin {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	public static Main INSTANCE;
	
	public Main() {
		INSTANCE = this;
	}
	
	@Override
	public void onLoad() {
	
	}
	
	@Override
	public void onEnable() {
	
	}
	
	@Override
	public void onDisable() {
	
	}
}
