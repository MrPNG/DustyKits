package br.com.dusty.dkits.command;

import br.com.dusty.dkits.enumeration.EnumRank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * Implementação manual da classe {@link Command} para ser utilizada em <b>todos</b> os comandos no plugin, evitando o
 * uso da categoria 'commands' do arquivo 'plugin.yml'.
 */
public abstract class CustomCommand extends Command {
	
	/**
	 * Prefixo de todos os comandos encontrado ao pressionar 'TAB' no chat.
	 */
	protected static final String PREFIX = "dusty";
	/**
	 * 'Placeholder' para comandos não autorizados a jogadores aleatórios.
	 */
	protected static final String UNKNOWN = "Unknown command. Type \"/help\" for help.";
	
	/**
	 * {@link CommandMap} onde este comando será registrado.
	 */
	private static CommandMap commandMap;
	
	/**
	 * {@link EnumRank} mínimo necessário para usar este comando (embora o {@link org.bukkit.command.ConsoleCommandSender}
	 * sempre esteja autorizado).
	 */
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
	
	/**
	 * Método invocado quando este comando é executado.
	 *
	 * @param sender Pode ser um {@link org.bukkit.entity.Player} ou {@link org.bukkit.command.ConsoleCommandSender}.
	 * @param alias  '/[alias]' que foi usado.
	 * @param args   Argumentos do comando, vindos da separação por espaços do comando enviado (i.e. '/[alias] [arg1] [arg2] [...]').
	 * @return <b>true</b>, se algo deu errado/não foi autorizado, <b>false</b> se tudo ocorreu como previsto.
	 */
	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		return false;
	}
	
	/**
	 * Define a {@link java.util.ArrayList} de 'aliases' a ser retornada quando 'TAB' é pressionado.
	 *
	 * @param sender Pode ser um {@link org.bukkit.entity.Player} ou {@link org.bukkit.command.ConsoleCommandSender}.
	 * @param alias  '/[alias]' que foi usado (parcial ou completo).
	 * @param args   Argumentos do comando, vindos da separação por espaços do comando enviado (i.e. '/[alias] [arg1] [arg2] [...]').
	 * @return {@link java.util.ArrayList} de 'aliases' (pode ser vazia, mas não 'null').
	 * @throws IllegalArgumentException Caso a {@link List} seja 'null'.
	 */
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		return Collections.emptyList();
	}
	
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
	@Deprecated
	@Override
	public List<String> tabComplete(CommandSender sender,
	                                String alias,
	                                String[] args,
	                                Location location) throws IllegalArgumentException {
		return tabComplete(sender, alias, args);
	}
	
	/**
	 * Verfica se o {@link CommandSender} 'sender' está autorizado a utilizar este comando.
	 *
	 * @param sender
	 * @return <b>true</b> se o {@link CommandSender} 'sender' está autorizado a utilizar este comando ou <b>false</b>,
	 * caso contrário.
	 */
	@Override
	public boolean testPermission(CommandSender sender) {
		//TODO: return sender instanceof Player ? !Gamer.of((Player) sender).getRank().isBelow(rank) : sender instanceof ConsoleCommandSender;
		return false;
	}
	
	/**
	 * Registra este comando no 'commandMap' definido.
	 */
	public void register() {
		commandMap.register(getLabel(), PREFIX, this);
	}
}
