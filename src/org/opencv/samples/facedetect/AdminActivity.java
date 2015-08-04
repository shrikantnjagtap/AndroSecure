package org.opencv.samples.facedetect;

import org.opencv.samples.facedetect.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends Activity {
	boolean keep;
	public static int RESULT_VOICE_TEXTACTIVITY=0;//request code to identify the subactivity called
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		
		Intent intent = getIntent();
		keep = intent.getExtras().getBoolean("keep");
		
		if(keep!=false)
		{
			Log.i("AdminActivity","oncreate..");
		credentialEntity cred=new credentialEntity();
		
		cred=loadcredentialsfromDB();
		//populate cred info in UI
		
		}
	}
	private void startVoiceActivity()
	{
		try {
 			//start voice to text activity
 			Intent intent = new Intent(this, VoiceActivity.class); 	
 	 		intent.putExtra("keep", true);
 			startActivityForResult(intent, RESULT_VOICE_TEXTACTIVITY);			
 			
 			 
 		} catch (ActivityNotFoundException a) {
 			/*Toast t = Toast.makeText(getApplicationContext(),
 					"Ops! Your device doesn't support Speech to Text",
 					Toast.LENGTH_SHORT);
 			t.show();*/
 		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TextView txtText;  
		  txtText=(TextView) findViewById(R.id.txtNewpwd);
		
		if (resultCode == RESULT_OK && requestCode == RESULT_VOICE_TEXTACTIVITY) {
		    if (data.hasExtra("RESULT_SPEECH_TEXT")) {
		    	String pwdCommand=data.getExtras().getString("RESULT_SPEECH_TEXT");	    	
		    	Log.i("ADMIN ACTIVITY","voice activity new Pwd="+pwdCommand);
		    	
		    	//display pwd in ui
		    	txtText.setText(pwdCommand);
		    	
		    }
		}
		if (resultCode == 10000 && requestCode == RESULT_VOICE_TEXTACTIVITY) 
		{
			
    			//start voice activity again
	    		startVoiceActivity();    		
		}
	

	}
	public credentialEntity loadcredentialsfromDB()
	{
		credentialEntity cred=new credentialEntity();
		//get from db
		cred.mobileno="";
		cred.email="";
		cred.Voicepwd="";
		cred.messageText="";
		cred.AlarmMediaPath="";
		return cred;
		
	}
	public void speechtext(View view)//button onclick  
	{    	
		try {
 			//start voice to text activity
 			startVoiceActivity();
 			
 		} catch (ActivityNotFoundException a) {
 			/*Toast t = Toast.makeText(getApplicationContext(),
 					"Ops! Your device doesn't support Speech to Text",
 					Toast.LENGTH_SHORT);
 			t.show();*/
 		}
	}
	public void savetoDB(View view)//button onclick  
	{    		
		//1.update email,mobileno,voice pwd in db
		//2.return to mainActivity
		Intent data2 = new Intent();
	   Log.i("adminActivity","SAVE TO DB");
	    data2.putExtra("RESULT_SPEECH_TEXT", "success");
	    
	    // Activity finished ok, return the data
	    setResult(RESULT_OK, data2);
	    Toast t = Toast.makeText(getApplicationContext(),
					"SETTING CHANGED SUCEESFULLY!!!",
					Toast.LENGTH_LONG);
			t.show();
			
	    this.finish();//CLOSE THIS ACTIVITY	
		
	}
	
}
class credentialEntity
{
String email;
String mobileno;
String messageText;
String Voicepwd;
String AlarmMediaPath;
}

