package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Scoreboards
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.tab.HeaderFooters
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warp
import br.com.dusty.dkits.warp.Warps
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

fun Player.gamer() = Gamer[this]

class Gamer internal constructor(val player: Player, val primitiveGamer: PrimitiveGamer) {

	var protocolVersion: EnumProtocolVersion = EnumProtocolVersion.UNKNOWN

	val rank = EnumRank.ADMIN

	/**
	 * Menor [EnumRank] que pode ver este jogador.
	 */
	var visibleTo = EnumRank.DEFAULT
		set(visibleTo) {
			if (field == visibleTo) {
				var text = Text.neutralPrefix().basic("Você já está ").neutral("visível").basic(" apenas para ").append(visibleTo.toString())

				if (visibleTo.hasNext()) text = text.basic(" e acima")

				player.sendMessage(text.basic("!").toString())

				return
			}

			field = visibleTo

			for (gamer in GamerRegistry.onlineGamers()) if (gamer.rank.isLowerThan(rank)) gamer.player.hidePlayer(player)
			else gamer.player.showPlayer(player)

			if (rank.isHigherThanOrEquals(EnumRank.MOD)) {
				var text = Text.neutralPrefix().basic("Agora você está ").neutral("visível").basic(" apenas para ").append(visibleTo.toString())

				if (visibleTo.hasNext()) text = text.basic(" e acima")

				player.sendMessage(text.basic("!").toString())
			}
		}

	var mode = EnumMode.PLAY
		set(mode) {
			if (field == mode) return

			field = mode

			player.sendMessage(Text.neutralPrefix().basic("Agora você está no modo ").neutral(mode.name).basic("!").toString())

			when (mode) {
				EnumMode.PLAY     -> {
					player.gameMode = GameMode.ADVENTURE

					clear()
					fly(false)

					visibleTo = EnumRank.DEFAULT
				}
				EnumMode.SPECTATE -> { //TODO: Spectator mode
					player.gameMode = GameMode.ADVENTURE

					clear()
					fly(true)

					visibleTo = rank
				}
				EnumMode.ADMIN    -> {
					player.gameMode = GameMode.CREATIVE

					fly(true)

					visibleTo = rank

					if (isCombatTagged()) combatPartner?.kill(this) else removeCombatTag()
				}
			}
		}

	var chat = EnumChat.NORMAL

	val kills: Int
		get() = primitiveGamer.kills

	fun addKill() {
		primitiveGamer.kills++

		updateScoreboard()
	}

	fun addKillMoney() { //TODO: Different money for VIPs
		addMoney(when {
			         rank.isHigherThanOrEquals(EnumRank.PRO) -> 100.0
			         else                                    -> 50.0
		         })

		updateScoreboard()
	}

	fun addKillXp() {
		addXp(when {
			      rank.isHigherThanOrEquals(EnumRank.PRO) -> 20.0
			      else                                    -> 10.0
		      })

		updateScoreboard()
	}

	val deaths: Int
		get() = primitiveGamer.deaths

	fun addDeath() {
		primitiveGamer.deaths++

		updateScoreboard()
	}

	fun removeDeathMoney() {
		removeMoney(25.0)

		updateScoreboard()
	}

	fun removeDeathXp() {
		removeXp(5.0)

		updateScoreboard()
	}

	val killStreak: Int
		get() = primitiveGamer.killStreak

	fun addKillStreak() {
		primitiveGamer.killStreak++

		updateScoreboard()
	}

	fun resetKillStreak() {
		primitiveGamer.killStreak = 0

		updateScoreboard()
	}

	var maxKillStreak: Int
		get() = primitiveGamer.maxKillStreak
		set(maxKillStreak) {
			primitiveGamer.maxKillStreak = maxKillStreak
		}

	val xp: Double
		get() = primitiveGamer.xp

	fun addXp(amount: Double) {
		primitiveGamer.xp += amount

		player.sendMessage(Text.positiveOf("+").positive(Math.round(amount).toInt()).basic(" XP!").toString())
		updateScoreboard()
	}

	fun removeXp(amount: Double) {
		primitiveGamer.xp += amount

		player.sendMessage(Text.negativeOf("-").negative(Math.round(amount).toInt()).basic(" XP!").toString())
		updateScoreboard()
	}

	val money: Double
		get() = primitiveGamer.money

	fun addMoney(amount: Double) {
		primitiveGamer.money += amount

		player.sendMessage(Text.positiveOf("+").positive(Math.round(amount).toInt()).basic(" créditos!").toString())
		updateScoreboard()
	}

	fun removeMoney(amount: Double) {
		primitiveGamer.money += amount

		player.sendMessage(Text.negativeOf("-").negative(Math.round(amount).toInt()).basic(" créditos!").toString())
		updateScoreboard()
	}

	fun kill(gamer: Gamer) {
		player.sendMessage(Text.positivePrefix().basic("Você ").positive("matou").basic(" o jogador ").positive(gamer.player.displayName).basic("!").toString())

		addKill()
		addKillStreak()
		addKillMoney()
		addKillXp()

		gamer.player.sendMessage(Text.negativePrefix().basic("Você ").negative("foi morto").basic(" pelo jogador ").negative(player.displayName).basic("!").toString())

		gamer.addDeath()
		gamer.resetKillStreak()
		gamer.removeDeathMoney()
		gamer.removeDeathXp()
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

	fun canLogin(): Boolean = player.isOp //TODO: Login on full

	fun createScoreboard() = Scoreboards.create(player)
	fun updateScoreboard() = Scoreboards.update(this)

	fun updateHeaderFooter() = HeaderFooters.update(this)

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

	var cooldown: Long = -1
		set(period) {
			field = System.currentTimeMillis() + period
		}

	fun isOnCooldown() = cooldown > System.currentTimeMillis()

	fun removeCooldown() {
		cooldown = -1
	}

	var signCooldown: Long = -1
		get() = Math.round(((field - System.currentTimeMillis()) / 1000).toFloat()).toLong()
		set(period) {
			field = System.currentTimeMillis() + period
		}

	fun isOnSignCooldown() = signCooldown > System.currentTimeMillis()

	fun removeSignCooldown() {
		signCooldown = -1
	}

	var combatPartner: Gamer? = null
	var combatTask: BukkitTask? = null

	var combatTag: Long = -1
		get() = Math.round(((field - System.currentTimeMillis()) / 1000).toFloat()).toLong()
		set(period) {
			if (combatTask != null) combatTask!!.cancel()

			if (period == -1L) {
				field = period
			} else {
				if (!isCombatTagged()) player.sendMessage(Text.negativePrefix().basic("Você ").negative("entrou").basic(" em ").negative("combate").basic("!").toString())

				field = System.currentTimeMillis() + period

				combatTask = Tasks.sync(Runnable { this.removeCombatTag() }, 200)
			}

			updateScoreboard()
		}

	fun isCombatTagged() = combatTag > System.currentTimeMillis()

	fun removeCombatTag() {
		if (isCombatTagged()) {
			player.sendMessage(Text.positivePrefix().basic("Você ").positive("saiu").basic(" de ").positive("combate").basic("!").toString())

			combatTag = -1L
		}
	}

	var chatPartner: Gamer? = null
	var chatSpies: ArrayList<Player> = arrayListOf()

	var isNoFall: Boolean = false

	var kit: Kit = Kits.NONE
		set(kit) {
			field = kit

			updateScoreboard()
		}

	//TODO: Gamer.hasKit()
	fun hasKit(kit: Kit): Boolean {
		return true
	}

	var warp: Warp = Warps.LOBBY
	var warpTask: BukkitTask? = null

	fun sendToWarp(warp: Warp, announce: Boolean) {
		if (warpTask != null) {
			warpTask!!.cancel()
			warpTask = null
		}

		if (isCombatTagged()) {
			val ticks = Math.ceil((combatTag - System.currentTimeMillis()) / 50.0).toLong()
			val seconds = Math.round((ticks / 20).toFloat())

			warpTask = Tasks.sync(Runnable { sendToWarp(warp, announce) }, ticks)

			player.sendMessage(Text.neutralPrefix().basic("Você será teleportado em ").neutral(seconds).basic(" segundo(s), ").neutral("não").basic(" se ").neutral("mova").basic("!").toString())
			//TODO: Titles/subtitles for 1.8+ players
		} else {
			clear()

			this.warp = warp
			warp.receiveGamer(this, announce)

			updateScoreboard()

			if (protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) updateHeaderFooter()
		}
	}

	fun clear() {
		player.health = 20.0
		player.foodLevel = 20
		player.exp = 0F
		player.level = 0

		player.inventory.clear()

		player.activePotionEffects.forEach { player.removePotionEffect(it.type) }
	}

	fun fly(fly: Boolean) {
		player.allowFlight = fly
		player.isFlying = fly
	}

	companion object {

		operator fun get(player: Player): Gamer = GamerRegistry.gamer(player)
	}
}
