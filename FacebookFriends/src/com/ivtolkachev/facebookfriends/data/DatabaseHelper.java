package com.ivtolkachev.facebookfriends.data;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
	public static final String USER_USERNAMEE = "userUsername";
	public static final String USER_BIRTHDAY = "userBirthday";
	public static final String USER_LINK = "userLink";
	//Table Users
	public static final String LOCATION_ID = "locationId";
	public static final String LOCATION_COUNTRY = "country";
	public static final String LOCATION_STATE = "state";
	public static final String LOCATION_CITY = "city";
	public static final String LOCATION_STREET = "street";
	public static final String LOCATION_ZIP = "zip";
	public static final String LOCATION_LATITUDE = "latitude";
	public static final String LOCATION_LONGITUDE = "longitude";
	
	
	private static final String CREATE_USERS_TABLE_STRING = "(" 
			+ USER_ID + " text primary key, " 
			+ USER_NAME + " text , "
			+ USER_FIRST_NAME + " text, "
			+ USER_MIDDLE_NAME + " integer, "
			+ USER_LAST_NAME + " text, "
			+ USER_USERNAMEE + " text, "
			+ USER_BIRTHDAY + " text, "
			+ USER_LINK + " text"
			+ ");";
	
	private static final String CREATE_LOCATIONS_TABLE_STRING = "(" 
			+ LOCATION_ID + " integer primary key, " 
			+ LOCATION_COUNTRY + " text, "
			+ LOCATION_STATE + " text , "
			+ LOCATION_CITY + " integer, "
			+ LOCATION_STREET + " text, "
			+ LOCATION_ZIP + " text, "
			+ LOCATION_LATITUDE + " integer, "
			+ LOCATION_LONGITUDE + " integer, "
			+ USER_ID + " text, "
			+ "FOREIGN KEY (" + USER_ID + ") REFERENCES " + USERS_TABLE + " (" + USER_ID + ")"
			+ ");";
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE + USERS_TABLE + CREATE_USERS_TABLE_STRING);
		db.execSQL(CREATE_TABLE + LOCATIONS_TABLE + CREATE_LOCATIONS_TABLE_STRING);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
