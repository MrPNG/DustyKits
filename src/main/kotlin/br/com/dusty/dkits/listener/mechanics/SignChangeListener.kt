package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

object SignChangeListener: Listener {

	private val SOUP_SIGN = arrayOf(Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString(),
	                                Text.basicOf("[").positive("Grátis").basic("]").toString(),
	                                Text.of("Sopa").color(TextColor.YELLOW).toString(),
	                                Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString())

	private val RECRAFT_SIGN = arrayOf(Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString(),
	                                   Text.basicOf("[").positive("Grátis").basic("]").toString(),
	                                   Text.of("Recraft").color(TextColor.YELLOW).toString(),
	                                   Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString())

	private val MONEY_SIGN = arrayOf(Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString(),
	                                 Text.of("Créditos").color(TextColor.YELLOW).toString(),
	                                 Text.positiveOf("+").toString(),
	                                 Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString())

	private val XP_SIGN = arrayOf(Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString(),
	                              Text.of("XP").color(TextColor.YELLOW).toString(),
	                              Text.positiveOf("+").toString(),
	                              Text.of("=-=-=-=-=-=-=-=-=-=-=-=-=-=").color(TextColor.RED).toString())

	@EventHandler
	fun onSignChange(event: SignChangeEvent) {
		val player = event.player

		val gamer = player.gamer()

		if (gamer.mode == EnumMode.ADMIN) when (event.getLine(0)) {
			"soup"    -> for (i in 0 .. 3) event.setLine(i, SOUP_SIGN[i])
			"recraft" -> for (i in 0 .. 3) event.setLine(i, RECRAFT_SIGN[i])
			"money"   -> {
				val amount = event.getLine(1).toIntOrNull() ?: 0

				for (i in 0 .. 3) event.setLine(i, MONEY_SIGN[i])

				event.setLine(2, event.getLine(2) + amount)
			}
			"xp"      -> {
				val amount = event.getLine(1).toIntOrNull() ?: 0

				for (i in 0 .. 3) event.setLine(i, XP_SIGN[i])

				event.setLine(2, event.getLine(2) + amount)
			}
			else      -> for (i in 0 .. 3) event.setLine(i, event.getLine(i).replace("&", "§"))
		}
	}
}
