package br.com.dusty.dkits.listener.login

import br.com.dusty.dkits.clan.ClanRegistry
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

object AsyncPlayerPreLoginListener: Listener {

	private val KICK_NO_PROFILE = Text.negativeOf("Não foi possível carregar seu perfil!\n\nVolte em alguns instantes...").toString()

	@EventHandler
	fun onAsyncPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
		val uuid = event.uniqueId

		//TODO: Reactivate Web API
//		val primitiveGamer = GamerRegistry.primitiveGamerFromJson(WebAPI.loadProfile(uuid), uuid)
		val primitiveGamer = GamerRegistry.tempPrimitiveGamer(uuid)

		if (primitiveGamer == null) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, KICK_NO_PROFILE)
		} else if (primitiveGamer.clan != "" && !ClanRegistry.PRIMITIVE_CLAN_BY_STRING.containsKey(primitiveGamer.clan)) {
			val clanUuid = primitiveGamer.clan

			ClanRegistry.primitiveClanFromJson(WebAPI.loadClan(clanUuid))?.run { ClanRegistry.PRIMITIVE_CLAN_BY_STRING.put(clanUuid, this) }
		}
	}
}
