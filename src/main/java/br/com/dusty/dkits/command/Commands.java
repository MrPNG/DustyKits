package br.com.dusty.dkits.command;

import br.com.dusty.dkits.command.gameplay.KitCommand;
import br.com.dusty.dkits.command.gameplay.WarpCommand;
import br.com.dusty.dkits.command.staff.AdminCommand;
import br.com.dusty.dkits.command.staff.DisableCommand;
import br.com.dusty.dkits.command.staff.EnableCommand;
import br.com.dusty.dkits.gamer.EnumRank;

import java.util.ArrayList;

public class Commands {
	
	/**
	 * {@link ArrayList} que contém todos os {@link CustomCommand} a serem/já registrados pelo plugin.
	 */
	private static final ArrayList<CustomCommand> CUSTOM_COMMANDS = new ArrayList<>();
	
	/**
	 * Registra todos os {@link CustomCommand} da {@link ArrayList} CUSTOM_COMMANDS.
	 */
	public static void registerAll() {
		//Usage: CUSTOM_COMMANDS.add(new FooCommand());
		
		//Staff
		CUSTOM_COMMANDS.add(new AdminCommand(EnumRank.MOD, "admin"));
		CUSTOM_COMMANDS.add(new DisableCommand(EnumRank.ADMIN, "disable"));
		CUSTOM_COMMANDS.add(new EnableCommand(EnumRank.ADMIN, "enable"));
		
		//Gameplay
		CUSTOM_COMMANDS.add(new KitCommand(EnumRank.DEFAULT, "kit"));
		CUSTOM_COMMANDS.add(new WarpCommand(EnumRank.DEFAULT, "warp"));
		
		CUSTOM_COMMANDS.forEach(CustomCommand::register);
	}
}
