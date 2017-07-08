package br.com.dusty.dkits.util.protocol;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.UUID;

public class BossBar {
	
	private HashSet<Player> players = new HashSet<Player>();
	
	private UUID uuid;
	private BaseComponent title;
	private float progress;
	private EnumBarColor barColor;
	private EnumBarStyle barStyle;
	private EnumFlags flags;
	
	private static Class class_PacketPlayOutBoss;
	private static Field field_PacketPlayOutBoss_a;
	private static Field field_PacketPlayOutBoss_b;
	private static Field field_PacketPlayOutBoss_c;
	private static Field field_PacketPlayOutBoss_d;
	private static Field field_PacketPlayOutBoss_e;
	private static Field field_PacketPlayOutBoss_f;
	private static Field field_PacketPlayOutBoss_g;
	private static Field field_PacketPlayOutBoss_h;
	private static Field field_PacketPlayOutBoss_i;
	
	private static Object[] enum_Action_values;
	private static Object[] enum_BarColor_values;
	private static Object[] enum_BarStyle_values;
	
	static {
		try{
			class_PacketPlayOutBoss = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".PacketPlayOutBoss");
			field_PacketPlayOutBoss_a = ProtocolUtils.getAccessibleField(class_PacketPlayOutBoss, "a");
			field_PacketPlayOutBoss_b = ProtocolUtils.getAccessibleField(class_PacketPlayOutBoss, "b");
			field_PacketPlayOutBoss_c = ProtocolUtils.getAccessibleField(class_PacketPlayOutBoss, "c");
			field_PacketPlayOutBoss_d = ProtocolUtils.getAccessibleField(class_PacketPlayOutBoss, "d");
			field_PacketPlayOutBoss_e = ProtocolUtils.getAccessibleField(class_PacketPlayOutBoss, "e");
			field_PacketPlayOutBoss_f = ProtocolUtils.getAccessibleField(class_PacketPlayOutBoss, "f");
			field_PacketPlayOutBoss_g = ProtocolUtils.getAccessibleField(class_PacketPlayOutBoss, "g");
			field_PacketPlayOutBoss_h = ProtocolUtils.getAccessibleField(class_PacketPlayOutBoss, "h");
			field_PacketPlayOutBoss_i = ProtocolUtils.getAccessibleField(class_PacketPlayOutBoss, "i");
			
			Class enum_Action = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".PacketPlayOutBoss.Action");
			enum_Action_values = enum_Action.getEnumConstants();
			
			Class enum_BarColor = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".BossBattle.BarColor");
			enum_BarColor_values = enum_BarColor.getEnumConstants();
			
			Class enum_BarStyle = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".BossBattle.BarStyle");
			enum_BarStyle_values = enum_BarStyle.getEnumConstants();
		}catch(ClassNotFoundException | NoSuchFieldException e){
			e.printStackTrace();
		}
	}
	
	public static BossBar create(BaseComponent title,
	                             float progress,
	                             EnumBarColor color,
	                             EnumBarStyle division,
	                             EnumFlags flags) {
		BossBar bossBar = new BossBar();
		
		bossBar.uuid = UUID.randomUUID();
		bossBar.title = title;
		bossBar.progress = progress;
		bossBar.barColor = color == null ? EnumBarColor.PURPLE : color;
		bossBar.barStyle = division == null ? EnumBarStyle.PROGRESS : division;
		bossBar.flags = flags == null ? EnumFlags.NONE : flags;
		
		return bossBar;
	}
	
	public void send(Player player) {
		Object object_PacketPlayOutBoss = class_PacketPlayOutBoss.newInstance();
		player.spigot().
	}
	
	public void send(Player... players) {
	
	}
	
	private enum EnumAction {
		
		ADD(0),
		REMOVE(1),
		HEALTH(2),
		TITLE(3),
		STYLE(4),
		FLAGS(5);
		
		int code;
		
		EnumAction(int code) {
			this.code = code;
		}
	}
	
	enum EnumBarColor {
		
		PINK(0),
		BLUE(1),
		RED(2),
		GREEN(3),
		YELLOW(4),
		PURPLE(5),
		WHITE(6);
		
		int code;
		
		EnumBarColor(int code) {
			this.code = code;
		}
	}
	
	enum EnumBarStyle {
		
		PROGRESS(0),
		NOTCHED_6(1),
		NOTCHED_10(2),
		NOTCHED_12(3),
		NOTCHED_20(4);
		
		int code;
		
		EnumBarStyle(int code) {
			this.code = code;
		}
	}
	
	enum EnumFlags {
		
		NONE(0x0),
		DARKEN_SKY(0x1),
		PLAY_END_MUSIC(0x2);
		
		byte code;
		
		EnumFlags(int code) {
			this.code = (byte) code;
		}
	}
}
