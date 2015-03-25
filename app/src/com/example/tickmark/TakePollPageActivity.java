package com.example.tickmark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.JSON.JSONParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class TakePollPageActivity extends Activity {
	private static String insert_poll = "http://sunsimengkira.appspot.com/insert_poll";
	private static String poll_report = "http://sunsimengkira.appspot.com/poll_report";
	JSONParser jParser = new JSONParser();
	private Spinner spinner1;
	private String groupname=null;
	private EditText poll_question=null;
	private EditText option1=null;
	private EditText option2=null;
	private EditText option3=null;
	private EditText option4=null;
	private Button submit=null;
	private Button generate=null;
	String pollcount=null;
	String polldesc=null;
	String pollcourse=null;
	String pollid="";
	String course_return=null;
	private int prof_id = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_poll_page);
		
		Intent i = getIntent();
		prof_id = i.getIntExtra("pid", 0);
		
		spinner1 = (Spinner) findViewById(R.id.groupname);
		Bundle b=getIntent().getExtras();
		String c=b.getString("course");
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
		poll_question=(EditText)findViewById(R.id.Poll_question_professor);
		option1=(EditText)findViewById(R.id.option1);
		option2=(EditText)findViewById(R.id.option2);
		option3=(EditText)findViewById(R.id.option3);
		option4=(EditText)findViewById(R.id.option4);
		submit=(Button)findViewById(R.id.submit_poll);
		generate=(Button)findViewById(R.id.generatereport);
		generate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String op1=option1.getText().toString();
				String op2=option2.getText().toString();
				String que=poll_question.getText().toString();
				if(op1.equals("") || op2.equals("")||que.equals("")||pollid.equals(""))
				{
					Toast toast=Toast.makeText(getApplicationContext(), "Please submit Poll first!!!", Toast.LENGTH_SHORT); 
					toast.show();
				}
				else
				{
				new PollReport().execute();
				}
				
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
	              String op1=option1.getText().toString();
					String op2=option2.getText().toString();
					 if(op2.isEmpty() || op1.isEmpty())
			            {
			            	String optionvalidation="Please enter some value for option1 and option2";
			            	Toast toast=Toast.makeText(getApplicationContext(), optionvalidation, Toast.LENGTH_SHORT); 
							toast.show();
			            }
					 else
					 {
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
						 		pollresult();
					 }
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_poll_page, menu);
		return true;
	}
	
	public void pollresult(){
		new SubmitPollReport().execute();
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
	class SubmitPollReport extends AsyncTask<String, String, String> {
	
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
					jsonObject.put("course_name",groupname);
					jsonObject.put("question", poll_question.getText().toString());
		            jsonObject.put("prof_id", prof_id);
		            String op3=option3.getText().toString();
		            String op4=option4.getText().toString();
		            jsonObject.put("option1", option1.getText().toString());
		            jsonObject.put("option2", option2.getText().toString());
		            if(op3.equals(""))
		            jsonObject.put("option3","No value" );
		            else
		            	jsonObject.put("option3",op3 );	
		            if(op4.equals(""))
		            	jsonObject.put("option4","No value" );
		            else
		            	jsonObject.put("option4",op4 );	
		           
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JSONObject json = jParser.makeHttpRequest(insert_poll, "POST", jsonObject);
				Log.d("All Courses: ", json.toString());
				try {
					int success = json.getInt("success");
					if(success==1)
					{
							message=json.getString("message");	
							pollid=json.getString("pollid");
							course_return=json.getString("courseid");
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
			protected void onPostExecute(String message) {
				System.out.println(message);
				Toast toast=Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT); 
				toast.show();
			}

		}
	class PollReport extends AsyncTask<String, String, ArrayList<String>> {
		ArrayList<String> report=new ArrayList<String>();
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
		
		}

		/**
		 * getting All products from url
		 * */
		protected ArrayList<String> doInBackground(String... args) {
			
			String message=null;
			
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("course_id",course_return);
				jsonObject.put("prof_id",prof_id );
	            jsonObject.put("poll_id", pollid);
	           
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JSONObject json = jParser.makeHttpRequest(poll_report, "POST", jsonObject);
			Log.d("All Courses: ", json.toString());
			try {
				int success = json.getInt("success");
				if(success==1)
				{
					pollcourse=json.getString("course");
					pollcount=json.getString("poll_count");
					polldesc=json.getString("poll_description");
					report.add(pollcourse);
					report.add(pollcount);
					report.add(polldesc);
					}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return report;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(ArrayList<String> list) {
			if(pollcount.isEmpty() || pollcourse.isEmpty() || polldesc.isEmpty())
			{
				String message="No poll create so far!!!!";
				Toast toast=Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT); 
				toast.show();
			}
			else
			{
			Intent intent = new Intent(TakePollPageActivity.this,PollReportPageActivity.class);
			System.out.println(list.get(0));
			System.out.println(list.get(1));
			System.out.println(list.get(2));
			intent.putStringArrayListExtra("poll", list);
			
			startActivity(intent);
			}
		}

	}
}
