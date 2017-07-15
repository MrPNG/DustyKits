package br.com.dusty.dkits.util.tab;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.protocol.ProtocolUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class HeaderFooterUtils {
	
	private static final String HEADER_MAIN = Text.of("Dusty")
	                                              .color(TextColor.RED)
	                                              .append(" - ")
	                                              .color(TextColor.WHITE)
	                                              .append("dusty.com.br")
	                                              .color(TextColor.GOLD)
	                                              .toString();
	
	private static final String HEADER_BAR = Text.of("--------------------------------").color(TextColor.RED).toString();
	
	private static Class class_PacketPlayOutPlayerListHeaderFooter;
	private static Field field_PacketPlayOutPlayerListHeaderFooter_a;
	private static Field field_PacketPlayOutPlayerListHeaderFooter_b;
	
	static {
		try{
			class_PacketPlayOutPlayerListHeaderFooter = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".PacketPlayOutPlayerListHeaderFooter");
			field_PacketPlayOutPlayerListHeaderFooter_a = ProtocolUtils.getAccessibleField(
					class_PacketPlayOutPlayerListHeaderFooter,
					"a");
			field_PacketPlayOutPlayerListHeaderFooter_b = ProtocolUtils.getAccessibleField(
					class_PacketPlayOutPlayerListHeaderFooter,
					"b");
		}catch(ClassNotFoundException | NoSuchFieldException e){
			e.printStackTrace();
		}
	}
	
	public static void sendHeaderFooter(Gamer gamer) {
		Player player = gamer.getPlayer();
		
		Object object_PacketPlayOutPlayerListHeaderFooter = null;
		
		try{
			object_PacketPlayOutPlayerListHeaderFooter = class_PacketPlayOutPlayerListHeaderFooter.newInstance();
			field_PacketPlayOutPlayerListHeaderFooter_a.set(object_PacketPlayOutPlayerListHeaderFooter,
			                                                ProtocolUtils.chatMessage(HEADER_MAIN + "\n" + HEADER_BAR));
			field_PacketPlayOutPlayerListHeaderFooter_b.set(object_PacketPlayOutPlayerListHeaderFooter,
			                                                ProtocolUtils.chatMessage(HEADER_BAR));
		}catch(IllegalAccessException | InstantiationException | InvocationTargetException e){
			e.printStackTrace();
		}
		
		ProtocolUtils.sendPacket(object_PacketPlayOutPlayerListHeaderFooter, player);
	}
}
