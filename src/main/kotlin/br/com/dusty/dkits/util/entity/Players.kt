package br.com.dusty.dkits.util.entity

import br.com.dusty.dkits.gamer.GamerRegistry
import org.bukkit.entity.Player

fun Player.gamer() = GamerRegistry.gamer(this)
