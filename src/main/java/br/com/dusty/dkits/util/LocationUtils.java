package br.com.dusty.dkits.util;

import br.com.dusty.dkits.Main;
import org.bukkit.Location;

public class LocationUtils {
	
	public static Location spread(Location location, float range) {
		return location.clone().add((Main.RANDOM.nextFloat() - 0.5) * range, 0, (Main.RANDOM.nextFloat() - 0.5) * range);
	}
}
