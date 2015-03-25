package com.example.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public JSONObject makeHttpRequest(String url, String method,
			JSONObject params) {

		// Making HTTP request
		try {

			// check for request method
			if(method.equalsIgnoreCase("POST"))
			{
				String uri = url;
				JSONObject json = params;
				
//				Log.i("check", json.toString());
				
				HttpClient client = new DefaultHttpClient();
			    HttpPost post = new HttpPost(uri);
			    StringEntity se = new StringEntity( json.toString());

			    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			    post.setHeader("Content-type", "application/json");
			    
			    post.setEntity(se);
			    
			    HttpResponse response = client.execute(post);
			    
			    HttpEntity resultentity = response.getEntity();
			    InputStream inputstream = resultentity.getContent();
			    String resultstring = convertStreamToString(inputstream);
			    Log.i("doinbackground : Jsonparser", resultstring);
			    inputstream.close();
			    
			    JSONObject jsonResponse = new JSONObject(resultstring);

			    return jsonResponse;
			}
			else if(method.equalsIgnoreCase("GET")){
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				//String paramString = URLEncodedUtils.format(params, "utf-8");
				//url += "?" + paramString;
			
				HttpGet httpGet = new HttpGet(url);
				
				HttpResponse httpResponse = httpClient.execute(httpGet);
				System.out.println("sds");
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				
			}			

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) 
	    {
	    	Log.i("JSONPARSER", "Exception in makeHttpRequest :" + e.toString());
	    }

		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.i("msg", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}
	
	private String convertStreamToString(InputStream is) 
	{
	    String line = "";
	    StringBuilder total = new StringBuilder();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    try 
	    {
	        while ((line = rd.readLine()) != null) 
	        {
	            total.append(line);
	        }
	    } 
	    catch (Exception e) 
	    {
	    	Log.i("JSONPARSER", "Exception in convertStreamToString :" + e.toString());
	    }
	    return total.toString();
	}
}