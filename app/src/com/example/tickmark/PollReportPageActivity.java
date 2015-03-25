package com.example.tickmark;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PollReportPageActivity extends Activity {
	Button pollreport=null;
	TextView question=null;
	TextView coursename=null;
	TextView option1percent=null;
	TextView option2percent=null;
	TextView option3percent=null;
	TextView option4percent=null;
	
	
	TextView option1=null;
	TextView option2=null;
	TextView option3=null;
	TextView option4=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poll_report_page);
		pollreport=(Button)findViewById(R.id.ok);
		question=(TextView)findViewById(R.id.question);
		coursename=(TextView)findViewById(R.id.poll_groupname);
		question=(TextView)findViewById(R.id.question);
		
		option1=(TextView)findViewById(R.id.option1_Label);
		option2=(TextView)findViewById(R.id.option2_Label);
		option3=(TextView)findViewById(R.id.option3_Label);
		option4=(TextView)findViewById(R.id.option4_Label);
		
		
		option1percent=(TextView)findViewById(R.id.option1_Percent);
		option2percent=(TextView)findViewById(R.id.option2_Percent);
		option3percent=(TextView)findViewById(R.id.option3_Percent);
		option4percent=(TextView)findViewById(R.id.option4_Percent);
		
		ArrayList<String> b =getIntent().getStringArrayListExtra("poll");
		String course=b.get(0);
		String poll_count=b.get(1);
		String poll_desc=b.get(2);
	
		String replace = course.replace("[\"","");
        String replace1 = replace.replace("\"]","");
        coursename.setText(replace1);
        
        String replace2 = poll_desc.replace("[[","");
        String replace3 = replace2.replace("]]","");
        String replace4 = replace3.replace("\"","");
        List<String> myList = new ArrayList<String>(Arrays.asList(replace4.split(",")));
        question.setText(myList.get(0));
        option1.setText(myList.get(1));
        option2.setText(myList.get(2));
        if(myList.size()>3)
        {
        	if(myList.get(3)!="null")
        option3.setText(myList.get(3));
        	if(myList.size()>4)
        			option4.setText(myList.get(4));
        }
        
        
        String replace5 = poll_count.replace("[[","");
        String replace6 = replace5.replace("]]","");
        String replace7 = replace6.replace("\"","");
        List<String> myList1 = new ArrayList<String>(Arrays.asList(replace7.split(",")));
        
        float op1=Float.parseFloat(myList1.get(0));
        float op2=Float.parseFloat(myList1.get(1));
        float op3=Float.parseFloat(myList1.get(2));
        float op4=Float.parseFloat(myList1.get(3));
        if(op1==0.0 && op2==0.0 && op3==0.0 && op4==0.0)
        	
        {
        	
			   option1percent.setText(String.valueOf(op1)+"%");
		        option2percent.setText(String.valueOf(op2)+"%");
		        if(myList.size()>3)
		        {
		        option3percent.setText(String.valueOf(op3)+"%");
		        	if(myList.size()>4)
		        		option4percent.setText(String.valueOf(op4)+"%");
		        }
        }
        else
        {
        System.out.println(op1 + ""+op2+""+op3+""+op4);
        op1= ((op1*100)/(op1+op2+op3+op4));
        op2=((op2*100)/(op1+op2+op3+op4));
        op3=((op3*100)/(op1+op2+op3+op4));
        op4=((op4*100)/(op1+op2+op3+op4));
        
        option1percent.setText(String.valueOf(op1)+"%");
        option2percent.setText(String.valueOf(op2)+"%");
        if(myList.size()>3)
        {
        option3percent.setText(String.valueOf(op3)+"%");
        		if(myList.size()>4)
        			option4percent.setText(String.valueOf(op4)+"%");
        }
        }
		pollreport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent poll=new Intent(PollReportPageActivity.this,LeaderHomePageActivity.class);
				startActivity(poll);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poll_report_page, menu);
		return true;
	}
	public void show_report()
	{
		
	}

}
