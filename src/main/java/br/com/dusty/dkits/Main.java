package br.com.dusty.dkits;

import br.com.dusty.dkits.ability.Abilities;
import br.com.dusty.dkits.command.Commands;
import br.com.dusty.dkits.kit.Kits;
import br.com.dusty.dkits.listener.Listeners;
import br.com.dusty.dkits.warp.Warps;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Random;

public class Main extends JavaPlugin {
	
	/**
	 * <b>Singleton</b> deste {@link JavaPlugin}.
	 */
	public static Main INSTANCE;
	
	//TODO: public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	public static final Random RANDOM = new Random();
	public static final Gson GSON = new Gson();
	
	public static final File ROOT = new File(Bukkit.getWorldContainer(), "config");
	
	public static final int MAX_PLAYERS = 150;
	
	public static EnumServerStatus serverStatus = EnumServerStatus.OFFLINE;
	
	public Main() {
		INSTANCE = this;
		
		ROOT.mkdirs();
	}
	
	@Override
	public void onLoad() {
	
	}
	
	@Override
	public void onEnable() {
		Commands.registerAll();
		Listeners.registerAll();
		Abilities.registerAll();
		Kits.registerAll();
		Warps.registerAll();
		
		serverStatus = EnumServerStatus.ONLINE;
	}
	
	@Override
	public void onDisable() {
	
	}
}
