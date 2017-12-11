package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.clan.Clan
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Scoreboards
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.protocol.HeaderFooters
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warp
import br.com.dusty.dkits.warp.Warps
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

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

			hideFromPlayers()

			if (rank.isHigherThanOrEquals(EnumRank.MOD)) {
				var text = Text.neutralPrefix().basic("Agora você está ").neutral("visível").basic(" apenas para ").append(visibleTo.toString())

				if (visibleTo.hasNext()) text = text.basic(" e acima")

				player.sendMessage(text.basic("!").toString())
			}
		}

	fun shouldSee(gamer: Gamer) = gamer.rank.isHigherThanOrEquals(visibleTo)

	fun hidePlayers() {
		for (otherGamer in GamerRegistry.onlineGamers()) {
			val otherPlayer = otherGamer.player

			if (!shouldSee(otherGamer)) player.hidePlayer(otherPlayer)
			else player.showPlayer(otherPlayer)
		}
	}

	fun hideFromPlayers() {
		for (otherGamer in GamerRegistry.onlineGamers()) {
			val otherPlayer = otherGamer.player

			if (!otherGamer.shouldSee(this)) otherPlayer.hidePlayer(player)
			else otherPlayer.showPlayer(player)
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
			//TODO: Spectator mode
				EnumMode.SPECTATE -> {
					player.gameMode = GameMode.ADVENTURE

					clear()
					fly(true)

					visibleTo = rank
				}
				EnumMode.ADMIN    -> {
					player.gameMode = GameMode.CREATIVE

					fly(true)

					visibleTo = rank

					if (isCombatTagged()) combatPartner?.kill(this) else removeCombatTag(false)

					warp.dispatchGamer(this)
					warp = Warps.LOBBY
				}
			}

			Scoreboards.update()
		}

	var clan: Clan? = null
		set(value) {
			field = value

			primitiveGamer.clan = value?.uuid ?: ""
		}

	var chat = EnumChat.NORMAL

	val kills: Int
		get() = primitiveGamer.kills

	fun addKill() {
		primitiveGamer.kills++

		updateScoreboard()
	}

	var warpKills = 0

	fun addWarpKill() {
		warpKills++
	}

	fun addKillMoney() { //TODO: Different money for VIPs
		addMoney(when {
			         rank.isHigherThanOrEquals(EnumRank.PRO) && rank.isLowerThan(EnumRank.MOD) -> 100.0
			         else                                                                      -> 50.0
		         })

		updateScoreboard()
	}

	fun addKillXp() {
		addXp(when {
			      rank.isHigherThanOrEquals(EnumRank.PRO) && rank.isLowerThan(EnumRank.MOD) -> 200.0
			      else                                                                      -> 100.0
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
		removeXp(30.0)

		updateScoreboard()
	}

	val killStreak: Int
		get() = primitiveGamer.killStreak

	fun addKillStreak() {
		primitiveGamer.killStreak++

		if (killStreak > maxKillStreak) maxKillStreak = killStreak

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
		primitiveGamer.xp += Math.abs(amount)

		player.sendMessage(Text.positiveOf("+").positive(Math.round(amount).toInt()).basic(" XP!").toString())
		updateScoreboard()
	}

	fun removeXp(amount: Double) {
		primitiveGamer.xp -= Math.abs(amount)

		if (primitiveGamer.xp < 0) primitiveGamer.xp = 0.0

		player.sendMessage(Text.negativeOf("-").negative(Math.round(amount).toInt()).basic(" XP!").toString())
		updateScoreboard()
	}

	val money: Double
		get() = primitiveGamer.money

	fun addMoney(amount: Double) {
		primitiveGamer.money += Math.abs(amount)

		player.sendMessage(Text.positiveOf("+").positive(Math.round(amount).toInt()).basic(" créditos!").toString())
		updateScoreboard()
	}

	fun removeMoney(amount: Double) {
		primitiveGamer.money -= Math.abs(amount)

		if (primitiveGamer.money < 0) primitiveGamer.money = 0.0

		player.sendMessage(Text.negativeOf("-").negative(Math.round(amount).toInt()).basic(" créditos!").toString())
		updateScoreboard()
	}

	fun kill(gamer: Gamer) {
		val killer = gamer.player

		player.playSound(player.location, Sound.BLOCK_ANVIL_LAND, 10F, 1F)
		player.sendMessage(Text.positivePrefix().basic("Você ").positive("matou").basic(" o jogador ").positive(killer.displayName.clearFormatting()).basic("!").toString())

		addKill()
		addWarpKill()
		addKillStreak()
		addKillMoney()
		addKillXp()

		killer.playSound(killer.location, Sound.BLOCK_ANVIL_LAND, 10F, 1F)
		killer.sendMessage(Text.negativePrefix().basic("Você ").negative("foi morto").basic(" pelo jogador ").negative(player.displayName.clearFormatting()).basic("!").toString())

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

	fun addHgWin() = hgWins++

	var hgLosses: Int
		get() = primitiveGamer.hgLosses
		set(hgLosses) {
			primitiveGamer.hgLosses = hgLosses
		}

	fun addHgLoss() = hgLosses++

	var oneVsOneWins: Int
		get() = primitiveGamer.oneVsOneWins
		set(oneVsOneWins) {
			primitiveGamer.oneVsOneWins = oneVsOneWins
		}

	fun addOneVsOneWin() = oneVsOneWins++

	var oneVsOneLosses: Int
		get() = primitiveGamer.oneVsOneLosses
		set(oneVsOneLosses) {
			primitiveGamer.oneVsOneLosses = oneVsOneLosses
		}

	fun addOneVsOneLoss() = oneVsOneLosses++

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

	var freeze: Long = -1
		set(period) {
			field = System.currentTimeMillis() + period
		}

	fun isFrozen() = freeze > System.currentTimeMillis()

	fun removeFreeze() {
		freeze = -1
	}

	var kitCooldown: Long = -1
		set(period) {
			field = System.currentTimeMillis() + period
		}

	fun isOnKitCooldown() = kitCooldown > System.currentTimeMillis()

	fun removeKitCooldown() {
		kitCooldown = -1
	}

	var signCooldown: Long = -1
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
		set(period) {
			combatTask?.cancel()

			if (period == -1L) {
				field = period
			} else {
				if (!isCombatTagged()) player.sendMessage(Text.negativePrefix().basic("Você ").negative("entrou").basic(" em ").negative("combate").basic("!").toString())

				field = System.currentTimeMillis() + period

				combatTask = Tasks.sync(Runnable { if (player.isOnline) removeCombatTag(true) }, period / 50L)
			}

			updateScoreboard()
		}

	fun isCombatTagged() = combatTag > System.currentTimeMillis()

	fun removeCombatTag(announce: Boolean) {
		if (announce && isCombatTagged()) player.sendMessage(Text.positivePrefix().basic("Você ").positive("saiu").basic(" de ").positive("combate").basic("!").toString())

		combatTag = -1L
	}

	var chatPartner: Gamer? = null
	var chatSpies = arrayListOf<Player>()

	var isNoFall: Boolean = false

	var kit: Kit = Kits.NONE
		set(kit) {
			field = kit

			updateScoreboard()
		}

	fun setKitAndApply(kit: Kit, announce: Boolean) {
		this.kit = kit

		warp.applyKit(this, kit)

		if (announce) {
			player.sendMessage(Text.positivePrefix().basic("Você ").positive("escolheu").basic(" o kit ").positive(kit.name).basic("!").toString())
			if (protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) player.sendTitle(Text.basicOf("Você ").positive("escolheu").basic(" o kit ").positive(kit.name).basic("!").toString(),
			                                                                                             null,
			                                                                                             10,
			                                                                                             80,
			                                                                                             10)
		}
	}

	fun hasKit(kit: Kit): Boolean = player.hasPermission("dkits.kit." + kit.name.toLowerCase())

	var warp: Warp = Warps.LOBBY
	var warpTask: BukkitTask? = null

	fun sendToWarp(warp: Warp, force: Boolean, announce: Boolean) {
		if (warpTask != null) {
			warpTask!!.cancel()
			warpTask = null
		}

		if (force || !isCombatTagged()) {
			warp.dispatchGamer(this)

			warp.receiveGamer(this, if (this.warp == warp) false else announce)

			this.warp = warp

			updateScoreboard()

			if (protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) updateHeaderFooter()
		} else {
			val ticks = Math.ceil((combatTag - System.currentTimeMillis()) / 50.0).toLong()
			val seconds = Math.round((ticks / 20).toFloat())

			warpTask = Tasks.sync(Runnable { sendToWarp(warp, true, announce) }, ticks)

			player.sendMessage(Text.neutralPrefix().basic("Você será teleportado em ").neutral(seconds).basic(" segundo(s), ").neutral("não").basic(" se ").neutral("mova").basic("!").toString())
		}
	}

	fun clear() {
		player.health = 20.0
		player.foodLevel = 20
		player.exp = 0F
		player.level = 0

		player.inventory.clear()

		player.activePotionEffects.forEach { player.removePotionEffect(it.type) }

		removeCombatTag(false)
		removeKitCooldown()
		removeSignCooldown()

		warpKills = 0
	}

	fun fly(fly: Boolean) {
		player.allowFlight = fly
		player.isFlying = fly
	}

	override fun toString(): String {
		return "Gamer(player=$player, rank=$rank, mode=$mode, combatTag=$combatTag, kit=$kit, warp=$warp)"
	}
}
