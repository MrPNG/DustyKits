package br.com.dusty.dkits.util.web

import br.com.dusty.dkits.clan.Clan
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.store.Store
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import java.util.*

object WebAPI {

	val HTTP_CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(10000).build()).build()

	val URL = "http://api.dusty.com.br/handler.php"

	fun loadProfile(uuid: UUID) = HttpGet(URL + "?type=perfil&uuid=" + uuid).response()

	fun saveProfiles(vararg gamers: Gamer) = HttpPost(URL).setEntities(BasicNameValuePair("type", "salvarperfil"),
	                                                                   BasicNameValuePair("dataperfil", HttpClients.GSON.toJson(gamers.map { it.primitiveGamer }))).response()

	fun loadClan(uuid: String) = HttpGet(URL + "?type=perfilclan&uuid=" + uuid).response()

	fun saveClans(vararg clans: Clan) = HttpPost(URL).setEntities(BasicNameValuePair("type", "salvarclan"),
	                                                              BasicNameValuePair("dataclan", HttpClients.GSON.toJson(clans.map { it.primitiveClan }))).response()

	fun loadPurchases(uuid: String) = HttpGet(URL + "?type=getcompras&uuid=" + uuid).response()

	fun addPurchase(pseudoPurchase: Store.PseudoPurchase) = HttpGet(URL + "?type=addcompra&action=add&id=${pseudoPurchase.id}&json=" + HttpClients.GSON.toJson(pseudoPurchase)).response()

	fun updatePurchase(pseudoPurchase: Store.PseudoPurchase) = HttpGet(URL + "?type=addcompra&action=update&id=${pseudoPurchase.id}&json=" + HttpClients.GSON.toJson(pseudoPurchase)).response()

	fun report(name: String, reporter: String, reason: String) = HttpGet("http://api.dusty.com.br/report.php?player=$name&reportby=$reporter&reason=$reason").response()
}
