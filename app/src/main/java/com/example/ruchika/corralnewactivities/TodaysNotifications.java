package com.example.ruchika.corralnewactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class TodaysNotifications extends ActionBarActivity {

    private String msg;
    TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_notifications);
        Intent i=getIntent();
        Bundle bundle= i.getExtras();
        //String cuisinetype =  bundle.getString("Cuisine");
        //String place = bundle.getString("Place");
        String msg = bundle.getString("msg");
        /*msg = "Your friend is interested to order" + cuisinetype + " food today from "
                + place + ". To fulfill his minimum order of Rs. " + minorder
                + ", Would you like to join him?";*/
        message = (TextView) findViewById(R.id.txtfoodmessage);
        message.setText(msg);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todays_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
