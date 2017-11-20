package br.com.dusty.dkits.util

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.util.text.Text
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import java.util.ArrayList
import kotlin.Comparator

/**
 * Divide uma [String] em outras (não-formatadas) de tamanho máximo 'max', inserindo-as, em ordem, em uma [ArrayList].
 *
 * @param s
 * @param max
 * @return
 */
fun String.fancySplit(max: Int): ArrayList<String> {
	val arrayList = ArrayList<String>()

	val fragments = Text.clearFormatting(this).split(" ")
	var i = 0
	while (i < fragments.size) {
		val fragment = StringBuilder()
		fragment.append(fragments[i])

		while (i + 1 < fragments.size && fragment.length + 1 + fragments[i + 1].length <= max) {
			fragment.append(" ")
			fragment.append(fragments[i + 1])
			i++
		}

		arrayList.add(fragment.toString())
		i++
	}

	return arrayList
}

/**
 * Formata uma [Collection] de [String] por extenso, separadas por vírgulas.
 *
 * @param collection
 * @return
 */
fun Collection<String>.format(): String {
	val stringBuilder = StringBuilder()

	val iterator = this.iterator()

	if (iterator.hasNext()) stringBuilder.append(iterator.next())

	while (iterator.hasNext()) stringBuilder.append(", ").append(iterator.next())

	return stringBuilder.toString()
}

/**
 * Cria uma [String] que indica uma duração de tempo com dias, horas, minutos e segundos.
 *
 * @param millis
 * @return
 */
fun Long.periodString(): String {
	val periodFormatter = PeriodFormatterBuilder().appendDays().appendSuffix("d").appendSeparator(" ").appendHours().appendSuffix("h").appendSeparator(" ").appendMinutes().appendSuffix("min").appendSeparator(
			" ").appendSeconds().appendSuffix("s").toFormatter()

	val period = Period(this)

	return periodFormatter.print(period.toPeriod())
}

/**
 * Cria uma [ArrayList] contendo todas as [String]'s do parâmetro 'arrayList' que iniciarem com o parâmetro 'start'.
 *
 * @param start
 * @param arrayList
 * @return
 */
fun ArrayList<String>.sortOut(start: String): ArrayList<String> {
	this.filterNot { it.startsWith(start, true) }.forEach { this.remove(it) }

	this.sortWith(Comparator { obj, str -> obj.compareTo(str, ignoreCase = true) })

	return this
}

object StringUtils {

	private val ALPHANUMERIC = "ABCDEFGHIKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()

	/**
	 * Gera uma [String] alfanumérica aleatória com diferenciação entre maiúsculas e minúsculas.
	 *
	 * @param length
	 * @return
	 */
	fun randomString(length: Int): String {
		val stringBuilder = StringBuilder(length)

		for (i in 0 until length) {
			val index = Main.RANDOM.nextInt(ALPHANUMERIC.size)
			stringBuilder.append(ALPHANUMERIC[index])
		}

		return stringBuilder.toString()
	}
}
