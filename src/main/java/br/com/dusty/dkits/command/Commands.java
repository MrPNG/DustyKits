package br.com.dusty.dkits.command;

import java.util.ArrayList;

public class Commands {
	
	public static final ArrayList<CustomCommand> CUSTOM_COMMANDS = new ArrayList<>();
	
	public static void registerAll() {
		//Usage: CUSTOM_COMMANDS.add(new FooCommand());
		
		for(CustomCommand customCommand : CUSTOM_COMMANDS){
			customCommand.register();
		}
	}
}
