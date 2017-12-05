package br.com.dusty.dkits.clan

import br.com.dusty.dkits.Main
import java.util.*

object ClanRegistry {

	val PRIMITIVE_CLAN_BY_STRING = HashMap<String, PrimitiveClan>()
	val CLAN_BY_STRING = HashMap<String, Clan>()

	fun onlineClans() = CLAN_BY_STRING.values

	fun clan(uuid: String): Clan? {
		var clan = CLAN_BY_STRING[uuid]

		if (clan == null) {
			val primitiveClan = PRIMITIVE_CLAN_BY_STRING[uuid]

			if (primitiveClan != null) {
				clan = Clan(primitiveClan)

				CLAN_BY_STRING.put(uuid, clan)
				PRIMITIVE_CLAN_BY_STRING.remove(uuid)
			}
		}

		return clan
	}

	fun primitiveClanFromJson(json: String): PrimitiveClan? = if (json == "null") null else Main.GSON.fromJson(json, PrimitiveClan::class.java)
}
