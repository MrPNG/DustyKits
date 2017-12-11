package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.clan.Clan
import br.com.dusty.dkits.clan.PrimitiveClan
import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumChat
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.addUuidDashes
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.MojangAPI
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object ClanCommand: PlayerCustomCommand(EnumRank.DEFAULT, "clan") {

	val HELP = arrayOf(Text.positivePrefix().positive("Comandos para clans:\n").toString(),
	                   Text.positivePrefix().basic("/clan ").positive("criar <nomeDoClan> <tagDoClan>").basic(": Criar um clan com um nome (máximo de 16 caracteres) e uma tag (máximo de 3 caracteres)").toString(),
	                   Text.positivePrefix().basic("/clan ").positive("lider <nomeDoJogador>").basic(": Transferir a liderança do seu clan para outro membro").toString(),
	                   Text.positivePrefix().basic("/clan ").positive("convidar <nomeDoJogador>").basic(": Convidar um jogador para o seu clan").toString(),
	                   Text.positivePrefix().basic("/clan ").positive("aceitar <nomeDoLider>").basic(": Aceitar um convite feito por um líder de um clan").toString(),
	                   Text.positivePrefix().basic("/clan ").positive("remover <nomeDoJogador>").basic(": Remover um jogador do seu clan").toString(),
	                   Text.positivePrefix().basic("/clan ").positive("sair").basic(": Sair do seu clan atual").toString(),
	                   Text.positivePrefix().basic("/clan ").positive("chat").basic(": Entrar no/Sair do chat do seu clan").toString()).joinToString(separator = "\n")

	val USAGE = Text.negativePrefix().basic("Para informações sobre esse comando, use /clan ").negative("help").toString()
	val WAIT_MORE = Text.negativePrefix().basic("Você deve ").negative("aguardar").basic(" para ").negative("usar").basic(" esse comando ").negative("novamente").basic("!").toString()

	val USAGE_CREATE = Text.negativePrefix().basic("Uso: /clan criar ").negative("<nomeDoClan> <tagDoClan>").toString()
	val NAME_TOO_LONG = Text.negativePrefix().basic("O ").negative("nome").basic(" do seu clan deve conter, no máximo, ").negative("16").basic(" caracteres!").toString()
	val TAG_TOO_LONG = Text.negativePrefix().basic("A ").negative("tag").basic(" do seu clan deve conter, no máximo, ").negative("3").basic(" caracteres!").toString()
	val EXIT_OLD_CLAN = Text.negativePrefix().basic("Você deve ").negative("sair").basic(" do seu clan ").negative("antigo").basic(" antes de ").negative("criar").basic(" um novo!").toString()
	val CREATE_CLAN_FAIL = Text.negativePrefix().negative("Não").basic(" foi possível ").negative("criar").basic(" o clan!").toString()

	val USAGE_LEADER = Text.negativePrefix().basic("Uso: /clan lider ").negative("<nomeDoJogador>").toString()
	val ALREADY_LEADER = Text.negativePrefix().basic("Você ").negative("já").basic(" é o ").negative("líder").basic(" do seu ").negative("clan").basic("!").toString()
	val SET_LEADER_FAIL = Text.negativePrefix().negative("Não").basic(" foi possível alterar o ").negative("líder").basic(" do clan!").toString()

	val USAGE_INVITE = Text.negativePrefix().basic("Uso: /clan convidar ").negative("<nomeDoJogador>").toString()
	val HAS_NO_CLAN = Text.negativePrefix().basic("Você ").negative("não").basic(" faz parte de um ").negative("clan").basic("!").toString()
	val CLAN_IS_FULL = Text.negativePrefix().basic("Seu clan ").negative("já").basic(" está ").negative("lotado").basic("!").toString()
	val IS_NOT_LEADER = Text.negativePrefix().basic("Você ").negative("não").basic(" é o ").negative("líder").basic(" do seu ").negative("clan").basic("!").toString()
	val INVITE_YOURSELF = Text.negativePrefix().basic("Você ").negative("não").basic(" precisa convidar ").negative("você").basic(" mesmo para o seu próprio ").negative("clan").basic("!").toString()
	val WAIT_INVITE = Text.negativePrefix().basic("Você deve ").negative("aguardar").basic(" antes de ").negative("convidar").basic(" esse jogador ").negative("novamente").basic("!").toString()
	val ALREADY_HAS_CLAN = Text.negativePrefix().basic("Esse jogador ").negative("já").basic(" faz parte de um ").negative("clan").basic("!").toString()
	val ALREADY_ON_CLAN = Text.negativePrefix().basic("Esse jogador ").negative("já").basic(" está no seu ").negative("clan").basic("!").toString()

	val USAGE_REMOVE = Text.negativePrefix().basic("Uso: /clan remover ").negative("<nomeDoJogador>").toString()
	val REMOVE_YOURSELF = Text.negativePrefix().basic("Você ").negative("não").basic(" pode deixar seu clan ").negative("sem").basic(" um ").negative("líder").basic("!").toString()
	val NOT_IN_CLAN = Text.negativePrefix().basic("Esse jogador ").negative("não").basic(" faz parte do seu ").negative("clan").basic("!").toString()
	val REMOVE_FROM_CLAN_FAIL = Text.negativePrefix().negative("Não").basic(" foi possível ").negative("remover").basic(" esse jogador do seu clan!").toString()

	val USAGE_ACCEPT = Text.negativePrefix().basic("Uso: /clan aceitar ").negative("<nomeDoLider>").toString()
	val HAS_CLAN = Text.negativePrefix().basic("Você ").negative("já").basic(" faz parte de um ").negative("clan").basic("!").toString()
	val ACCEPT_YOURSELF = Text.negativePrefix().basic("Você ").negative("não").basic(" precisa forçar o seu ").negative("clan").basic(" a te ").negative("aceitar").basic(", basta que ").positive(
			"você").basic(" mesmo esteja contente com quem você ").positive("é").basic("!").toString()
	val NO_INVITATIONS = Text.negativePrefix().basic("Você ").negative("não").basic(" possui nenhum ").negative("convite").basic(" válido desse ").negative("jogador").basic("!").toString()
	val NEW_CLAN_IS_FULL = Text.negativePrefix().basic("Esse clan ").negative("já").basic(" está ").negative("lotado").basic("!").toString()
	val ENTER_CLAN_FAIL = Text.negativePrefix().negative("Não").basic(" foi possível ").negative("entrar").basic(" nesse clan!").toString()

	val LEADER_EXIT = Text.negativePrefix().basic("Você é o ").negative("líder").basic(" do seu ").negative("clan").basic(". Para ").negative("sair").basic(", escolha um novo líder ").negative(
			"antes").basic("!").toString()

	val AWAITING_API = arrayListOf<Player>()

	val INVITATIONS: Multimap<Player, ClanInvitation> = HashMultimap.create()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) {
			sender.sendMessage(USAGE)
		} else {
			val gamer = sender.gamer()

			when (args[0].toLowerCase()) {
				"help"     -> sender.sendMessage(HELP)
				"criar"    -> {
					if (gamer.clan != null) sender.sendMessage(EXIT_OLD_CLAN)
					else when {
						args.size < 3          -> sender.sendMessage(USAGE_CREATE)
						sender in AWAITING_API -> sender.sendMessage(WAIT_MORE)
						else                   -> {
							val name = args.copyOfRange(1, args.size - 1).joinToString(separator = " ")
							val tag = args.last().toUpperCase()

							when {
								name.length > 16 -> sender.sendMessage(NAME_TOO_LONG)
								tag.length > 16  -> sender.sendMessage(TAG_TOO_LONG)
								else             -> {
									val uuid = sender.uniqueId.toString()
									val clan = Clan(PrimitiveClan(UUID.randomUUID().toString(), name, tag, uuid, arrayOf(uuid)))

									AWAITING_API.add(sender)

									val onNext = Consumer<Clan> {
										//										WebAPI.saveClans(clan)

										gamer.clan = clan

										sender.sendMessage(Text.positivePrefix().basic("Você ").basic("criou").basic(" o clan ").positive(clan.name).basic(" (").positive(clan.tag).basic(")!").toString())

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
					}
				}
				"lider"    -> {
					val clan = gamer.clan

					when {
						clan == null         -> sender.sendMessage(HAS_NO_CLAN)
						clan.leader != gamer -> sender.sendMessage(IS_NOT_LEADER)
						args.size < 2        -> sender.sendMessage(USAGE_LEADER)
						else                 -> {
							val player = Bukkit.getPlayerExact(args[1])

							when (player) {
								null   -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um ").negative("jogador").basic(" com o nome ").negative(args[1]).basic("!").toString())
								sender -> sender.sendMessage(ALREADY_LEADER)
								else   -> {
									val invitedGamer = player.gamer()

									when {
										invitedGamer.clan != clan -> sender.sendMessage(NOT_IN_CLAN)
										sender in AWAITING_API    -> sender.sendMessage(WAIT_MORE)
										else                      -> {
											AWAITING_API.add(sender)

											val onNext = Consumer<Clan> {
												clan.leader = invitedGamer

//												WebAPI.saveClans(clan)

												sender.sendMessage(Text.positivePrefix().basic("Você ").basic("promoveu").basic(" o jogador ").positive(player.displayName.clearFormatting()).basic(
														" como líder do seu ").positive("clan").basic("!").toString())

												player.sendMessage(Text.positivePrefix().basic("Você foi ").basic("promovido").basic(" pelo jogador ").positive(sender.displayName.clearFormatting()).basic(
														" para líder do clan ").positive(clan.name).basic(" (").positive(clan.tag).basic(")!").toString())

												AWAITING_API.remove(sender)
											}

											val onError = Consumer<Throwable> {
												sender.sendMessage(SET_LEADER_FAIL)

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
													" para se juntar ao clan ").positive(clan.name).basic(" (").positive(clan.tag).basic(")!").toString())
											player.sendMessage(Text.positivePrefix().basic("Use o comando ").positive("/clan aceitar " + sender.name).basic(" para ").positive("aceitar").basic(
													" o convite!").toString())

											INVITATIONS.put(sender, ClanInvitation(player, clan, System.currentTimeMillis() + 60000L))
										}
									}
								}
							}
						}
					}
				}
				"remover"  -> {
					val clan = gamer.clan

					when {
						clan == null           -> sender.sendMessage(HAS_NO_CLAN)
						clan.leader != gamer   -> sender.sendMessage(IS_NOT_LEADER)
						args.size < 2          -> sender.sendMessage(USAGE_REMOVE)
						sender in AWAITING_API -> sender.sendMessage(WAIT_MORE)
						else                   -> {
							val player = Bukkit.getPlayerExact(args[1])

							when (player) {
								null   -> {
									val onNext = Consumer<String> {
										val profile = MojangAPI.profile(it)

										if (profile == null) {
											sender.sendMessage(Text.negativePrefix().negative("Não").basic(" existe um ").negative("jogador").basic(" com o nome ").negative(args[1]).basic("!").toString())
										} else {
											val uuid = profile.id.addUuidDashes()

											if (uuid !in clan.rawMembers) {
												sender.sendMessage(NOT_IN_CLAN)
											} else {
												clan.remove(uuid)
//												WebAPI.saveClans(clan)

												sender.sendMessage(Text.negativePrefix().basic("Você ").negative("removeu").basic(" o jogador ").negative(profile.name).basic(" do seu ").negative(
														"clan").basic("!").toString())
											}
										}

										AWAITING_API.remove(sender)
									}

									val onError = Consumer<Throwable> {
										sender.sendMessage(REMOVE_FROM_CLAN_FAIL)

										AWAITING_API.remove(sender)
									}

									Observable.just(args[1]).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
								}
								sender -> sender.sendMessage(REMOVE_YOURSELF)
								else   -> {
									val removedGamer = player.gamer()

									if (removedGamer !in clan.onlineMembers) {
										sender.sendMessage(NOT_IN_CLAN)
									} else {
										AWAITING_API.add(sender)

										val onNext = Consumer<Clan> {
											removedGamer.clan = null
//											WebAPI.saveProfiles(gamer)

											clan.remove(gamer)
//											WebAPI.saveClans(clan)

											player.sendMessage(Text.negativePrefix().basic("Você foi ").negative("removido").basic(" do clan ").negative(clan.name).basic(" (").negative(clan.tag).basic(
													")!").toString())

											sender.sendMessage(Text.negativePrefix().basic("Você ").negative("removeu").basic(" o jogador ").negative(player.name).basic(" do seu ").negative("clan").basic(
													"!").toString())

											AWAITING_API.remove(sender)
										}

										val onError = Consumer<Throwable> {
											sender.sendMessage(REMOVE_FROM_CLAN_FAIL)

											AWAITING_API.remove(sender)
										}

										Observable.just(clan).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
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

										INVITATIONS.forEach { key, value -> Bukkit.broadcastMessage("Key: ${key.name}; Value: $value") } //TODO: Debug only
									} else {
										val clan = clanInvitation.clan

										when {
											clan.rawMembers.size > 4 -> sender.sendMessage(NEW_CLAN_IS_FULL)
											sender in AWAITING_API   -> sender.sendMessage(WAIT_MORE)
											else                     -> {
												AWAITING_API.add(sender)

												val onNext = Consumer<Clan> {
													gamer.clan = clan
//													WebAPI.saveProfiles(gamer)

													clan.add(gamer)
//													WebAPI.saveClans(clan)

													sender.sendMessage(Text.positivePrefix().basic("Você ").positive("entrou").basic(" para o clan ").positive(clan.name).basic(" (").positive(
															clan.tag).basic(")!").toString())

													clan.leader?.player?.sendMessage(Text.positivePrefix().basic("O jogador ").positive(sender.name).basic(" entrou para o seu ").positive("clan").basic(
															"!").toString())

													INVITATIONS.forEach { key, value -> if (key == player && value.player == sender) INVITATIONS.remove(key, value) }

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
				"sair"     -> {
					val clan = gamer.clan

					when {
						clan == null           -> sender.sendMessage(HAS_NO_CLAN)
						clan.leader == gamer   -> sender.sendMessage(LEADER_EXIT)
						sender in AWAITING_API -> sender.sendMessage(WAIT_MORE)
						else                   -> {
							AWAITING_API.add(sender)

							val onNext = Consumer<Clan> {
								gamer.clan = null
//								WebAPI.saveProfiles(gamer)

								clan.remove(gamer)
//								WebAPI.saveClans(clan)

								sender.sendMessage(Text.negativePrefix().basic("Você ").negative("saiu").basic(" do clan ").negative(clan.name).basic(" (").negative(clan.tag).basic(")!").toString())

								clan.leader?.player?.sendMessage(Text.negativePrefix().basic("O jogador ").negative(sender.name).basic(" saiu do seu ").negative("clan").basic("!").toString())

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
				"chat"     -> {
					when {
						gamer.clan == null          -> sender.sendMessage(HAS_NO_CLAN)
						gamer.chat != EnumChat.CLAN -> {
							gamer.chat = EnumChat.CLAN

							sender.sendMessage(Text.positivePrefix().basic("Agora você ").positive("está").basic(" no chat do seu ").positive("clan").basic("!").toString())
						}
						else                        -> {
							gamer.chat = EnumChat.NORMAL

							sender.sendMessage(Text.negativePrefix().basic("Agora você ").negative("não").basic(" está mais no chat do seu ").negative("clan").basic("!").toString())
						}
					}

				}
				else       -> sender.sendMessage(USAGE)
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

		override fun toString(): String {
			return "ClanInvitation(player=$player, clan=$clan, expiresOn=$expiresOn)"
		}
	}
}
