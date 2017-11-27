package br.com.dusty.dkits.gamer

import java.util.*

class PrimitiveGamer(var uuid: String) {

	var id = -1

	var kills = 0
	var deaths = 0
	var killStreak = 0
	var maxKillStreak = 0
	var xp = 0.0
	var money = 0.0
	var hgWins = 0
	var hgLosses = 0
	var oneVsOneWins = 0
	var oneVsOneLosses = 0

	fun getUniqueId(): UUID = UUID.fromString(uuid)

	fun setUniqueId(uniqueId: UUID): PrimitiveGamer {
		this.uuid = uniqueId.toString()

		return this
	}
}
