package br.com.dusty.dkits.gamer;

import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import br.com.dusty.dkits.util.text.TextStyle;

import java.util.HashMap;

/**
 * Enum que contém todos os {@link EnumRank} do servidor associados a um valor numérico 'int' que identifica a hierarquia entre eles,
 * usada nos métodos da própria classe.
 */
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
	TextColor color;
	TextStyle[] styles;
	String name;
	
	EnumRank(int level) {
		this.level = level;
		name = format(name());
	}
	
	EnumRank(int level, TextColor color) {
		this.level = level;
		this.color = color;
		name = format(name());
	}
	
	EnumRank(int level, TextColor color, TextStyle... styles) {
		this.level = level;
		this.color = color;
		this.styles = styles;
		name = format(name());
	}
	
	/**
	 * Retorna o {@link EnumRank} definido por um valor númerico 'int'.
	 *
	 * @param level
	 * @return {@link EnumRank} definido por um valor númerico 'int', 'null' se não houver um valor númerico 'int'
	 * associado a nenhum {@link EnumRank}.
	 */
	public static EnumRank byLevel(int level) {
		return BY_LEVEL.get(level);
	}
	
	/**
	 * Retorna <b>true</b> se este {@link EnumRank} está hierarquicamente <b>acima</b> do parâmetro 'rank'.
	 *
	 * @param rank
	 * @return <b>true</b> se este {@link EnumRank} está hierarquicamente <b>acima</b> do parâmetro 'rank'.
	 */
	public boolean isGreaterThan(EnumRank rank) {
		return level > rank.level;
	}
	
	/**
	 * Retorna <b>true</b> se este {@link EnumRank} está hierarquicamente <b>abaixo</b> do parâmetro 'rank'.
	 *
	 * @param rank
	 * @return <b>true</b> se este {@link EnumRank} está hierarquicamente <b>abaixo</b> do parâmetro 'rank'.
	 */
	public boolean isLowerThan(EnumRank rank) {
		return level < rank.level;
	}
	
	/**
	 * Retorna <b>true</b> se este {@link EnumRank} não está hierarquicamente <b>abaixo</b> do parâmetro 'rank'.
	 *
	 * @param rank
	 * @return <b>true</b> se este {@link EnumRank} não está hierarquicamente <b>abaixo</b> do parâmetro 'rank'.
	 */
	public boolean isGreaterThanOrEquals(EnumRank rank) {
		return level >= rank.level;
	}
	
	/**
	 * Retorna <b>true</b> se este {@link EnumRank} não está hierarquicamente <b>acima</b> do parâmetro 'rank'.
	 *
	 * @param rank
	 * @return <b>true</b> se este {@link EnumRank} não está hierarquicamente <b>acima</b> do parâmetro 'rank'.
	 */
	public boolean isLowerThanOrEquals(EnumRank rank) {
		return level <= rank.level;
	}
	
	/**
	 * Retorna <b>true</b> se este {@link EnumRank} não é o <b>maior</b>.
	 *
	 * @return <b>true</b> se este {@link EnumRank} não é o <b>maior</b>.
	 */
	public boolean hasNext() {
		return level < ADMIN.level;
	}
	
	/**
	 * Retorna o {@link EnumRank} imediatamente <b>acima</b> deste na hirarquia.
	 *
	 * @return {@link EnumRank} imediatamente <b>acima</b> deste na hirarquia, 'null' se este for o mais alto.
	 */
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
	
	/**
	 * Retorna <b>true</b> se este {@link EnumRank} não é o <b>menor</b>.
	 *
	 * @return <b>true</b> se este {@link EnumRank} não é o <b>menor</b>.
	 */
	public boolean hasPrev() {
		return level > DEFAULT.level;
	}
	
	/**
	 * Retorna o {@link EnumRank} imediatamente <b>abaixo</b> deste na hirarquia.
	 *
	 * @return {@link EnumRank} imediatamente <b>abaixo</b> deste na hirarquia, 'null' se este for o mais baixo.
	 */
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
	
	public String format(String s) {
		return Text.of(s).color(color).styles(styles).toString();
	}
}
