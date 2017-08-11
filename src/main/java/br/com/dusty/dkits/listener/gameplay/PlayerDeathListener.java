package br.com.dusty.dkits.listener.gameplay;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.TaskUtils;
import br.com.dusty.dkits.util.protocol.ProtocolUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.lang.reflect.Field;

public class PlayerDeathListener implements Listener {
	
	private static Class class_PacketPlayInClientCommand;
	private static Field field_PacketPlayInClientCommand_a;
	
	private static Object[] enum_EnumClientCommand_values;
	
	static {
		try{
			class_PacketPlayInClientCommand = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".PacketPlayInClientCommand");
			field_PacketPlayInClientCommand_a = ProtocolUtils.getAccessibleField(class_PacketPlayInClientCommand, "a");
			
			Class enum_EnumClientCommand = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".PacketPlayInClientCommand$EnumClientCommand");
			enum_EnumClientCommand_values = enum_EnumClientCommand.getEnumConstants();
		}catch(ClassNotFoundException | NoSuchFieldException e){
			e.printStackTrace();
		}
	}
	
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		
		Gamer gamer = Gamer.of(player);
		
		try{
			final Object object_PacketPlayInClientCommand = class_PacketPlayInClientCommand.newInstance();
			field_PacketPlayInClientCommand_a.set(object_PacketPlayInClientCommand, enum_EnumClientCommand_values[0]);
			
			TaskUtils.sync(() -> {
				ProtocolUtils.sendPacket(object_PacketPlayInClientCommand, player);
				
				//TODO: If player was on MiniHG, send to Lobby.
				gamer.sendToWarp(gamer.getWarp());
			});
		}catch(InstantiationException | IllegalAccessException e){
			e.printStackTrace();
		}
	}
}
