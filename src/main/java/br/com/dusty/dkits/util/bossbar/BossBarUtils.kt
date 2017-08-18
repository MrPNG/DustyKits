package br.com.dusty.dkits.util.bossbar

import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor

object BossBarUtils {

	val MAIN = BossBar.create(Text.of("Dusty")
			                          .color(TextColor.RED)
			                          .append(" - ")
			                          .color(TextColor.WHITE)
			                          .append("dusty.com.br")
			                          .color(TextColor.GOLD)
			                          .toString(),
	                          1f,
	                          BossBar.EnumBarColor.PINK,
	                          BossBar.EnumBarStyle.PROGRESS,
	                          BossBar.EnumFlags.NONE)
}
