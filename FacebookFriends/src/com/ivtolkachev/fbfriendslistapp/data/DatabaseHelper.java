package com.ivtolkachev.fbfriendslistapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DatabaseHelperTag";

	private static final String DB_NAME = "FacebookFriendsDB";
	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
	//Tables names
	public static final String USERS_TABLE = "Users";
	public static final String LOCATIONS_TABLE = "Locations";
	//Table Users
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String USER_FIRST_NAME = "userFirstName";
	public static final String USER_MIDDLE_NAME = "userMiddleName";
	public static final String USER_LAST_NAME = "userLastName";
	public static final String USER_USERNAME = "userUsername";
	public static final String USER_BIRTHDAY = "userBirthday";
	public static final String USER_LINK = "userLink";	
	
	private static final String CREATE_USERS_TABLE_STRING = "(" 
			+ USER_ID + " text primary key, " 
			+ USER_NAME + " text , "
			+ USER_FIRST_NAME + " text, "
			+ USER_MIDDLE_NAME + " text, "
			+ USER_LAST_NAME + " text, "
			+ USER_USERNAME + " text, "
			+ USER_BIRTHDAY + " text, "
			+ USER_LINK + " text"
			+ ");";
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE + USERS_TABLE + CREATE_USERS_TABLE_STRING);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
