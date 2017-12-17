package br.com.dusty.dkits.command

import br.com.dusty.dkits.command.gameplay.*
import br.com.dusty.dkits.command.overwrite.StopCommand
import br.com.dusty.dkits.command.overwrite.TellCommand
import br.com.dusty.dkits.command.staff.*
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.warp.Warps
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import java.util.*

object Commands {

	/**
	 * Prefixo de todos os comandos encontrado ao pressionar 'TAB' no chat.
	 */
	val PREFIX = "dusty"

	val COMMAND_MAP: CommandMap

	/**
	 * [ArrayList] que contém todos os [CustomCommand] a serem/já registrados pelo plugin.
	 */
	val CUSTOM_COMMANDS = hashSetOf<CustomCommand>()

	init {
		val field_commandMap = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
		field_commandMap.isAccessible = true
		COMMAND_MAP = field_commandMap.get(Bukkit.getServer()) as CommandMap
	}

	/**
	 * Registra todos os [CustomCommand] da [ArrayList] CUSTOM_COMMANDS.
	 */
	fun registerAll() {
		//Usage: CUSTOM_COMMANDS.add(FooCommand)

		//Staff
		CUSTOM_COMMANDS.add(AdminCommand)
		CUSTOM_COMMANDS.add(ChatCommand)
		CUSTOM_COMMANDS.add(ClanCommand)
		CUSTOM_COMMANDS.add(DisableCommand)
		CUSTOM_COMMANDS.add(EnableCommand)
		CUSTOM_COMMANDS.add(FlyCommand)
		CUSTOM_COMMANDS.add(ForceCommand)
		CUSTOM_COMMANDS.add(GamerCommand)
		CUSTOM_COMMANDS.add(InvSeeCommand)
		CUSTOM_COMMANDS.add(IpCheckCommand)
		CUSTOM_COMMANDS.add(LocationCommand)
		CUSTOM_COMMANDS.add(ProtocolCommand)
		CUSTOM_COMMANDS.add(RamCommand)
		CUSTOM_COMMANDS.add(SpyCommand)
		CUSTOM_COMMANDS.add(SyncCommand)
		CUSTOM_COMMANDS.add(StaffChatCommand)
		CUSTOM_COMMANDS.add(VisInvisCommand)
		CUSTOM_COMMANDS.add(WorldCommand)

		//Gameplay

		CUSTOM_COMMANDS.add(ProfileCommand)
		CUSTOM_COMMANDS.add(ReportCommand)
		CUSTOM_COMMANDS.add(ShopCommand)

		CUSTOM_COMMANDS.add(KitCommand)
		KitCommand.aliases.addAll(Kits.enabledKitsNames)

		CUSTOM_COMMANDS.add(WarpCommand)
		WarpCommand.aliases.addAll(Warps.enabledWarpsNames)
		Warps.WARPS.filter { it.data.isEnabled }.forEach { WarpCommand.aliases.addAll(it.aliases) }

		//Overwrite
		CUSTOM_COMMANDS.add(StopCommand)
		CUSTOM_COMMANDS.add(TellCommand)

		CUSTOM_COMMANDS.forEach({ register(it) })
	}

	/**
	 * Registra este comando no 'commandMap' definido.
	 */
	fun register(command: Command) {
		COMMAND_MAP.register(command.label, PREFIX, command)
	}
}
