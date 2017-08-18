package br.com.dusty.dkits.command.gameplay;

import br.com.dusty.dkits.command.CustomCommand;
import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.kit.Kit;
import br.com.dusty.dkits.kit.Kits;
import br.com.dusty.dkits.util.text.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand extends CustomCommand {
	
	public KitCommand(EnumRank rank, String alias) {
		super(rank, alias);
	}
	
	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		if(!testPermission(sender))
			return true;
		
		if(!(sender instanceof Player))
			return true;
		
		Player player = (Player) sender;
		
		Gamer gamer = Gamer.Companion.of(player);
		
		if(args.length == 0){
			//TODO: Open kit menu
		}else{
			Kit kit = Kits.byName(args[0]);
			
			if(kit == null)
				player.sendMessage(Text.negativePrefix()
				                       .basic("Não")
				                       .basic(" há um kit com o nome \"")
				                       .negative(args[0])
				                       .basic("\"!")
				                       .toString());
			else
				kit.applyIfAllowed(gamer);
		}
		
		return false;
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		ArrayList<String> tabCompletions = new ArrayList<>();
		
		if(!(sender instanceof Player))
			return tabCompletions;
		
		Player player = (Player) sender;
		
		Gamer gamer = Gamer.Companion.of(player);
		
		for(Kit kit : Kits.KITS)
			if(kit.getData().isEnabled() && gamer.getWarp().getEnabledKits().contains(kit))
				if(args.length == 0 || kit.getName().toLowerCase().startsWith(args[0].toLowerCase()))
					tabCompletions.add(kit.getName().toLowerCase());
		
		return tabCompletions;
	}
}
