package br.com.dusty.dkits.warp

import br.com.dusty.dkits.clan.Clan
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.millisToSeconds
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.util.text.TextStyle
import org.bukkit.Material
import org.bukkit.SkullType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack

object OneVsOneWarp: Warp() {

	val FIGHTS = hashMapOf<Player, Fight>()

	init {
		name = "1v1"
		icon = ItemStack(Material.BLAZE_ROD)

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description)

		entryKit = OneVsOneLobbyKit

		loadData()
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
						Material.BLAZE_ROD     -> if (this == OneVsOneLobbyKit.STANDARD) {
							if (fight != null && fight.host.player == rightClicked) //TODO: Start fight
							else invite(player, rightClicked.player, FightType.STANDARD)
						}
						Material.MUSHROOM_SOUP -> if (this == OneVsOneLobbyKit.FULL) {
							if (fight != null && fight.host.player == rightClicked) //TODO: Start fight
							else invite(player, rightClicked.player, FightType.FULL)
						}
						Material.STICK         -> if (this == OneVsOneLobbyKit.NUDE) {
							if (fight != null && fight.host.player == rightClicked) //TODO: Start fight
							else invite(player, rightClicked.player, FightType.NUDE)
						}
						Material.IRON_BARDING  -> if (this == OneVsOneLobbyKit.GLADIATOR) {
							if (fight != null && fight.host.player == rightClicked) //TODO: Start fight
							else invite(player, rightClicked.player, FightType.GLADIATOR)
						}
					}
				}
			}
		}
	}

	fun invite(host: Player, guest: Player, type: FightType) {
		val oldFight = FIGHTS[host]

		if (oldFight != null && oldFight is OneVsOneFight && oldFight.guest.player == guest) {
			val cooldown = oldFight.expiresOn - System.currentTimeMillis()

			if (cooldown > 0) {
				host.sendMessage(Text.negativePrefix().basic("Você ainda deve ").negative("esperar").basic(" mais ").negative(cooldown.millisToSeconds().toInt()).basic(" segundos antes de convidar esse jogador para uma luta desse tipo novamente!").toString())

				return
			}
		}

		FIGHTS.put(host, OneVsOneFight(host.gamer(), guest.gamer(), System.currentTimeMillis() + 10000L, type, FightState.INVITATION))

		host.sendMessage(Text.positivePrefix().basic("Você ").positive("convidou").basic(" o jogador ").positive(guest.displayName.clearFormatting()).basic(" para uma luta do tipo ").positive(
				type.string).basic("!").toString())

		guest.sendMessage(Text.positivePrefix().basic("Você foi ").positive("convidado").basic(" pelo jogador ").positive(host.displayName.clearFormatting()).basic(" para uma luta do tipo ").positive(
				type.string).basic("!").toString())
		guest.sendMessage(Text.positivePrefix().basic("Clique ").positive("aqui").styles(TextStyle.ITALIC).basic(" ou no ").positive("jogador").basic(" com o item correto para ").positive("aceitar").basic(
				"!").toString())
	}

	object OneVsOneLobbyKit: Kit() {

		val STANDARD = ItemStack(Material.BLAZE_ROD).rename(Text.of("1v1 - Clássico").color(TextColor.GOLD).toString()).setDescription(arrayOf("Arena normal",
		                                                                                                                                       "Armadura de ferro completa",
		                                                                                                                                       "Espada de diamante encantada",
		                                                                                                                                       "Sopas apenas na hotbar").asList())
		val FULL = ItemStack(Material.MUSHROOM_SOUP).rename(Text.of("1v1 - Completo").color(TextColor.GOLD).toString()).setDescription(arrayOf("Arena normal",
		                                                                                                                                       "Armadura de ferro completa",
		                                                                                                                                       "Espada de diamante encantada",
		                                                                                                                                       "Sopas em todo o invetário",
		                                                                                                                                       "Material de recraft").asList())
		val NUDE = ItemStack(Material.STICK).rename(Text.of("1v1 - Sem Armadura").color(TextColor.GOLD).toString()).setDescription(arrayOf("Arena normal",
		                                                                                                                                   "Sem armadura",
		                                                                                                                                   "Espada de pedra",
		                                                                                                                                   "Sopas em todo o invetário",
		                                                                                                                                   "Material de recraft").asList())
		val GLADIATOR = ItemStack(Material.IRON_BARDING).rename(Text.of("1v1 - Gladiator").color(TextColor.GOLD).toString()).setDescription(arrayOf("Arena no estilo \"Gladiator\"",
		                                                                                                                                            "Sem armadura",
		                                                                                                                                            "Espada de pedra",
		                                                                                                                                            "Sopas em todo o invetário",
		                                                                                                                                            "Material de recraft").asList())
		val CLAN_VS_CLAN = ItemStack(Material.SKULL_ITEM,
		                             1,
		                             SkullType.PLAYER.ordinal.toShort()).rename(Text.of("5v5 - Clan vs Clan").color(TextColor.GOLD).toString()).setDescription(arrayOf("Arena normal",
		                                                                                                                                                               "Armadura de ferro completa",
		                                                                                                                                                               "Espada de diamante encantada",
		                                                                                                                                                               "Sopas em todo o invetário",
		                                                                                                                                                               "Material de recraft").asList())

		init {
			items = arrayOf(STANDARD, FULL, NUDE, GLADIATOR, CLAN_VS_CLAN, null, null, null, GameWarpKit.items[8])
		}
	}

	open class Fight(val host: Gamer, val expiresOn: Long, val type: FightType, var state: FightState)

	class OneVsOneFight(host: Gamer, val guest: Gamer, timestamp: Long, type: FightType, state: FightState): Fight(host, timestamp, type, state)
	class ClanVsClanFight(host: Gamer, val guest: Clan, timestamp: Long, type: FightType, state: FightState): Fight(host, timestamp, type, state)

	enum class FightType(val string: String) {

		STANDARD("Clássico"),
		FULL("Completo"),
		NUDE("Sem Armadura"),
		GLADIATOR("Gladiator"),
		CLAN_VS_CLAN("Clan vs Clan")
	}

	enum class FightState {

		INVITATION,
		ONGOING,
		TERMINATED
	}
}
