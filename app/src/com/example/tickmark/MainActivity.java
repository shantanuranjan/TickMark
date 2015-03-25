package com.example.tickmark;

import com.example.JSON.JSONParser;
import com.example.helper.*;

import org.json.JSONException;
import org.json.JSONObject;






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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity implements AsyncResponse{
	private static final String TAG = "MainActivity";
	public UserSignup use;
	public UserLogin used;
	String coursename;
	String courseid;
	String profname;
	String pid;
	int user_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_main);

		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
	}

	public void onbuttonclick(View v) {

		try {
			Button b = (Button) v;
			switch (b.getId()) {
			case R.id.signupbutton: {
				LinearLayout layout1 = (LinearLayout) findViewById(R.id.hiddenview_1);
				LinearLayout layout2 = (LinearLayout) findViewById(R.id.hiddenview_2);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				layout1.setLayoutParams(params);
				layout1.setVisibility(View.VISIBLE);
				layout2.setVisibility(View.INVISIBLE);
				break;
			}
			case R.id.loginbutton: {
				LinearLayout layout1 = (LinearLayout) findViewById(R.id.hiddenview_1);
				LinearLayout layout2 = (LinearLayout) findViewById(R.id.hiddenview_2);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				layout2.setLayoutParams(params);
				layout1.setVisibility(View.GONE);
				layout2.setVisibility(View.VISIBLE);
				break;
			}
			case R.id.registerbutton: {
				EditText editTextfirstName = (EditText) findViewById(R.id.firstnameeditbox);
				String firstname = editTextfirstName.getText().toString();
				EditText editTextlastName = (EditText) findViewById(R.id.lastnameeditbox);
				String lastname = editTextlastName.getText().toString();
				EditText editTextemail = (EditText) findViewById(R.id.emailideditbox);
				String email = editTextemail.getText().toString();
				EditText editTextpassword = (EditText) findViewById(R.id.passwordeditbox);
				String password = editTextpassword.getText().toString();
				RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radiochoice);

				int selectedradio = radiogroup.getCheckedRadioButtonId();
				RadioButton radiobutton = (RadioButton) findViewById(selectedradio);

				
				if (radiobutton.getText().equals("Student"))
					user_type = 0;
				else
					user_type = 1;

				if (firstname.isEmpty() || lastname.isEmpty()
						|| email.isEmpty() || password.isEmpty()) {
					Toast.makeText(this, "Please enter all fields",
							Toast.LENGTH_LONG).show();
				} else {
					JSONObject json = new JSONObject();
					json.put("firstname", firstname);
					json.put("lastname", lastname);
					json.put("emailname", email);
					json.put("passwordname", password);
					json.put("typeUser", user_type);
					
					
					if (isConnected()) {
						use = new UserSignup();
						use.execute(json);
						Toast.makeText(this, "New account created!",
								Toast.LENGTH_SHORT).show();
					}
				}

				break;
			}
			case R.id.Enterbutton: {
				EditText editTextemail = (EditText) findViewById(R.id.loginemaileditbox);
				String email = editTextemail.getText().toString();
				EditText editTextpassword = (EditText) findViewById(R.id.loginpswdeditbox);
				String password = editTextpassword.getText().toString();

				if (email.isEmpty() || password.isEmpty()) {
					Toast.makeText(this, "Please enter all fields",
							Toast.LENGTH_LONG).show();
				} else {
					JSONObject json = new JSONObject();
					json.put("emailname", email);
					json.put("passwordname", password);

					if (isConnected()) {
						used = new UserLogin();
						used.transferResult = this;
						used.execute(json);
						Toast.makeText(this, "Login Successful!",
								Toast.LENGTH_SHORT).show();
					}
					break;
				}
			}
			}
		} catch (JSONException e) {
			Log.i(TAG, "Exception in main activity  :" + e.toString());
			Toast.makeText(this, "Exception in showStatus : " + e.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean isConnected() {
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			return ni != null && ni.isConnected();
		} catch (Exception e) {
			Log.i(TAG, "Exception in isConnected :" + e.toString());
			Toast.makeText(this, "Exception in isConnected : " + e.toString(),
					Toast.LENGTH_LONG).show();
		}
		return false;
	}

	@Override
	public void processFinish(JSONObject output) {

		try {
			
			if (output != null) {
				int success;

				success = output.getInt("success");

				if (success == 1) {
					int userid = output.getInt("uid");
					coursename = output.getString("course");
					
					courseid=output.getString("courseid");
					
					profname = output.getString("profname");
					
					pid=output.getString("profid");
					Intent i = new Intent(this, HomepageActivity.class);
					i.putExtra("userid", userid);
					i.putExtra("coursename", coursename);
					i.putExtra("courseid", courseid);
					i.putExtra("profname", profname);
					i.putExtra("profid", pid);
					startActivity(i);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*public class SelectClass extends AsyncTask<Void, Void, JSONObject> {
		private static final String TAG = "SelectClass";
		private static final String url_login = "http://192.168.56.101:80/test/get_courses.php";
		public AsyncResponse transferResult;
		JSONParser jParser = new JSONParser();
		JSONObject json;

		@Override
		protected JSONObject doInBackground(Void... params) {

			try {
				JSONObject obj = new JSONObject();
				System.out.println("shantanu");
				json = jParser.makeHttpRequest(url_login, "POST", obj);
				
				json.put("Identifier", "SelectClass");
				//String c=json.toString();
				//System.out.println(c);
				//Log.i(TAG, json.toString());

			} catch (Exception e) {
				Log.i(TAG, "Exception in doInBackground :" + e.toString());

			}
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject result) 
		{	try {
			coursename = result.getString("course");
		
				courseid=result.getString("courseid");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}*/
}
