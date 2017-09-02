package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.util.TaskUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ItemSpawnEvent

object ItemSpawnListener: Listener {

	@EventHandler
	fun onItemSpawn(event: ItemSpawnEvent) {
		val item = event.entity

		TaskUtils.sync(Runnable { item.remove() }, 100)
	}
}
