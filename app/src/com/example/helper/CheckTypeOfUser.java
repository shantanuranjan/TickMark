package com.example.helper;

import org.json.JSONObject;

import com.example.JSON.JSONParser;

import android.os.AsyncTask;
import android.util.Log;

public class CheckTypeOfUser extends AsyncTask<JSONObject, Void, JSONObject>{
	private static String TAG = "CheckTypeOfUser";
	private static String url_login = "http://sunsimengkira.appspot.com/check_user";
	public AsyncResponse transferResult;
	JSONParser jParser = new JSONParser();
	JSONObject json;
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		try {
			JSONObject obj = params[0];
			
			json = jParser.makeHttpRequest(url_login, "POST", obj);
			json.put("Identifier", "CheckTypeOfUser");
			Log.i(TAG, obj.toString());

		} catch (Exception e) {
			Log.i(TAG, "Exception in doInBackground :" + e.toString());

		}
		return json;
	}
	@Override
	protected void onPostExecute(JSONObject result) 
	{
		//super.onPostExecute(result);
		transferResult.processFinish(result);
	}

}
