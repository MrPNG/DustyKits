package br.com.dusty.dkits.clan

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.add
import br.com.dusty.dkits.util.remove
import org.bukkit.Bukkit
import java.util.*

class Clan(val primitiveClan: PrimitiveClan) {

	val uuid = primitiveClan.uuid

	var name
		get() = primitiveClan.name
		set(value) {
			primitiveClan.name = value
		}

	var nickname
		get() = primitiveClan.nickname
		set(value) {
			primitiveClan.nickname = value
		}

	var leader = Bukkit.getPlayer(primitiveClan.leader).gamer()
		set(value) {
			field = leader

			primitiveClan.leader = leader.player.uniqueId.toString() //TODO: Sync Clan
		}

	val members = primitiveClan.members.mapTo(arrayListOf()) { Bukkit.getPlayer(UUID.fromString(it)).gamer() }

	fun add(gamer: Gamer) = {
		members.add(gamer)

		primitiveClan.members = primitiveClan.members.add(gamer.player.uniqueId.toString()).first
	}

	fun remove(gamer: Gamer) = {
		members.remove(gamer)

		primitiveClan.members = primitiveClan.members.remove(gamer.player.uniqueId.toString()).first
	}
}
