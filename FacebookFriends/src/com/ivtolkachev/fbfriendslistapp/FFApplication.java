package com.ivtolkachev.fbfriendslistapp;

import java.nio.MappedByteBuffer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.ivtolkachev.fbfriendslistapp.data.DatabaseWorker;
import com.ivtolkachev.fbfriendslistapp.model.User;

public class FFApplication {
	
	private static final String TAG = "FFApplicationTag";

	private static Context mAppContext;
	private static SharedPreferences mAppPreferences;
	private static DatabaseWorker mDatabaseWorker;
	private static User mCurrentUser;
	
	// Public methods
	
	public static void init(Context context){
		mAppContext = context;
		mAppPreferences = mAppContext.getSharedPreferences(mAppContext.getString(R.string.app_preferences), 0);
		mDatabaseWorker = DatabaseWorker.getDatabaseWorker(mAppContext);
	}
	
	public static void onCloseApplication(){
		mDatabaseWorker.closeDatabase();
	}
	
	/**
	 * @return the mCurrentUser
	 */
	public static User getCurrentUser() {
		return mCurrentUser;
	}

	/**
	 * @param mCurrentUser the mCurrentUser to set
	 */
	public static void setCurrentUser(User mCurrentUser) {
		FFApplication.mCurrentUser = mCurrentUser;
	}
	
	public static DatabaseWorker getOpenedDatabaseWorker(){
		mDatabaseWorker.openDatabase();
		return mDatabaseWorker;
	}
	
	public static String getCachedUserId(){
		return mAppPreferences.getString(mAppContext.getString(R.string.pref_user_id), null);
	}
	
	public static void cacheUserId(String userId){
		SharedPreferences.Editor editor = mAppPreferences.edit();
		editor.putString(mAppContext.getString(R.string.pref_user_id), userId);
		editor.commit();
	}
	
	/**
     * Authenticates the user if it need.
     */
    public static void authenticate(Activity activity) {   	   	
    	Log.d(TAG, "authenticate");
    	
    	Session.openActiveSession(activity, true, new Session.StatusCallback() {
 
    		@Override
    		public void call(Session session, SessionState state, Exception exception) {
    			if (session.isOpened()) {
    				Log.d(TAG, "openActiveSession: session is opened");
    			}
    		}
    	});
    }
	
	// Private methods
	
}
