package com.example.tickmark;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.JSON.JSONParser;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TakeAttendancePageActivity extends Activity {
	private Spinner spinner1;
	private Button Generate_Code=null;
	private Button End=null;
	private TextView code=null;
	private TextView timerValue;
	private String generatecode=null;
	private String groupname=null;
	double lat, lon;
	private int prof_id = 0; 
	private long startTime = 0L;
	private Button pauseButton;
	private Button startButton;
	private Handler customHandler = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	private static final String TAG_CODE = "generated_code";
	private static final String TAG_COURSE = "course_name";
	private static final String TAG_PID = "pid";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	private static String url_update_courses = "http://sunsimengkira.appspot.com/update_course";
	private static String url_get_student = "http://sunsimengkira.appspot.com/student_present";
	JSONParser jParser = new JSONParser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_attendance_page);
		
		Intent i = getIntent();
		prof_id = i.getIntExtra("pid", 0);
		
		
		checkInternet(this);
		checkGps(this);
		
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				
				lat = (location.getLatitude());
				lon = (location.getLongitude());
				System.out.println(lat+""+lon);
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
	
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 100, locationListener);
		
		
		Generate_Code=(Button)findViewById(R.id.generatecode);
		End=(Button)findViewById(R.id.end);
		spinner1 = (Spinner) findViewById(R.id.groupname);
		pauseButton = (Button) findViewById(R.id.pauseButton);
		startButton=(Button)findViewById(R.id.startButton);
		timerValue = (TextView) findViewById(R.id.timerValue);
		Bundle b=getIntent().getExtras();
		String c=b.getString("course");
		System.out.println(c);
		addGroupName(c);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) 
			{
				groupname=spinner1.getSelectedItem().toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
		
			}
		});
		startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyStringRandomGen msr = new MyStringRandomGen();
				code=(TextView)findViewById(R.id.codeText);
				generatecode=msr.generateRandomString();
				code.setText(generatecode);
				startTime = SystemClock.uptimeMillis();
				customHandler.postDelayed(updateTimerThread, 0);
			}
		});
		/*Generate code button*/
		
		Generate_Code.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
					AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
					
	              builder.setMessage("Saving Data...");
	              builder.setCancelable(true);

	              final AlertDialog dlg = builder.create();

	              dlg.show();

	              final Timer t = new Timer();
	              t.schedule(new TimerTask() {
	                  public void run() {
	                      dlg.dismiss(); // when the task active then close the dialog
	                      t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
	                  }
	              }, 1000);
	            if(generatecode!=null)
				storeGeneratedCode();
				else if(generatecode==null)
				{
					Context context = getApplicationContext();
					CharSequence text = "No Code Generated so far!";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
					
			}
		});
		/*pause button*/
		pauseButton.setOnClickListener(new View.OnClickListener() {
				 
				            public void onClick(View view) {
				 
				                timeSwapBuff = 0;
				                customHandler.removeCallbacks(updateTimerThread);
				 
				            }
				        });
		/*send data to server*/
		End.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				new GenerateReport().execute();
			}
		});		
		
		
	}
	
	public void checkInternet(Context contex){
		ConnectivityManager cwjManager=(ConnectivityManager)contex.getSystemService(Context.CONNECTIVITY_SERVICE); 

		NetworkInfo info = cwjManager.getActiveNetworkInfo(); 

		  if (info != null && info.isAvailable()){ 

		  }else{ 
			  Toast toast=Toast.makeText(getApplicationContext(), "Please connect to Internet first ! ", Toast.LENGTH_SHORT); 
				toast.show();
				
		  }
	}
	public void checkGps(Context context){
		 LocationManager locationManager =   
	                ((LocationManager)context.getSystemService(Context.LOCATION_SERVICE));  
	        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
	        	Toast toast=Toast.makeText(getApplicationContext(), "Please turn on GPS ! ", Toast.LENGTH_SHORT); 
				toast.show();
	        }

	}
	private Runnable updateTimerThread = new Runnable() {
			 
			        public void run() {
			 
			            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			 
			            updatedTime = timeSwapBuff + timeInMilliseconds;
			 
			            int secs = (int) (updatedTime / 1000);
			            int mins = secs / 60;
			            secs = secs % 60;
			            int milliseconds = (int) (updatedTime % 1000);
			            timerValue.setText("" + mins + ":"
			                    + String.format("%02d", secs) + ":"
			                    + String.format("%03d", milliseconds));
			            customHandler.postDelayed(this, 0);
			        }
			 
			    };
	/*Stores generated code alongwith group name,id,leader id to Group_Details table*/
	public void storeGeneratedCode()
	{
		
		
	new UpdateCourses().execute();
	//new upload().execute();	
         
	}
	
	/*Add group name from leader_group_details table*/
	public void addGroupName(String coursename) { 
		
		
		String replace = coursename.replace("[","");
        String replace1 = replace.replace("]","");
        String replace2=replace1.replace("\"", "");
        List<String> list = new ArrayList<String>(Arrays.asList(replace2.split(",")));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);	
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
		
	  }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_attendance_page, menu);
		return true;
	}
	public class MyStringRandomGen {
		 
	    private static final String CHAR_LIST ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	    private static final int RANDOM_STRING_LENGTH = 4;
	     
	    /**
	     * This method generates random string
	     * @return
	     */
	    public String generateRandomString(){
	         
	        StringBuffer randStr = new StringBuffer();
	        for(int i=0; i<RANDOM_STRING_LENGTH; i++){
	            int number = getRandomNumber();
	            char ch = CHAR_LIST.charAt(number);
	            randStr.append(ch);
	        }
	        return randStr.toString();
	    }
	     
	    /**
	     * This method generates random numbers
	     * @return int
	     */
	    private int getRandomNumber() {
	        int randomInt = 0;
	        Random randomGenerator = new Random();
	        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
	        if (randomInt - 1 == -1) {
	            return randomInt;
	        } else {
	            return randomInt - 1;
	        }
	    }
	}
	class UpdateCourses extends AsyncTask<String, String, String> {
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
			
			String Ucode = code.getText().toString();
            String message=null;
            
			System.out.println("latitude"+lat+"longitude"+lon);
			groupname = groupname.replace("\"", "");
         
            JSONObject jsonObject = new JSONObject();
            try {
				jsonObject.put(TAG_CODE,Ucode);
				jsonObject.put(TAG_COURSE, groupname);
	            jsonObject.put(TAG_PID, prof_id);
	            System.out.println(lat);
	            System.out.println(lon);
	            jsonObject.put(TAG_LATITUDE, (lat));
	            jsonObject.put(TAG_LONGITUDE, (lon));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
			JSONObject json = jParser.makeHttpRequest(url_update_courses, "POST", jsonObject);
			Log.d("All Courses: ", json.toString());
			try {
				int success = json.getInt("success");
				if(success==1)
				{
					
					  message=json.getString("message");
					System.out.println(message);
					 
				}
				else 
				{
					 message=json.getString("message");
					 System.out.println(message);
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
class GenerateReport extends AsyncTask<String, String, String> {
	private ProgressDialog pDialog1;
	String prof_id ="1";
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			pDialog1 = new ProgressDialog(TakeAttendancePageActivity.this);
            pDialog1.setMessage("Generating Student List ...");
            pDialog1.setIndeterminate(false);
            pDialog1.setCancelable(true);
            pDialog1.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
		
			String student_present = null;
			
			JSONObject jsonObject1 = new JSONObject();
			
			
            try {
            	jsonObject1.put(TAG_COURSE, groupname);
				jsonObject1.put(TAG_PID, prof_id);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JSONObject json = jParser.makeHttpRequest(url_get_student, "POST", jsonObject1);
			Log.d("All Courses: ", json.toString());
			try {
				int success = json.getInt("success");
				if(success==1)
				{
					student_present=json.getString("present_name"); 
				
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return student_present;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String student) {
			System.out.println(student);
			Intent intent = new Intent(TakeAttendancePageActivity.this,
					AttendanceReportPageActivity.class);
			intent.putExtra("name", student);
			intent.putExtra("course", groupname);
			intent.putExtra("professorid", prof_id);
			startActivity(intent);
		}

	}




}
