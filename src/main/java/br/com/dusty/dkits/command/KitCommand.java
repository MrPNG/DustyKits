package br.com.dusty.dkits.command;

import br.com.dusty.dkits.gamer.EnumMode;
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
	
	public KitCommand(EnumRank rank, String aliases) {
		super(rank, aliases);
	}
	
	//TODO: Check if the kit is enabled on the warp or if gamer is on admin-mode
	//TODO: Check if warp isn't MiniHG or if gamer is on admin-mode
	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		if(!testPermission(sender))
			return true;
		
		if(!(sender instanceof Player))
			return true;
		
		Player player = (Player) sender;
		
		Gamer gamer = Gamer.of(player);
		
		if(args.length == 0){
			//TODO: Open kit menu
		}else{
			Kit kit = Kits.byName(args[0]);
			
			if(kit == null){
				player.sendMessage(Text.negativeOf("Não")
				                       .neutral(" há um kit com o nome \"")
				                       .negative(args[0])
				                       .neutral("\"!")
				                       .toString());
			}else if(!gamer.hasKit(kit) && gamer.getMode() != EnumMode.ADMIN){
				player.sendMessage(Text.neutralOf("Você ")
				                       .negative("não")
				                       .neutral(" tem o kit ")
				                       .negative(kit.NAME)
				                       .neutral("!")
				                       .toString());
			}else{
				gamer.setKit(kit).apply(gamer);
				
				player.sendMessage(Text.neutralOf("Agora você está ")
				                       .positive("usando")
				                       .neutral(" o kit ")
				                       .positive(kit.NAME)
				                       .neutral("!")
				                       .toString());
			}
		}
		
		return false;
	}
	
	//TODO: Check if the kit is enabled on the warp or if gamer is on admin-mode
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		ArrayList<String> tabCompletions = new ArrayList<>();
		
		for(Kit kit : Kits.KITS)
			if(kit.DATA.ENABLED)
				if(args.length == 0 || kit.NAME.toLowerCase().startsWith(args[0].toLowerCase()))
					tabCompletions.add(kit.NAME.toLowerCase());
		
		return tabCompletions;
	}
}
