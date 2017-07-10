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
		
		for(TextStyle style : styles){
			sb.append(style);
		}
		sb.append(color);
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
