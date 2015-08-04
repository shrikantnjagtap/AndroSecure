package org.opencv.samples.facedetect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;

public class ControllerActivity extends Activity {
	public static int RESULT_ACTIVITY2=1;//request code to identify the subactivity called
	public static int RESULT_TIMERACTIVITY=2;//request code to identify the subactivity called
	public static int RESULT_VOICE_TEXTACTIVITY=3;//request code to identify the subactivity called
	public static int RESULT_SMSACTIVITY=4;//request code to identify the subactivity called
	public static int RESULT_ADMINACTIVITY=5;//
	public static int RESULT_ALARMACTIVITY=6;//
	public static int RESULT_FACEACTIVITY=7;
	public static int RESULT_MAILACTIVITY=8;
	
	public static String  TIMER_EXPIRY="TIMER_EXPIRY";
	public static int interval=30*1000;// in millisec
	public static String  EXTRA_MESSAGE="msgkey";
	public static String TAG="debug";
	FaceEntity face;
	timerActivityBackground ta;
	DataHandler dHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i(TAG,"oncreate()");		
		readFileFromInternalStorage();
		//initializeData();
		displaySingleValue();
	}
	
	public void displaySingleValue()
	{
		dHandler=new DataHandler(getBaseContext());
		dHandler.open();
		Toast.makeText(getBaseContext(),dHandler.getEmail()+" "+dHandler.getFirstNumber()+" "+dHandler.getSecondNumber()+" "+dHandler.getPassword(),Toast.LENGTH_LONG).show();
		dHandler.close();
	}
	public void initializeData()
	{
		dHandler=new DataHandler(getBaseContext());
		dHandler.open();
		dHandler.insertFirstData();
		dHandler.close();
		Toast.makeText(getBaseContext(), "first row inserted",Toast.LENGTH_LONG).show();
		
	}
	
	public void onclick_speechtoText(View view)
	{
		startVoiceActivity();
	}
	public void onclick_sendSMS(View view)
	{
		smsEntity sms=getSMSEntity();
		startSMSActivity(sms);		
					
	}
	public void onclick_alarmStart(View view)
	{
		//sound alarm
		AlarmEntity al=new AlarmEntity();
		al=getAlarmEntityfromDB();
		startAlarmActivity(al);
	}
	public void onclick_startProcess(View view)//button onclick  for testing 
	{    		
		//start face activity
		startFaceActivity();
		
		
	/*to be included in the result from face activity
		startTimer();//not for prototype demo
		
         		
 		try {
 			//start voice to text activity
 			startVoiceActivity();
 			
 		} catch (ActivityNotFoundException a) {
 			//Toast t = Toast.makeText(getApplicationContext(),
 				//	"Ops! Your device doesn't support Speech to Text",
 					//Toast.LENGTH_SHORT);
 			t.show();
 		}
	*/
 		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TextView txtText;  
		  txtText=(TextView) findViewById(R.id.txtResultfrom2);
		  Log.i("mainActivity","FACE"+requestCode+"==="+resultCode);
		  if (resultCode == RESULT_OK && requestCode == RESULT_FACEACTIVITY)
		  {
			  Log.i("mainActivity","RESULT FROM FACE");
			  
			  face =(FaceEntity) data.getSerializableExtra("faceEntity");//load face object
				Log.i("ControllerActivity","image path:"+face.path);
			 // start voice activity,timer
			  startTimer();//not for prototype demo				
       		
		 		try {
		 			//start voice to text activity
		 			startVoiceActivity();
		 			
		 		} catch (ActivityNotFoundException a) {
		 			Toast t = Toast.makeText(getApplicationContext(),
		 					"Ops! Your device doesn't support Speech to Text",
		 					Toast.LENGTH_SHORT);
		 			t.show();
		 		}
		  }
		  else if (resultCode == RESULT_OK && requestCode == RESULT_ADMINACTIVITY)
		  {
			  Log.i("mainActivity","close");
			 // this.finish();//close applcation or return to start page
		  }
		  else if (resultCode == 10000 && requestCode == RESULT_ADMINACTIVITY)
		  {
			  Log.i("mainActivity","close");
			 // this.finish();//close applcation or return to start page
		  }
		  else if (resultCode == RESULT_OK && requestCode == RESULT_ALARMACTIVITY)
		  {
			  Log.i("mainActivity","result from alarm-->normal");
			  Toast t = Toast.makeText(getApplicationContext(),
						"result from alarm-->normal!!!",
						Toast.LENGTH_LONG);
				t.show();
			 ControllerActivity.this.finish();//close applcation or return to start page
		  }
		  else if (resultCode == 10000 && requestCode == RESULT_ALARMACTIVITY)
		  {
			  Log.i("mainActivity","Return from alarm due to exception");
			  this.finish();//close applcation or return to start page
		  }
		  else if (resultCode == RESULT_OK && requestCode == RESULT_SMSACTIVITY)
		  {
			  Log.i("mainActivity","result from SMS-->normal");
			 // this.finish();//close applcation or return to start page
		  }
		  else if (resultCode == RESULT_OK && requestCode == RESULT_ADMINACTIVITY)
		  {
			  Log.i("mainActivity","result from ADMIN-->normal");
			 // this.finish();//close applcation or return to start page
		  }
		  else if (resultCode == 10000 && requestCode == RESULT_SMSACTIVITY)
		  {
			  Log.i("mainActivity","Return from SMS due to exception");
			 // this.finish();//close applcation or return to start page
		  }
		  else if (resultCode == RESULT_OK && requestCode == RESULT_VOICE_TEXTACTIVITY) 
		  {
		    if (data.hasExtra("RESULT_SPEECH_TEXT")) {
		    //	String pwdCommand=data.getExtras().getString("RESULT_SPEECH_TEXT");
		    /*	txtText = (TextView) findViewById(R.id.txtResultfrom2);
		    	txtText.setText(pwdCommand);
		    	*/
		     
		    	String pwdCommand=data.getExtras().getString("RESULT_SPEECH_TEXT");	    	
		    	Log.i(TAG,"in activity result voice activity RESULT_SPEECH_TEXT="+pwdCommand);
		    	//validate pwdcommand and pwd from db
		    	if(validate(pwdCommand)==true)
		    	{
		    	//pwd correct
		    	//1.stop timer
		    	//2.sms,mail,alarm-->required??
		    	//2.start adminActivity
		    	
		    	//stop timer
		    		Log.i("stop activity","stop timer activity");
		    		ta.cancel(true);
		    	//stop sms
		    		//Intent intent = new Intent(MainActivity.this, SMSActivity.class);
		    	//	Log.i("stop activity","stop sms activity");
		    		//stopActivity(intent);
		    		
		    	//start adminActivity
		    		Log.i("startAdminActivity","Start");
		    		startAdminActivity();
		    		Log.i("startAdminActivity","END");
		    	}
		    	else
		    	{
		    		if(ta.isCancelled()==false)
		    		{
		    			//start voice activity again
			    		startVoiceActivity();	
		    		}
		    		
		    	}
		    	
		    }
		  }
		  else if (resultCode == 10000 && requestCode == RESULT_VOICE_TEXTACTIVITY) 
		  {
			  //even if there is any error start voice activity again
			  //startVoiceActivity();
			if( ta.isCancelled()==false)//check if timer is cancelled
    		{
    			//start voice activity again
	    		startVoiceActivity();	
    		}
    		
		  }
	} 
private void startTimer()
{
	ta = new timerActivityBackground();
    ta.execute();
}
private void startAdminActivity()
	{
		try {
 			//start voice to text activity
 			//Intent intent = new Intent(this, AdminActivity.class); 	
			Intent intent = new Intent(this, AdminActivityWithDB.class);
 			intent.putExtra("keep", true);
 			Log.i("AdminActivityWithDB","Before Starting");
 			startActivityForResult(intent, RESULT_ADMINACTIVITY);			
 			Log.i("AdminActivityWithDB","After Start");
 			 
 		} catch (ActivityNotFoundException a) {
 			/*Toast t = Toast.makeText(getApplicationContext(),
 					"Ops! Your device doesn't support Speech to Text",
 					Toast.LENGTH_SHORT);
 			t.show();*/
 		}
	}
	private void startVoiceActivity()
	{
		try {
 			//start voice to text activity
 			Intent intent = new Intent(this, VoiceActivity.class);
 			//test exception
 			//intent=null;
 	 		intent.putExtra("keep", true);
 			startActivityForResult(intent, RESULT_VOICE_TEXTACTIVITY);			
 			
 			 
 		} catch (ActivityNotFoundException a) {
 			writeTofile("\n"+getApplicationContext()+a.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"Ops! Your device doesn't support Speech to Text",
 					Toast.LENGTH_SHORT);
 			t.show();
 			//testing exception
 			readFileFromInternalStorage();
 		}
		catch(Exception ex)
		{
			writeTofile("\n"+getApplicationContext()+ex.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS",
 					Toast.LENGTH_SHORT);
 			t.show();
 			//testing exception
 			readFileFromInternalStorage();
		}
		
	}
	private void startFaceActivity()
	{
		try {
 			//start voice to text activity
 			Intent intent = new Intent(this, FdActivity.class);
 			//test exception
 			//intent=null;
 			FaceEntity f=new FaceEntity();
 			f.path="";
 			intent.putExtra("faceEntity", f);
 	 		intent.putExtra("keep", true);
 			startActivityForResult(intent, RESULT_FACEACTIVITY);			
 			
 			 
 		} catch (ActivityNotFoundException a) {
 			writeTofile("\n"+getApplicationContext()+a.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"Ops! Your device doesn't support Face detection",
 					Toast.LENGTH_SHORT);
 			t.show();
 			//testing exception
 			readFileFromInternalStorage();
 		}
		catch(Exception ex)
		{
			writeTofile("\n"+getApplicationContext()+ex.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS",
 					Toast.LENGTH_SHORT);
 			t.show();
 			//testing exception
 			readFileFromInternalStorage();
		}
		
	}
	public void readFileFromInternalStorage() {
		  String eol = System.getProperty("line.separator");
		  
		  BufferedReader input = null;
		  String buffer = "";
		  try {
		    input = new BufferedReader(new InputStreamReader(openFileInput("log")));
		    String line;
		    
		    while ((line = input.readLine()) != null) {
		    	buffer+=line + eol;
		    	
		    }
		  } catch (Exception e) {
		     e.printStackTrace();
		  } finally {
		  if (input != null) {
		    try {
		    input.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    }
		  }
		  Log.i("logfile",buffer);
		} 
	public void writeTofile(String data)
    {
		OutputStreamWriter outputStreamWriter=null;
		String eol = System.getProperty("line.separator");
		 try {
	            outputStreamWriter = new OutputStreamWriter(openFileOutput("log", Context.MODE_APPEND));
	            outputStreamWriter.write(eol+data);
	            outputStreamWriter.close();	            
	            
	        }
	        catch (IOException e) {
	        	Toast t = Toast.makeText(getApplicationContext(),
     					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS..QUITTING APPLICATION!!",
     					Toast.LENGTH_SHORT);
     			t.show();
     			ControllerActivity.this.finish();
	        } finally {
	            if (outputStreamWriter != null) {
	                try {
	                	outputStreamWriter.close();
	                } catch (IOException e) {
	                	Toast t = Toast.makeText(getApplicationContext(),
	         					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS..QUITTING APPLICATION!!",
	         					Toast.LENGTH_SHORT);
	         			t.show();
	         			ControllerActivity.this.finish();
	                }
	                }
	              }	             
   
    }
	
	private void startSMSActivity(smsEntity sms)
	{
		try {
 			//start voice to text activity
 			Intent intent = new Intent(this, SMSActivity.class); 	
 			intent.putExtra("smsEntity", sms);
 			intent.putExtra("keep", true);
 			startActivityForResult(intent, RESULT_SMSACTIVITY);			
 			
 			 
 		 } catch (ActivityNotFoundException a) {
 			writeTofile("\n"+getApplicationContext()+a.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS",
 					Toast.LENGTH_SHORT);
 			t.show();
 		}
		catch(Exception ex)
		{
			writeTofile("\n"+getApplicationContext()+ex.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS",
 					Toast.LENGTH_SHORT);
 			t.show();
		}
	}
	
	private void startMailActivity(mailEntity mail)
	{
		try {
 			//start voice to text activity
 			Intent intent = new Intent(this, MailActivity.class); 	
 			intent.putExtra("mailEntity", mail);
 			intent.putExtra("keep", true);
 			startActivityForResult(intent, RESULT_MAILACTIVITY);			
 			
 			 
 		 } catch (ActivityNotFoundException a) {
 			writeTofile("\n"+getApplicationContext()+a.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS",
 					Toast.LENGTH_SHORT);
 			t.show();
 		}
		catch(Exception ex)
		{
			writeTofile("\n"+getApplicationContext()+ex.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS",
 					Toast.LENGTH_SHORT);
 			t.show();
		}
	}
	
	private void startAlarmActivity( AlarmEntity al)
	{
		try {
 			//start voice to text activity
 			Intent intent = new Intent(this, AlarmActivity.class); 	
 			intent.putExtra("alarmEntity", al);
 			intent.putExtra("keep", true);
 			startActivityForResult(intent, RESULT_ALARMACTIVITY);			
 			
 			 
 		} catch (ActivityNotFoundException a) {
 			writeTofile("\n"+getApplicationContext()+a.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS",
 					Toast.LENGTH_SHORT);
 			t.show();
 		}
		catch(Exception ex)
		{
			writeTofile("\n"+getApplicationContext()+ex.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS",
 					Toast.LENGTH_SHORT);
 			t.show();
		}
	}
	private void stopActivity(Intent intent)//stop activity using intent
	{
		try
		{
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("keep", false);
        startActivity(intent);
		}
		catch (ActivityNotFoundException a) {
 			writeTofile("\n"+getApplicationContext()+a.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS",
 					Toast.LENGTH_SHORT);
 			t.show();
 		}
		catch(Exception ex)
		{
			writeTofile("\n"+getApplicationContext()+ex.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS",
 					Toast.LENGTH_SHORT);
 			t.show();
		}
	}
	public boolean validate(String pwd)
	{
	//get pwd from db
		dHandler=new DataHandler(getBaseContext());
		dHandler.open();
		String pwdfromDB=dHandler.getPassword();//test
		dHandler.close();
		
	//compare with voice input
		if(pwdfromDB.equalsIgnoreCase(pwd))
		{
		return true;	
		}
		else
			return false;		
	}
	private smsEntity getSMSEntity()
	{
		dHandler=new DataHandler(getBaseContext());
		dHandler.open();
		smsEntity sms=new smsEntity();
		sms.phoneno=dHandler.getFirstNumber();//get from db
		dHandler.close();
		sms.message="PROPERTY AT RISK!!! sent from ANDRO SECURE";//get from db
		
		return sms;
	}
	private mailEntity getMailEntity()
	{
		dHandler=new DataHandler(getBaseContext());
		dHandler.open();
		mailEntity mail=new mailEntity();
		mail.TO=dHandler.getEmail();//  "farhan1165@gmail.com";//get from db
		mail.path=face.path;//get from face entity
		
		return mail;
	}
	
	private AlarmEntity getAlarmEntityfromDB()
	{
		AlarmEntity a=new AlarmEntity();
		//a.path="/storage/sdcard0/Ringtones/hangout_ringtone.ogg";//get from db
		a.path="/storage/sdcard0/Ringtones/hangout_ringtone.ogg";//get from db
		
		return a;
	}
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first
	    Log.i("Controller","onStop");	    
	    
	    }
	private class timerActivityBackground extends AsyncTask<String, Void, String>
{ int k=0;Handler mHandler = new Handler();
@Override
protected void onPreExecute() {
	 super.onPreExecute();
	 Log.i(TAG,"in onPreExecute");  
}
	@Override
	protected String doInBackground(String... urls) {		
		Log.i(TAG,"in background...");
		if (isCancelled())
		{
		    return null;
		}
		 Runnable timerexpiryRunnable =  new Runnable(){
			    public void run() {
			    	if (!isCancelled())
			    	{
			    	Log.i(TAG,"in runnable");
			    	k=1;
			    	}
			    }
			};		
		
		 mHandler.postDelayed(timerexpiryRunnable, interval);
		
		while(k!=1)
			{
			if (isCancelled())
			{
			    return null;
			};
			}		
	
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		//	1.stop voice activity
		//	2.send mail 
		//	3.send sms
		//	4.sound alarm
		super.onPostExecute(result);
		Log.i(TAG,"in onPostExecute");
		Toast t = Toast.makeText(getApplicationContext(),
				"TIMER EXPIRED",Toast.LENGTH_LONG);
		t.show();
		
		//stop voice activity	
		
		Intent intent2 = new Intent(ControllerActivity.this, VoiceActivity.class);
		stopActivity(intent2);
		cancel(true);//set cancel flag of timer to true 
		
		//start smsActivity
		//get phoneno,message from db
		
		smsEntity sms=getSMSEntity();
		startSMSActivity(sms);
		
		//start MailActivity
		mailEntity mail=getMailEntity();
		startMailActivity(mail);
		
			//sound alarm
		AlarmEntity al=new AlarmEntity();
		al=getAlarmEntityfromDB();
		startAlarmActivity(al);		
	}	
	
}	
	
	
}
class smsEntity implements Serializable
{
public String phoneno;
public String message;
}
class mailEntity implements Serializable
{
public String TO;
public String path;
}
class AlarmEntity implements Serializable
{
String path;
}
class FaceEntity implements Serializable
{
String path;
}


