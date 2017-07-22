package br.com.dusty.dkits.command.gameplay;

import br.com.dusty.dkits.command.CustomCommand;
import br.com.dusty.dkits.gamer.EnumMode;
import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.inventory.WarpMenu;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.warp.Warp;
import br.com.dusty.dkits.warp.Warps;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand extends CustomCommand {
	
	public WarpCommand(EnumRank rank, String alias) {
		super(rank, alias);
	}
	
	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		if(!testPermission(sender))
			return true;
		
		if(!(sender instanceof Player))
			return true;
		
		Player player = (Player) sender;
		
		Gamer gamer = Gamer.of(player);
		
		if(args.length == 0){
			player.openInventory(WarpMenu.menuWarpMain(player));
		}else{
			Warp warp = Warps.byName(args[0]);
			
			if(warp == null || (!warp.getData().isEnabled() && gamer.getMode() != EnumMode.ADMIN))
				player.sendMessage(Text.negativePrefix()
				                       .basic("Não")
				                       .basic(" há uma warp com o nome \"")
				                       .negative(args[0])
				                       .basic("\"!")
				                       .toString());
			else
				gamer.sendToWarp(warp);
		}
		
		return false;
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		ArrayList<String> tabCompletions = new ArrayList<>();
		
		if(!(sender instanceof Player))
			return tabCompletions;
		
		for(Warp warp : Warps.WARPS)
			if(warp.getData().isEnabled())
				if(args.length == 0 || warp.getName().toLowerCase().startsWith(args[0].toLowerCase()))
					tabCompletions.add(warp.getName().toLowerCase());
		
		return tabCompletions;
	}
}
