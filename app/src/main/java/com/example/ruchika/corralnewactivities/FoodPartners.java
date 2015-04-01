package com.example.ruchika.corralnewactivities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class FoodPartners extends ActionBarActivity {

    public  static final String EXTRA_MESSAGE = "ruchika";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    /* PROJECT ID = 590106964528*/

    private static final String SENDER_ID = "590106964528";
    static final String TAG = "PCSMA Project";


    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;
    String regid,cuisinetype,place,minorder,msg;
    EditText editMinOrder,editOtherFoodType,editPlace;
    RadioGroup rgfoodtype;
    int rgbutton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_partners);

        context = getApplicationContext();

        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

    @Override
    protected void onResume() {
        super.onResume();

    // Check device for Play Services APK.

        checkPlayServices();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
      if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    /**
     * Gets the current registration ID for application on GCM service, if there is one.

     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
          // Check if app was updated; if so, it must clear the registration ID
         // since the existing regID is not guaranteed to work with the new
        // app version.

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }

        return registrationId;
    }
    /**
     * Registers the application with GCM servers asynchronously.
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    //sendRegistrationIdToBackend(regid);
                    msg = "Device registered, registration ID=" + regid;
        //TODO
            // Send the registration ID to your server over HTTP, so it
            // can use GCM/HTTP or CCS to send messages to your app.
                    //sendRegistrationIdToBackend();
            // For this demo: we don't need to send it because the device will send
            // upstream messages to a server that echo back the message using the
            // 'from' address in the message.
            // Persist the regID - no need to register again.

                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
                }
                return msg;
            }
            @Override
            protected void onPostExecute(String msg) {
                mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }

    public void updatevalues(){

        rgfoodtype = (RadioGroup) findViewById(R.id.rgfoodtype);
        rgbutton = rgfoodtype.getCheckedRadioButtonId();
        editOtherFoodType = (EditText) findViewById(R.id.editothertype);
        //Log.i(TAG,"No." + rgbutton);
        cuisinetype = ((RadioButton) findViewById(rgfoodtype.getCheckedRadioButtonId())).getText().toString();
        int index = rgfoodtype.indexOfChild(findViewById(rgfoodtype.getCheckedRadioButtonId()));
        Log.i(TAG,"No." + index);
        if (index == 4){

            cuisinetype = editOtherFoodType.getText().toString();
        }
        editMinOrder = (EditText) findViewById(R.id.editminorder);
        minorder = editMinOrder.getText().toString();
        editPlace = (EditText) findViewById(R.id.editplace);
        place = editPlace.getText().toString();


    }
    private void sendNotification(String msg) {
        Intent resultIntent = new Intent(this, TodaysNotifications.class);
        resultIntent.putExtra("msg", msg);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Corral")
                .setContentText("You have received a message")
                .setSmallIcon(R.drawable.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText("Let's order some Food! ");
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(9001, mNotifyBuilder.build());
    }
    // Send an upstream message.
    public void sendrequest(final View view) {

        updatevalues();
        //Toast.makeText(getApplicationContext(), cuisinetype,Toast.LENGTH_LONG ).show();
        final Intent i = new Intent(this, TodaysNotifications.class);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {


                /*final Bundle bundle = new Bundle();
                bundle.putString("Cuisine", cuisinetype);
                bundle.putString("Place", place);
                bundle.putString("Order", minorder);
                String test = bundle.getString("Cuisine");*/

                Log.i(TAG,"ooo bhaii");
                //i.putExtras(bundle);
                //String msg = "";
                try {
                        Bundle data = new Bundle();
                        data.putString("my_message", "Hello World");
                       data.putString("my_action", "com.google.android.gcm.demo.app.ECHO_NOW");
                    String id = Integer.toString(msgId.incrementAndGet());
                    //bundle.putString("message_type", msg);
                   msg = "Your friend is interested to order " + cuisinetype + " food today from "
                            + place + ". To fulfill his minimum order of Rs. " + minorder
                            + ", Would you like to join him?";
                    gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);

                    Log.i(TAG, data.toString());

                    //Toast.makeText(getApplicationContext(),"hola hola" + cuisinetype, Toast.LENGTH_LONG).show();
                    Log.i(TAG , cuisinetype);
                   // sendNotification(msg);


                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
               //mDisplay.append(msg + "\n");
                //sendNotification(msg);
            }
        }.execute(null, null, null);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
// should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
// This sample app persists the registration ID in shared preferences, but
// how you store the regID in your app is up to you.
        return getSharedPreferences(FoodPartners.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    //TODO
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */

    /*private void sendRegistrationIdToBackend(String regid) {

        new sendRegId().execute(new Pair<Context, String>(this, regid));
        //Log.i(TAG,"Totall");

    }*/
}
