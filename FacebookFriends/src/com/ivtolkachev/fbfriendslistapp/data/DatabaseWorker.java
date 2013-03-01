package com.ivtolkachev.fbfriendslistapp.data;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ivtolkachev.fbfriendslistapp.model.User;

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
	 * @param user
	 * @return
	 */
	public synchronized String addUser(User user) {
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
			long row = mDatabase.insert(DatabaseHelper.USERS_TABLE, null, values);
			if (row > -1) result = user.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
				user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), 
						cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6), 
						cursor.getString(7));
				byte[] blob = cursor.getBlob(8);
				if (blob != null) {
					user.setImage(BitmapFactory.decodeByteArray(blob, 0, blob.length));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * Updates data of the user in database.
	 * @param user the user whose data was changed.
	 * @return userId, if update was successful.
	 */
	public synchronized String updateUser(User user){
		String result = null;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.USER_NAME, user.getName());
		values.put(DatabaseHelper.USER_FIRST_NAME, user.getFirstName());
		values.put(DatabaseHelper.USER_MIDDLE_NAME, user.getMiddleName());
		values.put(DatabaseHelper.USER_LAST_NAME, user.getLastName());
		values.put(DatabaseHelper.USER_USERNAME, user.getUsername());
		values.put(DatabaseHelper.USER_BIRTHDAY, user.getBirthday());
		Bitmap bmp = user.getImage();
		if (user.isImageChanged() && bmp != null){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			values.put(DatabaseHelper.USER_PHOTO, out.toByteArray());
		}
		try {
			int rowsCount = mDatabase.update(DatabaseHelper.USERS_TABLE, values, DatabaseHelper.USER_ID + "=" + user.getId(), null);
			if (rowsCount == 1) result = user.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
