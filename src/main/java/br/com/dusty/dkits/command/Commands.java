package br.com.dusty.dkits.command;

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
		
		CUSTOM_COMMANDS.forEach(CustomCommand::register);
	}
}
