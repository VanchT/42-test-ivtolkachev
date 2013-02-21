package com.ivtolkachev.facebookfriends.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.ivtolkachev.facebookfriends.R;
import com.ivtolkachev.facebookfriends.R.id;
import com.ivtolkachev.facebookfriends.data.DatabaseWorker;
import com.ivtolkachev.facebookfriends.model.User;

public class MainActivity extends Activity {

	private static final String APP_ID = "148081905344290";
	
	private DatabaseWorker mDatabaseWorker;
	private SharedPreferences mPreferences;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mPreferences = getSharedPreferences(getString(R.string.app_preferences), 0);
        mDatabaseWorker = DatabaseWorker.getDatabaseWorker(this.getApplicationContext());
        mDatabaseWorker.openDatabase();
        showUserData();
    }
    
    //TODO: The method added for testing.
    public void create(){
    	onCreate(null);
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	mDatabaseWorker.openDatabase();
    }
   
    //TODO: It is temporal for testing.
    public void setPrefs(){
    	SharedPreferences.Editor editor = mPreferences.edit();
    	editor.putLong(getString(R.string.preference_user_id), 120);
    	editor.commit();
    }
    
    
    private void showUserData(){  
    	//TODO: It is temporal for testing.
    	setPrefs();
    	
    	new AsyncTask<Void, Void, User>(){

			@Override
			protected User doInBackground(Void... params) {
				long userId = mPreferences.getLong(getString(R.string.preference_user_id), -1);
				User user = null;
				if (userId > -1) {
					user = mDatabaseWorker.getUser(userId);
				}
				return user;
			}
    		
			protected void onPostExecute(User result) {
				if (result != null) {
		    		TextView name = (TextView)findViewById(id.name_me);
		    		TextView surname = (TextView)findViewById(id.surname_me);
		    		TextView birthday = (TextView)findViewById(id.birthday_me);
		    		TextView bio = (TextView)findViewById(id.bio_me);
		    		TextView contacts = (TextView)findViewById(id.contacts_me);
		    		
		    		name.setText(result.getUserName());
		    		surname.setText(result.getUserSurname());
		    		birthday.setText(result.getUserBirthday());
		    		bio.setText(result.getUserBio());
		    		String[] userContacts = result.getUserContacts();
		    		for (int i = 0; i < userContacts.length; i++){
		    			contacts.setText(contacts.getText() + userContacts[i] + "\n");
		    		}		
		    	}
			}
			
    	}.execute();    	
     }    
}
