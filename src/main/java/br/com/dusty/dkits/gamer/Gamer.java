package br.com.dusty.dkits.gamer;

import org.bukkit.entity.Player;

public class Gamer {
	
	private Player player;
	
	private int protocolVersion;
	
	private int kills, deaths, killStreak, maxKillStreak;
	private float xp, money;
	private int hgWins, hgLoses;
	
	private EnumRank rank;
	
	/**
	 * Menor {@link EnumRank} que pode ver este jogador.
	 */
	private EnumRank visible;
	/**
	 * Maior {@link EnumRank} que este jogador pode ver.
	 */
	private EnumRank see;
	
	private int invincibleTicks, frozenTicks;
	private long combatTagged;
	
	//TODO: private EnumKit kit;
	//TODO: private EnumWarp warp;
	
	Gamer(Player player) {
		this.player = player;
	}
	
	public static Gamer of(Player player){
		return GamerRegistry.getGamerByPlayer(player);
	}
	
}
