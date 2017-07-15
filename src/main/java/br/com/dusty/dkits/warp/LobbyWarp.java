package br.com.dusty.dkits.warp;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.kit.Kit;
import br.com.dusty.dkits.util.ItemStackUtils;
import br.com.dusty.dkits.util.LocationUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LobbyWarp extends Warp {
	
	{
		NAME = "Lobby";
		ICON = new ItemStack(Material.BOOK);
		
		ENTRY_KIT = new LobbyKit();
		
		DATA.setSpreadRange(4);
		
		loadData();
	}
	
	@Override
	public void receiveGamer(Gamer gamer) {
		Player player = gamer.getPlayer();
		
		player.teleport(LocationUtils.spread(getSpawn(), DATA.getSpreadRange()));
		
		gamer.setKit(ENTRY_KIT);
		ENTRY_KIT.apply(gamer);
	}
	
	private class LobbyKit extends Kit {
		
		{
			//TODO: Rules book
			ITEMS = new ItemStack[]{ItemStackUtils.rename(new ItemStack(Material.EMPTY_MAP),
			                                              Text.of("Warps").color(TextColor.GOLD).toString()),
			                        null,
			                        null,
			                        null,
			                        null,
			                        null,
			                        null,
			                        null,
			                        ItemStackUtils.rename(new ItemStack(Material.WRITTEN_BOOK),
			                                              Text.of("Regras").color(TextColor.GOLD).toString())};
		}
	}
}
