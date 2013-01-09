package com.ivtolkachev.facebookfriends.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ivtolkachev.facebookfriends.model.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DatabaseWorker {

	private static final String TAG = "DatabaseWorkerTag";
	
	private static DatabaseWorker instance = null;
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	
	private DatabaseWorker(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	/**
	 * The method initializes an instance of DatabaseWorker if it is need and returns it.
	 * @param context the ApplicationContext
	 * @return an instance of DatabaneWorker.
	 */
	public static DatabaseWorker getDatabaseWorker(Context context){
		if (instance == null) {
			 instance = new DatabaseWorker(context);
		}
		return instance;
	}
	
	/**
	 * The method opens connection with database.
	 * @param context the ApplicationContext
	 */
	public void openDB(){
		if (db == null || !db.isOpen()){
			try {
				db = dbHelper.getWritableDatabase();
				Log.d(TAG, "DB was opened.");
			} catch (SQLiteException e) {
				Log.e(TAG, "Error with opening of db.");
				e.printStackTrace();
			}
		}
	}
	
	public void closeDB(){
		if (db != null && db.isOpen()) {
			db.close();
			Log.d(TAG, "DB was closed.");
		}
	}
	
	/**
	 * The method extracts the data of users from the database.
	 * @return a list of users.
	 */
	public synchronized ArrayList<User> getUsers(){
		ArrayList<User> users = new ArrayList<User>();
		if (db.isOpen()){
			Cursor cursor = db.query(DatabaseHelper.USERS_TABLE, null, null, null, null, null, null);
			while (cursor.moveToNext()) {
				User user = new User(
						cursor.getLong(DatabaseHelper.ID_USER_ID),
						cursor.getString(DatabaseHelper.ID_USER_NAME), 
						cursor.getString(DatabaseHelper.ID_USER_SURNAME), 
						cursor.getLong(DatabaseHelper.ID_USER_BIRTH), 
						cursor.getString(DatabaseHelper.ID_USER_BIO), 
						parseUserContacts(cursor.getString(DatabaseHelper.ID_USER_CONTACTS)));
				users.add(user);
			}
		} else {
			Log.e(TAG, "The database has not opened!");
		}
		return users;
	}
	
	// private methods
	
	/**
	 * The method parses the json string with user's contacts.
	 * @param jsonString the json string which must be parsed. 
	 * @return the ArrayList of user's contacts.
	 */
	private ArrayList<String> parseUserContacts(String jsonString){
		ArrayList<String> userContacts = new ArrayList<String>();
		try {
			JSONObject contacts = new JSONObject(jsonString);
			if (contacts.has("email")){
				userContacts.add("Email: " + contacts.getString("email"));
			}
			if (contacts.has("skype")){
				userContacts.add("Skype: " + contacts.getString("skype"));
			}
			if (contacts.has("jabber")){
				userContacts.add("Jabber: " + contacts.getString("jabber"));
			}
			if (contacts.has("phoneNumbers")){
				JSONArray numbers = contacts.getJSONArray("phoneNumbers");
				for (int i = 0; i < numbers.length(); i++){
					String phone = "";
					if (i > 0) {
						phone = "Phone " + i + ": ";
					} else {
						phone = "Phone: ";
					}
					phone += numbers.getString(i);
					userContacts.add(phone);
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, "Error with parsing of json string!");
			e.printStackTrace();
		}
		return userContacts;
	}
	
}
