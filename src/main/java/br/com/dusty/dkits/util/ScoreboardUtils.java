package br.com.dusty.dkits.util;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.gamer.GamerRegistry;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardUtils {
	
	private static final String[] LABELS = {Text.of("| XP: ").color(TextColor.GOLD).toString(),
	                                        Text.of("| Créditos: ").color(TextColor.GOLD).toString(),
	                                        Text.of("------------").color(TextColor.YELLOW).toString(),
	                                        Text.of("| Kills: ").color(TextColor.RED).toString(),
	                                        Text.of("| Deaths: ").color(TextColor.RED).toString(),
	                                        Text.of("| Killstreak: ").color(TextColor.RED).toString(),
	                                        Text.of("------------ ").color(TextColor.YELLOW).toString(),
	                                        //Text.of("| Kit: ").color(TextColor.AQUA).toString(),
	                                        //Text.of("| Combate: ").color(TextColor.AQUA).toString(),
	                                        Text.of("| Players: ").color(TextColor.AQUA).toString()};
	
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
		
		String[] values = {Text.of(Math.round(gamer.getXp())).color(TextColor.YELLOW).toString(),
		                   Text.of(Math.round(gamer.getMoney())).color(TextColor.YELLOW).toString(),
		                   "",
		                   Text.of(gamer.getKills()).color(TextColor.YELLOW).toString(),
		                   Text.of(gamer.getDeaths()).color(TextColor.YELLOW).toString(),
		                   Text.of(gamer.getKillStreak()).color(TextColor.YELLOW).toString(),
		                   "",
		                   //Text.of(gamer.getKit()).color(TextColor.YELLOW).toString(),
		                   //Text.of(gamer.isInCombat() ? "Sim" : "Não").color(TextColor.YELLOW).toString(),
		                   Text.of(GamerRegistry.getOnlineGamers().size()).color(TextColor.YELLOW).toString()};
		
		for(int i = 0; i < LABELS.length; i++){
			String score = LABELS[i] + values[i];
			objective.getScore(score).setScore(LABELS.length - i);
		}
	}
	
	public static void updateAll() {
		GamerRegistry.getOnlineGamers().forEach(ScoreboardUtils::update);
	}
	
	private static void clear(Scoreboard scoreboard) {
		scoreboard.getEntries().forEach(scoreboard::resetScores);
	}
}
