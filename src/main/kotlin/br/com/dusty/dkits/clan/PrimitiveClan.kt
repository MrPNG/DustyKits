package br.com.dusty.dkits.clan

data class PrimitiveClan(val uuid: String,
                         var name: String = "",
                         var tag: String = "",
                         var leader: String = "",
                         var members: Array<String> = arrayOf(),
                         var kills: Int = 0,
                         var deaths: Int = 0,
                         var xp: Double = 0.0,
                         var clanVsClanWins: Int = 0,
                         var clanVsClanLosses: Int = 0) {

	override fun equals(other: Any?) = when {
		this === other                        -> true
		javaClass != other?.javaClass         -> false
		uuid != (other as PrimitiveClan).uuid -> false
		else                                  -> true
	}

	override fun hashCode() = uuid.hashCode()
}
