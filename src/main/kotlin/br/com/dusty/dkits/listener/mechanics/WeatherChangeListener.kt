package br.com.dusty.dkits.listener.mechanics

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.weather.WeatherChangeEvent

object WeatherChangeListener: Listener {

	@EventHandler
	fun onWeatherChange(event: WeatherChangeEvent) {
		event.isCancelled = true
	}
}
