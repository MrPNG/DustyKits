package br.com.dusty.dkits.util.text;

import org.bukkit.ChatColor;

/**
 * Define estilos a serem usados em um {@link Text}.
 */
public enum TextStyle {
	
	MAGIC(0x10, ChatColor.MAGIC),
	BOLD(0x11, ChatColor.BOLD),
	STRIKETHROUGH(0x12, ChatColor.STRIKETHROUGH),
	UNDERLINE(0x13, ChatColor.UNDERLINE),
	ITALIC(0x14, ChatColor.ITALIC),
	RESET(0x15, ChatColor.RESET);
	
	int code;
	String string;
	
	TextStyle(int code, ChatColor chatColor) {
		this.code = code;
		string = chatColor.toString();
	}
	
	@Override
	public String toString() {
		return string;
	}
}
