package br.com.dusty.dkits.util

import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.warp.Warps
import org.apache.commons.io.FileUtils
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import java.io.File

object Worlds {

	val worldsDir = File(Bukkit.getWorldContainer(), "worlds")

	fun exists(name: String) = File(worldsDir, name.toLowerCase()).exists()

	fun isLoaded(name: String) = Bukkit.getWorld(name) != null

	fun load(name: String, edit: Boolean): World? = when {
		!exists(name) -> null
		edit          -> {
			val world = Bukkit.createWorld(WorldCreator("worlds/" + name))
			Bukkit.broadcastMessage("World: " + world?.name)

			world
		}
		else          -> {
			val worldDir = File(worldsDir, name)

			FileUtils.copyDirectory(worldDir, File(Bukkit.getWorldContainer(), name))

			val world = Bukkit.createWorld(WorldCreator(name))
			Bukkit.broadcastMessage("World: " + world?.name)

			world
		}
	}

	fun unload(name: String, rollback: Boolean): Boolean {
		val world = Bukkit.getWorld(name) ?: return false

		world.livingEntities.filter { it is Player }.forEach {
			val gamer = (it as Player).gamer()

			if (gamer.isCombatTagged()) gamer.removeCombatTag()

			gamer.sendToWarp(Warps.LOBBY, false)
		}

		return Bukkit.unloadWorld(world, rollback)
	}

	fun rollback(name: String) {
		if (!isLoaded(name)) return

		unload(name, false)
		FileUtils.deleteDirectory(File(Bukkit.getWorldContainer(), name))
	}
}
