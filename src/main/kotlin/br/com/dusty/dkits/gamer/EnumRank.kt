package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.util.text.TextStyle
import java.lang.Exception
import java.util.*

/**
 * Enum que contém todos os [EnumRank] do servidor associados a um valor numérico 'int' que identifica a hierarquia entre eles,
 * usada nos métodos da própria classe.
 */
enum class EnumRank {

	NONE(-1),
	DEFAULT(0, TextColor.GRAY),
	//	VIP(2, TextColor.GREEN),
	MVP(3, TextColor.BLUE),
	PRO(4, TextColor.GOLD),
	PRO_YOUTUBER(6, TextColor.AQUA),
	YOUTUBER(6, TextColor.AQUA, TextStyle.ITALIC),
	MOD(8, TextColor.DARK_PURPLE),
	MODPLUS(9, TextColor.DARK_PURPLE, TextStyle.ITALIC),
	ADMIN(Integer.MAX_VALUE, TextColor.RED, TextStyle.ITALIC),
	OWNER(Integer.MAX_VALUE, TextColor.DARK_RED, TextStyle.ITALIC);

	var level: Int = 0
	var color = TextColor.WHITE
	var styles = arrayOf<TextStyle>()
	var string = ""
	var prefix = ""

	constructor(level: Int) {
		this.level = level
		this.string = format(name.replace("_", " "))
		this.prefix = format("")
	}

	constructor(level: Int, color: TextColor) {
		this.level = level
		this.color = color
		this.string = format(name.replace("_", " "))
		this.prefix = format("")
	}

	constructor(level: Int, color: TextColor, vararg styles: TextStyle) {
		this.level = level
		this.color = color
		this.styles = styles as Array<TextStyle>
		this.string = format(name.replace("_", " "))
		this.prefix = format("")
	}

	/**
	 * @param rank
	 * @return **true** se este [EnumRank] está hierarquicamente **acima** do parâmetro 'rank'.
	 */
	fun isHigherThan(rank: EnumRank) = level > rank.level

	/**
	 * @param rank
	 * @return **true** se este [EnumRank] não está hierarquicamente **abaixo** do parâmetro 'rank'.
	 */
	fun isHigherThanOrEquals(rank: EnumRank) = level >= rank.level

	/**
	 * @param rank
	 * @return **true** se este [EnumRank] está hierarquicamente **abaixo** do parâmetro 'rank'.
	 */
	fun isLowerThan(rank: EnumRank) = level < rank.level

	/**
	 * @param rank
	 * @return **true** se este [EnumRank] não está hierarquicamente **acima** do parâmetro 'rank'.
	 */
	fun isLowerThanOrEquals(rank: EnumRank) = level <= rank.level

	/**
	 * @return **true** se este [EnumRank] não é o **maior**.
	 */
	fun hasNext() = level < Integer.MAX_VALUE

	/**
	 * @return [EnumRank] imediatamente **acima** deste na hirarquia, 'null' se este for o mais alto.
	 */
	fun next(): EnumRank {
		var rank: EnumRank

		var i = level
		do {
			i++
			rank = EnumRank[i]
		} while (rank == NONE)

		return rank
	}

	/**
	 * @return **true** se este [EnumRank] não é o **menor**.
	 */
	fun hasPrev() = level > 0

	/**
	 * @return [EnumRank] imediatamente **abaixo** deste na hirarquia, 'null' se este for o mais baixo.
	 */
	fun prev(): EnumRank {
		var rank: EnumRank

		var i = level
		do {
			i--
			rank = EnumRank[i]
		} while (rank == NONE)

		return rank
	}

	fun format(s: String) = Text.of(s).color(color).styles(*styles).toString()

	override fun toString() = string

	companion object {

		private val BY_LEVEL = HashMap<Int, EnumRank>()

		val names = values().map { it.name.toLowerCase() }

		init {
			values().forEach { BY_LEVEL.put(it.level, it) }
		}

		/**
		 * @param level
		 * @return [EnumRank] definido por um valor númerico 'int', [NONE] se não houver um valor númerico 'int'
		 * associado a nenhum [EnumRank].
		 */
		operator fun get(level: Int) = BY_LEVEL.getOrDefault(level, NONE)

		/**
		 * @param name
		 * @return [EnumRank] definido pelo seu nome, [NONE] se não houver nenhum com tal nome
		 * associado a nenhum [EnumRank].
		 */
		operator fun get(name: String) = try {
			EnumRank.valueOf(name.toUpperCase())
		} catch (e: Exception) {
			EnumRank.NONE
		}
	}
}
