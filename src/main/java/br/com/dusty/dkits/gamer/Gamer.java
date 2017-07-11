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
	
	private PrimitiveGamer primitiveGamer;
	
	private EnumRank rank = EnumRank.ADMIN;
	
	/**
	 * Menor {@link EnumRank} que pode ver este jogador.
	 */
	private EnumRank visibleTo;
	private EnumMode mode;
	
	private int invincible = -1, frozen = -1;
	private long cooldown = -1, signCooldown = -1;
	private long combatTagged = -1;
	
	//TODO: private EnumKit kit;
	//TODO: private EnumWarp warp;
	
	Gamer(Player player, PrimitiveGamer primitiveGamer) {
		this.player = player;
		this.primitiveGamer = primitiveGamer;
		
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
	
	public EnumMode getMode() {
		return mode;
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
	
	public PrimitiveGamer getPrimitiveGamer() {
		return primitiveGamer;
	}
	
	public int getKills() {
		return primitiveGamer.kills;
	}
	
	public void setKills(int kills) {
		primitiveGamer.kills = kills;
	}
	
	public int getDeaths() {
		return primitiveGamer.deaths;
	}
	
	public void setDeaths(int deaths) {
		primitiveGamer.deaths = deaths;
	}
	
	public int getKillStreak() {
		return primitiveGamer.killStreak;
	}
	
	public void setKillStreak(int killStreak) {
		primitiveGamer.killStreak = killStreak;
	}
	
	public int getMaxKillStreak() {
		return primitiveGamer.maxKillStreak;
	}
	
	public void setMaxKillStreak(int maxKillStreak) {
		primitiveGamer.maxKillStreak = maxKillStreak;
	}
	
	public float getXp() {
		return primitiveGamer.xp;
	}
	
	public void setXp(float xp) {
		primitiveGamer.xp = xp;
	}
	
	public float getMoney() {
		return primitiveGamer.money;
	}
	
	public void setMoney(float money) {
		primitiveGamer.money = money;
	}
	
	public int getHgWins() {
		return primitiveGamer.hgWins;
	}
	
	public void setHgWins(int hgWins) {
		primitiveGamer.hgWins = hgWins;
	}
	
	public int getHgLoses() {
		return primitiveGamer.hgLoses;
	}
	
	public void setHgLoses(int hgLoses) {
		primitiveGamer.hgLoses = hgLoses;
	}
	
	public EnumRank getRank() {
		return rank;
	}
	
	public void setRank(EnumRank rank) {
		this.rank = rank;
	}
}
