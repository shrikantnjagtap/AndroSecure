package org.opencv.samples.facedetect;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.opencv.samples.facedetect.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;


import android.widget.Toast;

public class VoiceActivity extends Activity {
	protected static final int RESULT_SPEECH = 1;
	
	boolean keep;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice);
		Intent intent = getIntent();
		keep = intent.getExtras().getBoolean("keep");
		if(keep!=false)
		{
			Intent intent2 = new Intent(
					RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			Log.i("voiceactivity","oncreate voiceActivity--");
			intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

			try {
				startActivityForResult(intent2, RESULT_SPEECH);
				//txtText.setText("");
			} catch (ActivityNotFoundException a) {
				Toast t = Toast.makeText(getApplicationContext(),
						"Ops! Your device doesn't support Speech to Text",
						Toast.LENGTH_SHORT);
				t.show();
				//write to file
				writeTofile("\n"+getApplicationContext()+a.getMessage());
				
				//return control to main
				setResult(10000, null);VoiceActivity.this.finish();//restart the voice activity from mainactivity
			}
			catch(Exception ex)
			{
				writeTofile("\n"+getApplicationContext()+ex.getMessage());
				setResult(10000, null);VoiceActivity.this.finish();//restart the voice activity from mainactivity
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
       
			setResult(10000, null);VoiceActivity.this.finish();//restart the voice activity from mainactivity
       } finally {
           if (outputStreamWriter != null) {
               try {
               	outputStreamWriter.close();
               } catch (IOException e) {
               	
        			setResult(10000, null);VoiceActivity.this.finish();//restart the voice activity from mainactivity
               }
               }
             }    
    }
	
	//receive text from speech engine
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
Log.i("requestCode",""+requestCode);
Log.i("resultCode",""+resultCode);
Log.i("data",""+data);
		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {
				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				//txtText.setText(text.get(0));
				
				//send text to mainActivity
				Intent data2 = new Intent();
			    Log.i("voiceactivity","password="+text.get(0));
			    data2.putExtra("RESULT_SPEECH_TEXT", text.get(0));
			    
			    // Activity finished ok, return the data
			    setResult(RESULT_OK, data2);
			    
			    //finish activity
			    VoiceActivity.this.finish();
			    
			}
			else
			{
				setResult(10000, null);VoiceActivity.this.finish();//restart the voice activity from mainactivity
			}
			break;			
		}
		default:
		{
			setResult(10000, null);VoiceActivity.this.finish();//restart the voice activity from mainactivity
		}
		

		}
	}
	protected void onNewIntent(Intent intent) //to stop this activity
	 {
	     super.onNewIntent(intent);
	     keep = intent.getExtras().getBoolean("keep");
	     if(keep==false)
	     {
	    	 Log.i("VoiceActivity","VoiceActivity stop");
	    	 
	    	 Toast t = Toast.makeText(getApplicationContext(),
						"VoiceActivity CLOSED!!!",
						Toast.LENGTH_LONG);
				t.show();
				VoiceActivity.this.finish();
	     }
	 }
}
