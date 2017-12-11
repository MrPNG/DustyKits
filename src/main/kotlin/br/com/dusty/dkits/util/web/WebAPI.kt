package br.com.dusty.dkits.util.web

import br.com.dusty.dkits.clan.Clan
import br.com.dusty.dkits.gamer.Gamer
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import java.util.*

object WebAPI {

	val HTTP_CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(10000).build()).build()

	val URL = "http://ec2-52-67-190-141.sa-east-1.compute.amazonaws.com/api/handler.php"

	fun loadProfile(uuid: UUID) = HttpGet(URL + "?type=perfil&uuid=" + uuid).response() ?: "null"

	fun saveProfiles(vararg gamers: Gamer) = HttpPost(URL).setEntities(BasicNameValuePair("type", "salvarperfil"),
	                                                                   BasicNameValuePair("dataperfil", HttpClients.GSON.toJson(gamers.map { it.primitiveGamer }))).response() ?: "null"

	fun loadClan(uuid: String) = HttpGet(URL + "?type=clan&uuid=" + uuid).response() ?: "null"

	fun saveClans(vararg clans: Clan) = HttpPost(URL).setEntities(BasicNameValuePair("type", "salvarclan"),
	                                                              BasicNameValuePair("dataclan", HttpClients.GSON.toJson(clans.map { it.primitiveClan }))).response() ?: "null"
}
