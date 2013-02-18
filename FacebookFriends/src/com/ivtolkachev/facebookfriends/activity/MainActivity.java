package com.ivtolkachev.facebookfriends.activity;

import java.util.ArrayList;

import com.ivtolkachev.facebookfriends.R;
import com.ivtolkachev.facebookfriends.R.id;
import com.ivtolkachev.facebookfriends.data.DatabaseWorker;
import com.ivtolkachev.facebookfriends.model.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String APP_ID = "148081905344290";
	
	private DatabaseWorker dbWorker;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbWorker = DatabaseWorker.getDatabaseWorker(this.getApplicationContext());
        dbWorker.openDatabase();
        showUserData();
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
    	dbWorker.openDatabase();
    }
    
    private void showUserData(){
    	new AsyncTask<Void, Void, ArrayList<User>>(){

			@Override
			protected ArrayList<User> doInBackground(Void... params) {
				return dbWorker.getUser();
			}
    		
			protected void onPostExecute(ArrayList<User> result) {
				if (result.size() > 0) {
		    		TextView name = (TextView)findViewById(id.name_me);
		    		TextView surname = (TextView)findViewById(id.surname_me);
		    		TextView birthday = (TextView)findViewById(id.birthday_me);
		    		TextView bio = (TextView)findViewById(id.bio_me);
		    		TextView contacts = (TextView)findViewById(id.contacts_me);
		    		
		    		User user = result.get(0);
		    		name.setText(user.getUserName());
		    		surname.setText(user.getUserSurname());
		    		birthday.setText(user.getUserBirthday());
		    		bio.setText(user.getUserBio());
		    		ArrayList<String> userContacts = user.getUserContacts();
		    		for (int i = 0; i < userContacts.size(); i++){
		    			contacts.setText(contacts.getText() + userContacts.get(i) + "\n");
		    		}		
		    	}
			}
			
    	}.execute();    	
     }    
}
