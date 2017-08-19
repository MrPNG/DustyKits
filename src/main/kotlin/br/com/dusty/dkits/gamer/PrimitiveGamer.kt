package br.com.dusty.dkits.gamer

import java.util.UUID

class PrimitiveGamer {

	private var uuid = ""

	private val id = -1

	var kills = 0
	var deaths = 0
	var killStreak = 0
	var maxKillStreak = 0
	var xp = 0f
	var money = 0f
	var hgWins = 0
	var hgLosses = 0

	internal fun getUniqueId(): UUID {
		return UUID.fromString(uuid)
	}

	internal fun setUniqueId(uuid: UUID) {
		this.uuid = uuid.toString()
	}
}
