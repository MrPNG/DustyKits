package br.com.dusty.dkits.gamer;

import br.com.dusty.dkits.util.gamer.GamerUtils;
import br.com.dusty.dkits.util.protocol.ProtocolUtils;
import br.com.dusty.dkits.util.gamer.TagUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class Gamer {
	
	private Player player;
	
	private int protocolVersion = -1;
	
	private int kills = 0, deaths = 0, killStreak = 0, maxKillStreak = 0;
	private float xp = 0, money = 0;
	private int hgWins = 0, hgLoses = 0;
	
	private EnumRank rank = EnumRank.ADMIN;
	
	/**
	 * Menor {@link EnumRank} que pode ver este jogador.
	 */
	private EnumRank visibleTo;
	private EnumMode mode;
	
	private int invincibleTicks = 0, frozenTicks = 0;
	private long combatTagged = -1;
	
	//TODO: private EnumKit kit;
	//TODO: private EnumWarp warp;
	
	Gamer(Player player) {
		this.player = player;
		
		try{
			this.protocolVersion = ProtocolUtils.protocolVersion(player);
		}catch(InvocationTargetException | IllegalAccessException e){
			e.printStackTrace();
		}
		
		//TODO: Get PrimitiveGamer data
		
		if(rank.isLowerThan(EnumRank.MOD)){
			setMode(EnumMode.PLAY);
			setVisibleTo(EnumRank.DEFAULT);
		}else{
			setMode(EnumMode.ADMIN);
		}
		
		for(Gamer gamer : GamerRegistry.getOnlineGamers()){
			if(rank.isLowerThan(gamer.visibleTo))
				player.hidePlayer(gamer.getPlayer());
		}
		
		TagUtils.applyTag(this);
	}
	
	public static Gamer of(Player player) {
		return GamerRegistry.getGamerByPlayer(player);
	}
	
	public void setVisibleTo(EnumRank visibleTo) {
		if(this.visibleTo == visibleTo)
			return;
		
		this.visibleTo = visibleTo;
		
		for(Gamer gamer : GamerRegistry.getOnlineGamers()){
			if(gamer.rank.isLowerThan(rank))
				gamer.player.hidePlayer(player);
			else
				gamer.player.showPlayer(player);
		}
		
		if(rank.isGreaterThanOrEquals(EnumRank.MOD)){
			Text text = Text.of("Agora você está ")
			                .color(TextColor.GRAY)
			                .append("visível")
			                .color(TextColor.GREEN)
			                .append(" apenas para ")
			                .color(TextColor.GRAY)
			                .append(visibleTo.name);
			
			if(rank.hasNext())
				text = text.append(" e acima!").color(TextColor.GRAY);
			
			player.sendMessage(text.toString());
		}
	}
	
	public void setMode(EnumMode mode) {
		if(this.mode == mode)
			return;
		
		this.mode = mode;
		
		switch(mode){
			case PLAY:
				GamerUtils.clear(this);
				GamerUtils.flight(this, false);
				
				//TODO: SURVIVAL on MiniHG
				player.setGameMode(GameMode.ADVENTURE);
				
				setVisibleTo(EnumRank.DEFAULT);
				break;
			case SPECTATE:
				break;
			case ADMIN:
				player.setGameMode(GameMode.CREATIVE);
				
				GamerUtils.flight(this, true);
				
				setVisibleTo(rank);
				break;
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getKills() {
		return kills;
	}
	
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	
	public int getKillStreak() {
		return killStreak;
	}
	
	public void setKillStreak(int killStreak) {
		this.killStreak = killStreak;
	}
	
	public int getMaxKillStreak() {
		return maxKillStreak;
	}
	
	public void setMaxKillStreak(int maxKillStreak) {
		this.maxKillStreak = maxKillStreak;
	}
	
	public float getXp() {
		return xp;
	}
	
	public void setXp(float xp) {
		this.xp = xp;
	}
	
	public float getMoney() {
		return money;
	}
	
	public void setMoney(float money) {
		this.money = money;
	}
	
	public int getHgWins() {
		return hgWins;
	}
	
	public void setHgWins(int hgWins) {
		this.hgWins = hgWins;
	}
	
	public int getHgLoses() {
		return hgLoses;
	}
	
	public void setHgLoses(int hgLoses) {
		this.hgLoses = hgLoses;
	}
	
	public EnumRank getRank() {
		return rank;
	}
	
	public void setRank(EnumRank rank) {
		this.rank = rank;
	}
}
