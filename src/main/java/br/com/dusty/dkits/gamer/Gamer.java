package br.com.dusty.dkits.gamer;

import br.com.dusty.dkits.kit.Kit;
import br.com.dusty.dkits.kit.Kits;
import br.com.dusty.dkits.util.ScoreboardUtils;
import br.com.dusty.dkits.util.TaskUtils;
import br.com.dusty.dkits.util.gamer.GamerUtils;
import br.com.dusty.dkits.util.gamer.TagUtils;
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion;
import br.com.dusty.dkits.util.protocol.ProtocolUtils;
import br.com.dusty.dkits.util.tab.HeaderFooterUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.warp.Warp;
import br.com.dusty.dkits.warp.Warps;
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
	private Gamer combatPartner;
	private BukkitTask combatTask;
	
	private boolean noFall;
	
	private Kit kit = Kits.NONE;
	private Warp warp = Warps.LOBBY;
	
	private BukkitTask warpTask;
	
	private EnumChat chat;
	
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
		
		if(rank.isHigherThanOrEquals(EnumRank.MOD)){
			Text text = Text.basicOf("Agora você está ").positive("visível").basic(" apenas para ").append(visibleTo.name);
			
			if(visibleTo.hasNext())
				text = text.basic(" e acima");
			
			player.sendMessage(text.basic("!").toString());
		}
	}
	
	public EnumMode getMode() {
		return mode;
	}
	
	public void setMode(EnumMode mode) {
		if(this.mode == mode)
			return;
		
		this.mode = mode;
		
		player.sendMessage(Text.basicOf("Agora você está no modo ").positive(mode.name()).basic("!").toString());
		
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
	
	public void addKillMoney() { //TODO: Different money for VIPs
		addMoney(100);
		
		ScoreboardUtils.update(this);
	}
	
	public void addKillXp() {
		addXp(10);
		
		ScoreboardUtils.update(this);
	}
	
	public int getDeaths() {
		return primitiveGamer.deaths;
	}
	
	public void addDeath() {
		primitiveGamer.deaths++;
		
		ScoreboardUtils.update(this);
	}
	
	public void removeDeathMoney() {
		removeMoney(50);
		
		ScoreboardUtils.update(this);
	}
	
	public void removeDeathXp() {
		removeXp(5);
		
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
		
		player.sendMessage(Text.positiveOf("+").positive(Math.round(amount)).basic(" XP!").toString());
		ScoreboardUtils.update(this);
	}
	
	public void removeXp(float amount) {
		primitiveGamer.xp += amount;
		
		player.sendMessage(Text.negativeOf("-").negative(Math.round(amount)).basic(" XP!").toString());
		ScoreboardUtils.update(this);
	}
	
	public float getMoney() {
		return primitiveGamer.money;
	}
	
	public void addMoney(float amount) {
		primitiveGamer.money += amount;
		
		player.sendMessage(Text.positiveOf("+").positive(Math.round(amount)).basic(" créditos!").toString());
		;
		ScoreboardUtils.update(this);
	}
	
	public void removeMoney(float amount) {
		primitiveGamer.money += amount;
		
		player.sendMessage(Text.negativeOf("-").negative(Math.round(amount)).basic(" créditos!").toString());
		;
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
		if(!isCombatTagged())
			player.sendMessage(Text.negativePrefix()
			                       .basic("Você ")
			                       .negative("entrou")
			                       .basic(" em ")
			                       .negative("combate")
			                       .basic("!")
			                       .toString());
		
		combatTag = System.currentTimeMillis() + period;
		
		if(combatTask != null)
			combatTask.cancel();
		
		combatTask = TaskUtils.sync(this::removeCombatTag, 200);
		
		ScoreboardUtils.update(this);
	}
	
	public void removeCombatTag() {
		player.sendMessage(Text.positivePrefix()
		                       .basic("Você ")
		                       .positive("saiu")
		                       .basic(" de ")
		                       .positive("combate")
		                       .basic("!")
		                       .toString());
		
		combatTag = -1;
		
		if(combatTask != null)
			combatTask.cancel();
		
		ScoreboardUtils.update(this);
	}
	
	public Gamer getCombatPartner() {
		return combatPartner;
	}
	
	public void setCombatPartner(Gamer combatPartner) {
		this.combatPartner = combatPartner;
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
		
		ScoreboardUtils.update(this);
	}
	
	public Warp getWarp() {
		return this.warp;
	}
	
	public void sendToWarp(Warp warp) {
		if(warpTask != null){
			warpTask.cancel();
			warpTask = null;
		}
		
		if(isCombatTagged()){
			long ticks = (long) Math.ceil((combatTag - System.currentTimeMillis()) / 50.0);
			int seconds = Math.round(ticks / 20);
			
			warpTask = TaskUtils.sync(() -> sendToWarp(warp), ticks);
			
			player.sendMessage(Text.neutralPrefix()
			                       .basic("Você será teleportado em ")
			                       .neutral(seconds)
			                       .basic(" segundo(s), ")
			                       .neutral("não")
			                       .basic(" se ")
			                       .neutral("mova")
			                       .basic("!")
			                       .toString());
		}else{
			GamerUtils.clear(this);
			
			this.warp = warp;
			warp.receiveGamer(this);
			
			ScoreboardUtils.update(this);
			
			if(protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8))
				HeaderFooterUtils.update(this);
		}
	}
	
	public BukkitTask getWarpTask() {
		return warpTask;
	}
	
	public void setWarpTask(BukkitTask warpTask) {
		this.warpTask = warpTask;
	}
	
	public EnumChat getChat() {
		return chat;
	}
	
	public void setChat(EnumChat chat) {
		this.chat = chat;
	}
}
