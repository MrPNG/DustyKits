package br.com.dusty.dkits.listener.login;

import br.com.dusty.dkits.EnumServerStatus;
import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.gamer.GamerRegistry;
import br.com.dusty.dkits.gamer.PrimitiveGamer;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import br.com.dusty.dkits.util.web.WebAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class AsyncPlayerPreLoginListener implements Listener {
	
	private static final String KICK_NOT_READY = Text.of("O servidor ainda não está aberto!\n\nVolte em alguns segundos...")
	                                                 .color(TextColor.RED)
	                                                 .toString();
	
	private static final String KICK_NO_PROFILE = Text.of("Não foi possível carregar seu perfil!\n\nVolte em alguns minutos...")
	                                                  .color(TextColor.RED)
	                                                  .toString();
	
	@EventHandler
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		if(Main.serverStatus != EnumServerStatus.ONLINE){
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, KICK_NOT_READY);
		}else{
			UUID uuid = event.getUniqueId();
			
			PrimitiveGamer primitiveGamer = GamerRegistry.fromJson(WebAPI.getProfile(uuid), uuid);
			
			if(primitiveGamer == null)
				event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, KICK_NO_PROFILE);
		}
	}
}
