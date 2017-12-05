package br.com.dusty.dkits.util.web

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.clan.Clan
import br.com.dusty.dkits.gamer.Gamer
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import java.util.*

object WebAPI {

	private val HTTP_CLIENT = HttpClientBuilder.create().build()

	private val URL = "http://ec2-52-67-190-141.sa-east-1.compute.amazonaws.com/api/handler.php"

	fun getProfile(uuid: UUID): String {
		val httpGet = HttpGet(URL + "?type=perfil&uuid=" + uuid)

		val httpResponse = HTTP_CLIENT.execute(httpGet)
		val httpEntity = httpResponse.entity

		return EntityUtils.toString(httpEntity) ?: "null"
	}

	fun saveProfiles(vararg gamers: Gamer): String {
		val primitiveGamers = gamers.map { it.primitiveGamer }

		val json = Main.GSON.toJson(primitiveGamers)

		val httpPost = HttpPost(URL)

		httpPost.entity = UrlEncodedFormEntity(arrayListOf<NameValuePair>().apply {
			add(BasicNameValuePair("type", "salvarperfil"))
			add(BasicNameValuePair("dataperfil", json))
		}, "UTF-8")

		val httpResponse = HTTP_CLIENT.execute(httpPost)
		val httpEntity = httpResponse.entity

		return EntityUtils.toString(httpEntity) ?: "null"
	}

	fun getClan(uuid: String): String {
		val httpGet = HttpGet(URL + "?type=clan&uuid=" + uuid)

		val httpResponse = HTTP_CLIENT.execute(httpGet)
		val httpEntity = httpResponse.entity

		return EntityUtils.toString(httpEntity) ?: "null"
	}

	fun saveClans(vararg clans: Clan): String {
		val primitiveClans = clans.map { it.primitiveClan }

		val json = Main.GSON.toJson(primitiveClans)

		val httpPost = HttpPost(URL)

		httpPost.entity = UrlEncodedFormEntity(arrayListOf<NameValuePair>().apply {
			add(BasicNameValuePair("type", "salvarperfil"))
			add(BasicNameValuePair("dataperfil", json))
		}, "UTF-8")

		val httpResponse = HTTP_CLIENT.execute(httpPost)
		val httpEntity = httpResponse.entity

		return EntityUtils.toString(httpEntity) ?: "null"
	}
}
