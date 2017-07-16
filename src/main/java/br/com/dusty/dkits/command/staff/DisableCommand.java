package br.com.dusty.dkits.command.staff;

import br.com.dusty.dkits.command.CustomCommand;
import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.kit.Kit;
import br.com.dusty.dkits.kit.Kits;
import br.com.dusty.dkits.util.text.Text;
import org.bukkit.command.CommandSender;

public class DisableCommand extends CustomCommand {
	
	public DisableCommand(EnumRank rank, String alias) {
		super(rank, alias);
	}
	
	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		if(!testPermission(sender))
			return true;
		
		if(args.length > 1){
			switch(args[0]){
				case "kit":
					Kit kit = Kits.byName(args[1]);
					
					if(kit == null){
						sender.sendMessage(Text.negativePrefix()
						                       .basic("Não")
						                       .basic(" há um kit com o nome \"")
						                       .negative(args[0])
						                       .basic("\"!")
						                       .toString());
					}else{
						if(args.length > 2){
							//TODO: Disable kit on certain warps
						}else{
							if(kit.enabled(false))
								sender.sendMessage(Text.negativePrefix()
								                       .basic("O kit ")
								                       .negative(kit.getName())
								                       .basic(" foi ")
								                       .negative("desabilitado")
								                       .basic("!")
								                       .toString());
							else
								sender.sendMessage(Text.negativePrefix()
								                       .basic("O kit ")
								                       .negative(kit.getName())
								                       .basic(" já está ")
								                       .negative("desabilitado")
								                       .basic("!")
								                       .toString());
						}
					}
					
					break;
				case "warp":
					//TODO: Disable warps
					break;
			}
		}
		
		return false;
	}
}
