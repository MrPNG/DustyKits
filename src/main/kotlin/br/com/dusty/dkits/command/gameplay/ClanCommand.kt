package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.clan.Clan
import br.com.dusty.dkits.clan.PrimitiveClan
import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object ClanCommand: PlayerCustomCommand(EnumRank.DEFAULT, "clan") {

	val USAGE = Text.negativePrefix().basic("Para informações sobre esse comando, use /clan ").negative("help").toString()
	val WAIT_MORE = Text.negativePrefix().basic("Você deve ").negative("aguardar").basic(" para ").negative("usar").basic(" esse comando ").negative("novamente").basic("!").toString()

	val USAGE_CREATE = Text.negativePrefix().basic("Uso: /clan criar ").negative("<nomeDoClan> <apelidoDoClan>").toString()
	val NICKNAME_TOO_LONG = Text.negativePrefix().basic("O ").negative("apelido").basic(" do seu clan deve conter apenas ").negative("três").basic(" letras!").toString()
	val EXIT_OLD_CLAN = Text.negativePrefix().basic("Você deve ").negative("sair").basic(" do seu clan ").negative("antigo").basic(" antes de ").negative("criar").basic(" um novo!").toString()
	val CREATE_CLAN_FAIL = Text.negativePrefix().negative("Não").basic(" foi possível ").negative("criar").basic(" o clan!").toString()

	val USAGE_INVITE = Text.negativePrefix().basic("Uso: /clan convidar ").negative("<nomeDoJogador>").toString()
	val HAS_NO_CLAN = Text.negativePrefix().basic("Você ").negative("não").basic(" faz parte de um ").negative("clan").basic("!").toString()
	val CLAN_IS_FULL = Text.negativePrefix().basic("Seu clan ").negative("já").basic(" está ").negative("lotado").basic("!").toString()
	val IS_NOT_LEADER = Text.negativePrefix().basic("Você ").negative("não").basic(" é o ").negative("líder").basic(" do seu ").negative("clan").basic("!").toString()
	val INVITE_YOURSELF = Text.negativePrefix().basic("Você ").negative("não").basic(" precisa convidar ").negative("você").basic(" mesmo para o seu próprio ").negative("clan").basic("!").toString()
	val WAIT_INVITE = Text.negativePrefix().basic("Você deve ").negative("aguardar").basic(" antes de ").negative("convidar").basic(" esse jogador ").negative("novamente").basic("!").toString()
	val ALREADY_HAS_CLAN = Text.negativePrefix().basic("Esse jogador ").negative("já").basic(" faz parte de um ").negative("clan").basic("!").toString()
	val ALREADY_ON_CLAN = Text.negativePrefix().basic("Esse jogador ").negative("já").basic(" está no seu ").negative("clan").basic("!").toString()

	val USAGE_ACCEPT = Text.negativePrefix().basic("Uso: /clan aceitar ").negative("<nomeDoLider>").toString()
	val HAS_CLAN = Text.negativePrefix().basic("Você ").negative("já").basic(" faz parte de um ").negative("clan").basic("!").toString()
	val ACCEPT_YOURSELF = Text.negativePrefix().basic("Você ").negative("não").basic(" pode aceitar ").negative("você").basic(" mesmo em um ").negative("clan").basic("!").toString()
	val NO_INVITATIONS = Text.negativePrefix().basic("Você ").negative("não").basic(" possui nenhum ").negative("convite").basic(" válido desse ").negative("jogador").basic("!").toString()
	val NEW_CLAN_IS_FULL = Text.negativePrefix().basic("Esse clan ").negative("já").basic(" está ").negative("lotado").basic("!").toString()
	val ENTER_CLAN_FAIL = Text.negativePrefix().negative("Não").basic(" foi possível ").negative("entrar").basic(" nesse clan!").toString()

	val AWAITING_API = arrayListOf<Player>()

	val INVITATIONS: Multimap<Player, ClanInvitation> = HashMultimap.create()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) {
			sender.sendMessage(USAGE)
		} else {
			val gamer = sender.gamer()

			when (args[0].toLowerCase()) {
				"criar"    -> {
					if (gamer.clan != null) sender.sendMessage(EXIT_OLD_CLAN)
					else when {
						args.size < 3          -> sender.sendMessage(USAGE_CREATE)
						args[2].length > 3     -> sender.sendMessage(NICKNAME_TOO_LONG)
						sender in AWAITING_API -> sender.sendMessage(WAIT_MORE)
						else                   -> {
							val uuid = sender.uniqueId.toString()
							val clan = Clan(PrimitiveClan(UUID.randomUUID().toString(), args[1], args[2].toUpperCase(), uuid, arrayOf(uuid)))

							AWAITING_API.add(sender)

							val onNext = Consumer<Clan> {
								WebAPI.saveClans(clan)

								gamer.clan = clan

								sender.sendMessage(Text.positivePrefix().basic("Você ").basic("criou").basic(" o clan ").positive(clan.name).basic(" (").positive(clan.nickname).basic(")!").toString())

								AWAITING_API.remove(sender)
							}

							val onError = Consumer<Throwable> {
								sender.sendMessage(CREATE_CLAN_FAIL)

								AWAITING_API.remove(sender)
							}

							Observable.just(clan).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
						}
					}
				}
				"convidar" -> {
					val clan = gamer.clan

					when {
						clan == null             -> sender.sendMessage(HAS_NO_CLAN)
						clan.leader != gamer     -> sender.sendMessage(IS_NOT_LEADER)
						args.size < 2            -> sender.sendMessage(USAGE_INVITE)
						clan.rawMembers.size > 4 -> sender.sendMessage(CLAN_IS_FULL)
						else                     -> {
							val player = Bukkit.getPlayerExact(args[1])

							when {
								player == null                                                                               -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um ").negative(
										"jogador").basic(" com o nome ").negative(args[1]).basic("!").toString())
								player == sender                                                                             -> sender.sendMessage(INVITE_YOURSELF)
								INVITATIONS[sender].any { it.player == player && it.expiresOn > System.currentTimeMillis() } -> sender.sendMessage(WAIT_INVITE)
								else                                                                                         -> {
									val invitedGamer = player.gamer()
									val oldClan = invitedGamer.clan

									when {
										oldClan == clan -> sender.sendMessage(ALREADY_ON_CLAN)
										oldClan != null -> sender.sendMessage(ALREADY_HAS_CLAN)
										else            -> {
											sender.sendMessage(Text.positivePrefix().basic("Você ").basic("convidou").basic(" o jogador ").positive(player.displayName.clearFormatting()).basic(
													" para o seu ").positive("clan").basic("!").toString())

											player.sendMessage(Text.positivePrefix().basic("Você foi ").basic("convidado").basic(" pelo jogador ").positive(sender.displayName.clearFormatting()).basic(
													" para se juntar ao clan ").positive(clan.name).basic(" (").positive(clan.nickname).basic(")!").toString())
											player.sendMessage(Text.positivePrefix().basic("Use o comando ").basic("/clan aceitar").basic(" para ").positive("aceitar").basic(" o convite!").toString())

											INVITATIONS.put(player, ClanInvitation(player, clan, System.currentTimeMillis() + 60000L))
										}
									}
								}
							}
						}
					}
				}
				"aceitar"  -> {
					val oldClan = gamer.clan

					when {
						oldClan != null -> sender.sendMessage(HAS_CLAN)
						args.size < 2   -> sender.sendMessage(USAGE_ACCEPT)
						else            -> {
							val player = Bukkit.getPlayerExact(args[1])

							when (player) {
								null   -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um ").negative("jogador").basic(" com o nome ").negative(args[1]).basic("!").toString())
								sender -> sender.sendMessage(ACCEPT_YOURSELF)
								else   -> {
									val clanInvitation = INVITATIONS[player].firstOrNull { it.player == sender && it.expiresOn > System.currentTimeMillis() }

									if (clanInvitation == null) {
										sender.sendMessage(NO_INVITATIONS)
									} else {
										val clan = clanInvitation.clan

										when {
											clan.rawMembers.size > 4 -> sender.sendMessage(NEW_CLAN_IS_FULL)
											sender in AWAITING_API   -> sender.sendMessage(WAIT_MORE)
											else                     -> {
												AWAITING_API.add(sender)

												val onNext = Consumer<Clan> {
													gamer.clan = clan
													WebAPI.saveProfiles(gamer)

													clan.add(gamer)
													WebAPI.saveClans(clan)

													sender.sendMessage(Text.positivePrefix().basic("Você ").basic("entrou").basic(" para o clan ").positive(clan.name).basic(" (").positive(clan.nickname).basic(
															")!").toString())

													AWAITING_API.remove(sender)
												}

												val onError = Consumer<Throwable> {
													sender.sendMessage(ENTER_CLAN_FAIL)

													AWAITING_API.remove(sender)
												}

												Observable.just(clan).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return false
	}

	class ClanInvitation(val player: Player, val clan: Clan, val expiresOn: Long) {

		override fun equals(other: Any?) = when {
			this === other                -> true
			javaClass != other?.javaClass -> false
			else                          -> {
				other as ClanInvitation

				when {
					player != other.player -> false
					clan != other.clan     -> false
					else                   -> true
				}
			}
		}

		override fun hashCode(): Int {
			var result = player.hashCode()

			result = 31 * result + clan.hashCode()

			return result
		}
	}
}
