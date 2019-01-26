package com.findpackers.android.aap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.SmsListener;
import com.findpackers.android.aap.commanUtill.SmsReceiver;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.notification.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by narendra on 2/15/2018.
 */

public class OtpVerificationActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnSubmit, btnResedOtp;
    EditText etOne, etTwo, etThree, etFour, etFive;
    String otp, pwd = " ", phoneNumber, veifytype;
    ProgressDialog prgDialog;
    String regId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        btnBack = (ImageView) findViewById(R.id.verification_back);


        prgDialog = new ProgressDialog(OtpVerificationActivity.this);
        prgDialog.setMessage("Please wait..");
        prgDialog.setCancelable(false);

        etOne = (EditText) findViewById(R.id.et_one);
        etTwo = (EditText) findViewById(R.id.et_two);
        etThree = (EditText) findViewById(R.id.et_three);
        etFour = (EditText) findViewById(R.id.et_four);
        etFive = (EditText) findViewById(R.id.et_five);

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnResedOtp = (Button) findViewById(R.id.btn_resend_otp);


        pwd = getIntent().getExtras().getString("password", "N/A");
        phoneNumber = getIntent().getExtras().getString("phoneNumber", "N/A");
        veifytype = getIntent().getExtras().getString("veifytype", "N/A");


        if (veifytype.equalsIgnoreCase("verifyed")) {
            veifytype = "";

            RequestParams params = new RequestParams();
            params.put("phoneNumber", phoneNumber);
            resendOtpWebservices(params);


        }
      /*  otp = getIntent().getExtras().getString("otp", "N/A");


        Logger.e("otp :", otp);
        etOne.setText(otp.substring(0).toString().trim());
        etTwo.setText(otp.substring(1).toString().trim());
        etThree.setText(otp.substring(2).toString().trim());
        etFour.setText(otp.substring(3).toString().trim());
        etFive.setText(otp.substring(4).toString().trim());*/

        btnResedOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestParams params = new RequestParams();
                params.put("phoneNumber", phoneNumber);
                resendOtpWebservices(params);


            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommanMethod.isOnline(OtpVerificationActivity.this)) {

                    boolean valid = checkValidationotpverify();
                    if (valid) {

                        if (!pwd.equalsIgnoreCase(" ")) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                            regId = pref.getString("regId", null);

                            RequestParams params = new RequestParams();
                            params.put("phoneNumber", phoneNumber);
                            params.put("password", pwd);
                            params.put("fcmKey", regId);
                            params.put("active", "1");
                            params.put("otp", etOne.getText().toString() + etTwo.getText().toString() + etThree.getText().toString() + etFour.getText().toString() + etFive.getText().toString());
                            System.out.println("otpPrequest :" + params.toString());
                            loginWS(params);

                        } else {
                            RequestParams params = new RequestParams();
                            params.put("userId", MyPreferences.getActiveInstance(OtpVerificationActivity.this).getUserId());// etPhoneNo.getText().toString().trim());
                            params.put("password", pwd);
                            params.put("otp", etOne.getText().toString() + etTwo.getText().toString() + etThree.getText().toString() + etFour.getText().toString() + etFive.getText().toString());
                            if (veifytype.equalsIgnoreCase("registration")) {
                                params.put("otpType", "reg");
                            } else {
                                params.put("otpType", "N/A");
                            }
                            System.out.println("otpPrequest :" + params.toString());
                            VerifayOtpWebServices(params);
                        }


                    }

                } else {

                    CommanMethod.showAlert("Network Error,Please try again", OtpVerificationActivity.this);
                }


            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent newIntent = new Intent(OtpVerificationActivity.this,WelComeActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
*/
                onBackPressed();

            }
        });


        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text", messageText);

                System.out.println("check msg " + messageText);

                if (messageText.startsWith("Use the OTP")) {

                    String msg = messageText.substring(11).trim();
                    Log.e("mymessage", msg);
                    etOne.setText(msg.substring(0).toString().trim());
                    etTwo.setText(msg.substring(1).toString().trim());
                    etThree.setText(msg.substring(2).toString().trim());
                    etFour.setText(msg.substring(3).toString().trim());
                    etFive.setText(msg.substring(4).toString().trim());

                }
            }
        });

        final StringBuilder sb = new StringBuilder();
        etOne.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sb.length() == 0 & etOne.length() == 1) {
                    sb.append(s);
                    etOne.clearFocus();
                    etTwo.requestFocus();
                    etTwo.setCursorVisible(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (sb.length() == 1) {
                    sb.deleteCharAt(0);
                }

            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 0) {
                    etOne.requestFocus();
                }

            }
        });


        etTwo.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sb.length() == 0 & etTwo.length() == 1) {
                    sb.append(s);
                    etTwo.clearFocus();
                    etThree.requestFocus();
                    etThree.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (sb.length() == 1) {
                    sb.deleteCharAt(0);
                }

            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 0) {
                    etOne.requestFocus();
                }
            }
        });

        etThree.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sb.length() == 0 & etThree.length() == 1) {
                    sb.append(s);
                    etThree.clearFocus();
                    etFour.requestFocus();
                    etFour.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (sb.length() == 1) {
                    sb.deleteCharAt(0);
                }

            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 0) {

                    etTwo.requestFocus();
                }

            }
        });
        etFour.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sb.length() == 0 & etFour.length() == 1) {
                    sb.append(s);
                    etFour.clearFocus();
                    etFive.requestFocus();
                    etFive.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (sb.length() == 1) {
                    sb.deleteCharAt(0);
                }
            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 0) {
                    etThree.requestFocus();
                }
            }
        });


        etFive.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sb.length() == 0 & etFour.length() == 1) {
                    sb.append(s);

                    if (CommanMethod.isOnline(OtpVerificationActivity.this)) {

                        boolean valid = checkValidationotpverify();
                        if (valid) {

                            if (!pwd.equalsIgnoreCase(" ")) {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                regId = pref.getString("regId", null);

                                RequestParams params = new RequestParams();
                                params.put("phoneNumber", phoneNumber);
                                params.put("password", pwd);
                                params.put("fcmKey", regId);
                                params.put("active", "1");
                                params.put("otp", etOne.getText().toString() + etTwo.getText().toString() + etThree.getText().toString() + etFour.getText().toString() + etFive.getText().toString());
                                System.out.println("otpPrequest :" + params.toString());
                                loginWS(params);

                            } else {
                                RequestParams params = new RequestParams();
                                params.put("userId", MyPreferences.getActiveInstance(OtpVerificationActivity.this).getUserId());// etPhoneNo.getText().toString().trim());
                                params.put("password", pwd);
                                params.put("otp", etOne.getText().toString() + etTwo.getText().toString() + etThree.getText().toString() + etFour.getText().toString() + etFive.getText().toString());
                                if (veifytype.equalsIgnoreCase("registration")) {
                                    params.put("otpType", "reg");
                                } else {
                                    params.put("otpType", "N/A");
                                }
                                System.out.println("otpPrequest :" + params.toString());
                                VerifayOtpWebServices(params);
                            }


                        }

                    } else {

                        CommanMethod.showAlert("Network Error,Please try again", OtpVerificationActivity.this);
                    }


                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (sb.length() == 1) {
                    sb.deleteCharAt(0);
                }

            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 0) {
                    etFour.requestFocus();


                }

            }
        });


    }


    private boolean checkValidationotpverify() {

        if (etOne.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter valid otp.", OtpVerificationActivity.this);
            return false;
        }
        if (etTwo.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter valid otp.", OtpVerificationActivity.this);
            return false;
        }
        if (etThree.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter valid otp.", OtpVerificationActivity.this);
            return false;
        }
        if (etFour.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter valid otp.", OtpVerificationActivity.this);
            return false;
        }
        if (etFive.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter valid otp.", OtpVerificationActivity.this);
            return false;
        }

        return true;
    }


    public void VerifayOtpWebServices(RequestParams params) {

        String Url = WebserviceUrlClass.verifayOtp;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    // Logger.e("nk", "VerifayOtpWebServices Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");


                    if (respcode.equals("200")) {

                        if (veifytype.equalsIgnoreCase("forget")) {
                            Intent in = new Intent(OtpVerificationActivity.this, CreatePasswordActivity.class);
                            startActivity(in);
                            finish();
                            Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();
                        } else {

                            Intent in = new Intent(OtpVerificationActivity.this, Howtoworkfirsttime.class);
                            startActivity(in);
                            MyPreferences.getActiveInstance(OtpVerificationActivity.this).setIsLoggedIn(true);
                            finish();
                            Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();


                        }
                    } else {
                        Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();
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

    public void loginWS(RequestParams params) {

        String Url = WebserviceUrlClass.Login;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    // Logger.e("nk", "Login Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {
                        String userId = response.getString("userId");
                        String firstName = response.getString("firstName");
                        String lastName = response.getString("lastName");
                        String emailId = response.getString("email");
                        String address = response.getString("address");
                        String phoneNumber = response.getString("phoneNumber");
                        MyPreferences.getActiveInstance(OtpVerificationActivity.this).setUserId(userId);
                        MyPreferences.getActiveInstance(OtpVerificationActivity.this).setFirstName(firstName);
                        MyPreferences.getActiveInstance(OtpVerificationActivity.this).setLastName(lastName);
                        MyPreferences.getActiveInstance(OtpVerificationActivity.this).setemailId(emailId);
                        MyPreferences.getActiveInstance(OtpVerificationActivity.this).setAddress(address);
                        MyPreferences.getActiveInstance(OtpVerificationActivity.this).setMobile(phoneNumber);
                        MyPreferences.getActiveInstance(OtpVerificationActivity.this).setIsLoggedIn(true);
                        Intent in = new Intent(OtpVerificationActivity.this, MainActivity.class);
                        startActivity(in);
                        finish();
                        Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();


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

    public void resendOtpWebservices(RequestParams params) {
        System.out.println(params.toString());
        String Url = WebserviceUrlClass.forgetPassword;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    // Logger.e("nk", "forget Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {
                        Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();
                        String userId = response.getString("userId");
                        MyPreferences.getActiveInstance(OtpVerificationActivity.this).setUserId(userId);


                    } else {
                        Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();

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
        new AlertDialog.Builder(this)
                .setTitle("Did not receive OTP?")
                .setMessage("if you did not receive otp try to login after sometime with sign in option with your mobile number and password")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OtpVerificationActivity.this.finish();
                    }
                })
                .setNegativeButton("Dismiss", null)
                .show();
    }

}
