package com.example.tickmark;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.JSON.JSONParser;
import com.example.tickmark.TakeAttendancePageActivity.UpdateCourses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LeaderGroupPageActivity extends Activity {
	private String courseName="";
	private String courseId = "";
	private String courseTime = "";
	private final String TAG = "LeaderGroupPageActivity";
	private int prof_id = 0;
	JSONParser jParser = new JSONParser();
	
	private static final String TAG_COURSE_NAME = "course_name";
	private static final String TAG_PID = "pid";
	private static final String TAG_COURSE_ID = "cid";
	private static final String TAG_COURSE_TIME = "ctime";
	private static String url_insert_group = "http://sunsimengkira.appspot.com/insert_group";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_group_page);
		Intent i = getIntent();
		prof_id = i.getIntExtra("pid", 0);
		
//		try{
//			Intent i = getIntent();
//			prof_id = i.getStringExtra("pid");
//		}catch (Exception e){
//			
//		}

		submitGroup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leader_group_page, menu);
		return true;
	}
	
	public void submitGroup(){
		Button bn = (Button) findViewById(R.id.submit_group);
		bn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getInfo();
				
				
				//store info
				
				new InsertGroup().execute();
				
				
				finish();
			}
		});
	}
	
	public void getInfo(){
		courseName = ((EditText)findViewById(R.id.enter_course_name)).getText().toString();
		courseId = ((EditText)findViewById(R.id.enter_course_id)).getText().toString();
		courseTime = ((EditText)findViewById(R.id.enter_course_time)).getText().toString();
		Log.i(TAG, courseName);
		Log.i(TAG, courseId);
		Log.i(TAG, courseTime);
	}
	
	class InsertGroup extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			  
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			
            String message=null;
            
         
            JSONObject jsonObject = new JSONObject();
            try {
            	jsonObject.put(TAG_PID,prof_id);
				jsonObject.put(TAG_COURSE_NAME, courseName );
	            jsonObject.put(TAG_COURSE_ID, courseId);
	            jsonObject.put(TAG_COURSE_TIME, courseTime);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            Log.i("check", jsonObject.toString());
            
			JSONObject json = jParser.makeHttpRequest(url_insert_group, "POST", jsonObject);
			Log.i("check", json.toString());
			try {
				int success = json.getInt("success");
				if(success==1)
				{
					
					message=json.getString("message");
					Log.i("check", message);
					 
				}
				else 
				{
					 message=json.getString("message");
					 Log.i("check", message);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return message;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String message1) {
			System.out.println(message1);
			Toast toast=Toast.makeText(getApplicationContext(), message1, Toast.LENGTH_SHORT); 
			toast.show();
		}

	}

}
