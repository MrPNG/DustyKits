package br.com.dusty.dkits.util.gamer

import br.com.dusty.dkits.gamer.GamerRegistry
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

fun Player.gamer() = GamerRegistry.gamer(this)

fun Player.instaKill() {
	this.damage(1024.0)
}

fun Player.instaKill(damager: Entity) {
	this.damage(1024.0, damager)
}
