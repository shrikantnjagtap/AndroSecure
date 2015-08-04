package org.opencv.samples.facedetect;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MailActivity extends Activity {
	boolean keep;
	String from = "ankit.ankuagrawal@gmail.com";
	String password = "ankitagrawal";
	List<String> to = new ArrayList<String>();
	String sub = "Warning!";
	String body = "Your property has been stolen. Attachment contains the intruders image.";
	String attachment = "";

	@SuppressLint({ "NewApi", "ShowToast" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		keep = intent.getExtras().getBoolean("keep");
		mailEntity mailObj =(mailEntity) intent.getSerializableExtra("mailEntity");
		to.add(mailObj.TO);
		attachment=mailObj.path;
		Log.i("SMSActivity","Email contact="+mailObj.TO+"Attachment Path="+mailObj.path);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		to.add("farhan1170@gmail.com");
		GMail androidEmail = new GMail(from, password, to, sub, body,
				attachment);
		try {
			androidEmail.createEmailMessage();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			androidEmail.sendEmail();
			Toast.makeText(getBaseContext(), "mail sent", Toast.LENGTH_LONG);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
