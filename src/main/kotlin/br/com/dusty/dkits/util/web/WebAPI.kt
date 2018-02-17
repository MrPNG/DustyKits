package br.com.dusty.dkits.util.web

import br.com.dusty.dkits.clan.Clan
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.leaderboard.Leaderboard
import br.com.dusty.dkits.store.Store
import br.com.dusty.dkits.util.stdlib.removeUuidDashes
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.message.BasicNameValuePair
import java.lang.Exception
import java.util.*

object WebAPI {

	val URL = "http://api.dusty.com.br/handler.php"

	fun loadProfile(uuid: UUID) = HttpGet(URL + "?type=perfil&uuid=" + uuid).response()

	fun saveProfiles(vararg gamers: Gamer) = HttpPost(URL).setEntities(BasicNameValuePair("type", "salvarperfil"),
	                                                                   BasicNameValuePair("dataperfil", HttpClients.GSON.toJson(gamers.map { it.primitiveGamer }))).response()

	fun loadClan(uuid: String) = HttpGet(URL + "?type=perfilclan&uuid=" + uuid).response()

	fun saveClans(vararg clans: Clan) = HttpPost(URL).setEntities(BasicNameValuePair("type", "salvarclan"),
	                                                              BasicNameValuePair("dataclan", HttpClients.GSON.toJson(clans.map { it.primitiveClan }))).response()

	fun loadPurchases(uuid: String) = HttpGet(URL + "?type=getcompras&uuid=" + uuid).response()

	fun addPurchase(pseudoPurchase: Store.PseudoPurchase) = HttpPost(URL).setEntities(BasicNameValuePair("type", "addcompra"),
	                                                                                  BasicNameValuePair("action", "add"),
	                                                                                  BasicNameValuePair("id", pseudoPurchase.id.toString()),
	                                                                                  BasicNameValuePair("json", HttpClients.GSON.toJson(pseudoPurchase))).response()

	fun updatePurchase(pseudoPurchase: Store.PseudoPurchase) = HttpGet(URL + "?type=addcompra&action=update&id=${pseudoPurchase.id}&json=" + HttpClients.GSON.toJson(pseudoPurchase)).response()

	fun bug(string: String) = HttpGet("http://api.dusty.com.br/reportbug.php?bug=$string").response()

	fun report(name: String, reporter: String, reason: String) = HttpGet("http://api.dusty.com.br/report.php?player=$name&reportby=$reporter&reason=${reason.replace(" ", "%20")}").response()

	fun leaderboard(leaderboard: Leaderboard): List<Pair<String, Int>> {
		val type = leaderboard.type.name.toLowerCase()

		val json = HttpGet("http://api.dusty.com.br/handler.php?type=getLeaderboard&tipo=$type&max=${leaderboard.amount}&ordem=${if (leaderboard.descending) "desc" else "asc"}").response()

		try {
			HttpClients.JSON_PARSER.parse(json).asJsonArray
		} catch (e: Exception) {
			bug("Erro na API de leaderboards porque isso não é um JSON válido: " + json)
		}

		return HttpClients.JSON_PARSER.parse(json).asJsonArray.map {
			val jsonObject = it.asJsonObject

			(MojangAPI.profile(jsonObject["uuid"].asString.removeUuidDashes())?.name ?: "null") to jsonObject[type].asInt
		}
	}
}
