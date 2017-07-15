package br.com.dusty.dkits.gamer;

import br.com.dusty.dkits.kit.Kit;
import br.com.dusty.dkits.util.ScoreboardUtils;
import br.com.dusty.dkits.util.TaskUtils;
import br.com.dusty.dkits.util.gamer.GamerUtils;
import br.com.dusty.dkits.util.gamer.TagUtils;
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion;
import br.com.dusty.dkits.util.protocol.ProtocolUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import br.com.dusty.dkits.warp.Warp;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;

public class Gamer {
	
	private Player player;
	
	private EnumProtocolVersion protocolVersion;
	
	private PrimitiveGamer primitiveGamer;
	
	private EnumRank rank = EnumRank.ADMIN;
	
	/**
	 * Menor {@link EnumRank} que pode ver este jogador.
	 */
	private EnumRank visibleTo;
	private EnumMode mode;
	
	private long invincibility = -1, freeze = -1;
	private long cooldown = -1, signCooldown = -1;
	
	private long combatTag = -1;
	private BukkitTask combatTask;
	
	private boolean noFall;
	
	private Kit kit;
	private Warp warp;
	
	private BukkitTask warpTask;
	
	Gamer(Player player, PrimitiveGamer primitiveGamer) {
		this.player = player;
		this.primitiveGamer = primitiveGamer;
		
		try{
			this.protocolVersion = EnumProtocolVersion.byVersionNumber(ProtocolUtils.protocolVersion(player));
		}catch(InvocationTargetException | IllegalAccessException e){
			e.printStackTrace();
		}
		
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
			Text text = Text.neutralOf("Agora você está ").positive("visível").neutral(" apenas para ").append(visibleTo.name);
			
			if(visibleTo.hasNext())
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
		
		player.sendMessage(Text.neutralOf("Agora você está no modo ").positive(mode.name()).neutral("!").toString());
		
		switch(mode){
			case PLAY:
				//TODO: SURVIVAL on MiniHG
				player.setGameMode(GameMode.ADVENTURE);
				
				GamerUtils.clear(this);
				GamerUtils.fly(this, false);
				
				setVisibleTo(EnumRank.DEFAULT);
				break;
			case SPECTATE:
				break;
			case ADMIN:
				player.setGameMode(GameMode.CREATIVE);
				
				GamerUtils.fly(this, true);
				
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
	
	public EnumProtocolVersion getProtocolVersion() {
		return protocolVersion;
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
		return invincibility > System.currentTimeMillis();
	}
	
	public void makeInvincible(long period) {
		invincibility = System.currentTimeMillis() + period;
	}
	
	public void removeInvincibility() {
		invincibility = -1;
	}
	
	public boolean isFrozen() {
		return freeze > System.currentTimeMillis();
	}
	
	public void freeze(long period) {
		freeze = System.currentTimeMillis() + period;
	}
	
	public void removeFreeze() {
		freeze = -1;
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
	
	public boolean isCombatTagged() {
		return combatTag > System.currentTimeMillis();
	}
	
	public int getCombatTag() {
		return Math.round((combatTag - System.currentTimeMillis()) / 1000);
	}
	
	public void setCombatTag(long period) {
		if(!isCombatTagged()){
			player.sendMessage(Text.neutralOf("Você ")
			                       .negative("entrou")
			                       .neutral(" em ")
			                       .negative("combate")
			                       .neutral("!")
			                       .toString());
		}else if(warpTask != null){
			warpTask.cancel();
			
			player.sendMessage(Text.neutralOf("Você entrou em ")
			                       .negative("combate")
			                       .neutral(" novamente, teleporte ")
			                       .negative("cancelado")
			                       .neutral("!")
			                       .toString());
		}
		
		combatTag = System.currentTimeMillis() + period;
		
		if(combatTask != null)
			combatTask.cancel();
		
		combatTask = TaskUtils.sync(() -> ScoreboardUtils.update(this), 200);
	}
	
	public void removeCombatTag() {
		if(isCombatTagged())
			player.sendMessage(Text.neutralOf("Você ")
			                       .positive("saiu")
			                       .neutral(" em ")
			                       .positive("combate")
			                       .neutral("!")
			                       .toString());
		
		combatTag = -1;
		
		if(combatTask != null)
			combatTask.cancel();
	}
	
	public boolean hasNoFall() {
		return noFall;
	}
	
	public void setNoFall(boolean noFall) {
		this.noFall = noFall;
	}
	
	public EnumRank getRank() {
		return rank;
	}
	
	//TODO: Gamer.hasKit()
	public boolean hasKit(Kit kit) {
		return true;
	}
	
	public Kit getKit() {
		return this.kit;
	}
	
	public void setKit(Kit kit) {
		this.kit = kit;
	}
	
	public Warp getWarp() {
		return this.warp;
	}
	
	public void sendToWarp(Warp warp) {
		if(warpTask != null)
			warpTask.cancel();
		
		if(isCombatTagged()){
			long ticks = (long) Math.ceil((combatTag - System.currentTimeMillis()) / 50.0);
			int seconds = Math.round(ticks / 20);
			
			warpTask = TaskUtils.sync(() -> sendToWarp(warp), ticks);
			
			player.sendMessage(Text.neutralOf("Você será teleportado em ")
			                       .negative(seconds)
			                       .neutral(" segundo(s), ")
			                       .negative("não")
			                       .neutral(" se ")
			                       .negative("mova")
			                       .neutral("!")
			                       .toString());
		}else{
			GamerUtils.clear(this);
			
			this.warp = warp;
			warp.receiveGamer(this);
		}
	}
}
