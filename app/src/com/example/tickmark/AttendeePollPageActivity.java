package com.example.tickmark;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.JSON.JSONParser;

public class AttendeePollPageActivity extends Activity {
	String answer;
	
	private final String TAG = "AttendeePollPageActivity";
	private int prof_id;
	private String pollid ;
	private String question ;
	private String cha ;
	private String chb ;
	private String chc ;
	private String chd ;
	private boolean connected = false;
	
	private TextView gname;
	private TextView gquestion;
	private TextView ca;
	private TextView cb;
	private TextView cc;
	private TextView cd;
	
	private String cid;
	private String pollquestion;
	JSONParser jParser = new JSONParser();
	
	private static final String TAG_PID = "pid";
	private static final String TAG_COURSE_ID = "cid";
	private static final String TAG_OPT = "opt";
	private static final String TAG_POLL_ID = "pollid";
	
	private static String url_update_poll = "http://sunsimengkira.appspot.com/update_poll";
	private static String url_get_poll = "http://sunsimengkira.appspot.com/get_poll";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendee_poll_page);
		
		checkInternet(this);

		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {
		    connected = true;
		}

		try{
			Intent i = getIntent();
			String[] s = i.getStringArrayExtra("poll");
			cid = s[0];
			prof_id = Integer.parseInt(s[1]);
		}catch (Exception e){
			
		}
		
		gname = (TextView)findViewById(R.id.groupname);
		gquestion = (TextView)findViewById(R.id.groupquestion);
		ca = (TextView)findViewById(R.id.choice_a);
		cb = (TextView)findViewById(R.id.choice_b);
		cc = (TextView)findViewById(R.id.choice_c);
		cd = (TextView)findViewById(R.id.choice_d);
		
		
		if(connected){
			new GetPoll().execute();
			
			submitAnswer();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attendee_poll_page, menu);
		return true;
	}

	
	public void checkInternet(Context contex){
		ConnectivityManager cwjManager=(ConnectivityManager)contex.getSystemService(Context.CONNECTIVITY_SERVICE); 

		NetworkInfo info = cwjManager.getActiveNetworkInfo(); 

		  if (info != null && info.isAvailable()){ 
			  connected = true;
			  Log.i("check", "con");
		  }else{ 
			  Toast toast=Toast.makeText(getApplicationContext(), "Please connect to Internet first ! ", Toast.LENGTH_SHORT); 
				toast.show();
		  }
	}
	
	
	
	
	class GetPoll extends AsyncTask<String, String, String> {
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
	            jsonObject.put(TAG_COURSE_ID, cid);
	            
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

//            Log.i("check", jsonObject.toString());
			JSONObject json = jParser.makeHttpRequest(url_get_poll, "POST", jsonObject);
//			Log.i("check", json.toString());
			try {
				int success = json.getInt("success");
				if(success==1)
				{
					
					pollid=json.getString("pollid");
					question=json.getString("pollquestion");
					cha=json.getString("opta");
					chb = json.getString("optb");
					chc = json.getString("optc");
					chd = json.getString("optd");
					
				}
				else 
				{
					 message=json.getString("message");
//					 Log.i("check", message);
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
			gname.setText(cid);
			gquestion.setText(question);
			ca.setText(cha);
			cb.setText(chb);
		
			
			if(chc!="null"){
				cc.setText(chc);
			}
			else{
				RadioButton c = (RadioButton)findViewById(R.id.radio_c);
				c.setEnabled(false); 
			}
			if(chd!="null"){
				cd.setText(chd);
			}
			else{
				RadioButton d = (RadioButton)findViewById(R.id.radio_d);
				d.setEnabled(false); 
			}
			
		}

	}
	
	
	


	
	public void submitAnswer(){
		Button bn = (Button) findViewById(R.id.submit_poll_answer);
		bn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
//				Toast.makeText(getApplicationContext(), answer+"", Toast.LENGTH_SHORT).show();
				
				new UpdatePoll().execute();
				finish();
			}
		});
	}
	public void onRadioButtonClicked(View view){

	    boolean checked = ((RadioButton) view).isChecked();
	    	    switch(view.getId()) {
	        case R.id.radio_a:
	            if (checked)
	                answer = "count_a";
	            break;
	        case R.id.radio_b:
	            if (checked)
	                answer = "count_b";
	            break;
	        case R.id.radio_c:
	            if (checked)
	                answer = "count_c";
	            break;
	        case R.id.radio_d:
	            if (checked)
	                answer = "count_d";
	            break;
	    }
	}
	
	class UpdatePoll extends AsyncTask<String, String, String> {
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
	            jsonObject.put(TAG_COURSE_ID, cid);
	            jsonObject.put(TAG_POLL_ID, pollid);
	            jsonObject.put(TAG_OPT, answer);
	            
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
//            Log.i("check", jsonObject.toString());
            
            Log.i("check", jsonObject.toString());
			JSONObject json = jParser.makeHttpRequest(url_update_poll, "POST", jsonObject);
			Log.d("check", json.toString());
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
