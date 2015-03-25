package com.example.helper;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.example.JSON.JSONParser;

public class SelectProf extends AsyncTask<JSONObject, Void, JSONObject> {
	private static String TAG = "SelectProf";
	private static String url_login = "http://192.168.56.101:80/test/get_all_profs.php";
	public AsyncResponse transferResult;
	JSONParser jParser = new JSONParser();
	JSONObject json;

	@Override
	protected JSONObject doInBackground(JSONObject... params) {

		try {
			JSONObject obj = params[0];
			json = jParser.makeHttpRequest(url_login, "POST", obj);
			json.put("Identifier", "SelectProf");
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
