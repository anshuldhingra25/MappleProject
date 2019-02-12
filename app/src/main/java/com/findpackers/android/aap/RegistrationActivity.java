package com.findpackers.android.aap;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.PasswordStrength;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.notification.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by narendra on 2/9/2018.
 */

public class RegistrationActivity extends AppCompatActivity implements TextWatcher,
        AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = RegistrationActivity.class.getName();
    private int selectRegion = -1;
    String selectedState;
    Button btnContinue;
    ProgressDialog prgDialog;
    String[] multiChoiceItems;
    List<String> multichoicesList;
    HashMap<String, Boolean> mapcity = new HashMap<>();
    boolean[] checkedItems;
    MultiSpinnerSearch spinnerSelectArea;
    Spinner area_spinner;
    public static ArrayList<String> citylist = new ArrayList<String>();
    public static ArrayList<Boolean> selectedcitylist = new ArrayList<Boolean>();
    private List<String> selectedCities = new ArrayList<>();


    JSONObject delhijsonObject = new JSONObject();
    JSONObject chennaijsonObject = new JSONObject();
    JSONObject bangalorejsonObject = new JSONObject();
    JSONObject punejsonObject = new JSONObject();
    JSONObject hyderabadjsonObject = new JSONObject();
    JSONObject kolkatajsonObject = new JSONObject();
    JSONObject mumbaijsonObject = new JSONObject();
    JSONObject chandigarhjsonObject = new JSONObject();
    JSONObject jaipurjsonObject = new JSONObject();
    String jsonStr;

    String[] state = {"State", "Andaman and Nicobar Islands", "Andhra Pradesh", " Arunachal Pradesh", "Assam", "Bihar",
            "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa",
            "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka",
            "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
            "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
            "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};

    public RegistrationActivity() {
        super();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
        rl_passwordchecker.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        updatePasswordStrengthView(s.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (etPassword.getText().length() == 0) {
            rl_passwordchecker.setVisibility(View.GONE);
        }
    }

    EditText etFirstName, etLastName, etEmailId, etPhoneNo, et_phone_noOptional, etConfirmPassword, met_painno, met_address,
            met_compnyname, et_select_area, et_company_city;
    EditText etPassword;
    TextView termCondtion, et_city;
    ImageView btnBack;
    Spinner state_spinner;
    CheckBox mchk_termCondtion;
    RelativeLayout rl_passwordchecker;
    boolean showpwd;
    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    String firstName, lastName, email, phoneNumber, password, address, PanNo, cnfpwd, companyCity;
    String regId;
    private List<String> nearMelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);
        btnBack = (ImageView) findViewById(R.id.signup_back);
        spinnerSelectArea = (MultiSpinnerSearch) findViewById(R.id.searchMultiSpinnerArea);
        et_select_area = (EditText) findViewById(R.id.et_select_area);
        et_select_area.setFocusable(false);
        et_company_city = (EditText) findViewById(R.id.et_company_city);
        //  et_select_area.setEnabled(false);
        etFirstName = (EditText) findViewById(R.id.et_firstName);
        etLastName = (EditText) findViewById(R.id.et_lastName);
        etEmailId = (EditText) findViewById(R.id.et_email);
        etPhoneNo = (EditText) findViewById(R.id.et_phone_no);
        // etPhoneNo.setText("+91");
        // Selection.setSelection(etPhoneNo.getText(), etPhoneNo.getText().length());


        et_phone_noOptional = (EditText) findViewById(R.id.et_phone_noOptional);

        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        met_painno = (EditText) findViewById(R.id.et_painno);
        met_address = (EditText) findViewById(R.id.et_address);
        met_compnyname = (EditText) findViewById(R.id.et_compnyname);
        et_city = (TextView) findViewById(R.id.et_city);

        rl_passwordchecker = (RelativeLayout) findViewById(R.id.rl_passwordchecker);
        termCondtion = (TextView) findViewById(R.id.txt_termcondtions);
        mchk_termCondtion = (CheckBox) findViewById(R.id.chk_termCondtion);
        state_spinner = (Spinner) findViewById(R.id.state_spinner);

        btnContinue = (Button) findViewById(R.id.btn_continue);


        termCondtion.setPaintFlags(termCondtion.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        termCondtion.setText("I accept terms and conditions");

        etPassword.addTextChangedListener(this);
        nearMelist = Arrays.asList(getResources().getStringArray(R.array.nearMe_cities));

        prgDialog = new ProgressDialog(RegistrationActivity.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        firstName = getIntent().getExtras().getString("firstName", "N/A");
        lastName = getIntent().getExtras().getString("lastName", "N/A");
        email = getIntent().getExtras().getString("emailId", "N/A");
        phoneNumber = getIntent().getExtras().getString("etPhoneNo", "N/A");
        password = getIntent().getExtras().getString("etPassword", "N/A");
        address = getIntent().getExtras().getString("etaddress", "N/A");
        PanNo = getIntent().getExtras().getString("etPanNo", "N/A");
        cnfpwd = getIntent().getExtras().getString("etConfirmPassword", "N/A");

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

        et_phone_noOptional.setFilters(new InputFilter[]{getPhoneFilter(et_phone_noOptional), new InputFilter.LengthFilter(13)});

        et_phone_noOptional.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (et_phone_noOptional.getText().toString().isEmpty()) {
                        et_phone_noOptional.setText("+91");
                        Selection.setSelection(et_phone_noOptional.getText(), et_phone_noOptional.getText().length());
                    }
                } else {
                    if (et_phone_noOptional.getText().toString().equalsIgnoreCase("+91")) {
                        et_phone_noOptional.setFilters(new InputFilter[]{});
                        et_phone_noOptional.setText("");
                        et_phone_noOptional.setFilters(new InputFilter[]{getPhoneFilter(et_phone_noOptional), new InputFilter.LengthFilter(13)});

                    }
                }
            }
        });

        state_spinner.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, state);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        state_spinner.setAdapter(aa);
        final List<KeyPairBoolData> listArray0 = new ArrayList<>();

        for (int i = 0; i < nearMelist.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(nearMelist.get(i));
            h.setSelected(false);
            listArray0.add(h);
        }
        et_select_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAreaSelectionDialog();
                //openDropDown(et_select_area);
            }
        });
        spinnerSelectArea.setItems(listArray0, -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i(TAG, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                    }
                }
            }
        });


        if (!firstName.equalsIgnoreCase(" ")) {
            etFirstName.setText(firstName);
        }
        if (!lastName.equalsIgnoreCase(" ")) {
            etLastName.setText(lastName);
        }
        if (!email.equalsIgnoreCase(" ")) {
            etEmailId.setText(email);
        }
        if (!phoneNumber.equalsIgnoreCase(" ")) {
            etPhoneNo.setText(phoneNumber);
        }
        if (!address.equalsIgnoreCase(" ")) {
            met_address.setText(address);
        }
        if (!address.equalsIgnoreCase(" ")) {
            met_address.setText(address);
        }
//        if (!companyCity.equalsIgnoreCase(" ")) {
//            et_company_city.setText(companyCity);
//        }

        // Done by Anshul
      /*  if (!PanNo.equalsIgnoreCase(" ")) {
            met_painno.setText(PanNo);
        }*/

        if (!password.equalsIgnoreCase(" ")) {
            etPassword.setText(password);
        }
        if (!cnfpwd.equalsIgnoreCase(" ")) {
            etConfirmPassword.setText(cnfpwd);
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });
        et_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showcitydialog();

            }
        });

        termCondtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RegistrationActivity.this, TermsAndConditionsSignup.class);
                startActivity(i);


                // onBackPressed();

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CommanMethod.isOnline(RegistrationActivity.this)) {
                    boolean valid = validation();
                    if (valid) {

                      /*  Intent in = new Intent(RegistrationActivity.this, RegistationTwoActivity.class);
                        in.putExtra("firstName", etFirstName.getText().toString().trim());
                        in.putExtra("lastName", etLastName.getText().toString().trim());
                        in.putExtra("emailId", etEmailId.getText().toString().trim());
                        in.putExtra("etPhoneNo", etPhoneNo.getText().toString().trim());
                        in.putExtra("etPassword", etPassword.getText().toString().trim());
                        in.putExtra("etaddress", met_address.getText().toString().trim());
                        in.putExtra("etPanNo", met_painno.getText().toString().trim());
                        in.putExtra("etConfirmPassword", etConfirmPassword.getText().toString().trim());
                        startActivity(in);*/

                        RequestParams params = new RequestParams();
                        params.put("firstName", etFirstName.getText().toString().trim());
                        params.put("lastName", etLastName.getText().toString().trim());
                        params.put("email", etEmailId.getText().toString().trim());
                        params.put("phoneNumber", etPhoneNo.getText().toString().trim().substring(3));
                        if (et_phone_noOptional.getText().toString().length() > 3) {
                            params.put("sec_mobile_number", et_phone_noOptional.getText().toString().substring(3));
                        }

                        params.put("password", etPassword.getText().toString().trim());
                        params.put("company_address", met_address.getText().toString().trim());
                        params.put("company_name", met_compnyname.getText().toString().trim());
                        params.put("area", jsonStr);
                        params.put("city", et_company_city.getText().toString().trim());
                        params.put("region", String.valueOf(selectRegion));

//                        params.put("state", selectedState);
                        // params.put("panCardNo", met_painno.getText().toString().trim());
                        params.put("fcmKey", regId);
                        userregestrationWebServices(params);


                    }


                } else {

                    CommanMethod.showAlert("Network Error,Please try again", RegistrationActivity.this);
                }

            }
        });


        etPassword.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {


                        if (etPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        } else {
                            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }
                        etPassword.setSelection(etPassword.getText().length());

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


                        if (etConfirmPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        } else {
                            etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }
                        etConfirmPassword.setSelection(etConfirmPassword.getText().length());

                    }
                }
                return false;
            }
        });

        RequestParams params = new RequestParams();
        fetchingCitiesWebservices(params);
    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        ((TextView) arg0.getChildAt(0)).setTextColor(Color.parseColor("#baeae6e6"));
        selectedState = state[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    private void updatePasswordStrengthView(String password) {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView strengthView = (TextView) findViewById(R.id.password_strength);
        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (password.isEmpty()) {
            strengthView.setText("");
            progressBar.setProgress(0);
            return;
        }

        PasswordStrength str = PasswordStrength.calculateStrength(password);
        strengthView.setText(str.getText(this));
        strengthView.setTextColor(str.getColor());

        progressBar.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (str.getText(this).equals("Weak")) {
            progressBar.setProgress(25);
        } else if (str.getText(this).equals("Medium")) {
            progressBar.setProgress(50);
        } else if (str.getText(this).equals("Strong")) {
            progressBar.setProgress(75);
        } else {
            progressBar.setProgress(100);
        }
    }


    public void userregestrationWebServices(RequestParams params) {

        String Url = WebserviceUrlClass.Registation;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(10000);
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    Log.e("nk", "registration Response is>>>>" + response.toString());
                    String respCode = response.getString("responseCode");
                    String responseMessage = response.getString("responseMessage");
                    if (respCode.equalsIgnoreCase("200")) {
                        String userId = response.getString("userId");
                        String otp = response.getString("otp");
                        String emailId = response.getString("otp");
                        String firstName = response.getString("firstName");
                        String lastName = response.getString("lastName");
                        String address = response.getString("address");


                        MyPreferences.getActiveInstance(RegistrationActivity.this).setFirstName(firstName);
                        MyPreferences.getActiveInstance(RegistrationActivity.this).setLastName(lastName);
                        MyPreferences.getActiveInstance(RegistrationActivity.this).setemailId(emailId);
                        MyPreferences.getActiveInstance(RegistrationActivity.this).setAddress(address);
                        MyPreferences.getActiveInstance(RegistrationActivity.this).setMobile(phoneNumber);
                        MyPreferences.getActiveInstance(RegistrationActivity.this).setLeadCityValue(selectRegion);


                        MyPreferences.getActiveInstance(RegistrationActivity.this).setUserId(userId);
                        // MyPreferences.getActiveInstance(RegistationTwoActivity.this).setemailId(email);
                        Intent i = new Intent(RegistrationActivity.this, OtpVerificationActivity.class);
                        //  i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("otp", otp);
                        i.putExtra("phoneNumber", etPhoneNo.getText().toString().trim());
                        i.putExtra("password", " ");
                        i.putExtra("veifytype", "registration");
                        startActivity(i);
                        finish();
                    } else {

                        final Dialog dialog = new Dialog(RegistrationActivity.this);
                        dialog.setContentView(R.layout.dialog_sorry);
                        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                        TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);
                        // if button is clicked, close the custom dialog

                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;


                        txt_msg.setText(responseMessage);

                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);
                        //CommanMethod.showAlert(responseMessage, RegistationTwoActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prgDialog.dismiss();
                //   Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();

                final Dialog dialog = new Dialog(RegistrationActivity.this);
                dialog.setContentView(R.layout.dialog_sorry);
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);
                // if button is clicked, close the custom dialog

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;


                txt_msg.setText("network connection error");

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.getWindow().setAttributes(lp);
                //CommanMethod.showAlert(responseMessage, RegistationTwoActivity.this);
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
        Matcher matcher = pattern.matcher(met_painno.getText().toString());
        if (etFirstName.length() == 0) {
            etFirstName.requestFocus();
            etFirstName.setError(getResources().getString(R.string.txt_firstNameEnter));
            valid = false;
        } /*else if (etLastName.length() == 0) {
            etLastName.requestFocus();
            etLastName.setError(getResources().getString(R.string.txt_lastNameEnter));
            valid = false;
        }*/ /*else if (etEmailId.length() == 0) {
            etEmailId.requestFocus();
            etEmailId.setError(getResources().getString(R.string.txt_emailIdEnter));
            valid = false;
        } */ else if (!(CommanMethod.isEmailValid(etEmailId.getText().toString().trim())) && etEmailId.length() != 0) {
            etEmailId.requestFocus();
            etEmailId.setError(getResources().getString(R.string.txt_emailIdvalidEnter));
            valid = false;
        } else if (etPhoneNo.length() == 0) {
            etPhoneNo.requestFocus();
            etPhoneNo.setError(getResources().getString(R.string.txt_phoneNoEnter));
            valid = false;
        } else if (met_compnyname.length() == 0) {
            met_compnyname.requestFocus();
            met_compnyname.setError("Please enter company name");
            valid = false;
        } else if (etPhoneNo.getText().toString().substring(3).length() < 10) {
            etPhoneNo.requestFocus();
            etPhoneNo.setError(getResources().getString(R.string.txt_validMobileEnter));
            valid = false;
        } else if (et_phone_noOptional.length() != 3 && et_phone_noOptional.length() > 0 && et_phone_noOptional.getText().toString().substring(3).length() < 10) {
            et_phone_noOptional.requestFocus();
            et_phone_noOptional.setError(getResources().getString(R.string.txt_validMobileEnter));
            valid = false;
        } else if (etPhoneNo.getText().toString().trim().equalsIgnoreCase(et_phone_noOptional.getText().toString().trim())) {
            et_phone_noOptional.requestFocus();
            et_phone_noOptional.setError(getResources().getString(R.string.txt_phonemismatch));
            valid = false;
        } else if (etPassword.length() == 0) {
            etPassword.requestFocus();
            etPassword.setError(getResources().getString(R.string.txt_passwordEnter));
            valid = false;
        } else if (etPassword.length() < 6) {
            etPassword.requestFocus();
            etPassword.setError(getResources().getString(R.string.txt_valid_password));
            valid = false;
        } else if (etConfirmPassword.getText().toString().trim().length() == 0) {
            etConfirmPassword.requestFocus();
            etConfirmPassword.setError(getResources().getString(R.string.txt_confirmPasswordEnter));
            valid = false;
        } else if (!etConfirmPassword.getText().toString().trim().equalsIgnoreCase(etPassword.getText().toString().trim())) {
            etConfirmPassword.requestFocus();
            etConfirmPassword.setError(getResources().getString(R.string.txt_notmatchPassword));
            valid = false;
        } else if (selectRegion == -1) {
            et_select_area.setError(getResources().getString(R.string.select_lead_locations));
            valid = false;
        }
        /*else if (selectedState.equalsIgnoreCase("state")) {
            state_spinner.requestFocus();
            CommanMethod.showAlert("Please select state", RegistrationActivity.this);
            valid = false;
        } else if (et_city.length() == 0) {
            et_city.requestFocus();
            et_city.setError(getResources().getString(R.string.txt_city));
            valid = false;
        }*/
        else if (met_address.length() == 0) {
            met_address.requestFocus();
            met_address.setError(getResources().getString(R.string.txt_entercompanyaddress));
            valid = false;
        } /*else if (met_painno.length() == 0) {
            met_painno.requestFocus();
            met_painno.setError(getResources().getString(R.string.txt_enterpanno));
            valid = false;
        } else if (!matcher.matches()) {
            met_painno.requestFocus();
            met_painno.setError(getResources().getString(R.string.txt_entervalidpanno));
            valid = false;
        }*/ else if (!mchk_termCondtion.isChecked()) {
            CommanMethod.showAlert("Please accept terms and conditions ", RegistrationActivity.this);
            valid = false;
        }
        return valid;
    }

    public void showcitydialog() {

        int pos = 0;
        for (String key : mapcity.keySet()) {
            System.out.println("key : " + key);
            System.out.println("value : " + mapcity.get(key));
            checkedItems[pos] = mapcity.get(key);
            multiChoiceItems[pos] = key;
            pos++;
        }
        multichoicesList = Arrays.asList(multiChoiceItems);


        new AlertDialog.Builder(this)
                .setTitle("Select your favourite Cities")
                .setMultiChoiceItems(multiChoiceItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index, boolean isChecked) {
                        Log.d("MainActivity", "clicked item index is " + index);


                        String currentItem = multichoicesList.get(index);


                        mapcity.put(currentItem, isChecked);


                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String selectedcity = "";
                        for (String key : mapcity.keySet()) {
                            System.out.println("key : " + key);
                            System.out.println("value : " + mapcity.get(key));

                            if (mapcity.get(key) == true) {

                                selectedcity = key + "," + selectedcity;
                            }
                        }


                        if (selectedcity.length() > 0) {
                            selectedcity = selectedcity.substring(0, selectedcity.length() - 1);
                            et_city.setText(selectedcity.trim());
                        }


                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

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
                        //  MyApplication.list.clear();
                        //MyApplication.list2.clear();
                        jsonArray = response.getJSONArray("ciries");
                        // arrayList.clear();
                        checkedItems = new boolean[jsonArray.length()];
                        multiChoiceItems = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            //citylist.add(obj.getString("city").toString().trim());
                            //  selectedcitylist.add(false);
                            mapcity.put(obj.getString("city").toString().trim(), false);

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

    /*Done by anshul*/


    private void showAreaSelectionDialog() {

        final Dialog customDialog;
        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.area_list, null);
        customDialog = new Dialog(RegistrationActivity.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(customView);
        ImageView filterCancelIv = customDialog.findViewById(R.id.filter_cancelIv);

        AppCompatCheckBox CBBangloreRegion = customDialog.findViewById(R.id.cbBangloreRegion);
        AppCompatCheckBox CBChennaiRegion = customDialog.findViewById(R.id.cbChennaiRegion);
        AppCompatCheckBox CBDelhiRegion = customDialog.findViewById(R.id.cbDelhiRegion);
        AppCompatCheckBox CBHyderabadRegion = customDialog.findViewById(R.id.cbHyderabadRegion);
        AppCompatCheckBox CBKolkataRegion = customDialog.findViewById(R.id.cbKolkataRegion);
        AppCompatCheckBox CBMumbaiRegion = customDialog.findViewById(R.id.cbMumbaiRegion);
        AppCompatCheckBox CBPuneRegion = customDialog.findViewById(R.id.cbPuneRegion);
        AppCompatCheckBox CBOtherRegion = customDialog.findViewById(R.id.cbOtherRegion);


        CBBangloreRegion.setOnCheckedChangeListener(this);
        CBChennaiRegion.setOnCheckedChangeListener(this);
        CBDelhiRegion.setOnCheckedChangeListener(this);
        CBHyderabadRegion.setOnCheckedChangeListener(this);
        CBKolkataRegion.setOnCheckedChangeListener(this);
        CBMumbaiRegion.setOnCheckedChangeListener(this);
        CBPuneRegion.setOnCheckedChangeListener(this);
        CBOtherRegion.setOnCheckedChangeListener(this);
        System.out.println("selectRegion==" + selectRegion);
        //  RecyclerView rvAreaList = customDialog.findViewById(R.id.recycler_view_area_name);
        final RadioGroup area_check_listRB = customDialog.findViewById(R.id.area_check_listRB);
        AppCompatRadioButton RBBangloreRegion = customDialog.findViewById(R.id.rbBangloreRegion);
        RBBangloreRegion.setChecked(selectRegion == 1);
        AppCompatRadioButton RBChennaiRegion = customDialog.findViewById(R.id.rbChennaiRegion);
        RBChennaiRegion.setChecked(selectRegion == 2);
        AppCompatRadioButton RBDelhiRegion = customDialog.findViewById(R.id.rbDelhiRegion);
        RBDelhiRegion.setChecked(selectRegion == 3);
        AppCompatRadioButton RBHyderabadRegion = customDialog.findViewById(R.id.rbHyderabadRegion);
        RBHyderabadRegion.setChecked(selectRegion == 4);
        AppCompatRadioButton RBKolkataRegion = customDialog.findViewById(R.id.rbKolkataRegion);
        RBKolkataRegion.setChecked(selectRegion == 5);
        AppCompatRadioButton RBMumbaiRegion = customDialog.findViewById(R.id.rbMumbaiRegion);
        RBMumbaiRegion.setChecked(selectRegion == 6);
        AppCompatRadioButton RBPuneRegion = customDialog.findViewById(R.id.rbPuneRegion);
        RBPuneRegion.setChecked(selectRegion == 7);
        AppCompatRadioButton RBOtherRegion = customDialog.findViewById(R.id.rbOtherRegion);
        RBOtherRegion.setChecked(selectRegion == 8);

        AppCompatRadioButton RBChandigarhRegion = customDialog.findViewById(R.id.rbChandigarhRegion);
        RBChandigarhRegion.setChecked(selectRegion == 9);
        AppCompatRadioButton RBJaipurRegion = customDialog.findViewById(R.id.rbJaipurRegion);
        RBJaipurRegion.setChecked(selectRegion == 10);

        TextView applyFilterTV = customDialog.findViewById(R.id.tv_apply);
        TextView cancelFilterTV = customDialog.findViewById(R.id.tv_cancel);

        applyFilterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                int areaId = area_check_listRB.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = customDialog.findViewById(areaId);

                if (area_check_listRB.getCheckedRadioButtonId() != -1) {
                    Log.e("UnderPerforming==", String.valueOf(selectedRadioButton.getText()));

                    switch (String.valueOf(selectedRadioButton.getText())) {
                        case "Bangalore Region":
                            selectRegion = 1;
                            try {
                                createJson(jsonObject, "bengaluru", "12.972442", "77.580643");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Chandigarh Region":
                            selectRegion = 9;
                            try {
                                createJson(jsonObject, "Chandigarh", "30.741482", "76.768066");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Chennai Region":
                            selectRegion = 2;
                            try {
                                createJson(jsonObject, "chennai", "13.067439", "80.237617");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Delhi-NCR Region":
                            selectRegion = 3;
                            try {
                                createJson(jsonObject, "delhi", "28.644800", "77.216721");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Hyderabad Region":
                            selectRegion = 4;
                            try {
                                createJson(jsonObject, "hyderabad", "17.387140", "78.491684");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Jaipur Region":
                            selectRegion = 10;
                            try {
                                createJson(jsonObject, "Jaipur", "26.922070", "75.778885");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Kolkata Region":
                            selectRegion = 5;
                            try {
                                createJson(jsonObject, "kolkata", "22.572645", "88.363892");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Mumbai Region":
                            selectRegion = 6;
                            try {
                                createJson(jsonObject, "mumbai", "19.228825", "72.854118");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Pune Region":
                            selectRegion = 7;
                            try {
                                createJson(jsonObject, "pune", "18.516726", "73.856255");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Any other city":
                            selectRegion = 8;
                            try {
                                createJson(jsonObject, "other", "0", "0");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }

                }


                JSONArray jsonArray = new JSONArray();
                jsonArray.put(bangalorejsonObject);
                jsonArray.put(chennaijsonObject);
                jsonArray.put(delhijsonObject);
                jsonArray.put(kolkatajsonObject);
                jsonArray.put(punejsonObject);
                jsonArray.put(mumbaijsonObject);
                jsonArray.put(hyderabadjsonObject);
                jsonArray.put(chandigarhjsonObject);
                jsonArray.put(jaipurjsonObject);
                JSONObject studentsObj = new JSONObject();
                try {
                    studentsObj.put("RegionArray", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //      jsonStr = studentsObj.toString();

                System.out.println("jsonString: " + jsonStr);
                System.out.println("jsonString: " + jsonObject.toString());
                customDialog.dismiss();
                et_select_area.setText(String.valueOf(selectedRadioButton.getText()));
                jsonStr = jsonObject.toString();
                et_select_area.setError(null);
            }
        });
        //   setAreaList(rvAreaList);

        cancelFilterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });
        filterCancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });


//        customDialog.getWindow()
//                .setLayout((int) (getScreenWidth(this) * .90), (int) (getScreenHeight(this) * .50));
        customDialog.show();

    }


    /*private void setAreaList(RecyclerView viewVesselsScreenFilterRV) {

        filterAdapterPortNames = new FilterAdapterPortNames(context, (ArrayList<FilterPort>) filterVesselNameList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RegistrationActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewVesselsScreenFilterRV.setLayoutManager(layoutManager);
        viewVesselsScreenFilterRV.setAdapter(filterAdapterPortNames);
    }*/
    public static int getScreenWidth(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cbBangloreRegion:

                if (b) {
                    try {
                        createJson(bangalorejsonObject, "bengaluru", "12.972442", "77.580643");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    selectedCities.add(getString(R.string.banglore_lat_lng));
                } else {
                    removeJson(bangalorejsonObject);
                    selectedCities.remove(getString(R.string.banglore_lat_lng));
                }
                break;

            case R.id.cbChennaiRegion:


                if (b) {
                    try {

                        createJson(chennaijsonObject, "chennai", "13.067439", "80.237617");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    selectedCities.add(getString(R.string.chennai_lat_lng));
                } else {
                    removeJson(chennaijsonObject);
                    selectedCities.remove(getString(R.string.chennai_lat_lng));
                }
                break;

            case R.id.cbDelhiRegion:

                if (b) {
                    try {
                        createJson(delhijsonObject, "delhi", "28.644800", "77.216721");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    selectedCities.add(getString(R.string.delhi_lat_lng));
                } else {
                    removeJson(chennaijsonObject);
                    selectedCities.remove(getString(R.string.delhi_lat_lng));
                }
                break;
            case R.id.cbHyderabadRegion:
                if (b) {
                    try {
                        createJson(hyderabadjsonObject, "hyderabad", "17.387140", "78.491684");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    selectedCities.add(getString(R.string.hyderabad_lat_lng));
                } else {
                    removeJson(hyderabadjsonObject);
                    selectedCities.remove(getString(R.string.hyderabad_lat_lng));
                }
                break;

            case R.id.cbKolkataRegion:
                if (b) {
                    try {
                        createJson(kolkatajsonObject, "kolkata", "22.572645", "88.363892");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    selectedCities.add(getString(R.string.kolkata_lat_lng));
                } else {
                    removeJson(kolkatajsonObject);
                    selectedCities.remove(getString(R.string.kolkata_lat_lng));
                }
                break;

            case R.id.cbMumbaiRegion:
                if (b) {
                    try {
                        createJson(mumbaijsonObject, "mumbai", "19.228825", "72.854118");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    selectedCities.add(getString(R.string.mumbai_lat_lng));
                } else {
                    removeJson(mumbaijsonObject);
                    selectedCities.remove(getString(R.string.mumbai_lat_lng));
                }
                break;
            case R.id.cbPuneRegion:
                if (b) {
                    try {
                        createJson(punejsonObject, "pune", "18.516726", "73.856255");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    selectedCities.add(getString(R.string.pune_lat_lng));
                } else {
                    removeJson(punejsonObject);
                    selectedCities.remove(getString(R.string.pune_lat_lng));
                }
                break;
            case R.id.cbOtherRegion:
                if (b) {
                    selectedCities.add("other");
                } else {
                    selectedCities.remove("other");
                }
                break;
            default:
                break;
        }
    }

    private void createJson(JSONObject jsonObject, String name, String lat, String lng) throws JSONException {
        jsonObject.put("name", name);
        jsonObject.put("latitude", lat);
        jsonObject.put("longitude", lng);
    }

    private void removeJson(JSONObject jsonObject) {
        jsonObject.remove("name");
        jsonObject.remove("latitude");
        jsonObject.remove("longitude");
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
