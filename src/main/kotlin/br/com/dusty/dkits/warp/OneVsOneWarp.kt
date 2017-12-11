package br.com.dusty.dkits.warp

import br.com.dusty.dkits.clan.Clan
import br.com.dusty.dkits.command.staff.LocationCommand
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.util.*
import br.com.dusty.dkits.util.inventory.*
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.warp.OneVsOneWarp.Fight.ClanVsClanFight
import br.com.dusty.dkits.warp.OneVsOneWarp.Fight.OneVsOneFight
import br.com.dusty.dkits.warp.OneVsOneWarp.FightState.INVITATION
import br.com.dusty.dkits.warp.OneVsOneWarp.FightState.ONGOING
import br.com.dusty.dkits.warp.OneVsOneWarp.FightType.*
import org.bukkit.Bukkit
import org.bukkit.Material.*
import org.bukkit.SkullType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

object OneVsOneWarp: Warp() {

	val FIGHTS = hashMapOf<Player, Fight>()

	var oneVsOneFirst = Locations.GENERIC
		get() {
			if (field == Locations.GENERIC && data is OneVsOneData) field = (data as OneVsOneData).oneVsOneFirst.toLocation(Bukkit.getWorlds()[0])

			return field
		}
		set(location) {
			if (data is OneVsOneData) {
				field = location

				(data as OneVsOneData).oneVsOneFirst = location.toSimpleLocation()

				saveData()
			}
		}

	var oneVsOneSecond = Locations.GENERIC
		get() {
			if (field == Locations.GENERIC && data is OneVsOneData) field = (data as OneVsOneData).oneVsOneSecond.toLocation(Bukkit.getWorlds()[0])

			return field
		}
		set(location) {
			if (data is OneVsOneData) {
				field = location

				(data as OneVsOneData).oneVsOneSecond = location.toSimpleLocation()

				saveData()
			}
		}

	var gladiatorFirst = Locations.GENERIC
		get() {
			if (field == Locations.GENERIC && data is OneVsOneData) field = (data as OneVsOneData).gladiatorFirst.toLocation(Bukkit.getWorlds()[0])

			return field
		}
		set(location) {
			if (data is OneVsOneData) {
				field = location

				(data as OneVsOneData).gladiatorFirst = location.toSimpleLocation()

				saveData()
			}
		}

	var gladiatorSecond = Locations.GENERIC
		get() {
			if (field == Locations.GENERIC && data is OneVsOneData) field = (data as OneVsOneData).gladiatorSecond.toLocation(Bukkit.getWorlds()[0])

			return field
		}
		set(location) {
			if (data is OneVsOneData) {
				field = location

				(data as OneVsOneData).gladiatorSecond = location.toSimpleLocation()

				saveData()
			}
		}

	var clanVsClanFirst = Locations.GENERIC
		get() {
			if (field == Locations.GENERIC && data is OneVsOneData) field = (data as OneVsOneData).clanVsClanFirst.toLocation(Bukkit.getWorlds()[0])

			return field
		}
		set(location) {
			if (data is OneVsOneData) {
				field = location

				(data as OneVsOneData).clanVsClanFirst = location.toSimpleLocation()

				saveData()
			}
		}

	var clanVsClanSecond = Locations.GENERIC
		get() {
			if (field == Locations.GENERIC && data is OneVsOneData) field = (data as OneVsOneData).clanVsClanSecond.toLocation(Bukkit.getWorlds()[0])

			return field
		}
		set(location) {
			if (data is OneVsOneData) {
				field = location

				(data as OneVsOneData).clanVsClanSecond = location.toSimpleLocation()

				saveData()
			}
		}

	init {
		name = "1v1"
		icon = ItemStack(BLAZE_ROD)

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description)

		entryKit = Kit(items = arrayOf(STANDARD.item, FULL.item, NUDE.item, GLADIATOR.item, CLAN_VS_CLAN.item, null, null, null, GAME_WARP_KIT.items[8]))

		data = OneVsOneData()
		data.spreadRange = 4.0

		loadData()

		LocationCommand.CUSTOM_EXECUTORS.add(this)
	}

	@EventHandler(priority = EventPriority.HIGH)
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val loser = event.entity.gamer()

		if (loser.warp == this) {
			if (FIGHTS.values.any { it is OneVsOneFight && (it.host == loser || it.guest == loser) }) end(loser, loser.combatPartner!!)
		}
	}

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEntityEvent) {
		if (event.rightClicked is Player) {
			val player = event.player
			val gamer = player.gamer()

			if (gamer.warp == this) {
				val rightClicked = event.rightClicked as Player

				player.inventory.itemInMainHand?.run {
					val fight = FIGHTS[rightClicked]

					when (type) {
						BLAZE_ROD     -> if (this == STANDARD.item) {
							if (fight != null && fight.type == STANDARD && fight.host.player == rightClicked) start(fight)
							else invite(player, rightClicked.player, STANDARD)
						}
						MUSHROOM_SOUP -> if (this == FULL.item) {
							if (fight != null && fight.type == FULL && fight.host.player == rightClicked) start(fight)
							else invite(player, rightClicked.player, FULL)
						}
						STICK         -> if (this == NUDE.item) {
							if (fight != null && fight.type == NUDE && fight.host.player == rightClicked) start(fight)
							else invite(player, rightClicked.player, NUDE)
						}
						IRON_BARDING  -> if (this == GLADIATOR.item) {
							if (fight != null && fight.type == GLADIATOR && fight.host.player == rightClicked) start(fight)
							else invite(player, rightClicked.player, GLADIATOR)
						}
					}
				}
			}
		}
	}

	fun invite(host: Player, guest: Player, type: FightType) {
		val oldFight = FIGHTS[host]

		if (oldFight != null && oldFight is OneVsOneFight && oldFight.guest.player == guest && oldFight.expiresOn > System.currentTimeMillis()) {
			host.sendMessage(Text.negativePrefix().basic("Você ainda deve ").negative("esperar").basic(" mais ").negative(oldFight.expiresOn.millisToSeconds().toInt()).basic(" segundos antes de convidar esse jogador para uma luta novamente!").toString())
		} else {
			FIGHTS.put(host, OneVsOneFight(host.gamer(), guest.gamer(), System.currentTimeMillis() + 10000L, type, INVITATION))

			host.sendMessage(Text.positivePrefix().basic("Você ").positive("convidou").basic(" o jogador ").positive(guest.displayName.clearFormatting()).basic(" para uma luta do tipo ").positive(
					type.string).basic("!").toString())

			guest.sendMessage(Text.positivePrefix().basic("Você foi ").positive("convidado").basic(" pelo jogador ").positive(host.displayName.clearFormatting()).basic(" para uma luta do tipo ").positive(
					type.string).basic("!").toString())
			guest.sendMessage(Text.positivePrefix().basic("Clique no ").positive("jogador").basic(" com o item correto para ").positive("aceitar").basic("!").toString())
		}
	}

	fun start(fight: Fight) {
		when (fight) {
			is OneVsOneFight   -> {
				val host = fight.host
				val guest = fight.guest

				val hostPlayer = host.player
				val guestPlayer = guest.player

				FIGHTS.values.forEach {
					if (it is OneVsOneFight && it.state == ONGOING) {
						val anotherHost = it.host.player

						anotherHost.hidePlayer(hostPlayer)
						anotherHost.hidePlayer(guestPlayer)

						hostPlayer.hidePlayer(anotherHost)
						guestPlayer.hidePlayer(anotherHost)

						val anotherGuest = it.guest.player

						anotherGuest.hidePlayer(hostPlayer)
						anotherGuest.hidePlayer(guestPlayer)

						hostPlayer.hidePlayer(anotherGuest)
						guestPlayer.hidePlayer(anotherGuest)
					}
				}

				hostPlayer.teleport(if (fight.type == GLADIATOR) gladiatorFirst else oneVsOneFirst)
				guestPlayer.teleport(if (fight.type == GLADIATOR) gladiatorSecond else oneVsOneSecond)

				host.freeze = 3000L
				guest.freeze = 3000L

				host.setKitAndApply(fight.type.kit, false)
				guest.setKitAndApply(fight.type.kit, false)

				when (fight.type) {
					STANDARD -> {
						hostPlayer.fillSoups(false)
						guestPlayer.fillSoups(false)
					}
					else     -> {
						hostPlayer.fillSoups(true)
						guestPlayer.fillSoups(true)

						hostPlayer.fillRecraft()
						guestPlayer.fillRecraft()
					}
				}

				host.combatPartner = guest
				guest.combatPartner = host

				host.combatTag = Integer.MAX_VALUE.toLong()
				guest.combatTag = Integer.MAX_VALUE.toLong()

				fight.state = ONGOING

				Tasks.sync(object: BukkitRunnable() {
					var i = 5

					override fun run() {
						if (i == 5) {
							val message = Text.positivePrefix().basic("A ").positive("luta").basic(" irá ").positive("começar").basic(" em...").toString()

							hostPlayer.sendMessage(message)
							guestPlayer.sendMessage(message)
						}

						val countdownMessage = Text.positivePrefix().basic(i.toString() + "...").toString()

						hostPlayer.sendMessage(countdownMessage)
						guestPlayer.sendMessage(countdownMessage)

						i--

						if (i == 0) {
							val goMessage = Text.positivePrefix().positive("Já").basic("!").toString()

							hostPlayer.sendMessage(goMessage)
							guestPlayer.sendMessage(goMessage)

							cancel()
						}
					}
				}, 0L, 20L)
			}
			is ClanVsClanFight -> {

			}
		}
	}

	fun end(loser: Gamer, winner: Gamer) {
		val loserPlayer = loser.player
		val winnerPlayer = winner.player

		FIGHTS.remove(loserPlayer)
		FIGHTS.remove(winnerPlayer)

		loser.addOneVsOneLoss()
		winner.addOneVsOneWin()

		/*val winnerHealth = (winnerPlayer.health / 2)

		val loserSoups = loserPlayer.inventory.filter { it.type == MUSHROOM_SOUP }.size
		val winnerSoups = winnerPlayer.inventory.filter { it != null && it.type == MUSHROOM_SOUP }.size

		val loserMessage = Text.negativePrefix().basic("Você ").negative("perdeu").basic(" uma luta contra o jogador ").negative(winnerPlayer.displayName.clearFormatting()).basic(", que ficou com ").negative(
				winnerHealth.toString().trimEnd('.',
				                                '0')).basic(" ${if (winnerHealth == 1.0) "coração" else "corações"} e ").negative(winnerSoups).basic(" ${if (winnerSoups == 1) "sopa" else "sopas"}!").toString()
		val winnerMessage = Text.positivePrefix().basic("Você ").positive("ganhou").basic(" uma luta contra o jogador ").positive(loserPlayer.displayName.clearFormatting()).basic(", que ficou com ").positive(
				loserSoups).basic(" ${if (loserSoups == 1) "sopa" else "sopas"}!").toString()

		loserPlayer.sendMessage(loserMessage)
		winnerPlayer.sendMessage(winnerMessage)*/

		loser.hidePlayers()
		winner.hidePlayers()

		winner.sendToWarp(this, true, false)
	}

	override fun applyKit(gamer: Gamer, kit: Kit) {
		gamer.clear()
		gamer.player.run {
			inventory.setItem(0, kit.weapon)
			inventory.addItemStacks(kit.items)
			setArmor(kit.armor)
		}
	}

	override fun receiveGamer(gamer: Gamer, announce: Boolean) {
		val player = gamer.player

		player.teleport(spawn.spread(data.spreadRange))

		if (announce) {
			player.sendMessage(Text.positivePrefix().basic("Você foi ").positive("teleportado").basic(" para a warp ").positive(name).basic("!").toString())

			if (gamer.protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) player.sendTitle(Text.basicOf("Você está na warp ").positive(name).basic("!").toString(),
			                                                                                                   Text.basicOf("Escolha um ").positive("oponente").basic(" e ").positive("divirta-se").basic(
					                                                                                                   "!").toString(),
			                                                                                                   10,
			                                                                                                   80,
			                                                                                                   10)
		}

		gamer.setKitAndApply(entryKit, false)
	}

	override fun dispatchGamer(gamer: Gamer) {
		if (FIGHTS.values.any { it is OneVsOneFight && (it.host == gamer || it.guest == gamer) }) end(gamer, gamer.combatPartner!!)
	}

	override fun setLocation(player: Player, args: Array<String>) {
		when (args[0]) {
			"oneVsOneFirst"    -> {
				oneVsOneFirst = player.location.normalize()

				player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("primeiro").basic(" spawn do ").positive("1v1").basic("!").toString())
			}
			"oneVsOneSecond"   -> {
				oneVsOneSecond = player.location.normalize()

				player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("segundo").basic(" spawn do ").positive("1v1").basic("!").toString())
			}
			"gladiatorFirst"   -> {
				gladiatorFirst = player.location.normalize()

				player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("primeiro").basic(" spawn do ").positive("gladiator").basic(" no 1v1!").toString())
			}
			"gladiatorSecond"  -> {
				gladiatorSecond = player.location.normalize()

				player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("segundo").basic(" spawn do ").positive("gladiator").basic(" no 1v1!").toString())
			}
			"clanVsClanFirst"  -> {
				clanVsClanFirst = player.location.normalize()

				player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("primeiro").basic(" spawn do ").positive("ClanVsClan").basic("!").toString())
			}
			"clanVsClanSecond" -> {
				clanVsClanSecond = player.location.normalize()

				player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("segundo").basic(" spawn do ").positive("ClanVsClan").basic("!").toString())
			}
		}
	}

	sealed class Fight(val host: Gamer, val expiresOn: Long, val type: FightType, var state: FightState) {

		class OneVsOneFight(host: Gamer, val guest: Gamer, timestamp: Long, type: FightType, state: FightState): Fight(host, timestamp, type, state)
		class ClanVsClanFight(host: Gamer, val guest: Clan, timestamp: Long, type: FightType, state: FightState): Fight(host, timestamp, type, state)
	}

	enum class FightType(val string: String, val kit: Kit, val item: ItemStack) {

		STANDARD("Clássico",
		         Kit(weapon = Inventories.DIAMOND_SWORD_SHARPNESS, armor = Inventories.ARMOR_FULL_IRON, isDummy = false),
		         ItemStack(BLAZE_ROD).rename(Text.of("1v1 - Clássico").color(TextColor.GOLD).toString()).setDescription(arrayOf("Arena normal",
		                                                                                                                        "Armadura de ferro completa",
		                                                                                                                        "Espada de diamante encantada",
		                                                                                                                        "Sopas apenas na hotbar").asList())),
		FULL("Completo",
		     Kit(weapon = Inventories.DIAMOND_SWORD_SHARPNESS, armor = Inventories.ARMOR_FULL_IRON, isDummy = false),
		     ItemStack(MUSHROOM_SOUP).rename(Text.of("1v1 - Completo").color(TextColor.GOLD).toString()).setDescription(arrayOf("Arena normal",
		                                                                                                                        "Armadura de ferro completa",
		                                                                                                                        "Espada de diamante encantada",
		                                                                                                                        "Sopas em todo o invetário",
		                                                                                                                        "Material de recraft").asList())),
		NUDE("Sem Armadura",
		     Kit(weapon = ItemStack(STONE_SWORD), armor = arrayOfNulls(4), isDummy = false),
		     ItemStack(STICK).rename(Text.of("1v1 - Sem Armadura").color(TextColor.GOLD).toString()).setDescription(arrayOf("Arena normal",
		                                                                                                                    "Sem armadura",
		                                                                                                                    "Espada de pedra",
		                                                                                                                    "Sopas em todo o invetário",
		                                                                                                                    "Material de recraft").asList())),
		GLADIATOR("Gladiator",
		          Kit(weapon = ItemStack(STONE_SWORD), armor = arrayOfNulls(4), isDummy = false),
		          ItemStack(IRON_BARDING).rename(Text.of("1v1 - Gladiator").color(TextColor.GOLD).toString()).setDescription(arrayOf("Arena no estilo \"Gladiator\"",
		                                                                                                                             "Sem armadura",
		                                                                                                                             "Espada de pedra",
		                                                                                                                             "Sopas em todo o invetário",
		                                                                                                                             "Material de recraft").asList())),
		CLAN_VS_CLAN("Clan vs Clan",
		             Kit(weapon = Inventories.DIAMOND_SWORD_SHARPNESS, armor = Inventories.ARMOR_FULL_IRON, isDummy = false),
		             ItemStack(SKULL_ITEM, 1, SkullType.PLAYER.ordinal.toShort()).rename(Text.of("5v5 - Clan vs Clan").color(TextColor.GOLD).toString()).setDescription(arrayOf("Arena normal",
		                                                                                                                                                                        "Armadura de ferro completa",
		                                                                                                                                                                        "Espada de diamante encantada",
		                                                                                                                                                                        "Sopas em todo o invetário",
		                                                                                                                                                                        "Material de recraft").asList()))
	}

	enum class FightState {

		INVITATION,
		ONGOING,
		TERMINATED
	}

	data class OneVsOneData(var oneVsOneFirst: SimpleLocation = SimpleLocation(),
	                        var oneVsOneSecond: SimpleLocation = SimpleLocation(),
	                        var gladiatorFirst: SimpleLocation = SimpleLocation(),
	                        var gladiatorSecond: SimpleLocation = SimpleLocation(),
	                        var clanVsClanFirst: SimpleLocation = SimpleLocation(),
	                        var clanVsClanSecond: SimpleLocation = SimpleLocation()): Data()
}
