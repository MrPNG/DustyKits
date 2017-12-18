package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.entity.Player

object BuyCommand: PlayerCustomCommand(EnumRank.DEFAULT, "buy", "loja", "store") {

	val STORE_LINK = ComponentBuilder("\n\n[").color(ChatColor.GREEN).append("Dusty").color(ChatColor.GOLD).append("] Visite nossa loja em ").color(ChatColor.GREEN).append("www.dusty.com.br").color(
			ChatColor.GOLD).underlined(true).event(ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.dusty.com.br/")).append("!\n\n").color(ChatColor.GREEN).create()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		sender.spigot().sendMessage(*STORE_LINK)

		return false
	}
}
