package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.ScoreboardUtils
import br.com.dusty.dkits.util.TaskUtils
import br.com.dusty.dkits.util.gamer.TagUtils
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.protocol.ProtocolUtils
import br.com.dusty.dkits.util.tab.HeaderFooterUtils
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warp
import br.com.dusty.dkits.warp.Warps
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class Gamer internal constructor(val player: Player, val primitiveGamer: PrimitiveGamer) {

	val protocolVersion: EnumProtocolVersion = EnumProtocolVersion.byVersionNumber(ProtocolUtils.protocolVersion(player))

	val rank = EnumRank.ADMIN

	/**
	 * Menor [EnumRank] que pode ver este jogador.
	 */
	var visibleTo = EnumRank.ADMIN
		set(visibleTo) {
			if (this.visibleTo == visibleTo)
				return

			field = visibleTo

			for (gamer in GamerRegistry.onlineGamers) {
				if (gamer.rank.isLowerThan(rank))
					gamer.player.hidePlayer(player)
				else
					gamer.player.showPlayer(player)
			}

			if (rank.isHigherThanOrEquals(EnumRank.MOD)) {
				var text = Text.basicOf("Agora você está ").positive("visível").basic(" apenas para ").append(visibleTo.toString())

				if (visibleTo.hasNext())
					text = text.basic(" e acima")

				player.sendMessage(text.basic("!").toString())
			}
		}

	var mode = EnumMode.PLAY
		set(mode) {
			if (this.mode == mode)
				return

			field = mode

			player.sendMessage(Text.basicOf("Agora você está no modo ").positive(mode.name).basic("!").toString())

			when (mode) {
				EnumMode.PLAY     -> {
					player.gameMode = GameMode.ADVENTURE

					clear()
					fly(false)

					visibleTo = EnumRank.DEFAULT
				}
				EnumMode.SPECTATE -> TODO()
				EnumMode.ADMIN    -> {
					player.gameMode = GameMode.CREATIVE

					fly(true)

					visibleTo = rank
				}
			}
		}

	var chat = EnumChat.NORMAL

	val kills: Int
		get() = primitiveGamer.kills

	fun addKill() {
		primitiveGamer.kills++

		ScoreboardUtils.update(this)
	}

	fun addKillMoney() { //TODO: Different money for VIPs
		addMoney(100f)

		ScoreboardUtils.update(this)
	}

	fun addKillXp() {
		addXp(10f)

		ScoreboardUtils.update(this)
	}

	val deaths: Int
		get() = primitiveGamer.deaths

	fun addDeath() {
		primitiveGamer.deaths++

		ScoreboardUtils.update(this)
	}

	fun removeDeathMoney() {
		removeMoney(50f)

		ScoreboardUtils.update(this)
	}

	fun removeDeathXp() {
		removeXp(5f)

		ScoreboardUtils.update(this)
	}

	val killStreak: Int
		get() = primitiveGamer.killStreak

	fun addKillStreak() {
		primitiveGamer.killStreak++

		ScoreboardUtils.update(this)
	}

	fun resetKillStreak() {
		primitiveGamer.killStreak = 0
	}

	var maxKillStreak: Int
		get() = primitiveGamer.maxKillStreak
		set(maxKillStreak) {
			primitiveGamer.maxKillStreak = maxKillStreak
		}

	val xp: Float
		get() = primitiveGamer.xp

	fun addXp(amount: Float) {
		primitiveGamer.xp += amount

		player.sendMessage(Text.positiveOf("+").positive(Math.round(amount)).basic(" XP!").toString())
		ScoreboardUtils.update(this)
	}

	fun removeXp(amount: Float) {
		primitiveGamer.xp += amount

		player.sendMessage(Text.negativeOf("-").negative(Math.round(amount)).basic(" XP!").toString())
		ScoreboardUtils.update(this)
	}

	val money: Float
		get() = primitiveGamer.money

	fun addMoney(amount: Float) {
		primitiveGamer.money += amount

		player.sendMessage(Text.positiveOf("+").positive(Math.round(amount)).basic(" créditos!").toString())
		ScoreboardUtils.update(this)
	}

	fun removeMoney(amount: Float) {
		primitiveGamer.money += amount

		player.sendMessage(Text.negativeOf("-").negative(Math.round(amount)).basic(" créditos!").toString())
		ScoreboardUtils.update(this)
	}

	var hgWins: Int
		get() = primitiveGamer.hgWins
		set(hgWins) {
			primitiveGamer.hgWins = hgWins
		}

	var hgLosses: Int
		get() = primitiveGamer.hgLosses
		set(hgLosses) {
			primitiveGamer.hgLosses = hgLosses
		}

	private var invincibility: Long = -1

	val isInvincible: Boolean
		get() = invincibility > System.currentTimeMillis()

	fun makeInvincible(period: Long) {
		invincibility = System.currentTimeMillis() + period
	}

	fun removeInvincibility() {
		invincibility = -1
	}

	private var freeze: Long = -1

	val isFrozen: Boolean
		get() = freeze > System.currentTimeMillis()

	fun freeze(period: Long) {
		freeze = System.currentTimeMillis() + period
	}

	fun removeFreeze() {
		freeze = -1
	}

	private var cooldown: Long = -1

	val isOnCooldown: Boolean
		get() = cooldown > System.currentTimeMillis()

	fun setCooldown(period: Long) {
		cooldown = System.currentTimeMillis() + period
	}

	fun removeCooldown() {
		cooldown = -1
	}

	private var signCooldown: Long = -1

	val isOnSignCooldown: Boolean
		get() = signCooldown > System.currentTimeMillis()

	fun getSignCooldown(): Int {
		return Math.round(((signCooldown - System.currentTimeMillis()) / 1000).toFloat())
	}

	fun setSignCooldown(period: Long) {
		signCooldown = System.currentTimeMillis() + period
	}

	fun removeSignCooldown() {
		signCooldown = -1
	}

	private var combatTag: Long = -1
	var combatPartner: Gamer? = null
	private var combatTask: BukkitTask? = null

	val isCombatTagged: Boolean
		get() = combatTag > System.currentTimeMillis()

	fun getCombatTag(): Int {
		return Math.round(((combatTag - System.currentTimeMillis()) / 1000).toFloat())
	}

	fun setCombatTag(period: Long) {
		if (!isCombatTagged)
			player.sendMessage(Text.negativePrefix()
					                   .basic("Você ")
					                   .negative("entrou")
					                   .basic(" em ")
					                   .negative("combate")
					                   .basic("!")
					                   .toString())

		combatTag = System.currentTimeMillis() + period

		if (combatTask != null)
			combatTask!!.cancel()

		combatTask = TaskUtils.sync(Runnable { this.removeCombatTag() }, 200)

		ScoreboardUtils.update(this)
	}

	fun removeCombatTag() {
		player.sendMessage(Text.positivePrefix()
				                   .basic("Você ")
				                   .positive("saiu")
				                   .basic(" de ")
				                   .positive("combate")
				                   .basic("!")
				                   .toString())

		combatTag = -1

		if (combatTask != null)
			combatTask!!.cancel()

		ScoreboardUtils.update(this)
	}

	private var noFall: Boolean = false

	fun hasNoFall(): Boolean {
		return noFall
	}

	fun setNoFall(noFall: Boolean) {
		this.noFall = noFall
	}

	var kit: Kit = Kits.NONE
		set(kit) {
			field = kit

			ScoreboardUtils.update(this)
		}

	//TODO: Gamer.hasKit()
	fun hasKit(kit: Kit): Boolean {
		return true
	}

	var warp: Warp = Warps.LOBBY
	var warpTask: BukkitTask? = null

	fun sendToWarp(warp: Warp) {
		if (warpTask != null) {
			warpTask!!.cancel()
			warpTask = null
		}

		if (isCombatTagged) {
			val ticks = Math.ceil((combatTag - System.currentTimeMillis()) / 50.0).toLong()
			val seconds = Math.round((ticks / 20).toFloat())

			warpTask = TaskUtils.sync(Runnable { sendToWarp(warp) }, ticks)

			player.sendMessage(Text.neutralPrefix()
					                   .basic("Você será teleportado em ")
					                   .neutral(seconds)
					                   .basic(" segundo(s), ")
					                   .neutral("não")
					                   .basic(" se ")
					                   .neutral("mova")
					                   .basic("!")
					                   .toString())
		} else {
			clear()

			this.warp = warp
			warp.receiveGamer(this)

			ScoreboardUtils.update(this)

			if (protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8))
				HeaderFooterUtils.update(this)
		}
	}

	fun clear() {
		val player = player

		player.health = 20.0
		player.foodLevel = 20
		player.exp = 0f
		player.level = 0

		player.inventory.clear()

		for (potionEffect in player.activePotionEffects)
			player.removePotionEffect(potionEffect.type)
	}

	fun fly(fly: Boolean) {
		val player = player

		player.allowFlight = fly
		player.isFlying = fly
	}

	init {
		if (rank.isLowerThan(EnumRank.MOD)) {
			mode = EnumMode.PLAY
			visibleTo = EnumRank.DEFAULT
		} else {
			mode = EnumMode.ADMIN
			visibleTo = rank
		}

		GamerRegistry.onlineGamers
				.filter { rank.isLowerThan(it.visibleTo) }
				.forEach { player.hidePlayer(it.player) }

		TagUtils.applyTag(this)
	}

	companion object {

		fun of(player: Player): Gamer {
			return GamerRegistry.getGamerByPlayer(player)
		}
	}
}
