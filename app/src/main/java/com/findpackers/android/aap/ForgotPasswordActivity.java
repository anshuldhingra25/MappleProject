package com.findpackers.android.aap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by narendra on 2/16/2018.
 */

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnVerify;
    ImageView btnBack;
    EditText etMobile;
    ProgressDialog prgDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etMobile = (EditText)findViewById(R.id.et_mobile_no);

        prgDialog = new ProgressDialog(ForgotPasswordActivity.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        btnBack = (ImageView) findViewById(R.id.forgot_back);
        btnVerify = (Button) findViewById(R.id.btn_verify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommanMethod.isOnline(ForgotPasswordActivity.this)) {

                    if (validation()) {
                        RequestParams params = new RequestParams();
                        params.put("phoneNumber", etMobile.getText().toString().trim());

                        forgetpassword(params);
                    }

                } else {

                    CommanMethod.showAlert("Network Error,Please try again", ForgotPasswordActivity.this);
                }


            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

    }

    private boolean validation() {
        boolean valid = true;
        if (etMobile.length() == 0) {
            etMobile.requestFocus();
            etMobile.setError(getResources().getString(R.string.txt_phoneNoEnter));
            valid = false;
        } else if (etMobile.length() < 10) {
            etMobile.requestFocus();
            etMobile.setError(getResources().getString(R.string.txt_validMobileEnter));
            valid = false;
        }
        return valid;
    }

    public void forgetpassword(RequestParams params) {

        String Url = WebserviceUrlClass.forgetPassword;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "forget Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {


                        String userId = response.getString("userId");
                        MyPreferences.getActiveInstance(ForgotPasswordActivity.this).setUserId(userId);

                        Intent i = new Intent(ForgotPasswordActivity.this, OtpVerificationActivity.class);
                        i.putExtra("otp", "");
                        i.putExtra("phoneNumber", etMobile.getText().toString());
                        i.putExtra("veifytype", "forget");
                        i.putExtra("password", " ");
                        startActivity(i);
                        finish();

                    }if((respcode.equals("0")) ){

                        final Dialog dialog = new Dialog(ForgotPasswordActivity.this);
                        dialog.setContentView(R.layout.dialog_sorry);
                        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                        TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);
                        // if button is clicked, close the custom dialog
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

                        txt_msg.setText(respMessage);

                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);
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
