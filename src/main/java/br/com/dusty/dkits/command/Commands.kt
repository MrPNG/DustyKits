package br.com.dusty.dkits.command

import br.com.dusty.dkits.command.gameplay.KitCommand
import br.com.dusty.dkits.command.gameplay.WarpCommand
import br.com.dusty.dkits.command.staff.AdminCommand
import br.com.dusty.dkits.command.staff.DisableCommand
import br.com.dusty.dkits.command.staff.EnableCommand
import br.com.dusty.dkits.gamer.EnumRank
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
		CUSTOM_COMMANDS.add(AdminCommand(EnumRank.MOD, "admin"))
		CUSTOM_COMMANDS.add(DisableCommand(EnumRank.ADMIN, "disable"))
		CUSTOM_COMMANDS.add(EnableCommand(EnumRank.ADMIN, "enable"))

		//Gameplay
		CUSTOM_COMMANDS.add(KitCommand(EnumRank.DEFAULT, "kit"))
		CUSTOM_COMMANDS.add(WarpCommand(EnumRank.DEFAULT, "warp"))

		CUSTOM_COMMANDS.forEach({ it.register() })
	}
}
