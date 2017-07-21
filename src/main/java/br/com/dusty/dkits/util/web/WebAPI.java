package br.com.dusty.dkits.util.web;

import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.gamer.PrimitiveGamer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class WebAPI {
	
	private static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();
	
	private static final String ENDPOINT = "http://localhost/api/handler.php";
	
	public static String getProfile(UUID uuid) {
		String json = null;
		
		HttpGet httpGet = new HttpGet(ENDPOINT + "?type=perfil&uuid=" + uuid);
		
		try{
			HttpResponse httpResponse = HTTP_CLIENT.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			json = EntityUtils.toString(httpEntity);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static String saveProfiles(Gamer... gamers) {
		String response = null;
		
		HashSet<PrimitiveGamer> primitiveGamers = new HashSet<>();
		
		for(Gamer gamer : gamers)
			primitiveGamers.add(gamer.getPrimitiveGamer());
		
		String json = Main.GSON.toJson(primitiveGamers);
		
		HttpPost httpPost = new HttpPost(ENDPOINT);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("type", "salvarperfil"));
		nameValuePairs.add(new BasicNameValuePair("dataperfil", json));
		
		try{
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse httpResponse = HTTP_CLIENT.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return response;
	}
}
