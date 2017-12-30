package br.com.dusty.dkits.util.graphics

import br.com.dusty.dkits.Main
import org.bukkit.Color

object Colors {

	val values = arrayOf(Color.WHITE,
	                     Color.SILVER,
	                     Color.GRAY,
	                     Color.BLACK,
	                     Color.RED,
	                     Color.MAROON,
	                     Color.YELLOW,
	                     Color.OLIVE,
	                     Color.LIME,
	                     Color.GREEN,
	                     Color.AQUA,
	                     Color.TEAL,
	                     Color.BLUE,
	                     Color.NAVY,
	                     Color.FUCHSIA,
	                     Color.PURPLE,
	                     Color.ORANGE)

	val size = values.size

	fun random() = values[Main.RANDOM.nextInt(size)]
}
