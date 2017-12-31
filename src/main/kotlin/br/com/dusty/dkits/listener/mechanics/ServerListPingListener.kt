package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.Config
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.MOTDs
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

object ServerListPingListener: Listener {

	@EventHandler
	fun onServerListPing(event: ServerListPingEvent) {
		event.removeAll { event.iterator().next().gamer().mode == EnumMode.ADMIN }
		event.maxPlayers = Config.data.slots

		when (Config.data.serverStatus) {
			Config.EnumServerStatus.ONLINE      -> event.motd = MOTDs.randomMOTD()
			Config.EnumServerStatus.OFFLINE     -> event.motd = MOTDs.offlineMOTD()
			Config.EnumServerStatus.MAINTENANCE -> event.motd = MOTDs.maintenanceMOTD()
		}
	}
}
