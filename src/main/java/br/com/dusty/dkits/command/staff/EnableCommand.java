package br.com.dusty.dkits.command.staff;

import br.com.dusty.dkits.command.CustomCommand;
import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.kit.Kit;
import br.com.dusty.dkits.kit.Kits;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.warp.Warp;
import br.com.dusty.dkits.warp.Warps;
import org.bukkit.command.CommandSender;

public class EnableCommand extends CustomCommand {
	
	public EnableCommand(EnumRank rank, String alias) {
		super(rank, alias);
	}
	
	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		if(!testPermission(sender))
			return true;
		
		if(args.length > 1)
			switch(args[0].toLowerCase()){
				case "kit":
					Kit kit = Kits.byName(args[1]);
					
					if(kit == null){
						sender.sendMessage(Text.positivePrefix()
						                       .basic("Não")
						                       .basic(" há um kit com o nome \"")
						                       .negative(args[0])
						                       .basic("\"!")
						                       .toString());
					}else{
						if(args.length > 2){
							Warp warp = Warps.byName(args[2]);
							
							if(warp == null){
								sender.sendMessage(Text.negativePrefix()
								                       .basic("Não")
								                       .basic(" há uma warp com o nome \"")
								                       .negative(args[0])
								                       .basic("\"!")
								                       .toString());
							}else{
								if(warp.enableKit(kit, true))
									sender.sendMessage(Text.positivePrefix()
									                       .basic("O kit ")
									                       .positive(kit.getName())
									                       .basic(" foi ")
									                       .positive("habilitado")
									                       .basic(" na warp ")
									                       .positive(warp.getName())
									                       .basic("!")
									                       .toString());
								else
									sender.sendMessage(Text.positivePrefix()
									                       .basic("O kit ")
									                       .positive(kit.getName())
									                       .basic(" já está ")
									                       .positive("habilitado")
									                       .basic(" na warp ")
									                       .positive(warp.getName())
									                       .basic("!")
									                       .toString());
							}
						}else{
							if(kit.enabled(true))
								sender.sendMessage(Text.positivePrefix()
								                       .basic("O kit ")
								                       .positive(kit.getName())
								                       .basic(" foi ")
								                       .positive("habilitado")
								                       .basic("!")
								                       .toString());
							else
								sender.sendMessage(Text.positivePrefix()
								                       .basic("O kit ")
								                       .positive(kit.getName())
								                       .basic(" já está ")
								                       .positive("habilitado")
								                       .basic("!")
								                       .toString());
						}
					}
					
					break;
				case "warp":
					Warp warp = Warps.byName(args[1]);
					
					if(warp == null){
						sender.sendMessage(Text.negativePrefix()
						                       .basic("Não")
						                       .basic(" há uma warp com o nome \"")
						                       .negative(args[0])
						                       .basic("\"!")
						                       .toString());
					}else{
						if(warp.enabled(true))
							sender.sendMessage(Text.positivePrefix()
							                       .basic("A warp ")
							                       .positive(warp.getName())
							                       .basic(" foi ")
							                       .positive("habilitada")
							                       .basic("!")
							                       .toString());
						else
							sender.sendMessage(Text.positivePrefix()
							                       .basic("A warp ")
							                       .positive(warp.getName())
							                       .basic(" já está ")
							                       .positive("habilitada")
							                       .basic("!")
							                       .toString());
					}
					
					break;
			}
		else
			sender.sendMessage(Text.negativePrefix()
			                       .basic("Uso: /enable ")
			                       .negative("<kit>/<warp> <nome>")
			                       .basic(" [warp]")
			                       .toString());
		
		return false;
	}
}
