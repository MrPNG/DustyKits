package br.com.dusty.dkits.util.bossbar

import br.com.dusty.dkits.util.protocol.Protocols
import org.bukkit.entity.Player
import java.lang.reflect.Field
import java.util.*

class BossBar {

	private val players = HashSet<Player>()

	private var uuid: UUID? = null

	private var title: String = ""
	private var progress: Float = 0.toFloat()
	private var barColor = EnumBarColor.WHITE
	private var barStyle = EnumBarStyle.PROGRESS
	private var flags = EnumFlags.NONE

	fun send(vararg players: Player) {
		if (players.size > 1) this.players.addAll(Arrays.asList(*players))
		else this.players.add(players[0])

		val object_PacketPlayOutBoss = class_PacketPlayOutBoss.newInstance()
		field_PacketPlayOutBoss_a.set(object_PacketPlayOutBoss, uuid)
		field_PacketPlayOutBoss_b.set(object_PacketPlayOutBoss, enum_Action_values[EnumAction.ADD.code])
		field_PacketPlayOutBoss_c.set(object_PacketPlayOutBoss, Protocols.chatMessage(title))
		field_PacketPlayOutBoss_d.set(object_PacketPlayOutBoss, progress)
		field_PacketPlayOutBoss_e.set(object_PacketPlayOutBoss, enum_BarColor_values[barColor.code])
		field_PacketPlayOutBoss_f.set(object_PacketPlayOutBoss, enum_BarStyle_values[barStyle.code])

		when (flags) {
			EnumFlags.NONE           -> {
			}
			EnumFlags.DARKEN_SKY     -> {
			}
			EnumFlags.PLAY_END_MUSIC -> {
			}
		}

		Protocols.sendPacket(object_PacketPlayOutBoss, *players)
	}

	fun remove(vararg players: Player) {
		if (players.size > 1) this.players.removeAll(Arrays.asList(*players))
		else this.players.remove(players[0])

		val object_PacketPlayOutBoss = class_PacketPlayOutBoss.newInstance()
		field_PacketPlayOutBoss_a.set(object_PacketPlayOutBoss, uuid)
		field_PacketPlayOutBoss_b.set(object_PacketPlayOutBoss, enum_Action_values[EnumAction.REMOVE.code])

		Protocols.sendPacket(object_PacketPlayOutBoss, *players)
	}

	fun updateProgress(progress: Float, vararg players: Player) {
		this.progress = progress

		val object_PacketPlayOutBoss = class_PacketPlayOutBoss.newInstance()
		field_PacketPlayOutBoss_a.set(object_PacketPlayOutBoss, uuid)
		field_PacketPlayOutBoss_b.set(object_PacketPlayOutBoss, enum_Action_values[EnumAction.HEALTH.code])
		field_PacketPlayOutBoss_d.set(object_PacketPlayOutBoss, progress)

		Protocols.sendPacket(object_PacketPlayOutBoss, *players)
	}

	fun updateTitle(title: String, vararg players: Player) {
		this.title = title

		val object_PacketPlayOutBoss = class_PacketPlayOutBoss.newInstance()
		field_PacketPlayOutBoss_a.set(object_PacketPlayOutBoss, uuid)
		field_PacketPlayOutBoss_b.set(object_PacketPlayOutBoss, enum_Action_values[EnumAction.TITLE.code])
		field_PacketPlayOutBoss_c.set(object_PacketPlayOutBoss, Protocols.chatMessage(title))

		Protocols.sendPacket(object_PacketPlayOutBoss, *players)
	}

	fun updateStyle(barColor: EnumBarColor, barStyle: EnumBarStyle, vararg players: Player) {
		this.barColor = barColor
		this.barStyle = barStyle

		val object_PacketPlayOutBoss = class_PacketPlayOutBoss.newInstance()
		field_PacketPlayOutBoss_a.set(object_PacketPlayOutBoss, uuid)
		field_PacketPlayOutBoss_b.set(object_PacketPlayOutBoss, enum_Action_values[EnumAction.STYLE.code])
		field_PacketPlayOutBoss_e.set(object_PacketPlayOutBoss, enum_BarColor_values[barColor.code])
		field_PacketPlayOutBoss_f.set(object_PacketPlayOutBoss, enum_BarStyle_values[barStyle.code])

		Protocols.sendPacket(object_PacketPlayOutBoss, *players)
	}

	private enum class EnumAction private constructor(internal var code: Int) {

		ADD(0),
		REMOVE(1),
		HEALTH(2),
		TITLE(3),
		STYLE(4),
		FLAGS(5)
	}

	enum class EnumBarColor private constructor(internal var code: Int) {

		PINK(0),
		BLUE(1),
		RED(2),
		GREEN(3),
		YELLOW(4),
		PURPLE(5),
		WHITE(6)
	}

	enum class EnumBarStyle private constructor(internal var code: Int) {

		PROGRESS(0),
		NOTCHED_6(1),
		NOTCHED_10(2),
		NOTCHED_12(3),
		NOTCHED_20(4)
	}

	enum class EnumFlags private constructor(code: Int) {

		NONE(0x0),
		DARKEN_SKY(0x1),
		PLAY_END_MUSIC(0x2);

		var code: Byte = 0

		init {
			this.code = code.toByte()
		}
	}

	companion object {

		var class_PacketPlayOutBoss: Class<*> = Class.forName(Protocols.NMS_PACKAGE + Protocols.NMS_VERSION + ".PacketPlayOutBoss")
		var field_PacketPlayOutBoss_a: Field
		var field_PacketPlayOutBoss_b: Field
		var field_PacketPlayOutBoss_c: Field
		var field_PacketPlayOutBoss_d: Field
		var field_PacketPlayOutBoss_e: Field
		var field_PacketPlayOutBoss_f: Field
		var field_PacketPlayOutBoss_g: Field
		var field_PacketPlayOutBoss_h: Field
		var field_PacketPlayOutBoss_i: Field

		var enum_Action_values: Array<*>
		var enum_BarColor_values: Array<*>
		var enum_BarStyle_values: Array<*>

		init {
			field_PacketPlayOutBoss_a = Protocols.getAccessibleField(class_PacketPlayOutBoss, "a")
			field_PacketPlayOutBoss_b = Protocols.getAccessibleField(class_PacketPlayOutBoss, "b")
			field_PacketPlayOutBoss_c = Protocols.getAccessibleField(class_PacketPlayOutBoss, "c")
			field_PacketPlayOutBoss_d = Protocols.getAccessibleField(class_PacketPlayOutBoss, "d")
			field_PacketPlayOutBoss_e = Protocols.getAccessibleField(class_PacketPlayOutBoss, "e")
			field_PacketPlayOutBoss_f = Protocols.getAccessibleField(class_PacketPlayOutBoss, "f")
			field_PacketPlayOutBoss_g = Protocols.getAccessibleField(class_PacketPlayOutBoss, "g")
			field_PacketPlayOutBoss_h = Protocols.getAccessibleField(class_PacketPlayOutBoss, "h")
			field_PacketPlayOutBoss_i = Protocols.getAccessibleField(class_PacketPlayOutBoss, "i")

			val enum_Action = Class.forName(Protocols.NMS_PACKAGE + Protocols.NMS_VERSION + ".PacketPlayOutBoss\$Action")
			enum_Action_values = enum_Action.enumConstants

			val enum_BarColor = Class.forName(Protocols.NMS_PACKAGE + Protocols.NMS_VERSION + ".BossBattle\$BarColor")
			enum_BarColor_values = enum_BarColor.enumConstants

			val enum_BarStyle = Class.forName(Protocols.NMS_PACKAGE + Protocols.NMS_VERSION + ".BossBattle\$BarStyle")
			enum_BarStyle_values = enum_BarStyle.enumConstants
		}

		fun create(title: String, progress: Float, color: EnumBarColor?, division: EnumBarStyle?, flags: EnumFlags?): BossBar {
			val bossBar = BossBar()

			bossBar.uuid = UUID.randomUUID()
			bossBar.title = title
			bossBar.progress = progress
			bossBar.barColor = color ?: EnumBarColor.PURPLE
			bossBar.barStyle = division ?: EnumBarStyle.PROGRESS
			bossBar.flags = flags ?: EnumFlags.NONE

			return bossBar
		}
	}
}
