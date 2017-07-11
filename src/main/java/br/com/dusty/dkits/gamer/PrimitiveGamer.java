package br.com.dusty.dkits.gamer;

import java.util.UUID;

public class PrimitiveGamer {
	
	private String uuid;
	
	private int id;
	
	int kills = 0, deaths = 0, killStreak = 0, maxKillStreak = 0;
	float xp = 0, money = 0;
	int hgWins = 0, hgLoses = 0;
	
	UUID getUUID() {
		return UUID.fromString(uuid);
	}
	
	void setUUID(UUID uuid){
		this.uuid = uuid.toString();
	}
}
