package br.com.dusty.dkits.command

import br.com.dusty.dkits.command.gameplay.KitCommand
import br.com.dusty.dkits.command.gameplay.ShopCommand
import br.com.dusty.dkits.command.gameplay.WarpCommand
import br.com.dusty.dkits.command.staff.*
import java.util.*

object Commands {

	/**
	 * [ArrayList] que contém todos os [CustomCommand] a serem/já registrados pelo plugin.
	 */
	private val CUSTOM_COMMANDS = ArrayList<CustomCommand>()

	/**
	 * Registra todos os [CustomCommand] da [ArrayList] CUSTOM_COMMANDS.
	 */
	fun registerAll() {
		//Usage: CUSTOM_COMMANDS.add(FooCommand());

		//Staff
		CUSTOM_COMMANDS.add(AdminCommand)
		CUSTOM_COMMANDS.add(DisableCommand)
		CUSTOM_COMMANDS.add(EnableCommand)
		CUSTOM_COMMANDS.add(InvisCommand)
		CUSTOM_COMMANDS.add(InvSeeCommand)

		//Gameplay
		CUSTOM_COMMANDS.add(KitCommand)
		CUSTOM_COMMANDS.add(ShopCommand)
		CUSTOM_COMMANDS.add(WarpCommand)

		CUSTOM_COMMANDS.forEach({ it.register() })
	}
}
