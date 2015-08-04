package org.opencv.samples.facedetect;

import java.util.concurrent.ConcurrentHashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataHandler {
	public static final String COLUMN_ID = "_id";
	public static final String EMAIL="email";
	public static final String FIRSTNUMBER="firstNumber";
	public static final String SECONDNUMBER="secondNumber";
	public static final String PASSWORD="password";
	public static final String TABLE_NAME="myTable2";
	public static final String DATABASE_NAME="myDatabase2";
	public static final int DATABASE_VERSION=1;
	public static final String TABLE_CREATE="CREATE TABLE "+TABLE_NAME+" ("+
			COLUMN_ID+" integer primary key,"+
			EMAIL+" text,"+
			FIRSTNUMBER+" text,"+
			SECONDNUMBER+" text,"+
			PASSWORD+" text"+
			");";
	
	DatabaseHelper dbHelper;
	Context ctx;
	
	SQLiteDatabase db;
	public DataHandler(Context ctx)
	{
		this.ctx=ctx;
		dbHelper=new DatabaseHelper(ctx);
		
	}
	
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		public DatabaseHelper(Context ctx)
		{
			super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase arg0) {
			// TODO Auto-generated method stub
			try
			{
				arg0.execSQL(TABLE_CREATE);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			arg0.execSQL("DROP TABLE IF EXIST "+TABLE_NAME);
			onCreate(arg0);
		}
		
	}
	
	public DataHandler open()
	{

		db=dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	
	public long insertFirstData()
	{
		ContentValues content=new ContentValues();
		content.put(COLUMN_ID,1);
		content.put(EMAIL,"ankit.ankuagrawal@gmail.com");
		content.put(FIRSTNUMBER,"8503990796");
		content.put(SECONDNUMBER,"8503994212");
		content.put(PASSWORD,"password");
		return db.insert(TABLE_NAME, null, content);
	}
	
	public void updateContactDetail(String email,String firstNumber, String secondNumber)
	{
		String strFilter = COLUMN_ID +"=1";
		ContentValues content = new ContentValues();
		content.put(EMAIL,email);
		content.put(FIRSTNUMBER,firstNumber);
		content.put(SECONDNUMBER,secondNumber);
		db.update(TABLE_NAME, content, strFilter, null);
	}
	
	public void updatePassword(String newPassword)
	{
		String strFilter = COLUMN_ID +"=1";
		ContentValues content = new ContentValues();
		content.put(PASSWORD,newPassword);
		db.update(TABLE_NAME, content, strFilter, null);
	}
	
	public String getEmail()
	{
		Cursor c=db.query(TABLE_NAME, new String[] {EMAIL}, null, null, null , null , null);
		String email="";
		if(c.moveToFirst())
		{		
			do
			{
				email=c.getString(0);
			}
			while(c.moveToNext());
		}
		return email;
	}
	public String getFirstNumber()
	{
		Cursor c=db.query(TABLE_NAME, new String[] {FIRSTNUMBER}, null, null, null , null , null);
		String firstNumber="";
		if(c.moveToFirst())
		{		
			do
			{
				firstNumber=c.getString(0);
			}
			while(c.moveToNext());
		}
		return firstNumber;
	}
	public String getSecondNumber()
	{
		Cursor c=db.query(TABLE_NAME, new String[] {SECONDNUMBER}, null, null, null , null , null);
		String secondNummber="";
		if(c.moveToFirst())
		{		
			do
			{
				secondNummber=c.getString(0);
			}
			while(c.moveToNext());
		}
		return secondNummber;
	}
	public String getPassword()
	{
		Cursor c=db.query(TABLE_NAME, new String[] {PASSWORD}, null, null, null , null , null);
		String password="";
		if(c.moveToFirst())
		{		
			do
			{
				password=c.getString(0);
			}
			while(c.moveToNext());
		}
		return password;
	}
	
	
	public Cursor returnData()
	{
		return db.query(TABLE_NAME, new String[] {COLUMN_ID,EMAIL,FIRSTNUMBER,SECONDNUMBER,PASSWORD}, null, null, null , null , null);
	}
	
}
