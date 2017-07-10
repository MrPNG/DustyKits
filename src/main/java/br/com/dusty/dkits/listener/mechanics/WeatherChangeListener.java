package br.com.dusty.dkits.listener.mechanics;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {
	
	@EventHandler
	public static void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled(true);
	}
}
