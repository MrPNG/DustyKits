package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.util.Tasks
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.ItemSpawnEvent

object ItemSpawnListener: Listener {

	@EventHandler
	fun onItemSpawn(event: ItemSpawnEvent) {
		val item = event.entity

		Tasks.sync(Runnable { item.remove() }, 100)
	}
}
