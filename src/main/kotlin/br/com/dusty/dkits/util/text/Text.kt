package br.com.dusty.dkits.util.text

/**
 * Classe que gera uma [String] formatada com [TextColor] e [TextStyle], evitando a concatenação para ganhos
 * em performance e clareza de código.
 */
class Text {

	private var styles = arrayOf<TextStyle>()
	private var color = TextColor.WHITE
	private var string: String? = null
	private var append: Text? = null

	/**
	 * Define um array de [TextStyle] a serem usados como estilos.
	 *
	 * @param styles
	 * @return Este [Text].
	 */
	fun styles(vararg styles: TextStyle): Text {
		this.styles = arrayOf(*styles)

		return this
	}

	/**
	 * Define o [TextColor] a ser usado como cor.
	 *
	 * @param color
	 * @return Este [Text].
	 */
	fun color(color: TextColor?): Text {
		if (color != null) this.color = color

		return this
	}

	/**
	 * Anexa uma [String] a este [Text].
	 *
	 * @param s
	 * @return Este [Text].
	 */
	fun append(s: String): Text {
		val text = of(s)
		text.append = this

		return text
	}

	/**
	 * Anexa um 'int' a este [Text].
	 *
	 * @param i
	 * @return Este [Text].
	 */
	fun append(i: Int): Text {
		val text = of(i)
		text.append = this

		return text
	}

	/**
	 * Anexa um [Text] básico.
	 *
	 * @param s
	 * @return
	 */
	fun basic(s: String): Text = append(s).color(TextColor.GRAY)

	/**
	 * Anexa um [Text] básico.
	 *
	 * @param i
	 * @return
	 */
	fun basic(i: Int): Text = append(i).color(TextColor.GRAY)

	/**
	 * Anexa um [Text] neutro.
	 *
	 * @param s
	 * @return
	 */
	fun neutral(s: String): Text = append(s).color(TextColor.YELLOW)

	/**
	 * Anexa um [Text] neutro.
	 *
	 * @param i
	 * @return
	 */
	fun neutral(i: Int): Text = append(i).color(TextColor.YELLOW)

	/**
	 * Anexa um [Text] positivo.
	 *
	 * @param s
	 * @return
	 */
	fun positive(s: String): Text = append(s).color(TextColor.GREEN)

	/**
	 * Anexa um [Text] positivo.
	 *
	 * @param i
	 * @return
	 */
	fun positive(i: Int): Text = append(i).color(TextColor.GREEN)

	/**
	 * Anexa um [Text] negativo.
	 *
	 * @param s
	 * @return
	 */
	fun negative(s: String): Text = append(s).color(TextColor.RED)

	/**
	 * Anexa um [Text] negativo.
	 *
	 * @param i
	 * @return
	 */
	fun negative(i: Int): Text = append(i).color(TextColor.RED)

	/**
	 * Processa este [Text] em uma [String], aplicando cor e estilos e anexando outro [Text], se houver.
	 *
	 * @return [String] formatada a partir deste [Text] e acrescentada de outros [Text], se houver.
	 */
	override fun toString(): String {
		val sb = StringBuilder()

		if (append == null) {
			sb.append(color)

			for (style in styles) sb.append(style)
		} else {
			sb.append(append!!.toString())

			if (append!!.styles != styles) {
				sb.append(TextStyle.RESET)

				sb.append(color)

				for (style in styles) sb.append(style)
			} else if (append!!.color != color) {
				sb.append(color)
			}
		}

		sb.append(string)

		return sb.toString()
	}

	companion object {

		/**
		 * Cria um [Text] a partir da [String] dada como parâmetro.
		 *
		 * @param string
		 * @return O [Text] criado.
		 */
		fun of(string: String): Text {
			val text = Text()
			text.string = string

			return text
		}

		/**
		 * Cria um [Text] a partir do 'int' dado como parâmetro.
		 *
		 * @param i
		 * @return O [Text] criado.
		 */
		fun of(i: Int): Text {
			val text = Text()
			text.string = i.toString()

			return text
		}

		/**
		 * Cria um [Text] básico.
		 *
		 * @param s
		 * @return
		 */
		fun basicOf(s: String): Text = Text.of(s).color(TextColor.GRAY)

		/**
		 * Cria um [Text] básico.
		 *
		 * @param i
		 * @return
		 */
		fun basicOf(i: Int): Text = Text.of(i).color(TextColor.GRAY)

		/**
		 * Cria um [Text] neutro.
		 *
		 * @param s
		 * @return
		 */
		fun neutralOf(s: String): Text = Text.of(s).color(TextColor.YELLOW)

		/**
		 * Cria um [Text] neutro.
		 *
		 * @param i
		 * @return
		 */
		fun neutralOf(i: Int): Text = Text.of(i).color(TextColor.YELLOW)

		/**
		 * Cria um [Text] positivo.
		 *
		 * @param s
		 * @return
		 */
		fun positiveOf(s: String): Text = Text.of(s).color(TextColor.GREEN)

		/**
		 * Cria um [Text] positivo.
		 *
		 * @param i
		 * @return
		 */
		fun positiveOf(i: Int): Text = Text.of(i).color(TextColor.GREEN)

		/**
		 * Cria um [Text] negativo.
		 *
		 * @param s
		 * @return
		 */
		fun negativeOf(s: String): Text = Text.of(s).color(TextColor.RED)

		/**
		 * Cria um [Text] negativo.
		 *
		 * @param i
		 * @return
		 */
		fun negativeOf(i: Int): Text = Text.of(i).color(TextColor.RED)

		fun positivePrefix(): Text = positiveOf("» ")

		fun neutralPrefix(): Text = of("≡ ").color(TextColor.YELLOW)

		fun negativePrefix(): Text = negativeOf("» ")
	}
}
