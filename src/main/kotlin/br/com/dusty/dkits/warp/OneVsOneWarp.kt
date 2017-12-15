package br.com.dusty.dkits.warp

import br.com.dusty.dkits.command.gameplay.WarpCommand
import br.com.dusty.dkits.command.staff.LocationCommand
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.util.*
import br.com.dusty.dkits.util.inventory.*
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.warp.OneVsOneWarp.Fight.OneVsOneFight
import br.com.dusty.dkits.warp.OneVsOneWarp.FightState.INVITATION
import br.com.dusty.dkits.warp.OneVsOneWarp.FightState.ONGOING
import br.com.dusty.dkits.warp.OneVsOneWarp.FightType.*
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material.*
import org.bukkit.SkullType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

object OneVsOneWarp: Warp() {

	val HAS_NO_CLAN = Text.negativePrefix().basic("Você ").negative("não").basic(" faz parte de um ").negative("clan").basic("!").toString()
	val IS_NOT_LEADER = Text.negativePrefix().basic("Você ").negative("não").basic(" é o ").negative("líder").basic(" do seu ").negative("clan").basic("!").toString()

	val FIGHTS = hashMapOf<Player, Fight>()

	val ARENAS = hashMapOf<Player, Location>()

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
		icon.setDescription(description, true)

		aliases = arrayOf(name.replace(" ", "").toLowerCase())

		entryKit = Kit(items = arrayOf(STANDARD.item, FULL.item, NUDE.item, GLADIATOR.item, null, null, null, null, GAME_WARP_KIT.items[8]))

		data = OneVsOneData()
		data.spreadRange = 4.0

		loadData()

		LocationCommand.CUSTOM_EXECUTORS.add(this)
		WarpCommand.CUSTOM_EXECUTORS.add(this)
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val loser = event.entity.gamer()

		if (loser.warp == this) {
			if (FIGHTS.values.any { it is OneVsOneFight && (it.host == loser || it.guest == loser) }) end(loser, loser.combatPartner!!)
		}
	}

	/*@EventHandler(priority = EventPriority.HIGHEST)
	fun onInventoryClick(event: InventoryClickEvent) {
		val itemStack = event.currentItem

		if (event.slotType == InventoryType.SlotType.CONTAINER && itemStack != Inventories.BACKGROUND) {
			if (event.clickedInventory.title == ClanVsClanMenu.TITLE_CLANS) {
				val player = event.whoClicked as Player
				val gamer = player.gamer()

				if (gamer.warp == this) {
					event.isCancelled = true

					itemStack.itemMeta?.lore?.run {
						val clan = ClanRegistry.clan(last().clearFormatting())

						when {
							clan == null                        -> player.sendMessage(CLAN_IS_OFFLINE)
							gamer.clan!!.onlineMembers.size < 5 -> player.sendMessage(SOMEONE_IS_OFFLINE)
							clan.onlineMembers.size < 5         -> player.sendMessage(SOMEONE_IS_OFFLINE_OTHER)
							else                                -> {
								val fight = FIGHTS[player]

								if (fight is ClanVsClanFight && fight.guest == clan && fight.expiresOn > System.currentTimeMillis()) player.sendMessage(Text.negativePrefix().basic("Você ainda deve ").negative(
										"esperar").basic(" mais ").negative(fight.expiresOn.millisToPeriod().toInt()).basic(" segundos antes de convidar esse ").negative("clan").basic(" para uma luta novamente!").toString())
								else invite(gamer, clan, CLAN_VS_CLAN)
							}
						}

						player.closeInventory()
					}
				}
			}
		}
	}*/

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp == this) {
			when {
				!gamer.isFrozen() && FIGHTS.values.any { it.state == ONGOING && it is OneVsOneFight && it.type == GLADIATOR && (it.host == gamer || it.guest == gamer) } -> event.isCancelled = false
				event.item != null                                                                                                                                       -> {
					val item = event.item

					if (item.type == SKULL_ITEM && item == CLAN_VS_CLAN.item) {
						val clan = gamer.clan

						when {
							clan == null         -> player.sendMessage(HAS_NO_CLAN)
							clan.leader != gamer -> player.sendMessage(IS_NOT_LEADER)
							else                 -> player.openInventory(ClanVsClanMenu.menuClans(player, true))
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
		if (event.rightClicked is Player) {
			val player = event.player
			val gamer = player.gamer()

			if (gamer.warp == this) {
				val rightClicked = event.rightClicked as Player

				player.inventory.itemInMainHand?.run {
					val fight = FIGHTS[rightClicked]

					when (type) {
						BLAZE_ROD     -> if (this == STANDARD.item) {
							if (fight is OneVsOneFight && fight.type == STANDARD && fight.host.player == rightClicked) start(fight)
							else invite(player, rightClicked.player, STANDARD)
						}
						MUSHROOM_SOUP -> if (this == FULL.item) {
							if (fight is OneVsOneFight && fight.type == FULL && fight.host.player == rightClicked) start(fight)
							else invite(player, rightClicked.player, FULL)
						}
						STICK         -> if (this == NUDE.item) {
							if (fight is OneVsOneFight && fight.type == NUDE && fight.host.player == rightClicked) start(fight)
							else invite(player, rightClicked.player, NUDE)
						}
						IRON_FENCE    -> if (this == GLADIATOR.item) {
							if (fight is OneVsOneFight && fight.type == GLADIATOR && fight.host.player == rightClicked) start(fight)
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
			host.sendMessage(Text.negativePrefix().basic("Você ainda deve ").negative("esperar").basic(" mais ").negative(oldFight.expiresOn.millisToPeriod().toInt()).basic(" segundos antes de convidar esse ").negative(
					"jogador").basic(" para uma luta novamente!").toString())
		} else {
			FIGHTS.put(host, OneVsOneFight(host.gamer(), guest.gamer(), System.currentTimeMillis() + 10000L, type, INVITATION))

			host.sendMessage(Text.positivePrefix().basic("Você ").positive("convidou").basic(" o jogador ").positive(guest.displayName.clearFormatting()).basic(" para uma luta do tipo ").positive(
					type.string).basic("!").toString())

			guest.sendMessage(Text.positivePrefix().basic("Você foi ").positive("convidado").basic(" pelo jogador ").positive(host.displayName.clearFormatting()).basic(" para uma luta do tipo ").positive(
					type.string).basic("!").toString())
			guest.sendMessage(Text.positivePrefix().basic("Clique no ").positive("jogador").basic(" com o item correto para ").positive("aceitar").basic("!").toString())
		}
	}

	/*fun invite(host: Gamer, guest: Clan, type: FightType) {
		val hostPlayer = host.player
		val guestPlayer = guest.leader!!.player

		FIGHTS.put(hostPlayer, ClanVsClanFight(host, guest, hashMapOf(), System.currentTimeMillis() + 10000L, type, INVITATION))

		hostPlayer.sendMessage(Text.positivePrefix().basic("Você ").positive("convidou").basic(" o clan ").positive(guest.name).basic(" para uma luta do tipo ").positive(type.string).basic("!").toString())

		guestPlayer.sendMessage(Text.positivePrefix().basic("Seu clan foi ").positive("convidado").basic(" pelo jogador ").positive(hostPlayer.displayName.clearFormatting()).basic(" para uma luta do tipo ").positive(
				type.string).basic("!").toString())

		val command = "/1v1 aceitar " + hostPlayer.name

		val hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Aceitar!").color(ChatColor.GREEN).create())
		val clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)

		guestPlayer.spigot().sendMessage(*ComponentBuilder("Clique ").color(ChatColor.GRAY).append("aqui").color(ChatColor.GREEN).italic(true).event(hoverEvent).event(clickEvent).append(" ou use o comando ").color(
				ChatColor.GRAY).append(command).color(ChatColor.GREEN).append(" para ").color(ChatColor.GRAY).append("aceitar").color(ChatColor.GREEN).append("!").color(ChatColor.GRAY).create())
	}*/

	fun start(fight: OneVsOneFight) {
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

		if (fight.type == GLADIATOR) {
			hostPlayer.gameMode = GameMode.SURVIVAL
			guestPlayer.gameMode = GameMode.SURVIVAL

			val startLocation = spawn.clone()
			startLocation.y = 240.0

			var x = 0.0
			var z = 0.0

			while (startLocation.add(x, 0.0, z).block.type != AIR) {
				x += 16
				z += 16
			}

			val triple = startLocation.generateGlassArena(15, 10, 15, false, false)

			ARENAS.put(hostPlayer, triple.first)

			hostPlayer.teleport(triple.second)
			guestPlayer.teleport(triple.third)
		} else {
			hostPlayer.teleport(oneVsOneFirst)
			guestPlayer.teleport(oneVsOneSecond)
		}

		host.freeze = 5000L
		guest.freeze = 5000L

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

	/*fun start(fight: ClanVsClanFight) {
		val hostClan = fight.host.clan!!
		val guestClan = fight.guest

		val hostGamers = hostClan.onlineMembers
		val guestGamers = guestClan.onlineMembers

		val hostPlayers = hostGamers.map { it.player }
		val guestPlayers = guestGamers.map { it.player }

		FIGHTS.values.forEach {
			if (it is ClanVsClanFight && it.state == ONGOING) {
				val anotherHostPlayers = it.host.clan!!.onlineMembers.map { it.player }
				val anotherGuestPlayers = it.guest.onlineMembers.map { it.player }

				anotherHostPlayers.forEach { anotherPlayer ->
					hostPlayers.forEach { player -> player.hidePlayer(anotherPlayer) }
					guestPlayers.forEach { player -> player.hidePlayer(anotherPlayer) }
				}

				anotherGuestPlayers.forEach { anotherPlayer ->
					hostPlayers.forEach { player -> player.hidePlayer(anotherPlayer) }
					guestPlayers.forEach { player -> player.hidePlayer(anotherPlayer) }
				}

				hostPlayers.forEach { player ->
					anotherHostPlayers.forEach { anotherPlayer -> anotherPlayer.hidePlayer(player) }
					anotherGuestPlayers.forEach { anotherPlayer -> anotherPlayer.hidePlayer(player) }
				}

				guestPlayers.forEach { player ->
					anotherHostPlayers.forEach { anotherPlayer -> anotherPlayer.hidePlayer(player) }
					anotherGuestPlayers.forEach { anotherPlayer -> anotherPlayer.hidePlayer(player) }
				}
			}
		}

		hostPlayers.forEach { it.teleport(clanVsClanFirst.spread(4.0)) }
		guestPlayers.forEach { it.teleport(clanVsClanSecond.spread(4.0)) }

		hostGamers.forEach { it.freeze = 5000L }
		guestGamers.forEach { it.freeze = 5000L }

		hostGamers.forEach { it.setKitAndApply(fight.type.kit, false) }
		guestGamers.forEach { it.setKitAndApply(fight.type.kit, false) }

		hostPlayers.forEach {
			it.fillSoups(true)
			it.fillRecraft()
		}
		guestPlayers.forEach {
			it.fillSoups(true)
			it.fillRecraft()
		}

		hostGamers.forEach {
			it.combatPartner = guestClan.leader
			it.combatTag = Integer.MAX_VALUE.toLong()
		}
		guestGamers.forEach {
			it.combatPartner = hostClan.leader
			it.combatTag = Integer.MAX_VALUE.toLong()
		}

		fight.state = ONGOING

		Tasks.sync(object: BukkitRunnable() {
			var i = 5

			override fun run() {
				if (i == 5) {
					val message = Text.positivePrefix().basic("A ").positive("luta").basic(" irá ").positive("começar").basic(" em...").toString()

					hostPlayers.forEach { it.sendMessage(message) }
					guestPlayers.forEach { it.sendMessage(message) }
				}

				val countdownMessage = Text.positivePrefix().basic(i.toString() + "...").toString()

				hostPlayers.forEach { it.sendMessage(countdownMessage) }
				guestPlayers.forEach { it.sendMessage(countdownMessage) }

				i--

				if (i == 0) {
					val goMessage = Text.positivePrefix().positive("Já").basic("!").toString()

					hostPlayers.forEach { it.sendMessage(goMessage) }
					guestPlayers.forEach { it.sendMessage(goMessage) }

					cancel()
				}
			}
		}, 0L, 20L)
	}*/

	fun end(loser: Gamer, winner: Gamer) {
		val loserPlayer = loser.player
		val winnerPlayer = winner.player

		var fight: OneVsOneFight? = null

		FIGHTS.remove(loserPlayer)?.run {
			this as OneVsOneFight

			if ((host == loser && guest == winner) || (host == winner && guest == loser)) fight = this
		}
		FIGHTS.remove(winnerPlayer)?.run {
			this as OneVsOneFight

			if ((host == loser && guest == winner) || (host == winner && guest == loser)) fight = this
		}

		if (fight?.type == GLADIATOR) {
			ARENAS.remove(loserPlayer)?.destroyGlassArena(15, 10, 15)
			ARENAS.remove(winnerPlayer)?.destroyGlassArena(15, 10, 15)

			loserPlayer.gameMode = GameMode.ADVENTURE
			winnerPlayer.gameMode = GameMode.ADVENTURE
		}

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

	override fun dispatchGamer(gamer: Gamer, newWarp: Warp) {
		if (gamer.isCombatTagged() && FIGHTS.values.any { it is OneVsOneFight && (it.host == gamer || it.guest == gamer) }) end(gamer, gamer.combatPartner!!)
	}

	override fun execute(gamer: Gamer, alias: String, args: Array<String>) {
		val player = gamer.player

		when (alias) {
			in aliases -> {
				if (args.isEmpty()) gamer.sendToWarp(this, false, true)
				else when (args[0]) {
					"aceitar" -> {
					}
				}
			}
			"location" -> when (args[0]) {
				"oneVsOneFirst"    -> {
					oneVsOneFirst = player.location.normalize()

					player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("primeiro").basic(" spawn do ").positive("1v1").basic("!").toString())
				}
				"oneVsOneSecond"   -> {
					oneVsOneSecond = player.location.normalize()

					player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("segundo").basic(" spawn do ").positive("1v1").basic("!").toString())
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
	}

	sealed class Fight(val expiresOn: Long, val type: FightType, var state: FightState) {

		class OneVsOneFight(val host: Gamer, val guest: Gamer, expiresOn: Long, type: FightType, state: FightState): Fight(expiresOn, type, state)
	}

	enum class FightType(val string: String, val kit: Kit, val item: ItemStack) {

		STANDARD("Clássico",
		         Kit(weapon = Inventories.DIAMOND_SWORD_SHARPNESS, armor = Inventories.ARMOR_FULL_IRON, isDummy = false),
		         ItemStack(BLAZE_ROD).rename(Text.of("1v1 - Clássico").color(TextColor.GOLD).toString()).setDescription(arrayListOf("Arena normal",
		                                                                                                                            "Armadura de ferro completa",
		                                                                                                                            "Espada de diamante encantada",
		                                                                                                                            "Sopas apenas na hotbar"), true)),
		FULL("Completo",
		     Kit(weapon = Inventories.DIAMOND_SWORD_SHARPNESS, armor = Inventories.ARMOR_FULL_IRON, isDummy = false),
		     ItemStack(MUSHROOM_SOUP).rename(Text.of("1v1 - Completo").color(TextColor.GOLD).toString()).setDescription(arrayListOf("Arena normal",
		                                                                                                                            "Armadura de ferro completa",
		                                                                                                                            "Espada de diamante encantada",
		                                                                                                                            "Sopas em todo o invetário",
		                                                                                                                            "Material de recraft"), true)),
		NUDE("Sem Armadura",
		     Kit(weapon = ItemStack(STONE_SWORD), armor = arrayOfNulls(4), isDummy = false),
		     ItemStack(STICK).rename(Text.of("1v1 - Sem Armadura").color(TextColor.GOLD).toString()).setDescription(arrayListOf("Arena normal",
		                                                                                                                        "Sem armadura",
		                                                                                                                        "Espada de pedra",
		                                                                                                                        "Sopas em todo o invetário",
		                                                                                                                        "Material de recraft"), true)),
		GLADIATOR("Gladiator",
		          Kit(weapon = ItemStack(STONE_SWORD),
		              armor = arrayOfNulls(4),
		              items = arrayOf(ItemStack(STONE_PICKAXE), ItemStack(COBBLE_WALL, 64), ItemStack(LAVA_BUCKET), ItemStack(WATER_BUCKET)),
		              isDummy = false),
		          ItemStack(IRON_FENCE).rename(Text.of("1v1 - Gladiator").color(TextColor.GOLD).toString()).setDescription(arrayListOf("Arena no estilo \"Gladiator\"",
		                                                                                                                               "Armadura de ferro completa",
		                                                                                                                               "Espada de diamante encantada",
		                                                                                                                               "Sopas em todo o invetário",
		                                                                                                                               "Material de recraft"), true)),
		CLAN_VS_CLAN("Clan vs Clan",
		             Kit(weapon = Inventories.DIAMOND_SWORD_SHARPNESS, armor = Inventories.ARMOR_FULL_IRON, isDummy = false),
		             ItemStack(SKULL_ITEM,
		                       1,
		                       SkullType.PLAYER.ordinal.toShort()).rename(Text.of("5v5 - Clan vs Clan").color(TextColor.GOLD).toString()).setDescription(arrayListOf("Arena normal",
		                                                                                                                                                             "Armadura de ferro completa",
		                                                                                                                                                             "Espada de diamante encantada",
		                                                                                                                                                             "Sopas em todo o invetário",
		                                                                                                                                                             "Material de recraft"),
		                                                                                                                                                 true))
	}

	enum class FightState {

		INVITATION,
		ONGOING,
		TERMINATED
	}

	data class OneVsOneData(var oneVsOneFirst: SimpleLocation = SimpleLocation(),
	                        var oneVsOneSecond: SimpleLocation = SimpleLocation(),
	                        var clanVsClanFirst: SimpleLocation = SimpleLocation(),
	                        var clanVsClanSecond: SimpleLocation = SimpleLocation()): Data()
}
