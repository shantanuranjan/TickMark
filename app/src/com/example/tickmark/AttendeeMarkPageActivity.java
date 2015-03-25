package com.example.tickmark;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.JSON.JSONParser;
import com.example.helper.AsyncResponse;
import com.example.helper.GetProfLocation;
import com.example.helper.Savedatastudent;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AttendeeMarkPageActivity extends Activity implements AsyncResponse {
	private String course = null;
	private String profid;
	private int uid;
	private int aid;
	private double lat;
	private double lon;
	private double latti;
	private double longi;
	public GetProfLocation gtpr = new GetProfLocation();
	private TextView showcode;
	private TextView showcourse;
	private String attcode;
	Button bn ;
	EditText ed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendee_mark_page);
		showcode = (TextView) findViewById(R.id.showcodeview);
		showcourse = (TextView) findViewById(R.id.coursename);
bn = (Button)findViewById(R.id.generatecode1);
		Intent i = getIntent();
		uid = i.getIntExtra("uid", 0);
		course = i.getStringExtra("Courseid");
		showcourse.setText(course);
		profid = i.getStringExtra("Profid");
		 ed = (EditText) findViewById(R.id.typecodeeditbox);
		checkInternet(this);
//		checkGps(this);
		this.getLocation();
		try {
			JSONObject json = new JSONObject();

			json.put("courseid", course);
			json.put("profid", profid);
			//gtpr.transferResult = this;
			gtpr.execute(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {

				lat = location.getLatitude();
				lon = location.getLongitude();

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

		};

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 1000, locationListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attendee_mark_page, menu);
		return true;
	}

	public void checkInternet(Context contex) {
		ConnectivityManager cwjManager = (ConnectivityManager) contex
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = cwjManager.getActiveNetworkInfo();

		if (info != null && info.isAvailable()) {

		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Please connect to Internet first ! ", Toast.LENGTH_SHORT);
			toast.show();

		}
	}

	public void checkGps(Context context) {
		LocationManager locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Please turn on GPS ! ", Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	public void markAttendance() {
		
		JSONObject json = new JSONObject();
		try {
			json.put("sid", uid);

			json.put("aid", aid);
			new Savedatastudent().execute(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent i = new Intent(AttendeeMarkPageActivity.this,
				HomepageActivity.class);
		startActivity(i);
		finish();
		
	}

	/*@Override
	public void processFinish(JSONObject output) {
		if (output != null) {
			try {
				latti = output.getDouble("lat");
				longi = output.getDouble("lon");
				attcode = output.getString("code");
				aid = output.getInt("aid");

				if (((latti - lat) <= 10) && (longi - lon) <= 10) {
					showcode.setText(output.getString("code"));
					Button bn = (Button) findViewById(R.id.generatecode1);
					final EditText ed = (EditText) findViewById(R.id.typecodeeditbox);
					if (ed.getText().toString().equals(attcode)) {

						bn.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						});
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
	public class GetProfLocation extends AsyncTask<JSONObject, String, JSONObject> {
		private static final String TAG = "GetProfLocation";
		private static final String url_login = "http://sunsimengkira.appspot.com/chk_location";
		public AsyncResponse transferResult;
		JSONParser jParser = new JSONParser();
		JSONObject json;
		@Override
		protected JSONObject doInBackground(JSONObject... params) {
			try {
				JSONObject obj = params[0];
				
				json = jParser.makeHttpRequest(url_login, "POST", obj);
				Log.i(TAG, obj.toString());

			} catch (Exception e) {
				Log.i(TAG, "Exception in doInBackground :" + e.toString());

			}
			return json;
		}

		
		@Override
		protected void onPostExecute(JSONObject output) 
		{
			if (output != null) {
				try {
					latti = output.getDouble("lat");
					longi = output.getDouble("lon");
					attcode = output.getString("code");
					aid = output.getInt("aid");
					System.out.println("dff"+aid);

					if (((latti - lat) <= 1) && (longi - lon) <= 1) {
						showcode.setText(output.getString("code"));
						System.out.println("savestudent");
						
						 {

							bn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									if(ed.getText().toString().equals(attcode))
									{
									JSONObject json = new JSONObject();
									try {
										json.put("sid", uid);

										json.put("aid", aid);
									new Savedatastudent().execute(json);
										
										
										
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
								}
							});
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void processFinish(JSONObject output) {
		// TODO Auto-generated method stub
		
	}
	
	public class Savedatastudent extends AsyncTask<JSONObject, Void, String> {
		private static final String TAG = "GetProfLocation";
		private static final String url_login = "http://sunsimengkira.appspot.com/save_data_student";
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
			Intent i = new Intent(AttendeeMarkPageActivity.this, HomepageActivity.class);
			startActivity(i);
		}

	}

}


