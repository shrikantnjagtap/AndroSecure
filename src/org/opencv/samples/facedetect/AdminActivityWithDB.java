package org.opencv.samples.facedetect;

/*using
 DataHandler dHandler= =new DataHandler(getBaseContext());
 dHandler.open();
 dHandler.close();
 */


import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivityWithDB extends Activity {

	TextView tvEmail;
	TextView tvFirstNumber;
	TextView tvSecondNumber;
	TextView tvCurrentPassword;
	TextView tvNewPassword;
	
	EditText etEmail;
	EditText etFirstNumber;
	EditText etSecondNumber;
	EditText etCurrentPassword;
	EditText etNewPassword;
	
	Button bContactDetail;
	Button bUpdatePassword;
	Button bChangePassword;
	Button bBack;
	
	public static int RESULT_VOICE_TEXTACTIVITY=3;
	DataHandler dHandler;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminwithdb);
        //initializeTextView();
        initializeEditView();
        initializeButton();
        //initializeData();
        displayData();
        bContactDetailEvent();
        bUpdatePasswordEvent();
        bChangePasswordEvent();
        //displaySingleValue();
        
        
    }

	/*public void initializeTextView()
	{
		 tvEmail=(TextView)findViewById(R.id.tvEmail);
	     tvFirstNumber=(TextView)findViewById(R.id.tvFirstNumber);
	     tvSecondNumber=(TextView)findViewById(R.id.tvSecondNumber);
	     tvCurrentPassword=(TextView)findViewById(R.id.tvCurrentPassword);
	     tvNewPassword=(TextView)findViewById(R.id.tvNewPassword);
	}*/
	
	public void initializeEditView()
	{
		etEmail=(EditText)findViewById(R.id.etEmail);
        etFirstNumber=(EditText)findViewById(R.id.etFirstNumber);
        etSecondNumber=(EditText)findViewById(R.id.etSecondNumber);
        etCurrentPassword=(EditText)findViewById(R.id.etCurrentPassword);
        etCurrentPassword.setEnabled(false);
        etNewPassword=(EditText)findViewById(R.id.etNewPassowrd);
        etNewPassword.setEnabled(false);
        
	}
	
	public void initializeButton()
	{
		bContactDetail=(Button) findViewById(R.id.bContactDetail);
		bUpdatePassword=(Button) findViewById(R.id.bUpdatePassword);
		bUpdatePassword.setEnabled(false);
		bChangePassword=(Button) findViewById(R.id.bChangePassword);
		
		bBack=(Button) findViewById(R.id.bBack);
	}
	
	public void initializeData()
	{
		dHandler=new DataHandler(getBaseContext());
		dHandler.open();
		dHandler.insertFirstData();
		dHandler.close();
		Toast.makeText(getBaseContext(), "first row inserted",Toast.LENGTH_LONG).show();
		
	}
	
	public void displayData()
	{
		
		dHandler=new DataHandler(getBaseContext());
		
		dHandler.open();
		
		Cursor c=dHandler.returnData();
		
		int i=0;
		if(c.moveToFirst())
		{
			
			do
			{
				
				etEmail.setText(c.getString(1));
				etFirstNumber.setText(c.getString(2));
				etSecondNumber.setText(c.getString(3));
				etCurrentPassword.setText(c.getString(4));
				Toast.makeText(getBaseContext(), (i++) +" id"+c.getString(0)+" data displayed",Toast.LENGTH_LONG).show();
			}
			while(c.moveToNext());
		}
	
		dHandler.close();
	}
	public void bContactDetailEvent()
	{
		bContactDetail.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dHandler=new DataHandler(getBaseContext());
				
				dHandler.open();
				String email=etEmail.getText().toString();
				String firstNumber=etFirstNumber.getText().toString();
				String secondNumber=etSecondNumber.getText().toString();
				dHandler.updateContactDetail(email, firstNumber, secondNumber);
				Toast.makeText(getBaseContext(),"data updated",Toast.LENGTH_LONG).show();
				
				dHandler.close();
				displayData();
				
			}
		});
	}
	
	public void bUpdatePasswordEvent()
	{
		bUpdatePassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dHandler=new DataHandler(getBaseContext());
				dHandler.open();
				String newPassword=etNewPassword.getText().toString();
				dHandler.updatePassword(newPassword);
				Toast.makeText(getBaseContext(),"password updated",Toast.LENGTH_LONG).show();
				dHandler.close();
				etNewPassword.setText("");
				displayData();
				bUpdatePassword.setEnabled(false);
			}
		});
	}
	
	public void bChangePasswordEvent()
	{
		bChangePassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startVoiceActivityForAdmin();
				/*String newPassword="shrikant";
				if(newPassword.length()!=0)
				{
					etNewPassword.setText(newPassword);
					bUpdatePassword.setEnabled(true);
				}
				*/
				
			}
		});
	}
	public void bBackEvent()
	{
		bBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//STOP ADMIN
				//stopadmin();
				//AdminActivityWithDB.this.finish();
			}
		});
	}
	public void stopadmin(View view)
	{
		try
		{
				
		
		Intent data2 = new Intent();
		   Log.i("AdminActivity","stop");
		    data2.putExtra("RESULT_ADMINACTIVITY", "success");
		    
		    // Activity finished ok, return the data
		    setResult(RESULT_OK, data2);
		    Toast t = Toast.makeText(getApplicationContext(),
						"admin CLOSED!!!",
						Toast.LENGTH_LONG);
				t.show();
				
				AdminActivityWithDB.this.finish();//CLOSE THIS ACTIVITY 
		}
		catch(Exception ex)
		{
			//write to file
			writeTofile("\n"+getApplicationContext()+ex.getMessage());
			
			//return control to main
			setResult(10000, null);AdminActivityWithDB.this.finish();//restart the voice activity from mainactivity
		}
		
	}
	public void displaySingleValue()
	{
		dHandler=new DataHandler(getBaseContext());
		dHandler.open();
		Toast.makeText(getBaseContext(),dHandler.getEmail()+" "+dHandler.getFirstNumber()+" "+dHandler.getSecondNumber()+" "+dHandler.getPassword(),Toast.LENGTH_LONG).show();
		dHandler.close();
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK && requestCode == RESULT_VOICE_TEXTACTIVITY) 
		  {
		    if (data.hasExtra("RESULT_SPEECH_TEXT")) {
		    //	String pwdCommand=data.getExtras().getString("RESULT_SPEECH_TEXT");
		    /*	txtText = (TextView) findViewById(R.id.txtResultfrom2);
		    	txtText.setText(pwdCommand);
		    	*/
		    	String newPassword=data.getExtras().getString("RESULT_SPEECH_TEXT");
				if(newPassword.length()!=0)
				{
					etNewPassword.setText(newPassword);
					bUpdatePassword.setEnabled(true);
				}
		    	//String pwdCommand=data.getExtras().getString("RESULT_SPEECH_TEXT");	    	
		    	Log.i("AdminActivityWithDB","in activity result voice activity RESULT_SPEECH_TEXT="+newPassword);
		    }
		  }
    }
    
    private void startVoiceActivityForAdmin()
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
 			//readFileFromInternalStorage();
 		}
		catch(Exception ex)
		{
			writeTofile("\n"+getApplicationContext()+ex.getMessage());
 			Toast t = Toast.makeText(getApplicationContext(),
 					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS",
 					Toast.LENGTH_SHORT);
 			t.show();
 			//testing exception
 			//readFileFromInternalStorage();
		}
		
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
     			AdminActivityWithDB.this.finish();
	        } finally {
	            if (outputStreamWriter != null) {
	                try {
	                	outputStreamWriter.close();
	                } catch (IOException e) {
	                	Toast t = Toast.makeText(getApplicationContext(),
	         					"EXCEPTION OCCURED.SEE LOG FOR MORE DETAILS..QUITTING APPLICATION!!",
	         					Toast.LENGTH_SHORT);
	         			t.show();
	         			AdminActivityWithDB.this.finish();
	                }
	                }
	              }	             
   
    }
}
