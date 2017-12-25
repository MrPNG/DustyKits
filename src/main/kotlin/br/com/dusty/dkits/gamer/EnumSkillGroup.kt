package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor

enum class EnumSkillGroup(val string: String, val range: IntRange, val color: TextColor) {

	INICIANTE("Iniciante", 0 until 5000, TextColor.GRAY),
	BRONZE("Bronze", 5000 until 10000, TextColor.GOLD),
	PRATA("Prata", 10000 until 25000, TextColor.DARK_GRAY),
	OURO("Ouro", 25000 until 50000, TextColor.YELLOW),
	RUBI("Rubi", 50000 until 120000, TextColor.DARK_RED),
	ESMERALDA("Esmeralda", 120000 until 750000, TextColor.DARK_GREEN),
	DIAMANTE("Diamante", 750000 until 1000000, TextColor.AQUA),
	LENDARIO("Lendário", 1000000 until Int.MAX_VALUE, TextColor.LIGHT_PURPLE);

	val prefix = Text.of(string.first().toString()).color(color).toString()
}
