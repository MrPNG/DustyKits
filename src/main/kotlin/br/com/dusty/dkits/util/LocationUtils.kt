package br.com.dusty.dkits.util

import br.com.dusty.dkits.Main
import org.bukkit.Location

fun Location.spread(range: Float): Location = this.clone().add((Main.RANDOM.nextFloat() - 0.5) * range, 0.0, (Main.RANDOM.nextFloat() - 0.5) * range)
