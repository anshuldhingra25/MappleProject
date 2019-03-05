package com.findpackers.android.aap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by narendra on 2/9/2018.
 */

public class WelComeActivity extends AppCompatActivity {


    Button btnSignIn, btnSignUp;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ProgressDialog prgDialog;
    TextView txt_title;
    WebView webView;
    String mimeType = "text/html";
    String encoding = "utf-8";
    ImageView imageView_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);

        prgDialog = new ProgressDialog(WelComeActivity.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);





       // hideStatusBar(this.findViewById(android.R.id.content));



        if(!MyPreferences.getActiveInstance(WelComeActivity.this).gethowTowork())

        {
            MyPreferences.getActiveInstance(WelComeActivity.this).sethowTowork(true);
            final Dialog dialog = new Dialog(WelComeActivity.this);
            dialog.setContentView(R.layout.dialog_how_it_works);
            // if button is clicked, close the custom dialog


            txt_title = (TextView) dialog.findViewById(R.id.txt_title);
            webView = (WebView) dialog.findViewById(R.id.webView);
            imageView_close = (ImageView) dialog.findViewById(R.id.imageView_close);

            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.setWebChromeClient(new WebChromeClient());

            if (CommanMethod.isOnline(WelComeActivity.this)) {
                RequestParams params = new RequestParams();
                changeHowItWork(params);
            } else {

                CommanMethod.showAlert("Network Error,Please try again", WelComeActivity.this);
            }


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setAttributes(lp);

            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);


            imageView_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });


        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(WelComeActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(WelComeActivity.this, RegistrationActivity.class);
                i.putExtra("firstName", " ");
                i.putExtra("lastName", " ");
                i.putExtra("emailId", " ");
                i.putExtra("etPhoneNo"," ");
                i.putExtra("etPassword", " ");
                i.putExtra("etaddress", " ");
                i.putExtra("etPanNo"," ");
                i.putExtra("etConfirmPassword"," ");
                startActivity(i);


            }
        });

        requestPermission();

    }



    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    /////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                //    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted ){
                        // Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    }

                    else {

                        // Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }



                    }

                }


                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(WelComeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public void changeHowItWork(RequestParams params) {

        String Url = WebserviceUrlClass.howitwork_termCondtions;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "howitwork_termCondtions Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {
                        JSONArray jsonArray = null;
                        jsonArray = response.getJSONArray("pages");
                        String textslug;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            textslug=obj.getString("slug").toString().trim();
                            if(textslug.equalsIgnoreCase("how-it-works"))
                            {
                                txt_title.setText(Html.fromHtml(obj.getString("page_title").toString().trim()));
                                webView.loadData(obj.getString("page_content").toString(), mimeType, encoding);
                                Log.e("nk", "how-it-works obj Response is>>>>" + obj.toString());
                            }

                        }
                    } else if (respcode.equals("300")) {
                        // Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();
                        CommanMethod.showAlert(respMessage,WelComeActivity.this);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prgDialog.dismiss();
                //   Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                prgDialog.dismiss();
            }

        });
    }



}
