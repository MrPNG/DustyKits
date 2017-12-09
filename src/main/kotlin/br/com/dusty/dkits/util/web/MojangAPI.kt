package br.com.dusty.dkits.util.web

import com.google.gson.reflect.TypeToken
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder

object MojangAPI {

	val PROFILES_TYPE_TOKEN = object: TypeToken<Collection<Profile>>() {}.type

	val HTTP_CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(10000).build()).build()

	val URL_PROFILE = "https://api.mojang.com/users/profiles/minecraft/"
	val URL_PROFILES = "https://api.mojang.com/profiles/minecraft"
	val URL_NAME_HISTORY = "https://api.mojang.com/user/profiles/%s/names"

	fun profile(name: String) = HttpGet(URL_PROFILE + name).response()?.run { HttpClients.GSON.fromJson(this, Profile::class.java) }

	fun profiles(vararg names: String): Collection<Profile>? = HttpPost(URL_PROFILES).apply {
		setHeader("Content-type", "application/json")
		entity = StringEntity(HttpClients.GSON.toJson(names))
	}.response()?.run { HttpClients.GSON.fromJson(this, PROFILES_TYPE_TOKEN) }

	fun nameHistory(uuid: String): Collection<Profile>? = HttpGet(URL_NAME_HISTORY.format(uuid)).response()?.run {
		HttpClients.GSON.fromJson(this, PROFILES_TYPE_TOKEN)
	}

	class Profile(val id: String, val name: String, val changedToAt: Long, val legacy: Boolean, val demo: Boolean) {

		override fun toString() = "Profile(id='$id', name='$name', changedToAt=$changedToAt, legacy=$legacy, demo=$demo)"
	}
}
