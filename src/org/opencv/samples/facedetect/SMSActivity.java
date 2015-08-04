package org.opencv.samples.facedetect;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.opencv.samples.facedetect.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.content.ContentValues;
import android.content.Context;


public class SMSActivity extends Activity {
	private String  phoneNumber;
	private String messageText;
	boolean keep;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		
		Intent intent = getIntent();
		keep = intent.getExtras().getBoolean("keep");
		smsEntity smsObj =(smsEntity) intent.getSerializableExtra("smsEntity");
		phoneNumber=smsObj.phoneno;
		messageText=smsObj.message;
		Log.i("SMSActivity","phoneNumber="+phoneNumber+"messageText="+messageText);
		if(keep!=false)
		{
			try
			{
				if(messageText.length()!=0 && messageText.length()<=160)
				{
					//send short sms
					 Log.i("SMSActivity","start sending sms");
					sendSMS(messageText,phoneNumber);
					Log.i("SMSActivity","save sent sms");
					saveInSent(messageText,phoneNumber);
					
					//send success message to main activity
					//sendStatus();//optional
					this.finish();
				}
				if(messageText.length()!=0 && messageText.length()>160)
				{
					//send long sms
					Log.i("SMSActivity","start sending sms");
					sendLongSMS(messageText,phoneNumber);	
					Log.i("SMSActivity","save sent sms");
					saveInSent(messageText,phoneNumber);
					
					//send success message to main activity
					//sendStatus();//optional
					this.finish();
				}	
			}
			catch(Exception ex)
			{
				 Toast t = Toast.makeText(getApplicationContext(),
							"EXCEPTION OCCURED-->CLOSING SMS!!",
							Toast.LENGTH_SHORT);
					t.show();
				//write to file
					writeTofile("\n"+getApplicationContext()+ex.getMessage());
					
					//return control to main
					setResult(10000, null);SMSActivity.this.finish();//restart the voice activity from mainactivity
			}
			
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
						"EXCEPTION OCCURED-->CLOSING SMSActivity!!",
						Toast.LENGTH_SHORT);
				t.show();
				setResult(10000, null);SMSActivity.this.finish();//restart the voice activity from mainactivity
				
	       } finally {
	           if (outputStreamWriter != null) {
	               try {
	               	outputStreamWriter.close();
	               } catch (IOException e) {
	               	
	            	   setResult(10000, null);SMSActivity.this.finish();//restart the voice activity from mainactivity
	               }
	               }
	             }		
    }
	private void sendStatus()
	{
		Intent data2 = new Intent();
	    Log.i("SMSActivity","in SMSActivity send status to main activity");
	    data2.putExtra("RESULT_SMS", "SUCCESS");
	    
	    // Activity finished ok, return the data
	    setResult(RESULT_OK, data2);
	    //stop this activity
	    this.finish();
	    
	}
	 public void sendSMS(String messageText,String phoneNumber) {
	        //String phoneNo = "0123456789";
	        //String message = "Hello World!";
		 try {
	        SmsManager smsManager = SmsManager.getDefault();
	        smsManager.sendTextMessage(phoneNumber, null, messageText, null, null);
	        Log.i("SMSActivity","sent sms");
	        //smsManager.sendTextMessage(phoneNumber.getText().toString(), null, messageText.getText().toString(), null, null);	        
	       // Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_LONG).show();
		 }
		 catch(Exception ex)
		 {
			 Toast t = Toast.makeText(getApplicationContext(),
						"EXCEPTION OCCURED-->CLOSING SMSActivity!!",
						Toast.LENGTH_SHORT);
				t.show();
 			writeTofile("\n"+getApplicationContext()+ex.getMessage());
 			setResult(10000, null);SMSActivity.this.finish();//restart the voice activity from mainactivity
		 }
	}
	 public void sendLongSMS(String messageText,String phoneNumber) {    	 
	        //String phoneNo = "0123456789";
	        //String message = "Hello World! Now we are going to demonstrate how to send a message with more than 160 characters from your Android application.";
		 try
		 	{
	        SmsManager smsManager = SmsManager.getDefault();
	        ArrayList<String> parts = smsManager.divideMessage(messageText); 
	        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
	        Log.i("SMSActivity","sent long sms");
	       // Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_LONG).show();
		 	}
		 catch(Exception ex)
		 {
			 Toast t = Toast.makeText(getApplicationContext(),
				"EXCEPTION OCCURED-->CLOSING SMSActivity!!",
				Toast.LENGTH_SHORT);
			 t.show();
			 writeTofile("\n"+getApplicationContext()+ex.getMessage());
			 setResult(10000, null);SMSActivity.this.finish();//restart the voice activity from mainactivity
		 }
	    }
	
	 public void saveInSent(String messageText,String phoneNumber)//save in sent folder of application 
	 {
		 try
		 {
	    	ContentValues values = new ContentValues(); 
	        
	        values.put("address", phoneNumber); 
	                  
	        values.put("body", messageText); 
	                  
	        getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	        
		 }
		 catch(Exception ex)
		 {
			 Toast t = Toast.makeText(getApplicationContext(),
					"EXCEPTION OCCURED-->CLOSING SMSActivity!!",
					Toast.LENGTH_SHORT);
			 t.show();
			 writeTofile("\n"+getApplicationContext()+ex.getMessage());
			 setResult(10000, null);SMSActivity.this.finish();//restart the voice activity from mainactivity
		 }
	  }  
	 
	 @Override
	 protected void onNewIntent(Intent intent) //to stop this activity
	 {
	     super.onNewIntent(intent);
	     keep = intent.getExtras().getBoolean("keep");
	     if(keep==false)
	     {
	    	 Log.i("SMSActivity","SMSActivity stop");
	    	 SMSActivity.this.finish();
	     }
	 }
}
