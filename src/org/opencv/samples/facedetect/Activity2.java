package org.opencv.samples.facedetect;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

public class Activity2 extends Activity {
	boolean keep;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra(ControllerActivity.EXTRA_MESSAGE);
		 boolean keep = intent.getExtras().getBoolean("keep");
		if(keep!=false)
		{
		TextView t=new TextView(this);
		 t.setTextSize(40);
		    t.setText(message);
		    setContentView(t);
		    
		    //loop till stopped by timer in main activity
		    Log.i("activity2","infinite loop in activity2");
		    int k=1;
		    while(k==1);
		    	//++k;
		    
		    ////create intent and return data to to caller of this activity
		    Intent data = new Intent();
		    Log.i("activity2","in activity2");
		    data.putExtra("returnKey1", "Swinging on a star. ");
		    data.putExtra("returnKey2", "You could be better then you are. ");
		    // Activity finished ok, return the data
		    setResult(RESULT_OK, data);
		    	    
		    
		    
		    this.finish();//to stop this activity
		}
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity2, menu);
		return true;
	}*/
	
	 @Override
	 protected void onNewIntent(Intent intent) //to stop this activity
	 {
	     super.onNewIntent(intent);
	     keep = intent.getExtras().getBoolean("keep");
	     if(keep==false)
	     {
	    	 Log.i("activity2","activity 2 stop");
	         Activity2.this.finish();
	     }
	 }
}
