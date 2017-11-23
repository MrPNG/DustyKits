package br.com.dusty.dkits.listener.mechanics

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent

object FoodLevelChangeListener: Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onFoodLevelChange(event: FoodLevelChangeEvent) {
		event.isCancelled = true
	}
}
