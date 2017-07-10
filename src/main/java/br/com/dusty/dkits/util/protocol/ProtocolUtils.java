package br.com.dusty.dkits.util.protocol;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProtocolUtils {
	
	public static final int PROTOCOL_VERSION = 335;
	
	static final String CRAFTBUKKIT_PACKAGE = "org.bukkit.craftbukkit.";
	public static final String NMS_PACKAGE = "net.minecraft.server.";
	public static final String NMS_VERSION = "v1_12_R1";
	
	static final String PROTOCOL_SUPPORT_PACKAGE = "protocolsupport.api.";
	
	private static Constructor<?> constructor_ChatMessage;
	private static Method method_CraftPlayer_getHandle;
	private static Field field_playerConnection;
	private static Method method_PlayerConnection_sendPacket;
	
	private static Method method_ProtocolSupportAPI_getProtocolVersion;
	private static Method method_ProtocolVersion_getId;
	
	static {
		try{
			Class<? extends Object> class_ChatMessage = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".ChatMessage");
			constructor_ChatMessage = class_ChatMessage.getDeclaredConstructor(String.class, Object[].class);
			
			Class class_Packet = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".Packet");
			
			Class class_CraftPlayer = Class.forName(ProtocolUtils.CRAFTBUKKIT_PACKAGE + ProtocolUtils.NMS_VERSION + ".entity.CraftPlayer");
			method_CraftPlayer_getHandle = class_CraftPlayer.getDeclaredMethod("getHandle");
			
			Class class_EntityPlayer = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".EntityPlayer");
			field_playerConnection = class_EntityPlayer.getDeclaredField("playerConnection");
			
			Class class_PlayerConnection = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".PlayerConnection");
			method_PlayerConnection_sendPacket = class_PlayerConnection.getDeclaredMethod("sendPacket", class_Packet);
			
			Class class_ProtocolSupportAPI = Class.forName(PROTOCOL_SUPPORT_PACKAGE + "ProtocolSupportAPI");
			method_ProtocolSupportAPI_getProtocolVersion = class_ProtocolSupportAPI.getDeclaredMethod("getProtocolVersion",
			                                                                                          Player.class);
			
			Class class_ProtocolVersion = Class.forName(PROTOCOL_SUPPORT_PACKAGE + "ProtocolVersion");
			method_ProtocolVersion_getId = class_ProtocolVersion.getDeclaredMethod("getId");
		}catch(ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e){
			e.printStackTrace();
		}
	}
	
	public static Field getAccessibleField(Class<?> clazz, String name) throws NoSuchFieldException {
		Field field = clazz.getDeclaredField(name);
		field.setAccessible(true);
		
		return field;
	}
	
	public static Object chatMessage(String s) throws IllegalAccessException, InvocationTargetException, InstantiationException {
		return constructor_ChatMessage.newInstance(s, new Object[0]);
	}
	
	public static Integer protocolVersion(Player player) throws InvocationTargetException, IllegalAccessException {
		Object object_ProtocolVersion = method_ProtocolSupportAPI_getProtocolVersion.invoke(null, player);
		
		return (Integer) method_ProtocolVersion_getId.invoke(object_ProtocolVersion);
	}
	
	public static void sendPacket(Object object_Packet, Player... players) {
		for(Player player : players){
			try{
				Object object_EntityPlayer = method_CraftPlayer_getHandle.invoke(player);
				Object object_PlayerConnection = field_playerConnection.get(object_EntityPlayer);
				method_PlayerConnection_sendPacket.invoke(object_PlayerConnection, object_Packet);
			}catch(IllegalAccessException | InvocationTargetException e){
				e.printStackTrace();
			}
		}
	}
}
