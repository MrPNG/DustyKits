package br.com.dusty.dkits.listener.mechanics

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent

object WorldLoadListener: Listener {

	@EventHandler
	fun onWorldLoad(event: WorldLoadEvent) {
		event.world.setGameRuleValue("announceAdvancements", "false")
	}
}
