package br.com.dusty.dkits.command;

import br.com.dusty.dkits.gamer.EnumMode;
import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.gamer.Gamer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand extends CustomCommand {
	
	protected AdminCommand(EnumRank rank, String aliases) {
		super(rank, aliases);
	}
	
	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		if(!testPermission(sender))
			return true;
		
		if(!(sender instanceof Player))
			return true;
		
		Gamer gamer = Gamer.of((Player) sender);
		
		gamer.setMode(gamer.getMode() == EnumMode.ADMIN ? EnumMode.PLAY : EnumMode.ADMIN);
		
		return false;
	}
}
