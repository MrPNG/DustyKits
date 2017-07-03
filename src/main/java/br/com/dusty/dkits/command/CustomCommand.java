package br.com.dusty.dkits.command;

import br.com.dusty.dkits.enumeration.EnumRank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public abstract class CustomCommand extends Command {
	
	protected static final String PREFIX = "dusty";
	protected static final String UNKNOWN = "Unknown command. Type \"/help\" for help.";
	
	private static CommandMap commandMap;
	
	protected final EnumRank rank;
	
	protected CustomCommand(String command, EnumRank rank) {
		super(command, UNKNOWN, UNKNOWN, Collections.emptyList());
		this.rank = rank;
		
		if(commandMap == null){
			try{
				Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
				f.setAccessible(true);
				commandMap = (CommandMap) f.get(Bukkit.getServer());
			}catch(IllegalAccessException | NoSuchFieldException e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		return false;
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		return Collections.emptyList();
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender,
	                                String alias,
	                                String[] args,
	                                Location location) throws IllegalArgumentException {
		return tabComplete(sender, alias, args);
	}
	
	@Override
	public boolean testPermission(CommandSender sender) {
		/*return sender instanceof Player ? !Gamer.of((Player) sender)
		                                        .getRank()
		                                        .isBelow(rank) : sender instanceof ConsoleCommandSender;*/
		return false;
	}
	
	public void register() {
		commandMap.register(getLabel(), PREFIX, this);
	}
}
