package br.com.dusty.dkits.gamer;

import java.util.UUID;

public class PrimitiveGamer {
	
	private String uuid;
	
	private int kills, deaths, killStreak, maxKillStreak;
	private float xp, money;
	private int hgWins, hgLoses;
	
	int getKills() {
		return kills;
	}
	
	int getDeaths() {
		return deaths;
	}
	
	int getKillStreak() {
		return killStreak;
	}
	
	int getMaxKillStreak() {
		return maxKillStreak;
	}
	
	float getXp() {
		return xp;
	}
	
	float getMoney() {
		return money;
	}
	
	int getHgWins() {
		return hgWins;
	}
	
	int getHgLoses() {
		return hgLoses;
	}
	
	UUID getUUID() {
		return UUID.fromString(uuid);
	}
}
