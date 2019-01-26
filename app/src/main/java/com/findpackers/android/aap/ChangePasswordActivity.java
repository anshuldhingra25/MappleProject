package com.findpackers.android.aap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class ChangePasswordActivity extends AppCompatActivity {


    Toolbar toolbar;
    TextView toolbarTitle,txt_avlableCredit;
    ImageView IvBack;
    Button btn_verify;
    EditText etCurrentPwd,etNewPwd,etConfirmPwd;
    ProgressDialog prgDialog;
    LinearLayout ln_creditbalance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        prgDialog = new ProgressDialog(ChangePasswordActivity.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        txt_avlableCredit = (TextView) findViewById(R.id.txt_avlableCredit);
        IvBack = (ImageView) findViewById(R.id.back_icon);
        etCurrentPwd = (EditText) findViewById(R.id.etCurrentPwd);
        etNewPwd = (EditText) findViewById(R.id.etNewPwd);
        etConfirmPwd = (EditText) findViewById(R.id.etConfirmPwd);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        ln_creditbalance = (LinearLayout) findViewById(R.id.ln_creditbalance);
        setSupportActionBar(toolbar);
        //toolbarTitle.setText("Change Password");
        txt_avlableCredit.setText(MyPreferences.getActiveInstance(ChangePasswordActivity.this).getCreditbalance());

        Log.e("userID", MyPreferences.getActiveInstance(ChangePasswordActivity.this).getUserId());

        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ln_creditbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(ChangePasswordActivity.this, AvlableBlance.class);
                startActivity(in);
            }
        });


        etNewPwd.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etNewPwd.getRight() - etNewPwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {


                        if(etNewPwd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            etNewPwd.setInputType( InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }else {
                            etNewPwd.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                        }
                        etNewPwd.setSelection(etNewPwd.getText().length());

                    }
                }
                return false;
            }
        });

        etConfirmPwd.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etConfirmPwd.getRight() - etConfirmPwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {


                        if(etConfirmPwd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            etConfirmPwd.setInputType( InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }else {
                            etConfirmPwd.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                        }
                        etConfirmPwd.setSelection(etConfirmPwd.getText().length());

                    }
                }
                return false;
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommanMethod.isOnline(ChangePasswordActivity.this)) {


                    if (validation()) {
                        RequestParams params = new RequestParams();
                        params.put("newPassword", etNewPwd.getText().toString().trim());
                        params.put("oldPassword", etCurrentPwd.getText().toString().trim());
                        params.put("userId", MyPreferences.getActiveInstance(ChangePasswordActivity.this).getUserId());
                        changePasswordWebServices(params);
                    }

                } else {

                    CommanMethod.showAlert("Network Error,Please try again", ChangePasswordActivity.this);
                }


            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    public void changePasswordWebServices(RequestParams params) {

        String Url = WebserviceUrlClass.ChangePassword;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "change password Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");


                    if (respcode.equals("200")) {
                        CommanMethod.showAlert(respMessage,ChangePasswordActivity.this);

                        final Dialog dialog = new Dialog(ChangePasswordActivity.this);
                        dialog.setContentView(R.layout.dialog_successfull);
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
                                finish();
                            }
                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);





                    } else if (respcode.equals("0")) {
                        final Dialog dialog = new Dialog(ChangePasswordActivity.this);
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
        if (etCurrentPwd.length() == 0) {
            etCurrentPwd.requestFocus();
            etCurrentPwd.setError(getResources().getString(R.string.txt_CurrentpasswordEnter));
            valid = false;
        } else if (etNewPwd.length()==0) {
            etNewPwd.requestFocus();
            etNewPwd.setError(getResources().getString(R.string.txt_passwordEnter));
            valid = false;
        } else if (etConfirmPwd.length() == 0) {
            etConfirmPwd.requestFocus();
            etConfirmPwd.setError(getResources().getString(R.string.txt_ConfirmpasswordEnter));
            valid = false;
        }else if (!etConfirmPwd.getText().toString().trim().equalsIgnoreCase(etNewPwd.getText().toString().trim())) {
            etConfirmPwd.requestFocus();
            etConfirmPwd.setError(getResources().getString(R.string.txt_notmatchPassword));
            valid = false;
        }
        return valid;
    }
}
