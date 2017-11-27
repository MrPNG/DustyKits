package br.com.dusty.dkits.command

import br.com.dusty.dkits.command.gameplay.*
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
		//Usage: CUSTOM_COMMANDS.add(FooCommand)

		//Staff
		CUSTOM_COMMANDS.add(AdminCommand)
		CUSTOM_COMMANDS.add(ChatCommand)
		CUSTOM_COMMANDS.add(DisableCommand)
		CUSTOM_COMMANDS.add(EnableCommand)
		CUSTOM_COMMANDS.add(FlyCommand)
		CUSTOM_COMMANDS.add(ForceCommand)
		CUSTOM_COMMANDS.add(InvSeeCommand)
		CUSTOM_COMMANDS.add(IpCheckCommand)
		CUSTOM_COMMANDS.add(ProtocolCommand)
		CUSTOM_COMMANDS.add(RamCommand)
		CUSTOM_COMMANDS.add(SpyCommand)
		CUSTOM_COMMANDS.add(StopCommand)
		CUSTOM_COMMANDS.add(SyncCommand)
		CUSTOM_COMMANDS.add(StaffChatCommand)
		CUSTOM_COMMANDS.add(VisInvisCommand)
		CUSTOM_COMMANDS.add(WorldCommand)

		//Gameplay
		CUSTOM_COMMANDS.add(KitCommand)
		CUSTOM_COMMANDS.add(ProfileCommand)
		CUSTOM_COMMANDS.add(ReportCommand)
		CUSTOM_COMMANDS.add(ShopCommand)
		CUSTOM_COMMANDS.add(TellCommand)
		CUSTOM_COMMANDS.add(WarpCommand)

		CUSTOM_COMMANDS.forEach({ it.register() })
	}
}
