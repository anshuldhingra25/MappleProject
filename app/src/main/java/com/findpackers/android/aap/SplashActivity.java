package com.findpackers.android.aap;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.findpackers.android.aap.commanUtill.MyApplication;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.Utility;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.notification.Config;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by narendra on 2/9/2018.
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ImageView logoView;
    String mNotificationdata = "";
    String mNotificationdata2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splesh);


        logoView = (ImageView) findViewById(R.id.logo);
        logoView.setBackgroundResource(R.drawable.dd);
        AnimationDrawable frameAnimation = (AnimationDrawable) logoView.getBackground();
        frameAnimation.start();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent mainIntent;


                if (MyPreferences.getActiveInstance(SplashActivity.this).getIsLoggedIn()) {


                    Bundle bundle = getIntent().getExtras();
                    if (bundle != null) {

                        try {
                            System.out.println(" bundale data : " + Utility.bundleToString(bundle));

                            mNotificationdata = bundle.getString("On Change Status");
                            mNotificationdata2 = bundle.getString("On delete");
                            if (mNotificationdata == null)
                                mNotificationdata = "";
                            System.out.println(" FCM mNotificationdata : " + mNotificationdata);

                            if (mNotificationdata2 == null)
                                mNotificationdata2 = "";
                            System.out.println(" FCM mNotificationdata2 : " + mNotificationdata2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        MyApplication.notificatonother = true;
                    }
                    if (mNotificationdata.equalsIgnoreCase("Your account has been deactivated, by admin so please contact to admin for any query.")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                        builder.setMessage("Sorry your account has deactivated. Please contact to admin.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        // String userId = MyPreferences.getActiveInstance(SplashActivity.this).getUserId();
                                        //  RequestParams params = new RequestParams();
                                        //  params.put("userId", userId);
                                        // logOutWebServices(params);

                                        // get prompts.xml view
                                    }
                                });


                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Account deactivated");
                        alert.show();


                    } else if (mNotificationdata2.startsWith("Your account has been deleted")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                        builder.setMessage("Your account has been deleted.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        MyPreferences.getActiveInstance(SplashActivity.this).setUserId(" ");
                                        MyPreferences.getActiveInstance(SplashActivity.this).setIsLoggedIn(false);

                                        MyApplication.filterlead = false;
                                        MyApplication.filtercount = 0;
                                        MyApplication.sortlead = false;

                                        MyApplication.strcheck_3bhk = "";
                                        MyApplication.strcheck_1bhk = "";
                                        MyApplication.strcheck_2bhk = "";
                                        MyApplication.strcheck_lesthen1bhk = "";
                                        MyApplication.strcheck_above3bhk = "";
                                        MyApplication.strcheck_hot = "";
                                        MyApplication.strcheck_active = "";
                                        MyApplication.strcheck_sold = "";
                                        MyApplication.strcheck_myleads = "";
                                        MyApplication.strcheck_allleads = "";
                                        MyApplication.strcheck_withincity = "";
                                        MyApplication.strcheck_outsidecity = "";
                                        MyApplication.strcheck_international2 = "";
                                        MyApplication.mcities = "";

                                        MyApplication.mstr_low_high = "";
                                        MyApplication.mstr_high_low = "";
                                        MyApplication.mstr_latest = "";
                                        MyApplication.mstr_oldest = "";
                                        MyApplication.mstr_nearshiftingdate = "";

                                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();
                                        // get prompts.xml view
                                    }
                                });


                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Account deleted");
                        alert.show();
                    } else {

                        mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        SplashActivity.this.finish();

                    }


                } else {

                    Intent i = new Intent(SplashActivity.this, WelComeActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };


    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("Splash  ", "Firebase reg id: " + regId);
    }

    public void logOutWebServices(RequestParams params) {

        String Url = WebserviceUrlClass.LogOut;

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "logOut Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");


                    if (respcode.equals("200")) {
                        MyPreferences.getActiveInstance(SplashActivity.this).setUserId(" ");
                        MyPreferences.getActiveInstance(SplashActivity.this).setIsLoggedIn(false);

                        MyApplication.filterlead = false;
                        MyApplication.filtercount = 0;
                        MyApplication.sortlead = false;
                        MyApplication.mcities = "";
                        MyApplication.strcheck_3bhk = "";
                        MyApplication.strcheck_1bhk = "";
                        MyApplication.strcheck_2bhk = "";
                        MyApplication.strcheck_lesthen1bhk = "";
                        MyApplication.strcheck_above3bhk = "";
                        MyApplication.strcheck_hot = "";
                        MyApplication.strcheck_active = "";
                        MyApplication.strcheck_sold = "";
                        MyApplication.strcheck_myleads = "";
                        MyApplication.strcheck_allleads = "";
                        MyApplication.strcheck_withincity = "";
                        MyApplication.strcheck_outsidecity = "";
                        MyApplication.strcheck_international2 = "";

                        MyApplication.mstr_low_high = "";
                        MyApplication.mstr_high_low = "";
                        MyApplication.mstr_latest = "";
                        MyApplication.mstr_oldest = "";
                        MyApplication.mstr_nearshiftingdate = "";

                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();

                    } else if (respcode.equals("300")) {

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                //   Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (mRegistrationBroadcastReceiver != null) {
                this.unregisterReceiver(mRegistrationBroadcastReceiver);
            }
        } catch (Exception e) {
            Log.i("", "broadcastReceiver is already unregistered");
            mRegistrationBroadcastReceiver = null;
        }

    }
}
