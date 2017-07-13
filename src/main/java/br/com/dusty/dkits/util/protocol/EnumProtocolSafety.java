package br.com.dusty.dkits.util.protocol;

import br.com.dusty.dkits.util.text.TextColor;

public enum EnumProtocolSafety {
	
	EXCELLENT(TextColor.DARK_GREEN),
	GOOD(TextColor.GREEN),
	REGULAR(TextColor.YELLOW),
	BAD(TextColor.RED),
	TERRIBLE(TextColor.DARK_RED);
	
	TextColor color;
	
	EnumProtocolSafety(TextColor color) {
		this.color = color;
	}
}
