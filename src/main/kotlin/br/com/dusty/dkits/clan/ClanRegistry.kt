package br.com.dusty.dkits.clan

import br.com.dusty.dkits.Config
import java.util.*

object ClanRegistry {

	val PRIMITIVE_CLAN_BY_STRING = HashMap<String, PrimitiveClan>()
	val CLAN_BY_STRING = linkedMapOf<String, Clan>()

	fun onlineClans() = CLAN_BY_STRING.values

	fun clan(uuid: String): Clan? {
		var clan = CLAN_BY_STRING[uuid]

		if (clan == null) {
			val primitiveClan = PRIMITIVE_CLAN_BY_STRING[uuid]

			if (primitiveClan != null) {
				clan = Clan(primitiveClan)

				PRIMITIVE_CLAN_BY_STRING.remove(uuid)
				CLAN_BY_STRING.put(uuid, clan)
			}
		}

		return clan
	}

	fun primitiveClanFromJson(json: String?): PrimitiveClan? = when (json) {
		null             -> null
		"{\"status\":2}" -> null
		else             -> Config.GSON.fromJson(json, PrimitiveClan::class.java)
	}
}
