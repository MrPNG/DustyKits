package br.com.dusty.dkits.util.web

import com.google.gson.Gson
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.util.EntityUtils

fun HttpPost.setEntities(vararg pairs: NameValuePair) = this.apply {
	val list = arrayListOf<NameValuePair>()
	list.addAll(pairs)

	entity = UrlEncodedFormEntity(list)
}

fun HttpGet.response(): String? = EntityUtils.toString(WebAPI.HTTP_CLIENT.execute(this).entity)

fun HttpPost.response(): String? = EntityUtils.toString(WebAPI.HTTP_CLIENT.execute(this).entity)

object HttpClients {

	val GSON = Gson()
}
