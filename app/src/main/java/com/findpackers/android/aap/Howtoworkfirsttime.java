package com.findpackers.android.aap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.MyApplication;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Howtoworkfirsttime extends AppCompatActivity {

    VideoView videoview;
    Toolbar toolbar;
    TextView toolbarTitle, mtxt_title, mtxt_content;
    ImageView IvBack;
    TextView txt_avlableCredit;
    ProgressDialog prgDialog;
    WebView webView;
    String mimeType = "text/html";
    String encoding = "utf-8";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtoworkfirsttime);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        IvBack = (ImageView) findViewById(R.id.back_icon);
        mtxt_title = (TextView) findViewById(R.id.txt_title);
        txt_avlableCredit = (TextView) findViewById(R.id.txt_avlableCredit);
        webView = (WebView) findViewById(R.id.webView);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("How It Works");
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        prgDialog = new ProgressDialog(Howtoworkfirsttime.this);
        prgDialog.setMessage("Please wait..");
        prgDialog.setCancelable(false);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebChromeClient(new WebChromeClient());



        if(!MyApplication.showthankyoumsg.equalsIgnoreCase(" "))
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(Howtoworkfirsttime.this);
            builder.setMessage(MyApplication.showthankyoumsg)
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
            alert.setTitle("Welcome ");
            alert.show();
            MyApplication.showthankyoumsg=" ";
        }


        txt_avlableCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(Howtoworkfirsttime.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });


        if (CommanMethod.isOnline(Howtoworkfirsttime.this)) {
            RequestParams params = new RequestParams();
            changeHowItWork(params);
        } else {

            CommanMethod.showAlert("Network Error,Please try again", Howtoworkfirsttime.this);
        }
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
                            textslug = obj.getString("slug").toString().trim();
                            if (textslug.equalsIgnoreCase("how-it-works")) {
                                mtxt_title.setText(Html.fromHtml(obj.getString("page_title").toString().trim()));
                                webView.loadData(obj.getString("page_content").toString(), mimeType, encoding);
                                Log.e("nk", "how-it-works obj Response is>>>>" + obj.toString());
                            }

                        }
                    } else if (respcode.equals("300")) {
                        // Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();
                        CommanMethod.showAlert(respMessage, Howtoworkfirsttime.this);
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

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else {
            getFragmentManager().popBackStack();
        }


    }


}
