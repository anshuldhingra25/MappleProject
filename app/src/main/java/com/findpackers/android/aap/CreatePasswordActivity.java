package com.findpackers.android.aap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class CreatePasswordActivity extends AppCompatActivity {

    ImageView btnBack;
    EditText etNewPassord, etConfirmPassword;
    Button btnUpdate;
    ProgressDialog prgDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        prgDialog = new ProgressDialog(CreatePasswordActivity.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        etNewPassord = (EditText) findViewById(R.id.et_new_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnBack = (ImageView) findViewById(R.id.creat_password_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        etNewPassord.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etNewPassord.getRight() - etNewPassord.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {


                        if(etNewPassord.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            etNewPassord.setInputType( InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }else {
                            etNewPassord.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                        }
                        etNewPassord.setSelection(etNewPassord.getText().length());

                    }
                }
                return false;
            }
        });

        etConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etConfirmPassword.getRight() - etConfirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {


                        if(etConfirmPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            etConfirmPassword.setInputType( InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }else {
                            etConfirmPassword.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                        }
                        etConfirmPassword.setSelection(etConfirmPassword.getText().length());

                    }
                }
                return false;
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommanMethod.isOnline(CreatePasswordActivity.this)) {

                    boolean valid = validation();
                    if (valid) {

                        RequestParams params = new RequestParams();
                        params.put("newPassword", etNewPassord.getText().toString().trim());
                        params.put("userId", MyPreferences.getActiveInstance(CreatePasswordActivity.this).getUserId());
                        createNewPasswordWS(params);
                    }

                } else {

                    CommanMethod.showAlert("Network Error,Please try again", CreatePasswordActivity.this);
                }

            }
        });


    }


    public void createNewPasswordWS(RequestParams params) {

        String Url = WebserviceUrlClass.forgotPassword;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "forget password Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");


                    if (respcode.equals("200")) {
                        //CommanMethod.showAlert(respMessage, CreatePasswordActivity.this);
                        Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();
                        Intent in = new Intent(CreatePasswordActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();

                    } else if (respcode.equals("300")) {
                        Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();
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


    private boolean validation() {
        boolean valid = true;
        if (etNewPassord.length() == 0) {
            etNewPassord.requestFocus();
            etNewPassord.setError(getResources().getString(R.string.txt_passwordEnter));
            valid = false;
        } else if (etConfirmPassword.getText().toString().trim().length() == 0) {
            etConfirmPassword.requestFocus();
            etConfirmPassword.setError(getResources().getString(R.string.txt_confirmPasswordEnter));
            valid = false;
        } else if (!etConfirmPassword.getText().toString().trim().equalsIgnoreCase(etNewPassord.getText().toString().trim())) {
            etConfirmPassword.requestFocus();
            etConfirmPassword.setError(getResources().getString(R.string.txt_notmatchPassword));
            valid = false;
        }
        return valid;
    }


}
