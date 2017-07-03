package br.com.dusty.dkits.util.text;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.HashSet;

public class Text {
	
	private final HashSet<TextStyle> styles = new HashSet<>();
	private TextColor color = TextColor.WHITE;
	private String string = "";
	private String append = "";
	
	public static Text of(String string) {
		Text text = new Text();
		text.string = string;
		
		return text;
	}
	
	public Text styles(TextStyle style) {
		styles.add(style);
		
		return this;
	}
	
	public Text styles(TextStyle... styles) {
		this.styles.addAll(Arrays.asList(styles));
		
		return this;
	}
	
	public Text color(TextColor color) {
		this.color = color;
		
		return this;
	}
	
	public Text append(Text text) {
		append = text.toString();
		
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(TextStyle style : styles){
			sb.append(style);
		}
		sb.append(color);
		sb.append(string);
		sb.append(append);
		
		return sb.toString();
	}
	
	public static String stripColor(String string) {
		return ChatColor.stripColor(string);
	}
}
