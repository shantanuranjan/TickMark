package com.example.tickmark;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.JSON.JSONParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class LeaderHomePageActivity extends Activity {
	private static String url_all_courses = "http://sunsimengkira.appspot.com/get_all_courses";
	private String coursename=null;
	private int prof_id =1 ;
	JSONParser jParser = new JSONParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_home_page);
		
		Intent i = getIntent();
		prof_id = i.getIntExtra("userid", 0);
		Log.i("check", prof_id+"");
		
		createGroup();
	    goTakeAttendance();
		goTakeAPoll();
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leader_home_page, menu);
		return true;
		
		
	}
	
	public void createGroup(){
		Button bn = (Button) findViewById(R.id.create_group);
		bn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LeaderHomePageActivity.this,
						LeaderGroupPageActivity.class);
				intent.putExtra("pid", prof_id);
				startActivity(intent);
			}
		});
	}
	
	public void goTakeAttendance(){
		Button bn = (Button) findViewById(R.id.go_take_attendance);
		bn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new LoadAllCourses().execute();
				
			}
		});
	}
	
	public void goTakeAPoll(){
		Button bn = (Button) findViewById(R.id.go_take_a_poll);
		bn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new LoadAllCoursesPoll().execute();
			}
		});
	}
	class LoadAllCourses extends AsyncTask<String, String, String> {
		
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
			String profid="1";
			JSONObject jsonObject = new JSONObject();
           try {
			jsonObject.put("profid", profid);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			JSONObject json = jParser.makeHttpRequest(url_all_courses, "POST", jsonObject);
			
			try {
				int success = json.getInt("success");
				if(success==1)
				{
					 coursename=json.getString("course"); 
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return coursename;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			
			Intent intent = new Intent(LeaderHomePageActivity.this,
					TakeAttendancePageActivity.class);
			intent.putExtra("course", file_url);
			intent.putExtra("pid", prof_id);
			startActivity(intent);
		}

	}
	class LoadAllCoursesPoll extends AsyncTask<String, String, String> {
		
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
			String profid="1";
			JSONObject jsonObject = new JSONObject();
           try {
			jsonObject.put("profid", profid);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			JSONObject json = jParser.makeHttpRequest(url_all_courses, "POST", jsonObject);
			
			try {
				int success = json.getInt("success");
				if(success==1)
				{
					 coursename=json.getString("course"); 
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return coursename;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			
			Intent intent = new Intent(LeaderHomePageActivity.this,
					TakePollPageActivity.class);
			intent.putExtra("course", file_url);
			intent.putExtra("pid", prof_id);
			
			startActivity(intent);
		}

	}
}
