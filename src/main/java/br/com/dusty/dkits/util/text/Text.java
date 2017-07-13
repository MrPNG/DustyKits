package br.com.dusty.dkits.util.text;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Classe que gera uma {@link String} formatada com {@link TextColor} e {@link TextStyle}, evitando a concatenação para ganhos
 * em performance e clareza de código.
 */
public class Text {
	
	private final HashSet<TextStyle> styles = new HashSet<>();
	private TextColor color = TextColor.WHITE;
	private String string = null;
	private Text append = null;
	
	/**
	 * Cria um {@link Text} a partir da {@link String} dada como parâmetro.
	 *
	 * @param string
	 * @return O {@link Text} criado.
	 */
	public static Text of(String string) {
		Text text = new Text();
		text.string = string;
		
		return text;
	}
	
	/**
	 * Cria um {@link Text} a partir do 'int' dado como parâmetro.
	 *
	 * @param i
	 * @return O {@link Text} criado.
	 */
	public static Text of(int i) {
		Text text = new Text();
		text.string = String.valueOf(i);
		
		return text;
	}
	
	/**
	 * Cria um {@link Text} a partir do 'float' dado como parâmetro.
	 *
	 * @param f
	 * @return O {@link Text} criado.
	 */
	public static Text of(float f) {
		Text text = new Text();
		text.string = String.valueOf(f);
		
		return text;
	}
	
	/**
	 * Cria um {@link Text} a partir do 'boolean' dado como parâmetro.
	 *
	 * @param bool
	 * @return O {@link Text} criado.
	 */
	public static Text of(boolean bool) {
		Text text = new Text();
		text.string = String.valueOf(bool);
		
		return text;
	}
	
	/**
	 * Define um array de {@link TextStyle} a serem usados como estilos.
	 *
	 * @param styles
	 * @return Este {@link Text}.
	 */
	public Text styles(TextStyle... styles) {
		if(styles != null)
			this.styles.addAll(Arrays.asList(styles));
		
		return this;
	}
	
	/**
	 * Define o {@link TextColor} a ser usado como cor.
	 *
	 * @param color
	 * @return Este {@link Text}.
	 */
	public Text color(TextColor color) {
		if(color != null)
			this.color = color;
		
		return this;
	}
	
	/**
	 * Anexa uma {@link String} a este {@link Text}.
	 *
	 * @param s
	 * @return Este {@link Text}.
	 */
	public Text append(String s) {
		Text text = of(s);
		text.append = this;
		
		return text;
	}
	
	/**
	 * Anexa um 'int' a este {@link Text}.
	 *
	 * @param i
	 * @return Este {@link Text}.
	 */
	public Text append(int i) {
		Text text = of(i);
		text.append = this;
		
		return text;
	}
	
	/**
	 * Anexa um 'float' a este {@link Text}.
	 *
	 * @param f
	 * @return Este {@link Text}.
	 */
	public Text append(float f) {
		Text text = of(f);
		text.append = this;
		
		return text;
	}
	
	/**
	 * Anexa um 'boolean' a este {@link Text}.
	 *
	 * @param b
	 * @return Este {@link Text}.
	 */
	public Text append(boolean b) {
		Text text = of(b);
		text.append = this;
		
		return text;
	}
	
	/**
	 * Cria um {@link Text} neutro.
	 *
	 * @param s
	 * @return
	 */
	public static Text neutralOf(String s) {
		return Text.of(s).color(TextColor.GRAY);
	}
	
	/**
	 * Cria um {@link Text} neutro.
	 *
	 * @param i
	 * @return
	 */
	public static Text neutralOf(int i) {
		return Text.of(i).color(TextColor.GRAY);
	}
	
	/**
	 * Cria um {@link Text} neutro.
	 *
	 * @param b
	 * @return
	 */
	public static Text neutralOf(boolean b) {
		return Text.of(b).color(TextColor.GRAY);
	}
	
	/**
	 * Anexa um {@link Text} neutro.
	 *
	 * @param s
	 * @return
	 */
	public Text neutral(String s) {
		return append(s).color(TextColor.GRAY);
	}
	
	/**
	 * Anexa um {@link Text} neutro.
	 *
	 * @param i
	 * @return
	 */
	public Text neutral(int i) {
		return append(i).color(TextColor.GRAY);
	}
	
	/**
	 * Anexa um {@link Text} neutro.
	 *
	 * @param b
	 * @return
	 */
	public Text neutral(boolean b) {
		return append(b).color(TextColor.GRAY);
	}
	
	/**
	 * Cria um {@link Text} positivo.
	 *
	 * @param s
	 * @return
	 */
	public static Text positiveOf(String s) {
		return Text.of(s).color(TextColor.GREEN);
	}
	
	/**
	 * Cria um {@link Text} positivo.
	 *
	 * @param i
	 * @return
	 */
	public static Text positiveOf(int i) {
		return Text.of(i).color(TextColor.GREEN);
	}
	
	/**
	 * Cria um {@link Text} positivo.
	 *
	 * @param b
	 * @return
	 */
	public static Text positiveOf(boolean b) {
		return Text.of(b).color(TextColor.GREEN);
	}
	
	/**
	 * Anexa um {@link Text} positivo.
	 *
	 * @param s
	 * @return
	 */
	public Text positive(String s) {
		return append(s).color(TextColor.GREEN);
	}
	
	/**
	 * Anexa um {@link Text} positivo.
	 *
	 * @param i
	 * @return
	 */
	public Text positive(int i) {
		return append(i).color(TextColor.GREEN);
	}
	
	/**
	 * Anexa um {@link Text} positivo.
	 *
	 * @param b
	 * @return
	 */
	public Text positive(boolean b) {
		return append(b).color(TextColor.GREEN);
	}
	
	/**
	 * Cria um {@link Text} negativo.
	 *
	 * @param s
	 * @return
	 */
	public static Text negativeOf(String s) {
		return Text.of(s).color(TextColor.RED);
	}
	
	/**
	 * Cria um {@link Text} negativo.
	 *
	 * @param i
	 * @return
	 */
	public static Text negativeOf(int i) {
		return Text.of(i).color(TextColor.RED);
	}
	
	/**
	 * Cria um {@link Text} negativo.
	 *
	 * @param b
	 * @return
	 */
	public static Text negativeOf(boolean b) {
		return Text.of(b).color(TextColor.RED);
	}
	
	/**
	 * Anexa um {@link Text} negativo.
	 *
	 * @param s
	 * @return
	 */
	public Text negative(String s) {
		return append(s).color(TextColor.RED);
	}
	
	/**
	 * Anexa um {@link Text} negativo.
	 *
	 * @param i
	 * @return
	 */
	public Text negative(int i) {
		return append(i).color(TextColor.RED);
	}
	
	/**
	 * Anexa um {@link Text} negativo.
	 *
	 * @param b
	 * @return
	 */
	public Text negative(boolean b) {
		return append(b).color(TextColor.RED);
	}
	
	/**
	 * Processa este {@link Text} em uma {@link String}, aplicando cor e estilos e anexando outro {@link Text}, se houver.
	 *
	 * @return {@link String} formatada a partir deste {@link Text} e acrescentada de outros {@link Text}, se houver.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if(append != null)
			sb.append(append.toString());
		
		sb.append(TextStyle.RESET);
		
		sb.append(color);
		for(TextStyle style : styles)
			sb.append(style);
		sb.append(string);
		
		return sb.toString();
	}
	
	/**
	 * Remove qualquer formatação de uma {@link String}, se houver.
	 *
	 * @param string
	 * @return {@link String} sem formatação.
	 */
	public static String clearFormatting(String string) {
		return ChatColor.stripColor(string);
	}
}
