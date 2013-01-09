package com.ivtolkachev.facebookfriends.data;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DatabaseHelperTag";

	private static final String DB_NAME = "FacebookFriendsDB";
	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
	//Tables names
	public static final String USERS_TABLE = "Users";
	//Table Users
	public static final String USER_ID = "user_id";
	public static final String USER_NAME = "user_name";
	public static final String USER_SURNAME = "user_surname";
	public static final String USER_BIRTH = "user_birth";
	public static final String USER_BIO = "user_bio";
	public static final String USER_CONTACTS = "user_contacts";
	//Table Users IDs
	public static final int ID_USER_ID = 0;
	public static final int ID_USER_NAME = 1;
	public static final int ID_USER_SURNAME = 2;
	public static final int ID_USER_BIRTH = 3;
	public static final int ID_USER_BIO = 4;
	public static final int ID_USER_CONTACTS = 5;
	
	private static final String CREATE_USERS_TABLE_STRING = "(" 
			+ USER_ID + " integer primary key, " 
			+ USER_NAME + " text not null, "
			+ USER_SURNAME + " text, "
			+ USER_BIRTH + " integer, "
			+ USER_BIO + " text, "
			+ USER_CONTACTS + " text"
			+ ");";
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE + USERS_TABLE + CREATE_USERS_TABLE_STRING);
		
		insertData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * The method fills the database with test data.
	 * @param db the instance of database connection.
	 */
	private void insertData(SQLiteDatabase db){
		String birth_date="1988-12-16";
		SimpleDateFormat formatter ; 
		Date date = null ; 
		formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		try {
			date = (Date) formatter.parse(birth_date);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		Log.d(TAG, "Today is " + date.getTime());
		
		db.execSQL("INSERT INTO " + USERS_TABLE + " VALUES (null, '����', '��������', " + date.getTime() 
				+ ", 'Android developer from Kherson'," 
				+ " '{\"email\":\"ivtolkachev@gmail.com\", \"skype\":\"john_pilgrim_midnight_warrior\", \"jabber\":\"ivtolkachev@jabber.org\","
				+ "\"phoneNumbers\":[\"+380950731193\"] }')");
	}

}