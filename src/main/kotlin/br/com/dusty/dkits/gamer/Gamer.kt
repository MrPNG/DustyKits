package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.clan.Clan
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.scoreboard.Scoreboards
import br.com.dusty.dkits.store.EnumAdvantage
import br.com.dusty.dkits.store.Store
import br.com.dusty.dkits.util.Inventories
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.protocol.HeaderFooters
import br.com.dusty.dkits.util.stdlib.clearFormatting
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.util.text.TextStyle
import br.com.dusty.dkits.util.world.Worlds
import br.com.dusty.dkits.warp.Warp
import br.com.dusty.dkits.warp.Warps
import com.sk89q.worldguard.protection.flags.DefaultFlag
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class Gamer(val player: Player, var primitiveGamer: PrimitiveGamer) {

	var protocolVersion: EnumProtocolVersion = EnumProtocolVersion.UNKNOWN

	lateinit var purchases: Store.Purchases

	val advantages = arrayListOf<EnumAdvantage>()

	fun hasAdvantage(advantage: EnumAdvantage) = advantage in advantages

	var rank = EnumRank.NONE

	var displayName = player.name

	var tag = EnumRank.NONE
		set(value) {
			field = value

			val tag = value.format(displayName) + TextStyle.RESET

			player.displayName = tag
			player.playerListName = tag
		}

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

	fun shouldSee(gamer: Gamer) = rank.isHigherThanOrEquals(gamer.visibleTo)

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
			if (field != mode) {
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

						warp.dispatchGamer(this, Warps.NONE)
						warp = Warps.NONE
					}
				}

				Scoreboards.update()
			}
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

	fun addKillMoney() {
		addMoney(50.0)

		updateScoreboard()
	}

	fun addKillXp() {
		addXp(100.0)

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

	var skillGroup = EnumSkillGroup.values().firstOrNull { primitiveGamer.xp in it.range } ?: EnumSkillGroup.INICIANTE

	var xp = primitiveGamer.xp
		set(value) {
			field = value

			primitiveGamer.xp = value

			val oldSkillGroup = skillGroup

			skillGroup = EnumSkillGroup.values().firstOrNull { value in it.range } ?: EnumSkillGroup.INICIANTE

			if (skillGroup > oldSkillGroup) player.sendMessage(Text.positivePrefix().basic("Você ").positive("subiu").basic(" para o rank ").positive(skillGroup.string).basic("!").toString())
			else if (skillGroup < oldSkillGroup) player.sendMessage(Text.negativePrefix().basic("Você ").negative("caiu").basic(" para o rank ").negative(skillGroup.string).basic("!").toString())
		}

	fun addXp(amount: Double) {
		val advantage = (rank.isHigherThanOrEquals(EnumRank.PRO) && rank.isLowerThan(EnumRank.MOD)) || hasAdvantage(EnumAdvantage.DOUBLE)

		val absAmount = Math.abs(amount)

		xp += if (advantage) {
			player.sendMessage(Text.positiveOf("+").positive(Math.round(absAmount).toInt()).append("x2").color(TextColor.GOLD).basic(" = ").append(Math.round(absAmount * 2).toInt()).color(
					TextColor.GOLD).basic(" XP!").toString())

			absAmount * 2
		} else {
			player.sendMessage(Text.positiveOf("+").positive(Math.round(absAmount).toInt()).basic(" XP!").toString())

			absAmount
		}

		updateScoreboard()
	}

	fun removeXp(amount: Double) {
		val absAmount = Math.abs(amount)

		if (xp < absAmount) xp = 0.0
		else xp -= absAmount

		player.sendMessage(Text.negativeOf("-").negative(Math.round(absAmount).toInt()).basic(" XP!").toString())
		updateScoreboard()
	}

	val money: Double
		get() = primitiveGamer.money

	fun addMoney(amount: Double) {
		val advantage = (rank.isHigherThanOrEquals(EnumRank.PRO) && rank.isLowerThan(EnumRank.MOD)) || hasAdvantage(EnumAdvantage.DOUBLE)

		val absAmount = Math.abs(amount)

		primitiveGamer.money += if (advantage) {
			player.sendMessage(Text.positiveOf("+").positive(Math.round(absAmount).toInt()).append("x2").color(TextColor.GOLD).basic(" = ").append(Math.round(absAmount * 2).toInt()).color(
					TextColor.GOLD).basic(" créditos!").toString())

			absAmount * 2
		} else {
			player.sendMessage(Text.positiveOf("+").positive(Math.round(absAmount).toInt()).basic(" créditos!").toString())

			absAmount
		}

		updateScoreboard()
	}

	fun removeMoney(amount: Double) {
		val absAmount = Math.abs(amount)

		if (primitiveGamer.money < absAmount) primitiveGamer.money = 0.0
		else primitiveGamer.money -= absAmount

		player.sendMessage(Text.negativeOf("-").negative(Math.round(amount).toInt()).basic(" créditos!").toString())
		updateScoreboard()
	}

	fun kill(gamer: Gamer) {
		val killer = gamer.player

		player.run {
			playSound(killer.location, Sound.ANVIL_LAND, 1F, 1F)
			sendMessage(Text.positivePrefix().basic("Você ").positive("matou").basic(" o jogador ").positive(killer.displayName.clearFormatting()).basic("!").toString())
		}

		addKill()
		addWarpKill()
		addKillStreak()
		addKillMoney()
		addKillXp()
		removeCombatTag(false)
		combatPartner = null


		killer.run {
			playSound(location, Sound.ANVIL_LAND, 1F, 1F)
			sendMessage(Text.negativePrefix().basic("Você ").negative("foi morto").basic(" pelo jogador ").negative(this@Gamer.player.displayName.clearFormatting()).basic("!").toString())
		}

		gamer.run {
			addDeath()
			removeDeathMoney()
			removeDeathXp()
			removeCombatTag(false)
			combatPartner = null
		}
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

	fun createScoreboard() = Scoreboards.create(player)
	fun updateScoreboard() = Scoreboards.update(this)

	fun updateHeaderFooter() = HeaderFooters.update(this)

	var invincibility: Long = -1
		set(period) {
			field = System.currentTimeMillis() + period
		}

	val isInvincible: Boolean
		get() = invincibility > System.currentTimeMillis()

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
		set(value) {
			if (value != this) field = value
		}

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

	val kits = arrayListOf<Kit>()

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
//			if (protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) player.sendTitle(Text.basicOf("Você ").positive("escolheu").basic(" o kit ").positive(kit.name).basic("!").toString(),
//			                                                                                             null,
//			                                                                                             10,
//			                                                                                             80,
//			                                                                                             10) //TODO: 1.8 switch
		}
	}

	fun hasKit(kit: Kit) = mode == EnumMode.ADMIN || kit in kits

	fun canUse() = mode == EnumMode.PLAY && !Worlds.REGION_MANAGER!!.getApplicableRegions(player.location).allows(DefaultFlag.INVINCIBILITY)

	fun canUse(otherGamer: Gamer) = this != otherGamer && canUse() && otherGamer.mode == EnumMode.PLAY && otherGamer.player.canSee(player) && !Worlds.REGION_MANAGER!!.getApplicableRegions(
			otherGamer.player.location).allows(DefaultFlag.INVINCIBILITY)

	var warp: Warp = Warps.LOBBY
	var warpTask: BukkitTask? = null

	fun sendToWarp(warp: Warp, force: Boolean, announce: Boolean) {
		if (warpTask != null) {
			warpTask!!.cancel()
			warpTask = null
		}

		if (force || !isCombatTagged()) {
			val oldWarp = this.warp

			this.warp = warp

			oldWarp.dispatchGamer(this, warp)

			warp.receiveGamer(this, if (oldWarp == warp) false else announce)

			updateScoreboard()

			if (protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) updateHeaderFooter()
		} else {
			val ticks = Math.ceil((combatTag - System.currentTimeMillis()) / 50.0).toLong()
			val seconds = Math.round((ticks / 20).toFloat())

			warpTask = Tasks.sync(Runnable {
				player.closeInventory()

				sendToWarp(warp, true, announce)
			}, ticks)

			player.sendMessage(if (seconds < 60 * 60 * 24) Text.neutralPrefix().basic("Você será teleportado em ").neutral(seconds).basic(if (seconds == 1) " segundo, " else " segundos, ").neutral(
					"não").basic(" se ").neutral("mova").basic("!").toString() else Text.negativePrefix().basic("Você ").negative("não").basic(" pode ir para essa ").negative("warp").basic(" nesse momento!").toString())
		}
	}

	fun clear() {
		player.run {
			health = player.maxHealth
			saturation = 0F
			foodLevel = 20
			exp = 0F
			level = 0
			fireTicks = 0

			inventory.clear()
			inventory.armorContents = Inventories.NO_ARMOR

			activePotionEffects.forEach { removePotionEffect(it.type) }
		}

		removeFreeze()
		removeInvincibility()
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

	override fun equals(other: Any?) = when {
		this === other                                      -> true
		javaClass != other?.javaClass                       -> false
		player.uniqueId != (other as Gamer).player.uniqueId -> false
		else                                                -> true
	}

	override fun hashCode(): Int = player.uniqueId.hashCode()
}
