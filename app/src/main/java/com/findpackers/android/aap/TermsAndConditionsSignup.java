package com.findpackers.android.aap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class TermsAndConditionsSignup extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbarTitle,mtxt_title,txt_avlableCredit;
    ImageView IvBack;
    ProgressDialog prgDialog;
    WebView webView;
    String mimeType = "text/html";
    String encoding = "utf-8";
    LinearLayout ln_creditbalance;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions_signup);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        IvBack = (ImageView) findViewById(R.id.back_icon);
        mtxt_title = (TextView) findViewById(R.id.txt_title);
        txt_avlableCredit = (TextView) findViewById(R.id.txt_avlableCredit);
        ln_creditbalance = (LinearLayout) findViewById(R.id.ln_creditbalance);
        webView = (WebView) findViewById(R.id.webView);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Conditions");

        txt_avlableCredit.setText(MyPreferences.getActiveInstance(TermsAndConditionsSignup.this).getCreditbalance());
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ln_creditbalance.setVisibility(View.INVISIBLE);
        ln_creditbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(TermsAndConditionsSignup.this, AvlableBlance.class);
                startActivity(in);
            }
        });

        prgDialog = new ProgressDialog(TermsAndConditionsSignup.this);
        prgDialog.setMessage("Please wait..");
        prgDialog.setCancelable(false);

        if (CommanMethod.isOnline(TermsAndConditionsSignup.this)) {
            RequestParams params = new RequestParams();
            termCondtionsWebServices(params);
        } else {

            CommanMethod.showAlert("Network Error,Please try again", TermsAndConditionsSignup.this);
        }

    }

    public void termCondtionsWebServices(RequestParams params) {

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
                            if(textslug.equalsIgnoreCase("terms-conditions"))
                            {
                                mtxt_title.setText(Html.fromHtml(obj.getString("page_title").toString().trim()));
                                webView.loadData(obj.getString("page_content").toString(), mimeType, encoding);}
                        }
                        // CommanMethod.showAlert(respMessage,HowItWorkActivity.this);
                    } else if (respcode.equals("300")) {
                        CommanMethod.showAlert(respMessage,TermsAndConditionsSignup.this);
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
