package br.com.dusty.dkits.util.web

import br.com.dusty.dkits.Main
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

	private val ENDPOINT = "http://ec2-52-67-190-141.sa-east-1.compute.amazonaws.com/api/handler.php"

	fun getProfile(uuid: UUID): String? {
		val httpGet = HttpGet(ENDPOINT + "?type=perfil&uuid=" + uuid)

		val httpResponse = HTTP_CLIENT.execute(httpGet)
		val httpEntity = httpResponse.entity

		return EntityUtils.toString(httpEntity)
	}

	fun saveProfiles(vararg gamers: Gamer): String? {

		val primitiveGamers = gamers.map { it.primitiveGamer }

		val json = Main.GSON.toJson(primitiveGamers)

		val httpPost = HttpPost(ENDPOINT)

		val nameValuePairs = ArrayList<NameValuePair>()
		nameValuePairs.add(BasicNameValuePair("type", "salvarperfil"))
		nameValuePairs.add(BasicNameValuePair("dataperfil", json))

		httpPost.entity = UrlEncodedFormEntity(nameValuePairs, "UTF-8")
		val httpResponse = HTTP_CLIENT.execute(httpPost)
		val httpEntity = httpResponse.entity

		return EntityUtils.toString(httpEntity)
	}
}
