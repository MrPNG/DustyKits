package br.com.dusty.dkits.util.inventory

import br.com.dusty.dkits.clan.Clan
import br.com.dusty.dkits.clan.ClanRegistry
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.skull
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

object ClanVsClanMenu {

	val TITLE_CLANS = Text.of("Clans Online").color(TextColor.GOLD).toString()

	fun menuClans(player: Player, allMembersOnlineOnly: Boolean): Inventory {
		val clans = if (allMembersOnlineOnly) ClanRegistry.onlineClans().filter { it.leader != null && it.onlineMembers.size == 5 } else ClanRegistry.onlineClans()

		return Bukkit.createInventory(player, 27 + Math.floor(clans.size / 7.0).toInt() * 9, TITLE_CLANS).apply {
			fillBackground(false)
			fillClans(clans)
		}
	}

	fun Inventory.fillClans(clans: Collection<Clan>) {
		var i = -1

		clans.forEach {
			i += if (i + 2 % 9 == 0) 3 else 1

			val leader = if (it.leader == null) arrayListOf() else arrayListOf(Text.positiveOf("Líder:").toString(),
			                                                                   Text.neutralOf(it.leader!!.player.displayName.clearFormatting()).toString(),
			                                                                   " ")

			val members = arrayListOf(Text.positiveOf("Membros Online:").toString())
			it.onlineMembers.forEach { members.add(Text.neutralOf(it.player.displayName.clearFormatting()).toString()) }
			members.add(" ")

			val uuid = arrayListOf(Text.positiveOf("UUID:").toString(), Text.neutralOf(it.uuid).toString())

			val description = arrayListOf<String>()
			description.addAll(leader)
			description.addAll(members)
			description.addAll(uuid)

			setItem(10 + i, it.leader!!.player.skull().rename(Text.of(it.name).color(TextColor.GOLD).toString()).description(description, false))
		}
	}
}
