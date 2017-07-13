package br.com.dusty.dkits.gamer;

import java.util.UUID;

public class PrimitiveGamer {
	
	private String uuid;
	
	private int id = -1;
	
	int kills = 0, deaths = 0, killStreak = 0, maxKillStreak = 0;
	float xp = 0, money = 0;
	int hgWins = 0, hgLoses = 0;
	
	UUID getUUID() {
		return UUID.fromString(uuid);
	}
	
	void setUUID(UUID uuid) {
		this.uuid = uuid.toString();
	}
	
	@Override
	public String toString() {
		return "PrimitiveGamer{" + "uuid='" + uuid + '\'' + ", id=" + id + ", kills=" + kills + ", deaths=" + deaths + ", killStreak=" + killStreak + ", maxKillStreak=" + maxKillStreak + ", xp=" + xp + ", money=" + money + ", hgWins=" + hgWins + ", hgLoses=" + hgLoses + '}';
	}
}
