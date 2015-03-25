package com.example.helper;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.JSON.JSONParser;

import android.os.AsyncTask;
import android.util.Log;

public class GetProfLocation extends AsyncTask<JSONObject, Void, JSONObject> {
	private static String TAG = "GetProfLocation";
	private static String url_login = "http://sunsimengkira.appspot.com/chk_location";
	public AsyncResponse transferResult;
	JSONParser jParser = new JSONParser();
	JSONObject json;
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		try {
			JSONObject obj = params[0];
			
			json = jParser.makeHttpRequest(url_login, "POST", obj);
			System.out.println(json.toString());
			if(json==null)
			{Log.i(TAG, obj.toString()+"shantanu");}

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
			System.out.println(result.getString("aid"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transferResult.processFinish(result);
	}

}
