package br.com.dusty.dkits.command

import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.Gamer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.List

/**
 * Implementação manual da classe [Command] para ser utilizada em **todos** os comandos no plugin, evitando o
 * uso da categoria 'commands' do arquivo 'plugin.yml'.
 */
abstract class CustomCommand constructor(
		/**
		 * [EnumRank] mínimo necessário para usar este comando (embora o [org.bukkit.command.ConsoleCommandSender]
		 * sempre esteja autorizado).
		 */
		val rank: EnumRank, vararg aliases: String): Command(aliases[0], UNKNOWN, UNKNOWN, Arrays.asList(*aliases)) {

	init {
		if (commandMap == null) {
			val f = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
			f.isAccessible = true
			commandMap = f.get(Bukkit.getServer()) as CommandMap
		}
	}

	/**
	 * Método invocado quando este comando é executado.
	 *
	 * @param sender Pode ser um [org.bukkit.entity.Player] ou [org.bukkit.command.ConsoleCommandSender].
	 * @param alias  '/[alias]' que foi usado.
	 * @param args   Argumentos do comando, vindos da separação por espaços do comando enviado (i.e. '/[alias] [arg1] [arg2] [...]').
	 * @return **true**, se algo deu errado/não foi autorizado, **false** se tudo ocorreu como previsto.
	 */
	override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean = false

	/**
	 * Define a [java.util.ArrayList] de 'aliases' a ser retornada quando 'TAB' é pressionado.
	 *
	 * @param sender Pode ser um [org.bukkit.entity.Player] ou [org.bukkit.command.ConsoleCommandSender].
	 * @param alias  '/[alias]' que foi usado (parcial ou completo).
	 * @param args   Argumentos do comando, vindos da separação por espaços do comando enviado (i.e. '/[alias] [arg1] [arg2] [...]').
	 * @return [java.util.ArrayList] de 'aliases' (pode ser vazia, mas não 'null').
	 * @throws IllegalArgumentException Caso a [List] seja 'null'.
	 */
	override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String>? = arrayListOf()

	/**
	 * Usar 'tabComplete(CommandSender sender, String alias, String[] args)'.
	 *
	 * @param sender
	 * @param alias
	 * @param args
	 * @param location
	 * @return
	 * @throws IllegalArgumentException
	 */
	override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>, location: Location?): MutableList<String>? = tabComplete(sender, alias, args)

	/**
	 * Verfica se o [CommandSender] 'sender' está autorizado a utilizar este comando.
	 *
	 * @param sender
	 * @return **true** se o [CommandSender] 'sender' está autorizado a utilizar este comando ou **false**,
	 * caso contrário.
	 */
	override fun testPermission(sender: CommandSender): Boolean {
		val b = if (sender is Player) Gamer[sender].rank.isHigherThanOrEquals(rank)
		else sender is ConsoleCommandSender

		if (!b) sender.sendMessage(UNKNOWN)

		return b
	}

	/**
	 * Registra este comando no 'commandMap' definido.
	 */
	fun register() {
		commandMap!!.register(label, PREFIX, this)
	}

	companion object {

		/**
		 * Prefixo de todos os comandos encontrado ao pressionar 'TAB' no chat.
		 */
		val PREFIX = "dusty"
		/**
		 * 'Placeholder' para comandos não autorizados a jogadores aleatórios.
		 */
		val UNKNOWN = "Unknown command. Type \"/help\" for help."

		/**
		 * [CommandMap] onde este comando será registrado.
		 */
		var commandMap: CommandMap? = null
	}
}
