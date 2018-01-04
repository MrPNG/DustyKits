package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.Config
import org.bukkit.entity.Player
import java.util.*

object GamerRegistry {

	val PRIMITIVE_GAMER_BY_UUID = linkedMapOf<UUID, PrimitiveGamer>()
	val GAMER_BY_PLAYER = linkedMapOf<UUID, Gamer>()

	fun onlineGamers() = GAMER_BY_PLAYER.values

	fun gamer(player: Player): Gamer {
		var gamer = GAMER_BY_PLAYER[player.uniqueId]

		if (gamer == null) {
			val uuid = player.uniqueId

			gamer = Gamer(player, PRIMITIVE_GAMER_BY_UUID[uuid] ?: tempPrimitiveGamer(uuid))

			PRIMITIVE_GAMER_BY_UUID.remove(uuid)
		}

		return gamer
	}

	fun primitiveGamerFromJson(json: String?, uuid: UUID) = when (json) {
		null             -> null
		"{\"status\":2}" -> PrimitiveGamer(uuid.toString())
		else             -> Config.GSON.fromJson(json, PrimitiveGamer::class.java)
	}

	fun tempPrimitiveGamer(uuid: UUID) = PrimitiveGamer(uuid.toString())

	fun unregister(uuid: UUID): Gamer? {
		PRIMITIVE_GAMER_BY_UUID.remove(uuid)

		return GAMER_BY_PLAYER.remove(uuid)
	}
}
