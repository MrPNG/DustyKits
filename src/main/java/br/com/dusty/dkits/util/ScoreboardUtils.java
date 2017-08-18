package br.com.dusty.dkits.util;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.gamer.GamerRegistry;
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardUtils {
	
	private static final String[] LABELS = {Text.of("XP: ").color(TextColor.GOLD).toString(),
	                                        Text.of("Créditos: ").color(TextColor.GOLD).toString(),
	                                        Text.of("------------").color(TextColor.YELLOW).toString(),
	                                        Text.of("Kills: ").color(TextColor.RED).toString(),
	                                        Text.of("Deaths: ").color(TextColor.RED).toString(),
	                                        Text.of("Killstreak: ").color(TextColor.RED).toString(),
	                                        Text.of("------------ ").color(TextColor.YELLOW).toString(),
	                                        Text.of("Kit: ").color(TextColor.AQUA).toString(),
	                                        Text.of("Combate: ").color(TextColor.AQUA).toString(),
	                                        Text.of("Players: ").color(TextColor.AQUA).toString()};
	
	private static final String[] LABELS_OLD = {ChatColor.GOLD + "XP: ",
	                                            ChatColor.GOLD + "$: ",
	                                            ChatColor.YELLOW + "=-=-=-=-=-=-",
	                                            ChatColor.RED + "Kills: ",
	                                            ChatColor.RED + "Deaths: ",
	                                            ChatColor.RED + "KS: ",
	                                            ChatColor.YELLOW + "-=-=-=-=-=-=",
	                                            ChatColor.AQUA + "Kit: ",
	                                            ChatColor.AQUA + "Combate: ",
	                                            ChatColor.AQUA + "Players: "};
	
	public static void create(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		player.setScoreboard(scoreboard);
		
		Objective objective = scoreboard.registerNewObjective(player.getName(), "dummy");
		objective.setDisplayName(player.getDisplayName());
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public static void update(Gamer gamer) {
		Player player = gamer.getPlayer();
		
		Scoreboard scoreboard = player.getScoreboard();
		clear(scoreboard);
		
		Objective objective = scoreboard.getObjective(player.getName());
		
		if(gamer.getProtocolVersion().isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)){
			String[] values = {Text.of(Math.round(gamer.getXp())).color(TextColor.YELLOW).toString(),
			                   Text.of(Math.round(gamer.getMoney())).color(TextColor.YELLOW).toString(),
			                   "",
			                   Text.of(gamer.getKills()).color(TextColor.YELLOW).toString(),
			                   Text.of(gamer.getDeaths()).color(TextColor.YELLOW).toString(),
			                   Text.of(gamer.getKillStreak()).color(TextColor.YELLOW).toString(),
			                   "",
			                   Text.of(gamer.getKit().getName()).color(TextColor.YELLOW).toString(),
			                   Text.of(gamer.isCombatTagged() ? "Sim" : "Não").color(TextColor.YELLOW).toString(),
			                   Text.of(GamerRegistry.INSTANCE.getOnlineGamers().size()).color(TextColor.YELLOW).toString()};
			
			for(int i = 0; i < LABELS.length; i++){
				if(gamer.getKit().isDummy() && values[i].endsWith("None"))
					continue;
				
				Score score = objective.getScore(LABELS[i] + values[i]);
				
				if(score != null)
					score.setScore(LABELS.length - i);
			}
		}else{
			String[] values_old = {ChatColor.YELLOW + "" + Math.round(gamer.getXp()),
			                       ChatColor.YELLOW + "" + Math.round(gamer.getMoney()),
			                       "",
			                       ChatColor.YELLOW + "" + gamer.getKills(),
			                       ChatColor.YELLOW + "" + gamer.getDeaths(),
			                       ChatColor.YELLOW + "" + gamer.getKillStreak(),
			                       "",
			                       ChatColor.YELLOW + "" + gamer.getKit().getName(),
			                       ChatColor.YELLOW + "" + (gamer.isCombatTagged() ? "Sim" : "Não"),
			                       ChatColor.YELLOW + "" + GamerRegistry.INSTANCE.getOnlineGamers().size()};
			
			for(int i = 0; i < LABELS_OLD.length; i++){
				if(gamer.getKit().isDummy() && values_old[i].endsWith("None"))
					continue;
				
				Score score = objective.getScore(LABELS_OLD[i] + values_old[i]);
				
				if(score != null)
					score.setScore(LABELS_OLD.length - i);
			}
		}
	}
	
	public static void updateAll() {
		GamerRegistry.INSTANCE.getOnlineGamers().forEach(ScoreboardUtils::update);
	}
	
	private static void clear(Scoreboard scoreboard) {
		scoreboard.getEntries().forEach(scoreboard::resetScores);
	}
}
