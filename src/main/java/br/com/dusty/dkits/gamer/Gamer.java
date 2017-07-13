package br.com.dusty.dkits.gamer;

import br.com.dusty.dkits.util.ScoreboardUtils;
import br.com.dusty.dkits.util.gamer.GamerUtils;
import br.com.dusty.dkits.util.gamer.TagUtils;
import br.com.dusty.dkits.util.protocol.ProtocolUtils;
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
	
	private long invincible = -1, frozen = -1;
	private long cooldown = -1, signCooldown = -1;
	private long combatTagged = -1;
	
	//TODO: private Kit kit;
	//TODO: private Warp warp;
	
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
	
	public void addKill() {
		primitiveGamer.kills++;
		
		ScoreboardUtils.update(this);
	}
	
	public int getDeaths() {
		return primitiveGamer.deaths;
	}
	
	public void addDeath() {
		primitiveGamer.deaths++;
		
		ScoreboardUtils.update(this);
	}
	
	public int getKillStreak() {
		return primitiveGamer.killStreak;
	}
	
	public void addToKillStreak() {
		primitiveGamer.killStreak++;
		
		ScoreboardUtils.update(this);
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
	
	public void addXp(float amount) {
		primitiveGamer.xp += amount;
		
		player.sendMessage(Text.of("+" + Math.round(amount) + " XP!").color(TextColor.GOLD).toString());
		ScoreboardUtils.update(this);
	}
	
	public float getMoney() {
		return primitiveGamer.money;
	}
	
	public void addMoney(float amount) {
		primitiveGamer.money += amount;
		
		player.sendMessage(Text.of("+" + Math.round(amount) + " créditos!").color(TextColor.GOLD).toString());
		ScoreboardUtils.update(this);
	}
	
	public int getHgWins() {
		return primitiveGamer.hgWins;
	}
	
	public void addHgWin() {
		primitiveGamer.hgWins++;
	}
	
	public int getHgLoses() {
		return primitiveGamer.hgLoses;
	}
	
	public void addHgLoss() {
		primitiveGamer.hgLoses++;
	}
	
	public boolean isInvincible() {
		return invincible > System.currentTimeMillis();
	}
	
	public void makeInvincible(long period) {
		invincible = System.currentTimeMillis() + period;
	}
	
	public void removeInvincibility() {
		invincible = -1;
	}
	
	public boolean isFrozen() {
		return frozen > System.currentTimeMillis();
	}
	
	public void freeze(long period) {
		frozen = System.currentTimeMillis() + period;
	}
	
	public void removeFreeze() {
		frozen = -1;
	}
	
	public boolean isOnCooldown() {
		return cooldown > System.currentTimeMillis();
	}
	
	public void setCooldown(long period) {
		cooldown = System.currentTimeMillis() + period;
	}
	
	public void removeCooldown() {
		cooldown = -1;
	}
	
	public boolean isOnSignCooldown() {
		return signCooldown > System.currentTimeMillis();
	}
	
	public int getSignCooldown() {
		return Math.round((signCooldown - System.currentTimeMillis()) / 1000);
	}
	
	public void setSignCooldown(long period) {
		signCooldown = System.currentTimeMillis() + period;
	}
	
	public void removeSignCooldown() {
		signCooldown = -1;
	}
	
	public EnumRank getRank() {
		return rank;
	}
}
