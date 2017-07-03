package br.com.dusty.dkits.enumeration;

import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import br.com.dusty.dkits.util.text.TextStyle;

import java.util.HashMap;

public enum EnumRank {
	
	DEFAULT(0),
	//VIP(2, TextColor.GREEN),
	//MVP(3, TextColor.BLUE),
	PRO(4, TextColor.GOLD),
	YOUTUBER(6, TextColor.AQUA),
	MOD(8, TextColor.DARK_PURPLE),
	MODPLUS(9, TextColor.DARK_PURPLE, TextStyle.ITALIC),
	ADMIN(Integer.MAX_VALUE, TextColor.RED, TextStyle.ITALIC);
	
	private static final HashMap<Integer, EnumRank> BY_LEVEL = new HashMap<>();
	
	static {
		for(EnumRank rank : EnumRank.values()){
			BY_LEVEL.put(rank.level, rank);
		}
	}
	
	int level;
	String name;
	
	EnumRank(int level) {
		this.level = level;
		name = Text.of(name()).toString();
	}
	
	EnumRank(int level, TextColor color) {
		this.level = level;
		name = Text.of(name()).color(color).toString();
	}
	
	EnumRank(int level, TextColor color, TextStyle... styles) {
		this.level = level;
		name = Text.of(name()).color(color).styles(styles).toString();
	}
	
	public static EnumRank byLevel(int level) {
		return BY_LEVEL.get(level);
	}
	
	public boolean isAbove(EnumRank rank) {
		return level > rank.level;
	}
	
	public boolean isBelow(EnumRank rank) {
		return level < rank.level;
	}
	
	public boolean hasNext() {
		return level < ADMIN.level;
	}
	
	public EnumRank next() {
		EnumRank rank = null;
		
		int i = level;
		do{
			i++;
			rank = byLevel(i);
		}
		while(rank == null);
		
		return rank;
	}
	
	public boolean hasPrev() {
		return level > DEFAULT.level;
	}
	
	public EnumRank prev() {
		EnumRank rank = null;
		
		int i = level;
		do{
			i--;
			rank = byLevel(i);
		}
		while(rank == null);
		
		return rank;
	}
}
