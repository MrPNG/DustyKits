package br.com.dusty.dkits.listener.login

import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.text.Text
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

object AsyncPlayerPreLoginListener: Listener {

	private val KICK_NO_PROFILE = Text.negativeOf("Não foi possível carregar seu perfil!\n\nVolte em alguns instantes...").toString()

	@EventHandler
	fun onAsyncPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
		val uuid = event.uniqueId

		//TODO: Reactivate Web API
//		val primitiveGamer = GamerRegistry.primitiveGamerFromJson(WebAPI.getProfile(uuid), uuid)
		val primitiveGamer = GamerRegistry.tempPrimitiveGamer(uuid)

		if (primitiveGamer == null) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, KICK_NO_PROFILE)
	}
}
