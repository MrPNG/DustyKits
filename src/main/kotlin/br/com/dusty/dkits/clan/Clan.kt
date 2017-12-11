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

	var tag
		get() = primitiveClan.tag
		set(value) {
			primitiveClan.tag = value
		}

	var leader = Bukkit.getPlayer(UUID.fromString(primitiveClan.leader))?.gamer()
		set(value) {
			if (value != null) {
				field = leader

				primitiveClan.leader = value.player.uniqueId.toString()
			}
		}

	val rawMembers
		get() = primitiveClan.members

	val onlineMembers = primitiveClan.members.mapNotNull { Bukkit.getPlayer(UUID.fromString(it))?.gamer() }.toMutableList()

	var kills
		get() = primitiveClan.kills
		set(value) {
			primitiveClan.kills = value
		}

	fun addKill() = kills++

	var deaths
		get() = primitiveClan.deaths
		set(value) {
			primitiveClan.deaths = value
		}

	fun addDeath() = kills++

	var xp
		get() = primitiveClan.xp
		set(value) {
			primitiveClan.xp = value
		}

	fun addXp(amount: Double) {
		primitiveClan.xp += Math.abs(amount)
	}

	fun removeXp(amount: Double) {
		primitiveClan.xp -= Math.abs(amount)
	}

	var clanVsClanWins
		get() = primitiveClan.clanVsClanWins
		set(value) {
			primitiveClan.clanVsClanWins = value
		}

	fun addClanVsClanWin() = clanVsClanWins++

	var clanVsClanLosses
		get() = primitiveClan.clanVsClanLosses
		set(value) {
			primitiveClan.clanVsClanLosses = value
		}

	fun addClanVsClanLoss() = clanVsClanLosses++

	fun add(gamer: Gamer) {
		onlineMembers.add(gamer)

		primitiveClan.members = primitiveClan.members.add(gamer.player.uniqueId.toString()).first
	}

	fun add(uuid: String) {
		primitiveClan.members = primitiveClan.members.add(uuid).first
	}

	fun remove(gamer: Gamer) {
		onlineMembers.remove(gamer)

		primitiveClan.members = primitiveClan.members.remove(gamer.player.uniqueId.toString()).first
	}

	fun remove(uuid: String) {
		primitiveClan.members = primitiveClan.members.remove(uuid).first
	}

	override fun equals(other: Any?) = when {
		this === other                        -> true
		javaClass != other?.javaClass         -> false
		uuid != (other as PrimitiveClan).uuid -> false
		else                                  -> true
	}

	override fun hashCode() = uuid.hashCode()

	override fun toString(): String {
		return "Clan(uuid='$uuid', leader=$leader, onlineMembers=$onlineMembers)"
	}
}
