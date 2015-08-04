package org.opencv.samples.facedetect;


import java.io.IOException;
import java.io.OutputStreamWriter;

import org.opencv.samples.facedetect.R;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class AlarmActivity extends Activity {
	MediaPlayer mp;
	boolean keep;
	String path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		
		Intent intent = getIntent();
		keep = intent.getExtras().getBoolean("keep");
		AlarmEntity alarm =(AlarmEntity) intent.getSerializableExtra("alarmEntity");
		path=alarm.path;
		
		Log.i("AlarmActivity","path="+path);
		if(keep!=false)
		{
			 mp=new MediaPlayer();
			 
			 try{  
				 mp.setDataSource(alarm.path);//mp.setDataSource("/sdcard/Music/maine.mp3");//Write your location here  
		            mp.prepare();  
		            mp.start();  //start alarm.
		            mp.setLooping(true);//Play till stop button pressed
		              
		        }
			 catch(Exception e)
		        {
				 Toast t = Toast.makeText(getApplicationContext(),
							"EXCEPTION OCCURED-->CLOSING ALARM!!",
							Toast.LENGTH_SHORT);
					t.show();
				//write to file
					writeTofile("\n"+getApplicationContext()+e.getMessage());
					
					//return control to main
					setResult(10000, null);AlarmActivity.this.finish();//restart the voice activity from mainactivity
		        }  
	
		}
			}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm, menu);
		return true;
	}
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first
	    Log.i("alarm","onStop");
	    mp.stop();
	    AlarmActivity.this.finish();
	    }
	public void stopalarm(View view)
	{
		try
		{
		mp.stop();		
		
		Intent data2 = new Intent();
		   Log.i("alarmActivity","stop");
		    data2.putExtra("RESULT_ALARMACTIVITY", "success");
		    
		    // Activity finished ok, return the data
		    setResult(RESULT_OK, data2);
		    Toast t = Toast.makeText(getApplicationContext(),
						"ALARM CLOSED!!!",
						Toast.LENGTH_LONG);
				t.show();
				
		    this.finish();//CLOSE THIS ACTIVITY 
		}
		catch(Exception ex)
		{
			//write to file
			writeTofile("\n"+getApplicationContext()+ex.getMessage());
			
			//return control to main
			setResult(10000, null);AlarmActivity.this.finish();//restart the voice activity from mainactivity
		}
		
	}
	public void writeTofile(String str)
    {
		OutputStreamWriter outputStreamWriter=null;
		String eol = System.getProperty("line.separator");
		 try {
	           outputStreamWriter = new OutputStreamWriter(openFileOutput("log", Context.MODE_APPEND));
	           outputStreamWriter.write(eol+str);
	           outputStreamWriter.close();
	           
	       }
	       catch (IOException e) {
	    	   Toast t = Toast.makeText(getApplicationContext(),
						"EXCEPTION OCCURED-->CLOSING ALARM!!",
						Toast.LENGTH_SHORT);
				t.show();
				setResult(10000, null);AlarmActivity.this.finish();//restart the voice activity from mainactivity
				
	       } finally {
	           if (outputStreamWriter != null) {
	               try {
	               	outputStreamWriter.close();
	               } catch (IOException e) {
	               	
	            	   setResult(10000, null);AlarmActivity.this.finish();//restart the voice activity from mainactivity
	               }
	               }
	             }		
    
    }
	
	
}

