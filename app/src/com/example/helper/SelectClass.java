package com.example.helper;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.JSON.JSONParser;
import com.example.tickmark.HomepageActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class SelectClass extends AsyncTask<Void, Void, JSONObject> {
	private static String TAG = "SelectClass";
	private static String url_login = "http://192.168.56.101:80/test/get_courses.php";
	public AsyncResponse transferResult;
	JSONParser jParser = new JSONParser();
	JSONObject json;

	@Override
	protected JSONObject doInBackground(Void... params) {

		try {
			JSONObject obj = new JSONObject();
			System.out.println("shantanu");
			json = jParser.makeHttpRequest(url_login, "POST", obj);
			
			json.put("Identifier", "SelectClass");
			//String c=json.toString();
			//System.out.println(c);
			//Log.i(TAG, json.toString());

		} catch (Exception e) {
			Log.i(TAG, "Exception in doInBackground :" + e.toString());

		}
		return json;
	}
	@Override
	protected void onPostExecute(JSONObject result) 
	{
		//super.onPostExecute(result);
		try {
			System.out.println(result.getString("course"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//transferResult.processFinish(result);
	}

}
