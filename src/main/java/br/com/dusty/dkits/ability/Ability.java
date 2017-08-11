package br.com.dusty.dkits.ability;

import br.com.dusty.dkits.gamer.Gamer;
import org.bukkit.event.Listener;

public class Ability implements Listener {
	
	public boolean isUsing(Gamer gamer) {
		return this.getClass().isAssignableFrom(gamer.getKit().getAbility().getClass());
	}
}
