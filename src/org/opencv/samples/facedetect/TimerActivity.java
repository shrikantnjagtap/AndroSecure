package org.opencv.samples.facedetect;

import java.util.Timer;
import java.util.TimerTask;

import org.opencv.samples.facedetect.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class TimerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_timer);
		//receive time interval from main activity 
		Intent intent = getIntent();
		long interval =(long) intent.getIntExtra(ControllerActivity.TIMER_EXPIRY,0);
		Timer t = new Timer();
		//test
		/*
		TextView txtText;
		txtText=(TextView) findViewById(R.id.editText1);
		txtText.setText("interval start:"+interval);
		setContentView(txtText);
		*/
		///////
		//return to mainActivity after given interval
		//Schedule a task for single execution after a specified delay.
	/*	Handler handler = new Handler();
		 t.schedule(new TimerTask() {
			 @Override
		        public void run() {
			 handler.post(new Runnable() {
                 public void run() {
                	
                 }
			 });
             
			 }},interval);
		 */
		 t.schedule(new TimerTask() {
		        @Override
		        public void run() {
		        	
		        ////create intent and return data to to caller of this activity
				    Intent data = new Intent();
				    data.putExtra("TIMER_EXPIRY_STATUS", "EXPIRED");				   
				    // Activity finished ok, return the data
				    setResult(RESULT_OK, data);	                     
             TimerActivity.this.finish();
		        }
		 }, interval);
		 
		// txtText.setText("interval22");setContentView(txtText);
		 //this.finish();
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timer, menu);
		return true;
	}

}
