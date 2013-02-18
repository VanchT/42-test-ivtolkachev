package com.ivtolkachev.facebookfriends.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import com.ivtolkachev.facebookfriends.activity.MainActivity;
import com.ivtolkachev.facebookfriends.data.DatabaseWorker;
import com.ivtolkachev.facebookfriends.model.User;

@RunWith(RobolectricTestRunner.class)
public class DatabaseWorkerTest {
	
	Activity mActivity;
	DatabaseWorker mDatabaseWorker;
	
	@Before
	public void setUp() throws Exception {
		mActivity = new MainActivity();
	}
	
	@Test
	public void testGetDatabaseWorker() throws Exception {
		mDatabaseWorker = DatabaseWorker.getDatabaseWorker(mActivity.getApplicationContext());
		assertNotNull(mDatabaseWorker);
	}
	
	@Test
	public void testOpenDatabase() throws Exception {
		mDatabaseWorker.openDatabase();
		SQLiteDatabase database = mDatabaseWorker.getDatabase(); 
		assertTrue(database != null && database.isOpen());
	}
	
	@Test
	public void testGetCurrentUser() throws Exception {
		User user = null;
		user = mDatabaseWorker.getUser(0);
		assertNotNull(user);
	}
	
	@Test
	public void testCloseDatabase() throws Exception {
		mDatabaseWorker.closeDatabase();
		SQLiteDatabase database = mDatabaseWorker.getDatabase();
		assertTrue(database == null || !database.isOpen());
	}
	
}
