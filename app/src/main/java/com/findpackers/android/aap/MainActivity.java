

package com.findpackers.android.aap;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.findpackers.android.aap.commanUtill.ForceUpdate;
import com.findpackers.android.aap.commanUtill.MyApplication;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.fragment.AllLeadsFragment;
import com.findpackers.android.aap.fragment.NotificationFragment;
import com.findpackers.android.aap.fragment.ProfileFragment;
import com.findpackers.android.aap.fragment.SettingFragment;
import com.findpackers.android.aap.referandearn.ReferandEarn;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    ImageView ivReferEarn;
    TextView notificationsCount;
    Toolbar toolbar;
    TextView txtTitle;
    public static TextView avlable_creditbalance, txt_notification;
    LinearLayout ln_creditbalance, ln_inercredit;
    FrameLayout frmnotification;

    private BroadcastReceiver registerReceiver;
    private BottomNavigationView mBottomNavigationView;
    private Menu menuNav;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Crashlytics.getInstance().crash(); // Force a crash


        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        txtTitle = (TextView) findViewById(R.id.title);
        txt_notification = (TextView) findViewById(R.id.txt_notification);
        frmnotification = (FrameLayout) findViewById(R.id.frmnotification);
        ivReferEarn = (ImageView) findViewById(R.id.ivReferEarn);
        ivReferEarn.setVisibility(View.GONE);
        avlable_creditbalance = (TextView) findViewById(R.id.avlable_creditbalance);
        ln_creditbalance = (LinearLayout) findViewById(R.id.ln_creditbalance);
        ln_inercredit = (LinearLayout) findViewById(R.id.ln_inercredit);
        txtTitle.setText("All Leads");
        setSupportActionBar(toolbar);
        Sessionout();
        AccountDeletBYadmin();
        welcometoPacker_mover();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        RequestParams params = new RequestParams();
        params.put("userId", MyPreferences.getActiveInstance(MainActivity.this).getUserId());
        params.put("fcmKey", refreshedToken);
        params.put("version", BuildConfig.VERSION_NAME);
        //fetchingCitiesWebservices(params);
        updateTokenWebService(params);
        //  BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        // startService(new Intent(this, TokenUpdateService.class));
        setupBottomNavigation();

        menuNav = mBottomNavigationView.getMenu();
        String userId = MyPreferences.getActiveInstance(MainActivity.this).getUserId();
        String fcmKey = FirebaseInstanceId.getInstance().getToken();
        frmnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTitle.setText("Notification");
                loadNotificationfregmentFragment();
            }
        });
       /* ivReferEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReferandEarn.class);
                startActivity(intent);
            }
        });*/
        if (MyApplication.notificatonother) {
            MyApplication.notificatonother = false;
            if (savedInstanceState == null) {
                txtTitle.setText("Notification");
                loadNotificationfregmentFragment();
            }
        } else {

            if (savedInstanceState == null) {
                loadHomeFragment();
            }
        }

        avlable_creditbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(MainActivity.this, AvlableBlance.class);
                startActivity(in);
            }
        });

       /* ln_creditbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(MainActivity.this, AvlableBlance.class);
                startActivity(in);
            }
        });*/
        ln_inercredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(MainActivity.this, AvlableBlance.class);
                startActivity(in);
            }
        });

    }


    public void Sessionout() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sessionOut");
        registerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Sorry your account has deactivated. Please contact to admin.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                //   String userId = MyPreferences.getActiveInstance(MainActivity.this).getUserId();
                                //  RequestParams params = new RequestParams();
                                //  params.put("userId", userId);
                                //  logOutWebServices(params);

                                // get prompts.xml view
                            }
                        });


                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("account deactivated");
                alert.show();

            }
        };


    }

    public void AccountDeletBYadmin() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("accountdelet");
        registerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Your account has been deleted.")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                MyPreferences.getActiveInstance(MainActivity.this).setUserId(" ");
                                MyPreferences.getActiveInstance(MainActivity.this).setIsLoggedIn(false);

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
                                MyApplication.strcheck_remainlead = "";
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

                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
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
                alert.setTitle("account deleted");
                alert.show();

            }
        };


    }

    public void welcometoPacker_mover() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("welcomemessage");
        registerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Thank you for registering with us and you have got 200 Credit Points in your wallet.")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();


                                // get prompts.xml view
                            }
                        });


                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Session timeout warning");
                alert.show();

            }
        };
       /* if (this != null)
            try {
                this.registerReceiver(registerReceiver, intentFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        ForceUpdate forceUpdate = new ForceUpdate(MainActivity.this);
        forceUpdate.build();
    }


    @Override
    protected void onStop() {

        try {
            if (registerReceiver != null) {
                this.unregisterReceiver(registerReceiver);
            }
        } catch (Exception e) {
            Log.i("", "broadcastReceiver is already unregistered");
            registerReceiver = null;
        }

        super.onStop();
    }

    private void setupBottomNavigation() {

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // initializeCountDrawer();

                switch (item.getItemId()) {
                    case R.id.action_home:

                        loadHomeFragment();
                        txtTitle.setText("All Leads");
                        MyApplication.activefregment = "home";

                        return true;
                    case R.id.action_profile:

                        loadProfileFragment();
                        txtTitle.setText("Profile");
                        MyApplication.activefregment = "profile";
                        //  toolbar.setVisibility(View.GONE);
                        return true;
                    case R.id.action_settings:

                        loadSettingsFragment();
                        txtTitle.setText("Settting");
                        MyApplication.activefregment = "setting";
                        // toolbar.setVisibility(View.VISIBLE);
                        return true;

                }
                return false;
            }
        });
    }

    private void loadHomeFragment() {


        AllLeadsFragment fragment = AllLeadsFragment.newInstance();
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    private void loadProfileFragment() {

        ProfileFragment fragment = ProfileFragment.newInstance();
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    private void loadSettingsFragment() {


        SettingFragment fragment = SettingFragment.newInstance();
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();


    }

    private void loadNotificationfregmentFragment() {


        NotificationFragment fragment = NotificationFragment.newInstance();
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();


    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure want to exit the application ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        System.out.println("check active " + MyApplication.activefregment);
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Confirm  Exit");
        alert.show();

    }

    public void fetchingCitiesWebservices(RequestParams params) {

        String Url = WebserviceUrlClass.Cities;
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(10000);
        client.get(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    Log.e("nk", "Cities Response is>>>>" + response.toString());
                    String respCode = response.getString("responseCode");
                    String responseMessage = response.getString("responseMessage");
                    if (respCode.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = null;
                        MyApplication.list.clear();
                        MyApplication.list2.clear();
                        jsonArray = response.getJSONArray("ciries");
                        // arrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            MyApplication.list.add(obj.getString("city").toString().trim());
                            MyApplication.list2.add(obj.getString("city").toString().trim());
                            //  arrayList.add(obj.getString("city").toString().trim());

                        }


                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }

            @Override
            public void onFinish() {
                super.onFinish();

            }

        });
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
                        MyPreferences.getActiveInstance(MainActivity.this).setUserId(" ");
                        MyPreferences.getActiveInstance(MainActivity.this).setIsLoggedIn(false);

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
                        MyApplication.strcheck_remainlead = "";
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

                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
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

    private void initializeCountDrawer() {
        //Gravity property aligns the text

        System.out.println("notificationCount: " + 3);
        if (notificationsCount != null) {
            System.out.println("notificationCount is not null: " + notificationsCount);
        } else {
            System.out.println("notificationCount is  null: " + notificationsCount);
        }
        notificationsCount.setGravity(Gravity.TOP);
        notificationsCount.setTextColor(getResources().getColor(R.color.app_color));

        notificationsCount.setText("3");
        System.out.println("notificationCount text: " + notificationsCount.getText());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registerReceiver != null) {
            // unregisterReceiver(registerReceiver);
        }
//        if (isMyServiceRunning(UpdateFcmTokenService.class)) {
//            stopService(new Intent(this, UpdateFcmTokenService.class));
//        }


    }


    private void updateTokenWebService(RequestParams params) {

        Log.e("nk", "Update Token params is>>>>" + params.toString());
        String Url = WebserviceUrlClass.updateFcmToken;
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(10000);
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "FCM Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {
                        System.out.println("UpdateServiceResponse=====" + "200");
                        //  String userId = String.valueOf(response.getInt("userId"));//;


                    } else {
                        System.out.println("UpdateServiceResponse=====" + "Not 200");
                    }
                } catch (JSONException e) {
                    System.out.println("UpdateServiceResponse=====" + "Exception");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //   Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                System.out.println("UpdateServiceResponse=====" + "failure");
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

        });

    }

}
