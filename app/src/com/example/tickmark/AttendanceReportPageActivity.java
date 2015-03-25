package com.example.tickmark;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.JSON.JSONParser;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AttendanceReportPageActivity extends Activity {
	Button report=null;
	//static String TAG = "ExelLog";
	private TextView course1=null;
	private static String url_download_student = "http://sunsimengkira.appspot.com/download";
	String name="";
	String course="";
	String profid="";
	JSONParser jParser = new JSONParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance_report_page);
   Bundle b=getIntent().getExtras();
   course=b.getString("course");
   name=b.getString("name");
   profid=b.getString("professorid");
 
  course1 = (TextView) findViewById(R.id.group_attendance1);
  course1.setText(course);
  report=(Button)findViewById(R.id.generate_report1);
  report.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		
        builder.setMessage("Downloading Data...");
        builder.setCancelable(true);

        final AlertDialog dlg = builder.create();

        dlg.show();

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dlg.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 2000);
	new DownloadReport().execute();	
	}
});
  show();
  
	}
	public void show(){
		String replace = name.replace("[","");
        String replace1 = replace.replace("]","");
        String replace2=replace1.replace("\"", "");
        List<String> list = new ArrayList<String>(Arrays.asList(replace2.split(",")));
        
        TextView t = (TextView) findViewById(R.id.students_list);
        String res = "";
        for(String tmp: list){
        	res += tmp+"\n";
   
        }
   	  t.setText(res);
	}
	
	
	class DownloadReport extends AsyncTask<String, String, String> {
		private ProgressDialog pDialog1;
		private String filename="";
		private String email1="";
			/**
			 * Before starting background thread Show Progress Dialog
			 * */
		ProgressDialog dialog = null;
			@Override
			protected void onPreExecute() {
				
			}

			/**
			 * getting All products from url
			 * */
			protected String doInBackground(String... args) {
				
				String replace = name.replace("[","");
		        String replace1 = replace.replace("]","");
		        String replace2=replace1.replace("\"", "");
		        List<String> list = new ArrayList<String>(Arrays.asList(replace2.split(",")));
				JSONObject jsonObject1 = new JSONObject();
				String message="";
				
	            try {
	            	System.out.println(course);
	            	System.out.println(profid);
	            	jsonObject1.put("student", list);
	            	jsonObject1.put("course", course);
	            	jsonObject1.put("prof_id", profid);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JSONObject json = jParser.makeHttpRequest(url_download_student, "POST", jsonObject1);
				Log.d("All Courses: ", json.toString());
				try {
					int success = json.getInt("success");
					filename=json.getString("filename");
					email1=json.getString("professor_id");
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
			protected void onPostExecute(String message) {
				
				String DownloadUrl = "http://192.168.56.101:80/test/"+filename;
			     String fileName = filename;
			     Uri URI = null;
			     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			     StrictMode.setThreadPolicy(policy);
			     try {
			         File root = android.os.Environment.getExternalStorageDirectory();
			         File dir = new File(root.getAbsolutePath() + "/mnt/sdcard/");
			         if(dir.exists() == false){
			              dir.mkdirs();  
			         }
			        System.out.println(dir);
			         URL url = new URL(DownloadUrl);
			         File file = new File(dir,fileName);

			         long startTime = System.currentTimeMillis();
			         Log.d("DownloadManager" , "download url:" +url);
			         Log.d("DownloadManager" , "download file name:" + fileName);

			         URLConnection uconn = url.openConnection();
			         uconn.setReadTimeout(2000);
			         uconn.setConnectTimeout(2000);

			         InputStream is = uconn.getInputStream();
			         BufferedInputStream bufferinstream = new BufferedInputStream(is);

			         ByteArrayBuffer baf = new ByteArrayBuffer(5000);
			         int current = 0;
			         while((current = bufferinstream.read()) != -1){
			             baf.append((byte) current);
			         }

			         FileOutputStream fos = new FileOutputStream( file);
			         fos.write(baf.toByteArray());
			         fos.flush();
			         fos.close();
			         Log.d("DownloadManager" , "download ready in" + ((System.currentTimeMillis() - startTime)/1000) + "sec");
			         int dotindex = fileName.lastIndexOf('.');
			         if(dotindex>=0){
			             fileName = fileName.substring(0,dotindex);

			     }
			     }
			     catch(IOException e) {
			         Log.d("DownloadManager" , "Error:" + e);
			     }
			     System.out.println(email1);
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[]{email1});		  
				email.putExtra(Intent.EXTRA_SUBJECT, "Attendance Report");
				email.putExtra(Intent.EXTRA_TEXT, "message");
				File root = android.os.Environment.getExternalStorageDirectory();
				File dir = new File(root.getAbsolutePath() + "/mnt/sdcard/");
				URI = Uri.parse("file://" + dir+filename);
				email.putExtra(Intent.EXTRA_STREAM, URI);
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email, "Choose an Email client :"));
				
			}
			  

	}
}
