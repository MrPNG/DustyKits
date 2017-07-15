package br.com.dusty.dkits.listener.quit;

import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.gamer.GamerRegistry;
import br.com.dusty.dkits.util.ScoreboardUtils;
import br.com.dusty.dkits.util.TaskUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.web.WebAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
	
	private static final String QUIT_MESSAGE_PREFIX = Text.neutralOf("[").negative("-").neutral("] ").toString();
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		Gamer gamer = GamerRegistry.unregister(player);
		
		if(gamer.isCombatTagged()){
			Gamer combatPartner = gamer.getCombatPartner();
			
			if(combatPartner != null){
				combatPartner.addKill();
				combatPartner.addKillMoney();
				combatPartner.addKillXp();
				
				gamer.addDeath();
				gamer.removeDeathMoney();
				gamer.removeDeathXp();
			}
			
			Bukkit.broadcastMessage(Text.negativeOf(player.getName())
			                            .neutral(" deslogou em ")
			                            .negative("combate")
			                            .neutral("!")
			                            .toString());
		}
		
		TaskUtils.async(() -> WebAPI.saveProfiles(gamer));
		
		if(gamer.getRank().isLowerThan(EnumRank.MOD))
			event.setQuitMessage(QUIT_MESSAGE_PREFIX + player.getName());
		else
			event.setQuitMessage(null);
		
		ScoreboardUtils.updateAll();
	}
}
