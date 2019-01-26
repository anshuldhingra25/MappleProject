
package com.findpackers.android.aap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.MyApplication;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.notification.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by narendra on 2/9/2018.
 */

public class LoginActivity extends AppCompatActivity {

    TextView txtSignUp;
    Button btnSignIn;
    TextView txtForgotPassword;
    ImageView btnBack;
    EditText etPhoneNo, etPassword;
    ProgressDialog prgDialog;
    String regId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prgDialog = new ProgressDialog(LoginActivity.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        txtSignUp = (TextView) findViewById(R.id.txt_signup);
        txtForgotPassword = (TextView) findViewById(R.id.txt_forgotpassword);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnBack = (ImageView) findViewById(R.id.verification_back);

        etPhoneNo = (EditText) findViewById(R.id.et_phone_no);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPhoneNo.setFilters(new InputFilter[]{getPhoneFilter(etPhoneNo), new InputFilter.LengthFilter(13)});

        etPhoneNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (etPhoneNo.getText().toString().isEmpty()) {
                        etPhoneNo.setText("+91");
                        Selection.setSelection(etPhoneNo.getText(), etPhoneNo.getText().length());
                    }
                } else {
                    if (etPhoneNo.getText().toString().equalsIgnoreCase("+91")) {
                        etPhoneNo.setFilters(new InputFilter[]{});
                        etPhoneNo.setText("");
                        etPhoneNo.setFilters(new InputFilter[]{getPhoneFilter(etPhoneNo), new InputFilter.LengthFilter(13)});

                    }
                }
            }
        });
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);
//        EditText txtfcm = findViewById(R.id.txtfcm);
//        txtfcm.setText(""+regId);
        Log.e("FierbaceToken :", "Firebase reg id: " + regId);
        // MyPreferences.getActiveInstance(LoginActivity.this).setLeadCityValue(1);
        SpannableString ss = new SpannableString("Don't have an account? Sign Up");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                i.putExtra("firstName", " ");
                i.putExtra("lastName", " ");
                i.putExtra("emailId", " ");
                i.putExtra("etPhoneNo", " ");
                i.putExtra("etPassword", " ");
                i.putExtra("etaddress", " ");
                i.putExtra("etPanNo", " ");
                i.putExtra("etConfirmPassword", " ");
                startActivity(i);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 22, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtSignUp.setText(ss);
        txtSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        txtSignUp.setHighlightColor(Color.TRANSPARENT);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CommanMethod.isOnline(LoginActivity.this)) {

                    boolean valid = validation();
                    if (valid) {
                        RequestParams params = new RequestParams();
                        params.put("phoneNumber", etPhoneNo.getText().toString().trim().substring(3));
                        params.put("password", etPassword.getText().toString().trim());
                        params.put("fcmKey", regId);
                        System.out.println("fcmKey" + regId);

                        loginWS(params, false, etPassword.getText().toString().trim());
                    }

                } else {

                    CommanMethod.showAlert("Network Error,Please try again", LoginActivity.this);
                }

            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }


    public void loginWS(RequestParams params, final boolean active_account, final String password) {
        Log.e("nk", "Login params is>>>>" + params.toString());
        String Url = WebserviceUrlClass.Login;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(10000);
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "Login Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {

                        //  String userId = String.valueOf(response.getInt("userId"));//;

                        if (active_account) {

                            String userId = response.getString("userId");
                            String firstName = response.getString("firstName");
                            String lastName = response.getString("lastName");
                            String emailId = response.getString("email");
                            String address = response.getString("address");
                            String phoneNumber = response.getString("phoneNumber");
//                            String area = response.getString("area");
                            int selectRegion = response.getInt("selectRegion");
                            System.out.println("selectRegionActive===" + selectRegion);
                            MyPreferences.getActiveInstance(LoginActivity.this).setUserId(userId);
                            MyPreferences.getActiveInstance(LoginActivity.this).setFirstName(firstName);
                            MyPreferences.getActiveInstance(LoginActivity.this).setLastName(lastName);
                            MyPreferences.getActiveInstance(LoginActivity.this).setemailId(emailId);
                            MyPreferences.getActiveInstance(LoginActivity.this).setAddress(address);
                            MyPreferences.getActiveInstance(LoginActivity.this).setUpdateValue(1);
//                            MyPreferences.getActiveInstance(LoginActivity.this).setMobile(phoneNumber);
                            if (selectRegion != 0) {
                                MyPreferences.getActiveInstance(LoginActivity.this).setLeadCityValue(selectRegion);
                            } else {
                                MyPreferences.getActiveInstance(LoginActivity.this).setLeadCityValue(3);
                            }

                            Intent in = new Intent(LoginActivity.this, OtpVerificationActivity.class);
                            in.putExtra("password", password);
                            in.putExtra("phoneNumber", phoneNumber);
                            in.putExtra("veifytype", "no");
                            startActivity(in);
                            finish();

                        } else {
                            String userId = response.getString("userId");
                            String firstName = response.getString("firstName");
                            String lastName = response.getString("lastName");
                            String emailId = response.getString("email");
                            String address = response.getString("address");
                            String phoneNumber = response.getString("phoneNumber");
                            int selectRegion = response.getInt("region");
                            System.out.println("selectRegionNotActive===" + selectRegion);
                            MyPreferences.getActiveInstance(LoginActivity.this).setUserId(userId);
                            MyPreferences.getActiveInstance(LoginActivity.this).setFirstName(firstName);
                            MyPreferences.getActiveInstance(LoginActivity.this).setLastName(lastName);
                            MyPreferences.getActiveInstance(LoginActivity.this).setemailId(emailId);
                            MyPreferences.getActiveInstance(LoginActivity.this).setAddress(address);
                            MyPreferences.getActiveInstance(LoginActivity.this).setMobile(phoneNumber);
                            MyPreferences.getActiveInstance(LoginActivity.this).setIsLoggedIn(true);
                            if (selectRegion != 0) {
                                MyPreferences.getActiveInstance(LoginActivity.this).setLeadCityValue(selectRegion);
                            } else {
                                MyPreferences.getActiveInstance(LoginActivity.this).setLeadCityValue(3);
                            }

                            MyPreferences.getActiveInstance(LoginActivity.this).setUpdateValue(1);
                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(in);
                            finish();
                            //Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();
                        }

                    } else if (respcode.equals("0")) {
                        // CommanMethod.showAlert(respMessage,LoginActivity.this);

                        final Dialog dialog = new Dialog(LoginActivity.this);
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

                    } else if (respcode.equals("400")) {
                        // CommanMethod.showAlert(respMessage,LoginActivity.this);
                        String userId = response.getString("userId");
                        String firstName = response.getString("firstName");
                        String lastName = response.getString("lastName");
                        String emailId = response.getString("email");
                        String address = response.getString("address");
                        String phoneNumber = response.getString("phoneNumber");
                        int selectRegion = response.getInt("region");
                        MyPreferences.getActiveInstance(LoginActivity.this).setUserId(userId);
                        MyPreferences.getActiveInstance(LoginActivity.this).setFirstName(firstName);
                        MyPreferences.getActiveInstance(LoginActivity.this).setLastName(lastName);
                        MyPreferences.getActiveInstance(LoginActivity.this).setemailId(emailId);
                        MyPreferences.getActiveInstance(LoginActivity.this).setAddress(address);
                        MyPreferences.getActiveInstance(LoginActivity.this).setMobile(phoneNumber);
                        MyPreferences.getActiveInstance(LoginActivity.this).setIsLoggedIn(true);
                        MyPreferences.getActiveInstance(LoginActivity.this).setLeadCityValue(selectRegion);

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage(respMessage)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();


                                        Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(in);
                                        finish();
                                    }
                                });


                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("account deactivated");
                        alert.show();

                    } else if (respcode.equals("1")) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Do you want to activate your account ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();

                                        if (CommanMethod.isOnline(LoginActivity.this)) {
                                            RequestParams params = new RequestParams();
                                            params.put("phoneNumber", etPhoneNo.getText().toString().trim().substring(3));
                                            params.put("password", etPassword.getText().toString().trim());
                                            params.put("fcmKey", regId);
                                            params.put("active", "1");

                                            loginWS(params, true, etPassword.getText().toString().trim());
                                        } else {

                                            CommanMethod.showAlert("Network Error,Please try again", LoginActivity.this);
                                        }
                                        // get prompts.xml view
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
                        alert.setTitle("Active account");
                        alert.show();


                    } else if (respcode.equals("300")) {
                        String userId = response.getString("userId");
                        String firstName = response.getString("firstName");
                        String lastName = response.getString("lastName");
                        String emailId = response.getString("email");
                        String address = response.getString("address");
                        String phoneNumber = response.getString("phoneNumber");
                        int selectRegion = response.getInt("region");

                        MyPreferences.getActiveInstance(LoginActivity.this).setUserId(userId);
                        MyPreferences.getActiveInstance(LoginActivity.this).setFirstName(firstName);
                        MyPreferences.getActiveInstance(LoginActivity.this).setLastName(lastName);
                        MyPreferences.getActiveInstance(LoginActivity.this).setemailId(emailId);
                        MyPreferences.getActiveInstance(LoginActivity.this).setMobile(phoneNumber);
                        MyPreferences.getActiveInstance(LoginActivity.this).setAddress(address);
                        MyPreferences.getActiveInstance(LoginActivity.this).setLeadCityValue(selectRegion);
                        Intent in = new Intent(LoginActivity.this, OtpVerificationActivity.class);
                        in.putExtra("password", " ");
                        in.putExtra("phoneNumber", phoneNumber);
                        in.putExtra("veifytype", "verifyed");
                        startActivity(in);
                        finish();
                        Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "Please check your login details", Toast.LENGTH_LONG).show();
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
        if (etPhoneNo.length() == 0) {
            etPhoneNo.requestFocus();
            etPhoneNo.setError(getResources().getString(R.string.txt_phoneNoEnter));
            valid = false;
        } else if (etPhoneNo.getText().toString().substring(3).length() < 10) {
            etPhoneNo.requestFocus();
            etPhoneNo.setError(getResources().getString(R.string.txt_validMobileEnter));
            valid = false;
        } else if (etPassword.length() == 0) {
            etPassword.requestFocus();
            etPassword.setError(getResources().getString(R.string.txt_passwordEnter));
            valid = false;
        }
        return valid;
    }

    private InputFilter getPhoneFilter(final EditText editText) {

        Selection.setSelection(editText.getText(), editText.getText().length());

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+91")) {
                    if (editText.getFilters() != null && editText.getFilters().length > 0) {
                        editText.setText("+91");
                        Selection.setSelection(editText.getText(), editText.getText().length());
                    }
                }
            }
        });

        // Input filter to restrict user to enter only digits..
        InputFilter filter = new InputFilter() {

            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {

                    if (!String.valueOf(getString(R.string.digits_number)).contains(String.valueOf(source.charAt(i)))) {
                        return "";
                    }
                }
                return null;
            }
        };
        return filter;
    }

}