package com.info.bdnationalid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLiteEntry extends SQLiteOpenHelper
{
	public static final String KEY_ROWID = "_id";
	public static final String KEY_MY_NAME = "myname";
	public static final String KEY_FATHER_NAME = "fname";
	public static final String KEY_MOTHER_NAME = "mname";
	public static final String KEY_OCCUPATION = "occupation";
	public static final String KEY_DAY = "day";
	public static final String KEY_MONTH = "month";
	public static final String KEY_YEAR = "year";
	public static final String KEY_ID_NO = "idno";
	public static final String KEY_ADDRESS = "address";
	
	
	
	
	public static final String DATABASE_NAME = "NationalID.db";
	//public static final String DATABASE_TABLE = "IDList";
	public static final int DATABASE_VERSION = 1;
	public static String DATABASE_TABLE_MALE="MaleTable";
	public static String DATABASE_TABLE_FEMALE="FemaleTable";
	
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	String table;
	
	public SQLiteEntry(Context context,String table) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		this.table = table;
		ourContext = context;
		ourDatabase = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE "+ DATABASE_TABLE_MALE + " (" +
				KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				KEY_MY_NAME + " TEXT NOT NULL, " +
				KEY_FATHER_NAME + " TEXT NOT NULL, "+
				KEY_MOTHER_NAME+" TEXT NOT NULL, "+
				KEY_OCCUPATION+" TEXT NOT NULL, "+
				KEY_DAY+" TEXT NOT NULL, "+
				KEY_MONTH+" TEXT NOT NULL, "+
				KEY_YEAR+" TEXT NOT NULL, "+
				KEY_ID_NO+" TEXT NOT NULL, "+
				KEY_ADDRESS+" TEXT NOT NULL);"
			);
		
		db.execSQL("CREATE TABLE "+ DATABASE_TABLE_FEMALE + " (" +
				KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				KEY_MY_NAME + " TEXT NOT NULL, " +
				KEY_FATHER_NAME + " TEXT NOT NULL, "+
				KEY_MOTHER_NAME+" TEXT NOT NULL, "+
				KEY_OCCUPATION+" TEXT NOT NULL, "+
				KEY_DAY+" TEXT NOT NULL, "+
				KEY_MONTH+" TEXT NOT NULL, "+
				KEY_YEAR+" TEXT NOT NULL, "+
				KEY_ID_NO+" TEXT NOT NULL, "+
				KEY_ADDRESS+" TEXT NOT NULL);"
			);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE_MALE);
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE_FEMALE);
		onCreate(db);
	}
	
	public void DBdelete(int value)
	{
		try
		{
			ourDatabase = this.getWritableDatabase();
			ourDatabase.delete(DATABASE_TABLE_MALE, KEY_ROWID+"="+value, null);
			ourDatabase.delete(DATABASE_TABLE_FEMALE, KEY_ROWID+"="+value, null);
			ourDatabase.close();
		}
		catch(Exception e)
		{
			Log.e("SQLiteEntry", "Error when deleting.");
		}
	}
	
	public long createEntry(String myname, String fname,String mname,String Occu, int day,int month,int year,String idno,String address) 
	{
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_MY_NAME, myname);
		cv.put(KEY_FATHER_NAME, fname);
		cv.put(KEY_MOTHER_NAME, mname);
		cv.put(KEY_OCCUPATION, Occu);
		cv.put(KEY_DAY, day);
		cv.put(KEY_MONTH, month);
		cv.put(KEY_YEAR, year);
		cv.put(KEY_ID_NO, idno);
		cv.put(KEY_ADDRESS, address);
		return ourDatabase.insert(this.table, null, cv);
	}
}
