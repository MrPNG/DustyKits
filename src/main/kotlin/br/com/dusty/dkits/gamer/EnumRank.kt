package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.util.text.TextStyle
import java.util.*

/**
 * Enum que contém todos os [EnumRank] do servidor associados a um valor numérico 'int' que identifica a hierarquia entre eles,
 * usada nos métodos da própria classe.
 */
enum class EnumRank {

	NONE(-1),
	DEFAULT(0),
	//VIP(2, TextColor.GREEN),
	//MVP(3, TextColor.BLUE),
	PRO(4, TextColor.GOLD),
	YOUTUBER(6, TextColor.AQUA),
	MOD(8, TextColor.DARK_PURPLE),
	MODPLUS(9, TextColor.DARK_PURPLE, TextStyle.ITALIC),
	ADMIN(Integer.MAX_VALUE, TextColor.RED, TextStyle.ITALIC);

	var level: Int = 0
	var color = TextColor.WHITE
	var styles: Array<TextStyle> = arrayOf()
	var string = ""

	constructor(level: Int) {
		this.level = level
		this.string = format(name)
	}

	constructor(level: Int, color: TextColor) {
		this.level = level
		this.color = color
		this.string = format(name)
	}

	constructor(level: Int, color: TextColor, vararg styles: TextStyle) {
		this.level = level
		this.color = color
		this.styles = styles as Array<TextStyle>
		this.string = format(name)
	}

	/**
	 * Retorna **true** se este [EnumRank] está hierarquicamente **acima** do parâmetro 'rank'.
	 *
	 * @param rank
	 * @return **true** se este [EnumRank] está hierarquicamente **acima** do parâmetro 'rank'.
	 */
	fun isHigherThan(rank: EnumRank): Boolean = level > rank.level

	/**
	 * Retorna **true** se este [EnumRank] não está hierarquicamente **abaixo** do parâmetro 'rank'.
	 *
	 * @param rank
	 * @return **true** se este [EnumRank] não está hierarquicamente **abaixo** do parâmetro 'rank'.
	 */
	fun isHigherThanOrEquals(rank: EnumRank): Boolean = level >= rank.level

	/**
	 * Retorna **true** se este [EnumRank] está hierarquicamente **abaixo** do parâmetro 'rank'.
	 *
	 * @param rank
	 * @return **true** se este [EnumRank] está hierarquicamente **abaixo** do parâmetro 'rank'.
	 */
	fun isLowerThan(rank: EnumRank): Boolean = level < rank.level

	/**
	 * Retorna **true** se este [EnumRank] não está hierarquicamente **acima** do parâmetro 'rank'.
	 *
	 * @param rank
	 * @return **true** se este [EnumRank] não está hierarquicamente **acima** do parâmetro 'rank'.
	 */
	fun isLowerThanOrEquals(rank: EnumRank): Boolean = level <= rank.level

	/**
	 * Retorna **true** se este [EnumRank] não é o **maior**.
	 *
	 * @return **true** se este [EnumRank] não é o **maior**.
	 */
	fun hasNext(): Boolean = level < ADMIN.level

	/**
	 * Retorna o [EnumRank] imediatamente **acima** deste na hirarquia.
	 *
	 * @return [EnumRank] imediatamente **acima** deste na hirarquia, 'null' se este for o mais alto.
	 */
	fun next(): EnumRank {
		var rank: EnumRank?

		var i = level
		do {
			i++
			rank = EnumRank[i]
		} while (rank == null)

		return rank
	}

	/**
	 * Retorna **true** se este [EnumRank] não é o **menor**.
	 *
	 * @return **true** se este [EnumRank] não é o **menor**.
	 */
	fun hasPrev(): Boolean = level > DEFAULT.level

	/**
	 * Retorna o [EnumRank] imediatamente **abaixo** deste na hirarquia.
	 *
	 * @return [EnumRank] imediatamente **abaixo** deste na hirarquia, 'null' se este for o mais baixo.
	 */
	fun prev(): EnumRank {
		var rank: EnumRank?

		var i = level
		do {
			i--
			rank = EnumRank[i]
		} while (rank == null)

		return rank
	}

	fun format(s: String): String = Text.of(s).color(color).styles(*styles).toString()

	override fun toString(): String = string

	companion object {

		private val BY_LEVEL = HashMap<Int, EnumRank>()

		init {
			values().forEach { BY_LEVEL.put(it.level, it) }
		}

		/**
		 * Retorna o [EnumRank] definido por um valor númerico 'int'.
		 *
		 * @param level
		 * @return [EnumRank] definido por um valor númerico 'int', 'null' se não houver um valor númerico 'int'
		 * associado a nenhum [EnumRank].
		 */
		operator fun get(level: Int): EnumRank = BY_LEVEL.getOrDefault(level, DEFAULT)
	}
}
