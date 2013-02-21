package com.ivtolkachev.facebookfriends.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ivtolkachev.facebookfriends.R;
import com.ivtolkachev.facebookfriends.activity.MainActivity;
import com.ivtolkachev.facebookfriends.data.DatabaseWorker;
import com.ivtolkachev.facebookfriends.model.User;

@RunWith(RobolectricTestRunner.class)
public class DatabaseWorkerTest {
		
	private MainActivity mActivity;
	private DatabaseWorker mDatabaseWorker;
	private SharedPreferences mAppPref;
	
	@Before
	public void setUp() throws Exception {
		mActivity = new MainActivity();
		mActivity.create();
		mDatabaseWorker = DatabaseWorker.getDatabaseWorker(mActivity.getApplicationContext());
		assertNotNull(mDatabaseWorker);
		mAppPref = mActivity.getSharedPreferences(mActivity.getString(R.string.app_preferences), 0);
		assertNotNull(mAppPref);
	}
	
	@Test
	public void testOpenDatabase() throws Exception {
		assertNotNull(mDatabaseWorker);
		mDatabaseWorker.openDatabase();
		SQLiteDatabase database = mDatabaseWorker.getDatabase(); 
		assertTrue(database != null && database.isOpen());
	}
	
	@Test
	public void testGetCurrentUser() throws Exception {
		User user = null;
		long testId = mAppPref.getLong(mActivity.getString(R.string.preference_user_id), -1);
		assertTrue(testId > -1);
		assertNotNull(mDatabaseWorker);
		user = mDatabaseWorker.getUser(testId);
		assertNotNull(user);
		assertNotNull(user.getUserId());
		assertTrue(testId == user.getUserId());
	}
	
	@Test 
	public void testAddUser() throws Exception {
		User user = new User(111, "Ivan", "Ivanov", System.currentTimeMillis(), "Java developer", new String[] {"phone: +3809523432312"}); 
		long rowId = mDatabaseWorker.addUser(user);
		assertTrue(rowId > -1);
	}
	
	@Test
	public void testCloseDatabase() throws Exception {
		assertNotNull(mDatabaseWorker);
		mDatabaseWorker.closeDatabase();
		SQLiteDatabase database = mDatabaseWorker.getDatabase();
		assertTrue(database == null || !database.isOpen());
	}
	
}
