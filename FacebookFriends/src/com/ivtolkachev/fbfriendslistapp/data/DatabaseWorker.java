package com.ivtolkachev.fbfriendslistapp.data;

import java.util.ArrayList;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.model.GraphUser;
import com.ivtolkachev.fbfriendslistapp.model.Location;
import com.ivtolkachev.fbfriendslistapp.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;

public class DatabaseWorker {

	private static final String TAG = "DatabaseWorkerTag";
	
	private static DatabaseWorker mInstance = null;
	private SQLiteDatabase mDatabase;
	private DatabaseHelper mDatabaseHelper;
	
	private DatabaseWorker(Context context){
		mDatabaseHelper = new DatabaseHelper(context);
	}
	
	public SQLiteDatabase getDatabase() {
		return mDatabase;
	}
	
	/**
	 * The method initializes an instance of DatabaseWorker if it is need and returns it.
	 * @param context the ApplicationContext
	 * @return an instance of DatabaneWorker.
	 */
	public static DatabaseWorker getDatabaseWorker(Context context){
		if (mInstance == null) {
			 mInstance = new DatabaseWorker(context);
		}
		return mInstance;
	}
	
	/**
	 * The method opens connection with database.
	 * @param context the ApplicationContext
	 */
	public void openDatabase(){
		if (mDatabase == null || !mDatabase.isOpen()){
			try {
				mDatabase = mDatabaseHelper.getWritableDatabase();
				Log.d(TAG, "DB was opened.");
			} catch (SQLiteException e) {
				Log.e(TAG, "Error with opening of db.");
				e.printStackTrace();
			}
		}
	}
	
	public void closeDatabase(){
		if (mDatabase != null && mDatabase.isOpen()) {
			mDatabase.close();
			Log.d(TAG, "DB was closed.");
		}
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	public synchronized long addLocation(Location location){
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.LOCATION_COUNTRY, location.getCountry());
		values.put(DatabaseHelper.LOCATION_STATE, location.getState());
		values.put(DatabaseHelper.LOCATION_CITY, location.getCity());
		values.put(DatabaseHelper.LOCATION_STREET, location.getStreet());
		values.put(DatabaseHelper.LOCATION_ZIP, location.getZip());
		values.put(DatabaseHelper.LOCATION_LATITUDE, location.getLatitude());
		values.put(DatabaseHelper.LOCATION_LONGITUDE, location.getLongitude());
		values.put(DatabaseHelper.USER_ID, location.getUserId());
		try {
			rowId = mDatabase.insert(DatabaseHelper.LOCATIONS_TABLE, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowId;				
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public synchronized String addUser(GraphUser user) {
		String result = null;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.USER_ID, user.getId());
		values.put(DatabaseHelper.USER_NAME, user.getName());
		values.put(DatabaseHelper.USER_FIRST_NAME, user.getFirstName());
		values.put(DatabaseHelper.USER_MIDDLE_NAME, user.getMiddleName());
		values.put(DatabaseHelper.USER_LAST_NAME, user.getLastName());
		values.put(DatabaseHelper.USER_USERNAME, user.getUsername());
		values.put(DatabaseHelper.USER_LINK, user.getLink());
		values.put(DatabaseHelper.USER_BIRTHDAY, user.getBirthday());
		try {
			mDatabase.beginTransaction();
			long row = mDatabase.insert(DatabaseHelper.USERS_TABLE, null, values);
			if (row > 1 && user.getLocation() != null){
				Location location = (Location)user.getLocation();
				location.setUserId(user.getId());
				row = addLocation(location);
			}
			if (row > -1) result = user.getId();
			mDatabase.setTransactionSuccessful();
			mDatabase.endTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public synchronized Location getLocation(String userId) {
		Location location = null;
		try {
			Cursor cursor = mDatabase.query(DatabaseHelper.LOCATIONS_TABLE, null,
					DatabaseHelper.USER_ID + "=" + userId, null, null, null, null);
			if (cursor.moveToNext()) {
				location = new Location(
						cursor.getString(1), 
						cursor.getString(2), 
						cursor.getString(3), 
						cursor.getString(4), 
						cursor.getString(5), 
						cursor.getDouble(6), 
						cursor.getDouble(7), 
						cursor.getString(8));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}
	
	/**
	 * The method extracts the data of user from the database.
	 * @return user.
	 */
	public synchronized User getUser(String userId){
		User user = null;
		try {
			Cursor cursor = mDatabase.query(DatabaseHelper.USERS_TABLE, null,
					DatabaseHelper.USER_ID + "=" + userId, null, null, null, null);
			if (cursor.moveToNext()) {
				user = new User(
						cursor.getString(0), 
						cursor.getString(1), 
						cursor.getString(2), 
						cursor.getString(3), 
						cursor.getString(4), 
						cursor.getString(5), 
						cursor.getString(6), 
						cursor.getString(7));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setLocation(getLocation(user.getId()));
		return user;
	}
	
}
