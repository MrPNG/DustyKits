package br.com.dusty.dkits.util.entity

import br.com.dusty.dkits.gamer.GamerRegistry
import org.bukkit.entity.Player

fun Player.gamer() = GamerRegistry.gamer(this)

object Players {

	operator fun get(name: String) = GamerRegistry.onlineGamers().firstOrNull { it.displayName == name }?.player
}
