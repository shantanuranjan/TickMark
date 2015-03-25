package com.example.tickmark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.JSON.JSONParser;
import com.example.helper.AsyncResponse;
import com.example.helper.CheckTypeOfUser;
import com.example.helper.SelectClass;
import com.example.helper.SelectProf;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class HomepageActivity extends Activity implements AsyncResponse {
	public int userid;
	public CheckTypeOfUser chk = new CheckTypeOfUser();
	public SelectClass sel = new SelectClass();
	public SelectProf selp = new SelectProf();
	private Spinner spinner1 = null;
	private Spinner spinner2 = null;
	private String courseSelected;
	private int courseSelectedId;
	private String courseidtopass;
	private String profSelected;
	private int profSelectedId;
	private String profidtopass;
	List<String> list;
	List<String> listcourseid;

	ArrayAdapter<String> dataAdapter1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
		spinner1 = (Spinner) findViewById(R.id.course_spinner);
		spinner2 = (Spinner) findViewById(R.id.professor_spinner);
		chk.transferResult = this;
		// sel.transferResult = this;
		// selp.transferResult = this;
		// loadAllCourses();

		Intent i = getIntent();
		userid = i.getIntExtra("userid", 0);
		String coursename = i.getStringExtra("coursename");
		String courseid = i.getStringExtra("courseid");
		String replace = courseid.replace("[", "");
		String replace1 = replace.replace("]", "");
		String replace2 = replace1.replace("\"", "");

		listcourseid = new ArrayList<String>(Arrays.asList(replace2
				.split(",")));
		
		
		String profname = i.getStringExtra("profname");
		String profid = i.getStringExtra("profid");
		String replace3 = profid.replace("[", "");
		String replace4 = replace3.replace("]", "");
		String replace5 = replace4.replace("\"", "");

		List<String> listprofid = new ArrayList<String>(Arrays.asList(replace5
				.split(",")));

		addGroupName(coursename);
		addProfName(profname);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				courseSelected = spinner1.getSelectedItem().toString();
				
//				int i = 0;
//				for(String s : list){
//					if(s.equals(courseSelected))
//					break;
//					i++;
//				}
//				courseSelectedId =Integer.parseInt(listcourseid.get(i)) ;
				
				
				courseSelectedId = spinner1.getSelectedItemPosition();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		courseidtopass = listcourseid.get(courseSelectedId);

		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				profSelected = spinner2.getSelectedItem().toString();
				profSelectedId = spinner2.getSelectedItemPosition();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		profidtopass = listprofid.get(profSelectedId);

	}

	public void chooseLeader(View v) {
		JSONObject json = new JSONObject();
		try {
			json.put("uid", userid);

			chk.execute(json);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void chooseattendance(View v) {
		Intent i = new Intent(this, AttendeeMarkPageActivity.class);
		i.putExtra("uid", userid);
		i.putExtra("Courseid",courseidtopass);
		
		Log.i("check", courseSelected+"");
		i.putExtra("Profid", profidtopass);
		startActivity(i);
	}

	public void choosePoll(View v) {
		Intent i = new Intent(this, AttendeePollPageActivity.class);
		String[] s = new String[2];
		s[0] = courseidtopass;
		s[1] = profidtopass;
		i.putExtra("poll", s);
		startActivity(i);
	}

	public void loadAllCourses() {
		try {
			new SelectClass().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadAllProfessors(String course) {
		JSONObject json = new JSONObject();
		try {
			json.put("coursename", course);

			selp.execute(json);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.homepage, menu);
		return true;
	}

	@Override
	public void processFinish(JSONObject output) {
		try {

			int success;
			int utype;
			if (output != null) {
				String ident = output.getString("Identifier");
				if (ident.equals("CheckTypeOfUser")) {

					success = output.getInt("status");
					utype = output.getInt("utype");
					if ((success == 1) && (utype == 1)) {

						Intent i = new Intent(this,
								LeaderHomePageActivity.class);
						i.putExtra("userid", userid);
						startActivity(i);
					} else {
						Toast.makeText(this, "Insufficient privileges!!",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addGroupName(String coursename) {

		String replace = coursename.replace("[", "");
		String replace1 = replace.replace("]", "");
		String replace2 = replace1.replace("\"", "");

		 list = new ArrayList<String>(Arrays.asList(replace2
				.split(",")));
		System.out.println(list.get(0));
		System.out.println(list.get(1));
		System.out.println(list.get(2));

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);

	}

	public void addProfName(String profname) {

		String replace = profname.replace("[", "");
		String replace1 = replace.replace("]", "");
		String replace2 = replace1.replace("\"", "");

		List<String> list = new ArrayList<String>(Arrays.asList(replace2
				.split(",")));
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter1);
	}

}
