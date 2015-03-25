package com.example.helper;

import org.json.JSONObject;

import com.example.JSON.JSONParser;

import android.os.AsyncTask;
import android.util.Log;

public class UserSignup extends AsyncTask<JSONObject, Void, String> {
	private static String TAG = "UserSignup";
	private static String url_signup = "http://sunsimengkira.appspot.com/signup";
	JSONParser jParser = new JSONParser();

	
	/**
	 * setting user details into specified url
	 * */
	protected String doInBackground(JSONObject... params) {
		String errormessage = null;
		try {
			JSONObject obj = params[0];
			JSONObject json = jParser.makeHttpRequest(url_signup, "POST", obj);

			int success = json.getInt("status");
			if (success == 1) {
				errormessage = json.getString("error");

			} else {
				errormessage = json.getString("error");
			}

		} catch (Exception e) {
			Log.i(TAG, "Exception in doInBackground :" + e.toString());

		}
		return errormessage;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	protected void onPostExecute(JSONObject result) {

	}
}
