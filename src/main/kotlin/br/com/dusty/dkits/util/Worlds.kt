package br.com.dusty.dkits.util

import br.com.dusty.dkits.util.gamer.gamer
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
		edit          -> Bukkit.createWorld(WorldCreator("worlds/" + name))
		else          -> {
			FileUtils.copyDirectory(File(worldsDir, name), File(Bukkit.getWorldContainer(), name))

			Bukkit.createWorld(WorldCreator(name))
		}
	}

	fun unload(name: String, rollback: Boolean): Boolean {
		val world = Bukkit.getWorld(name) ?: return false

		world.livingEntities.filter { it is Player }.forEach {
			(it as Player).gamer().run {
				if (isCombatTagged()) removeCombatTag(false)

				sendToWarp(Warps.LOBBY, true, false)
			}
		}

		return Bukkit.unloadWorld(world, !rollback)
	}

	fun rollback(name: String) = unload(name, true).apply { if (this) FileUtils.deleteDirectory(File(Bukkit.getWorldContainer(), name)) }
}
