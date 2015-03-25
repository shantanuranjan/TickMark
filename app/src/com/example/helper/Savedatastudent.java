package com.example.helper;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.JSON.JSONParser;
import com.example.tickmark.AttendeeMarkPageActivity;
import com.example.tickmark.MainActivity;

public class Savedatastudent extends AsyncTask<JSONObject, Void, String> {
	private static String TAG = "GetProfLocation";
	private static String url_login = "http://sunsimengkira.appspot.com/save_data_student";
	public AsyncResponse transferResult;
	JSONParser jParser = new JSONParser();
	JSONObject json;
	String returnmsg = null;
	@Override
	protected String doInBackground(JSONObject... params) {
		try {
			JSONObject obj = params[0];
			System.out.println("value:"+obj.getString("sid"));
			json = jParser.makeHttpRequest(url_login, "POST", obj);
			
			returnmsg = json.toString();

		} catch (Exception e) {
			Log.i(TAG, "Exception in doInBackground :" + e.toString());

		}
		return returnmsg;
	}
	
	@Override
	protected void onPostExecute(String result) 
	{
		System.out.println(result);
		
	}

}
