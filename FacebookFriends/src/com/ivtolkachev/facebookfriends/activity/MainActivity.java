package com.ivtolkachev.facebookfriends.activity;

import com.ivtolkachev.facebookfriends.R;
import com.ivtolkachev.facebookfriends.R.layout;
import com.ivtolkachev.facebookfriends.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
