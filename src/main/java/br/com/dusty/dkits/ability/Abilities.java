package br.com.dusty.dkits.ability;

import br.com.dusty.dkits.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

public class Abilities {
	
	public static final ArrayList<Ability> ABILITIES = new ArrayList<>();
	
	public static void registerAll() {
		//Usage: ABILITIES.add(FOO_ABILITY);
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		ABILITIES.forEach(ability -> pluginManager.registerEvents(ability, Main.INSTANCE));
	}
}
