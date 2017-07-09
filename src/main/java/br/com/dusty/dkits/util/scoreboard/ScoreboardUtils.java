package br.com.dusty.dkits.util.scoreboard;

import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardUtils {
	
	private static final String[] LABELS = {Text.of("Score1").color(TextColor.GOLD).toString(),
	                                        Text.of("Score2").color(TextColor.GOLD).toString(),
	                                        Text.of("...").color(TextColor.GOLD).toString(),
	                                        Text.of("ScoreX").color(TextColor.GOLD).toString()};
	
	public static void create(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		player.setScoreboard(scoreboard);
		
		Objective objective = scoreboard.registerNewObjective(player.getName(), "dummy");
		objective.setDisplayName(player.getDisplayName());
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		update(player);
	}
	
	public static void update(Player... players) {
		for(Player player : players){
			Scoreboard scoreboard = player.getScoreboard();
			clear(scoreboard);
			
			Objective objective = scoreboard.getObjective(player.getName());
			
			String[] values = {Text.of("Value1").color(TextColor.YELLOW).toString(),
			                   Text.of("Value2").color(TextColor.YELLOW).toString(),
			                   "",
			                   Text.of("ValueX").color(TextColor.YELLOW).toString()};
			
			for(int i = 0; i < LABELS.length; i++){
				String score = LABELS[i] + ": " + values[i];
				objective.getScore(score).setScore(LABELS.length - i);
			}
		}
	}
	
	public static void clear(Scoreboard scoreboard) {
		for(String s : scoreboard.getEntries())
			scoreboard.resetScores(s);
	}
}
