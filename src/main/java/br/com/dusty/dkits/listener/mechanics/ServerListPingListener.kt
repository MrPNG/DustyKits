package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.EnumServerStatus
import br.com.dusty.dkits.Main
import br.com.dusty.dkits.util.MOTDUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

object ServerListPingListener: Listener {

	@EventHandler
	fun onServerListPing(event: ServerListPingEvent) {
		//TODO: Remove mods/admins from count
		event.maxPlayers = Main.MAX_PLAYERS

		when (Main.serverStatus) {
			EnumServerStatus.ONLINE      -> event.motd = MOTDUtils.randomMOTD()
			EnumServerStatus.OFFLINE     -> event.motd = MOTDUtils.offlineMOTD()
			EnumServerStatus.MAINTENANCE -> event.motd = MOTDUtils.maintenanceMOTD()
			else                         -> TODO()
		}
	}
}
