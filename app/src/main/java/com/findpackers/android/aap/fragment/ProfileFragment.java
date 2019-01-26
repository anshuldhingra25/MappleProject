package com.findpackers.android.aap.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.findpackers.android.aap.LoginActivity;
import com.findpackers.android.aap.MainActivity;
import com.findpackers.android.aap.ProfileCategory;
import com.findpackers.android.aap.R;
import com.findpackers.android.aap.RechargeWalletActivity;
import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.HttpClient;
import com.findpackers.android.aap.commanUtill.MyApplication;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.Utility;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.findpackers.android.aap.commanUtill.MyApplication.selectedRegion;


/**
 * Created by narendra on 2/16/2018.
 */

public class ProfileFragment extends Fragment {
    String jsonStr = "";
    TextView txtAddCredit, txtTotalLeads, txtCreditValue, txt_upgradnow, txt_profiletype;
    Button btn_logOut, btn_delete;
    ProgressDialog prgDialog;
    EditText etFirstName, etLastName, etEmailId, etContactNum, et_contectnoSecondry,
            etAddress, et_company_name, et_company_city, et_selectedcity, et_lead_city;
    ImageView ivEditableBtn;
    ImageView imageviewProfile, iv_updateProfile;
    boolean editableType = true;
    Bitmap updatedthumbnail;
    private int REQUEST_CAMERA = 0,
            SELECT_FILE = 1;
    private Button btnSelect;
    private String userChoosenTask;
    File destination;
    String userId;
    double imageName;
    byte[] mData = null;
    boolean editprofile;
    String category_status;
    BottomSheetDialog dialog;
    CheckBox check_standard, check_premium, check_luxry;
    LinearLayout ln_standard, ln_premium, ln_luxry;
    String profilecatogry;
    String[] multiChoiceItems;
    boolean[] checkedItems;
    List<String> multichoicesList;
    List<String> Citylist;
    String[] items;
    HashMap<String, Boolean> mapcity = new HashMap<>();
    private int selectRegion;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        et_lead_city = (EditText) view.findViewById(R.id.et_lead_city);
        et_lead_city.setEnabled(false);
        et_lead_city.setClickable(false);
        et_lead_city.setFocusable(false);
        txtAddCredit = (TextView) view.findViewById(R.id.txt_add_cradit);
        btn_logOut = (Button) view.findViewById(R.id.btn_logOut);
        btn_delete = (Button) view.findViewById(R.id.btn_delete);

        ivEditableBtn = (ImageView) view.findViewById(R.id.btn_editable);
        etFirstName = (EditText) view.findViewById(R.id.et_firstName);
        etLastName = (EditText) view.findViewById(R.id.et_lastName);
        etEmailId = (EditText) view.findViewById(R.id.et_email);
        etContactNum = (EditText) view.findViewById(R.id.et_contectno);
        et_contectnoSecondry = (EditText) view.findViewById(R.id.et_contectnoSecondry);
        etAddress = (EditText) view.findViewById(R.id.et_address);
        et_company_name = (EditText) view.findViewById(R.id.et_company_name);
        et_company_city = (EditText) view.findViewById(R.id.et_company_city);
        et_selectedcity = (EditText) view.findViewById(R.id.et_selectedcity);
        imageviewProfile = (ImageView) view.findViewById(R.id.imageviewProfile);
        iv_updateProfile = (ImageView) view.findViewById(R.id.iv_updateProfile);

        txtTotalLeads = (TextView) view.findViewById(R.id.txt_total_leads);
        txtCreditValue = (TextView) view.findViewById(R.id.txt_cradit_value);
        txt_upgradnow = (TextView) view.findViewById(R.id.txt_upgradnow);
        txt_profiletype = (TextView) view.findViewById(R.id.txt_profiletype);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        userId = MyPreferences.getActiveInstance(getActivity()).getUserId();
        selectRegion = MyPreferences.getActiveInstance(getActivity()).getLeadCityValue();
        System.out.println("selectRegion===" + selectRegion);
        setLeadcityValue();

        //txtTotalLeads.setText(String.valueOf(MyApplication.totalLeads));
        txtCreditValue.setText(MyPreferences.getActiveInstance(getActivity()).getCreditbalance());


        Log.e("userId", userId);
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
        getProfileDataWS(params);

        RequestParams params2 = new RequestParams();
        fetchingCitiesWebservices(params2);
        iv_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
        et_lead_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAreaSelectionDialog();
                //openDropDown(et_select_area);
            }
        });
        imageviewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivity(new Intent(getActivity(), ReferandEarn.class));
            }
        });
        txt_upgradnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilecatogry = "standard";
                if (!category_status.equalsIgnoreCase("3")) {

                    dialog = new BottomSheetDialog(getActivity());
                    dialog.setContentView(R.layout.profile_category_dialog);
                    Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
                    check_standard = (CheckBox) dialog.findViewById(R.id.check_standard);
                    check_premium = (CheckBox) dialog.findViewById(R.id.check_premium);
                    check_luxry = (CheckBox) dialog.findViewById(R.id.check_luxry);
                    ln_standard = (LinearLayout) dialog.findViewById(R.id.ln_standard);
                    ln_premium = (LinearLayout) dialog.findViewById(R.id.ln_premium);
                    ln_luxry = (LinearLayout) dialog.findViewById(R.id.ln_luxry);
                    ln_standard.setVisibility(View.GONE);


                    if (category_status.equalsIgnoreCase("1")) {
                        // ln_premium.setVisibility(View.GONE);
                        ln_luxry.setVisibility(View.VISIBLE);
                        ln_premium.setVisibility(View.VISIBLE);
                    }
                    if (category_status.equalsIgnoreCase("2")) {
                        ln_premium.setVisibility(View.GONE);
                        ln_luxry.setVisibility(View.VISIBLE);
                    }

                    //check_premium.setChecked(true);
                    check_standard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (check_standard.isChecked()) {
                                check_premium.setChecked(false);
                                check_luxry.setChecked(false);
                            }
                        }
                    });
                    check_premium.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (check_premium.isChecked()) {
                                check_standard.setChecked(false);
                                check_luxry.setChecked(false);
                            }
                        }
                    });
                    check_luxry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (check_luxry.isChecked()) {
                                check_standard.setChecked(false);
                                check_premium.setChecked(false);
                            }
                        }
                    });


                    assert btn_submit != null;
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (upgradevalidation()) {
                                Intent in = new Intent(getActivity(), ProfileCategory.class);
                                if (check_standard.isChecked()) {
                                    in.putExtra("Profilecategory", category_status);
                                } else if (check_premium.isChecked()) {
                                    in.putExtra("Profilecategory", "premium");
                                } else if (check_luxry.isChecked() && category_status.equalsIgnoreCase("1")) {
                                    in.putExtra("Profilecategory", "standadrtoluxry");
                                } else {
                                    in.putExtra("Profilecategory", "luxry");
                                }
                                startActivity(in);
                                dialog.dismiss();
                            } else {
                                CommanMethod.showAlert("Please select category ", getActivity());
                            }
                        }
                    });


                    dialog.show();
                }
            }
        });


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_delete.setBackgroundDrawable((ContextCompat.getDrawable(getActivity(), R.drawable.round_button_appcolor)));
                btn_logOut.setBackgroundDrawable((ContextCompat.getDrawable(getActivity(), R.drawable.round_button_delete)));

                btn_delete.setTextColor(Color.parseColor("#ffffff"));
                btn_logOut.setTextColor(Color.parseColor("#2e5d97"));

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to delete this account ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                // get prompts.xml view
                                LayoutInflater li = LayoutInflater.from(getActivity());
                                View promptsView = li.inflate(R.layout.prompts, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        getActivity());
                                // set prompts.xml to alertdialog builder
                                alertDialogBuilder.setView(promptsView);

                                final EditText userInput = (EditText) promptsView
                                        .findViewById(R.id.editTextDialogUserInput);

                                // set dialog message
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        if (CommanMethod.isOnline(getActivity())) {
                                                            RequestParams params = new RequestParams();
                                                            params.put("userId", userId);
                                                            params.put("password", userInput.getText().toString());
                                                            deleteAccountWebservices(params);
                                                        } else {

                                                            CommanMethod.showAlert("Network Error,Please try again", getActivity());
                                                        }
                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // show it
                                alertDialog.show();
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
                alert.setTitle("Delete Account");
                alert.show();


            }

        });
        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_logOut.setBackgroundDrawable((ContextCompat.getDrawable(getActivity(), R.drawable.round_button_appcolor)));
                btn_delete.setBackgroundDrawable((ContextCompat.getDrawable(getActivity(), R.drawable.round_button_delete)));

                btn_delete.setTextColor(Color.parseColor("#2e5d97"));
                btn_logOut.setTextColor(Color.parseColor("#ffffff"));
                if (CommanMethod.isOnline(getActivity())) {

                    if (btn_logOut.getText().toString().equalsIgnoreCase("LogOut")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to log out ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();

                                        if (CommanMethod.isOnline(getActivity())) {
                                            RequestParams params = new RequestParams();
                                            params.put("userId", userId);
                                            logOutWebServices(params);
                                        } else {

                                            CommanMethod.showAlert("Network Error,Please try again", getActivity());
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
                        alert.setTitle("Logger Out");
                        alert.show();

                    } else {

                        if (validation()) {
                            RegistationAsync RegAsynTask = new RegistationAsync();
                            RegAsynTask.execute();
                        }

                    }
                } else {

                    CommanMethod.showAlert("Network Error,Please try again", getActivity());
                }


            }
        });

        txtAddCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), RechargeWalletActivity.class);
                startActivity(in);
            }
        });

        ivEditableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hello", "click");


                if (editprofile) {
                    editprofile = false;
                    etFirstName.setEnabled(false);
                    etLastName.setEnabled(false);
                    etEmailId.setEnabled(false);
                    // etContactNum.setEnabled(false);
                    etAddress.setEnabled(false);
                    et_contectnoSecondry.setEnabled(false);
                    et_company_name.setEnabled(false);
                    et_company_city.setEnabled(false);
                    et_selectedcity.setEnabled(false);
                    et_lead_city.setEnabled(false);
                    et_lead_city.setClickable(false);
                    et_lead_city.setFocusable(false);
                    btn_logOut.setText(R.string.txt_logout);
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                } else {


                    etFirstName.setEnabled(false);
                    etLastName.setEnabled(false);
                    etEmailId.setEnabled(true);
                    et_lead_city.setEnabled(true);
                    et_lead_city.setClickable(true);
                    et_lead_city.setFocusable(false);
                    //etContactNum.setEnabled(true);
                    etAddress.setEnabled(true);
                    et_contectnoSecondry.setEnabled(true);
                    et_company_name.setEnabled(false);
                    et_company_city.setEnabled(false);
                    et_selectedcity.setEnabled(true);

                    btn_logOut.setText("Update");

                    editprofile = true;
                    etFirstName.setFocusableInTouchMode(true);
                    etFirstName.requestFocus();

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    }


                }

                if (etFirstName.isEnabled()) {
                    DrawableCompat.setTint(ivEditableBtn.getDrawable(), ContextCompat.getColor(getActivity(), R.color.gray));

                    iv_updateProfile.setVisibility(view.VISIBLE);
                } else {
                    DrawableCompat.setTint(ivEditableBtn.getDrawable(), ContextCompat.getColor(getActivity(), R.color.app_icon_color));
                    iv_updateProfile.setVisibility(view.INVISIBLE);
                }


            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("navin", "onKey Back listener is working!!!");

                    Intent in = new Intent(getActivity(), MainActivity.class);
                    startActivity(in);
                    getActivity().finish();

                    return true;
                }
                return false;
            }
        });
        et_selectedcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showcitydialog();
            }
        });
        return view;

        //
    }

    private void setLeadcityValue() {
        if (selectRegion == 1) {
            et_lead_city.setText(getResources().getString(R.string.txt_near_banglore));
            jsonStr = "{\"name\":\"bengaluru\",\"latitude\":\"12.972442\",\"longitude\":\"77.580643\"}";
            //jsonStr = "{\"name\":\"bangalore\",\"latitude\":\"12.972442\",\"longitude\":\"77.580643\"}";

        } else if (selectRegion == 2) {
            jsonStr = "{\"name\":\"chennai\",\"latitude\":\"13.067439\",\"longitude\":\"80.237617\"}";
            et_lead_city.setText(getResources().getString(R.string.txt_near_chennai));
        } else if (selectRegion == 3) {
            jsonStr = "{\"name\":\"delhi\",\"latitude\":\"28.644800\",\"longitude\":\"77.216721\"}";
            et_lead_city.setText(getResources().getString(R.string.txt_delhi_ncr));
        } else if (selectRegion == 4) {
            jsonStr = "{\"name\":\"hyderabad\",\"latitude\":\"17.387140\",\"longitude\":\"78.491684\"}";
            et_lead_city.setText(getResources().getString(R.string.txt_hyderabad));
        } else if (selectRegion == 5) {
            jsonStr = "{\"name\":\"kolkata\",\"latitude\":\"22.572645\",\"longitude\":\"88.363892\"}";
            et_lead_city.setText(getResources().getString(R.string.txt_kolkata));
        } else if (selectRegion == 6) {
            jsonStr = "{\"name\":\"mumbai\",\"latitude\":\"19.228825\",\"longitude\":\"72.854118\"}";
            et_lead_city.setText(getResources().getString(R.string.txt_mumbai));
        } else if (selectRegion == 7) {
            jsonStr = "{\"name\":\"pune\",\"latitude\":\"18.516726\",\"longitude\":\"73.856255\"}";
            et_lead_city.setText(getResources().getString(R.string.txt_pune));
        } else if (selectRegion == 8) {
            et_lead_city.setText(getResources().getString(R.string.txt_other));
            jsonStr = "other";
        } else if (selectRegion == 9) {//"Chandigarh", "30.741482", "76.768066");
            et_lead_city.setText(getResources().getString(R.string.txt_near_chandigarh));
            jsonStr = "{\"name\":\"Chandigarh\",\"latitude\":\"30.741482\",\"longitude\":\"76.768066\"}";
        } else if (selectRegion == 10) {//"Jaipur", "26.922070", "75.778885"
            et_lead_city.setText(getResources().getString(R.string.txt_jaipur));
            jsonStr = "{\"name\":\"Jaipur\",\"latitude\":\"26.922070\",\"longitude\":\"75.778885\"}";
        }
        selectedRegion = jsonStr;

    }


    /////////////////

    public void getProfileDataWS(RequestParams params) {

        String Url = WebserviceUrlClass.GetProfileData;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(15000);
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "getProfile Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {

                        String firstName = response.getString("firstName");
                        String lastName = response.getString("lastName");
                        String emailId = response.getString("email");
                        String address = response.getString("address");
                        String companyname = Utility.chkstringnull(response.getString("company_name"));
                        String profileImg = response.getString("profileImg");
                        String contactNo = response.getString("contactNo");
                        String SeccontactNo = response.getString("sec_mobile_number");
                        String creditBalance = response.getString("myCreditBalance");
                        String totalLeads = response.getString("totalLeads");
                        String city = response.getString("city");
                        String area = response.getString("area");
                        //  String area = response.getString("area");
                        setarea(area);
                        category_status = response.getString("category_status");

                        items = city.split(",");
                        Citylist = new ArrayList<String>(Arrays.asList(items));
                        if (category_status.equalsIgnoreCase("1")) {
                            txt_profiletype.setText("Category : Standard");

                        }
                        if (category_status.equalsIgnoreCase("2")) {
                            txt_profiletype.setText("Category : Premium");

                        }
                        if (category_status.equalsIgnoreCase("3")) {
                            txt_profiletype.setText("Category : Luxury");

                        }
                        et_selectedcity.setText(city);
                        et_company_city.setText(city);
                        Picasso.with(getActivity())
                                .load(WebserviceUrlClass.ImageUrl + "" + profileImg)
                                .placeholder(R.mipmap.face)
                                .error(R.mipmap.face)      // optional
                                .resize(400, 400)                        // optional
                                .into(imageviewProfile);
                        etFirstName.setText(firstName);
                        etLastName.setText(lastName);
                        etEmailId.setText(getaNull(emailId));
                        etAddress.setText(getaNull(address));
                        et_company_name.setText(companyname);
                        etContactNum.setText(contactNo);
                        et_contectnoSecondry.setText(getaNull(SeccontactNo));
                        txtTotalLeads.setText(totalLeads);
                        txtCreditValue.setText(creditBalance);
//                        et_lead_city.setText(area);


                        // Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getActivity(), "Please check your login details", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prgDialog.dismiss();
                //  Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                prgDialog.dismiss();
            }

        });
    }

    private void setarea(String area) {
        if (area.toLowerCase().contains("bengaluru".toLowerCase())) {
            et_lead_city.setText(getResources().getString(R.string.txt_near_banglore));
            selectRegion = 1;
        } else if (area.toLowerCase().contains("chennai".toLowerCase())) {
            et_lead_city.setText(getResources().getString(R.string.txt_near_chennai));
            selectRegion = 2;
        } else if (area.toLowerCase().contains("delhi".toLowerCase())) {
            et_lead_city.setText(getResources().getString(R.string.txt_delhi_ncr));
            selectRegion = 3;
        } else if (area.toLowerCase().contains("hyderabad".toLowerCase())) {
            et_lead_city.setText(getResources().getString(R.string.txt_hyderabad));
            selectRegion = 4;
        } else if (area.toLowerCase().contains("kolkata".toLowerCase())) {
            et_lead_city.setText(getResources().getString(R.string.txt_kolkata));
            selectRegion = 5;
        } else if (area.toLowerCase().contains("mumbai".toLowerCase())) {
            et_lead_city.setText(getResources().getString(R.string.txt_mumbai));
            selectRegion = 6;
        } else if (area.toLowerCase().contains("pune".toLowerCase())) {
            et_lead_city.setText(getResources().getString(R.string.txt_pune));
            selectRegion = 7;
        } else if (area.toLowerCase().contains("other".toLowerCase())) {
            et_lead_city.setText(getResources().getString(R.string.txt_other));
            selectRegion = 8;
        } else if (area.toLowerCase().contains("Chandigarh".toLowerCase())) {
            et_lead_city.setText(getResources().getString(R.string.txt_near_chandigarh));
            selectRegion = 9;
        } else if (area.toLowerCase().contains("Jaipur".toLowerCase())) {
            et_lead_city.setText(getResources().getString(R.string.txt_jaipur));
            selectRegion = 10;
        }
        setLeadcityValue();
    }

    private String getaNull(String seccontactNo) {
        return TextUtils.isEmpty(seccontactNo) || seccontactNo.equals("null") ? "" : seccontactNo;
    }

    public void updateProfileDataWS(RequestParams params) {

        String Url = WebserviceUrlClass.UpdateProfileData;
        prgDialog.show();
        Log.e("hello", "start");
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "updateProfile Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {

                        String firstName = response.getString("firstName");
                        String lastName = response.getString("lastName");
                        String emailId = response.getString("email");
                        String address = response.getString("address");
                        String compnyname = Utility.chkstringnull(response.getString("company_name"));
                        String profileImg = response.getString("profileImg");
                        String creditBalance = response.getString("myCreditBalance");
                        String totalLeads = response.getString("totalLeads");

                        etFirstName.setText(firstName);
                        etLastName.setText(lastName);
                        etEmailId.setText(emailId);
                        etAddress.setText(address);
                        et_company_name.setText(compnyname);
                        txtTotalLeads.setText(totalLeads);
                        txtCreditValue.setText(creditBalance);

                        etFirstName.setEnabled(false);
                        etLastName.setEnabled(false);
                        etEmailId.setEnabled(false);
                        etAddress.setEnabled(false);
                        et_company_name.setEnabled(false);
                        et_selectedcity.setEnabled(false);
                        btn_logOut.setText(R.string.txt_logout);


                        //Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getActivity(), "Please check your login details", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                prgDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prgDialog.dismiss();
                //  Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                prgDialog.dismiss();
            }

        });
    }


    public void logOutWebServices(RequestParams params) {

        String Url = WebserviceUrlClass.LogOut;

        prgDialog.show();
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
                        MyPreferences.getActiveInstance(getActivity()).setUserId(" ");
                        MyPreferences.getActiveInstance(getActivity()).setIsLoggedIn(false);

                        MyApplication.filterlead = false;
                        MyApplication.filtercount = 2;
                        MyApplication.sortlead = true;

                        MyApplication.strcheck_3bhk = "";
                        MyApplication.strcheck_1bhk = "";
                        MyApplication.strcheck_2bhk = "";
                        MyApplication.strcheck_lesthen1bhk = "";
                        MyApplication.strcheck_above3bhk = "";
                        MyApplication.strcheck_hot = "";
                        MyApplication.strcheck_active = "";
                        MyApplication.strcheck_sold = "";
                        MyApplication.strcheck_myleads = "";
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

                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();

                    } else if (respcode.equals("300")) {
                        Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                    // cameraIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        updatedthumbnail = thumbnail;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        mData = bytes.toByteArray();
        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageviewProfile.setImageBitmap(thumbnail);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        mData = stream.toByteArray();
        imageviewProfile.setImageBitmap(bm);
    }


    ////////////////////////////////////text Multipart//////////////////////
    public class RegistationAsync extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            System.out.println("in asynctask");
            super.onPreExecute();
            mDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            mDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);

            if (mDialog != null) {
                mDialog.dismiss();
            }
            Log.d("##########Response", "" + response);

            JSONObject object;

            if (response != null) {
                try {
                    object = new JSONObject(response);
                    String success = object.getString("responseCode");
                    String respMessage = object.getString("responseMessage");
                    Log.d("object", "" + object);
                    if (success.equals("200")) {
                        MyPreferences.getActiveInstance(getActivity()).setLeadCityValue(selectRegion);
                        setLeadcityValue();
                        String firstName = object.getString("firstName");
                        String lastName = object.getString("lastName");
                        String emailId = object.getString("email");
                        String address = object.getString("company_address");
                        String compnyname = object.getString("company_name");
                        String profileImg = object.getString("profileImg");
                        String creditBalance = object.getString("myCreditBalance");
                        String totalLeads = object.getString("totalLeads");
                        String sec_mobile_number = object.getString("sec_mobile_number");
                        String city = object.getString("city");
                        et_lead_city.setEnabled(false);
                        et_lead_city.setClickable(false);
                        et_lead_city.setFocusable(false);
                        etFirstName.setText(firstName);
                        etLastName.setText(lastName);
                        etEmailId.setText(emailId);
                        etAddress.setText(address);
                        et_company_name.setText(compnyname);
                        txtTotalLeads.setText(totalLeads);
                        txtCreditValue.setText(creditBalance);
                        et_contectnoSecondry.setText(sec_mobile_number);
                        et_selectedcity.setText(city);

                        etFirstName.setEnabled(false);
                        etLastName.setEnabled(false);
                        etEmailId.setEnabled(false);
                        etContactNum.setEnabled(false);
                        et_contectnoSecondry.setEnabled(false);
                        etAddress.setEnabled(false);
                        et_company_name.setEnabled(false);
                        et_selectedcity.setEnabled(false);
                        btn_logOut.setText(R.string.txt_logout);

                        final Dialog dialog = new Dialog(getActivity());
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

                            }
                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);


                        //Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getActivity(), "Please check your login details", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        private String callService() {
            String url = WebserviceUrlClass.UpdateProfileData;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {
                client.connectForMultipart();
                Log.e("after connection", "" + url);
                client.addFormPart("userId", userId);
                client.addFormPart("firstName", etFirstName.getText().toString().trim());
                client.addFormPart("lastName", etLastName.getText().toString().trim());
                client.addFormPart("email", etEmailId.getText().toString().trim());
                client.addFormPart("phoneNumber", etContactNum.getText().toString().trim());
                client.addFormPart("company_address", etAddress.getText().toString().trim());
                client.addFormPart("sec_mobile_number", et_contectnoSecondry.getText().toString().trim());
                client.addFormPart("company_name", et_company_name.getText().toString().trim());
                client.addFormPart("city", et_selectedcity.getText().toString().trim());
                client.addFormPart("city", et_company_city.getText().toString().trim());
                client.addFormPart("area", jsonStr);
                client.addFormPart("region", String.valueOf(selectRegion));


                Log.e("userId", userId);
                Log.e("firstName", etFirstName.getText().toString().trim());
                Log.e("lastName", etLastName.getText().toString().trim());
                Log.e("email", etEmailId.getText().toString().trim());
                Log.e("phoneNumber", etContactNum.getText().toString().trim());
                Log.e("company_address", etAddress.getText().toString().trim());
                Log.e("sec_mobile_number", et_contectnoSecondry.getText().toString().trim());
                Log.e("company_name", et_company_name.getText().toString().trim());
                Log.e("city", et_selectedcity.getText().toString().trim());
                Log.e("city", et_company_city.getText().toString().trim());
                Log.e("area", jsonStr);
                Log.e("region", String.valueOf(selectRegion));


                if (mData != null) {
                    client.addFilePart("profileImg", imageName + ".jpg", mData);
                    Log.e("imageeeeeeeee", imageName + ".jpg" + ", " + mData);
                }


                Log.d("client", client.toString());
                client.finishMultipart();

                response = client.getResponse().toString();
                Log.d("response", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }
    }

    public void deleteAccountWebservices(RequestParams params) {

        String Url = WebserviceUrlClass.deleteAccount;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "deleteAccountWebservices Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {

                        MyPreferences.getActiveInstance(getActivity()).setUserId(" ");
                        MyPreferences.getActiveInstance(getActivity()).setIsLoggedIn(false);

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
                        MyApplication.strcheck_allleads = "";
                        MyApplication.strcheck_withincity = "";
                        MyApplication.strcheck_outsidecity = "";
                        MyApplication.strcheck_international2 = "";

                        MyApplication.mstr_low_high = "";
                        MyApplication.mstr_high_low = "";
                        MyApplication.mstr_latest = "";
                        MyApplication.mstr_oldest = "";
                        MyApplication.mstr_nearshiftingdate = "";


                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        startActivity(i);
                        getActivity().finish();
                        // Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();


                    } else if (respcode.equals("300")) {
                        CommanMethod.showAlert(respMessage, getActivity());
                    } else {
                        CommanMethod.showAlert(respMessage, getActivity());
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

    private boolean upgradevalidation() {
        boolean valid = false;
        if (check_standard.isChecked()) {
            return true;
        } else if (check_premium.isChecked()) {
            return true;
        } else if (check_luxry.isChecked()) {
            return true;
        } else {
            return valid;
        }


    }


    private boolean validation() {
        boolean valid = true;
        if (etFirstName.length() == 0) {
            etFirstName.requestFocus();
            etFirstName.setError(getResources().getString(R.string.txt_firstNameEnter));
            valid = false;
        } else if (etLastName.length() == 0) {
            etLastName.requestFocus();
            etLastName.setError(getResources().getString(R.string.txt_lastNameEnter));
            valid = false;
        } /*else if (etEmailId.length() == 0 ) {
            etEmailId.requestFocus();
            etEmailId.setError(getResources().getString(R.string.txt_emailIdEnter));
            valid = false;
        }*/ else if (etEmailId.length() != 0 && !CommanMethod.isEmailValid(etEmailId.getText().toString().trim())) {
            etEmailId.requestFocus();
            etEmailId.setError(getResources().getString(R.string.txt_emailIdvalidEnter));
            valid = false;
        } else if (etContactNum.length() == 0) {
            etContactNum.requestFocus();
            etContactNum.setError(getResources().getString(R.string.txt_phoneNoEnter));
            valid = false;
        } else if (etContactNum.length() < 10) {
            etContactNum.requestFocus();
            etContactNum.setError(getResources().getString(R.string.txt_validMobileEnter));
            valid = false;
        } else if (et_contectnoSecondry.length() == 0) {
            et_contectnoSecondry.requestFocus();
            et_contectnoSecondry.setError(getResources().getString(R.string.txt_phoneNoEnter));
            valid = false;
        } else if (et_contectnoSecondry.length() < 10) {
            et_contectnoSecondry.requestFocus();
            et_contectnoSecondry.setError(getResources().getString(R.string.txt_validMobileEnter));
            valid = false;
        } else if (etAddress.length() == 0) {
            etAddress.requestFocus();
            etAddress.setError(getResources().getString(R.string.txt_enteraddress));
            valid = false;
        } else if (et_selectedcity.length() == 0) {
            et_selectedcity.requestFocus();
            et_selectedcity.setError(getResources().getString(R.string.txt_entercity));
            valid = false;
        } else if (et_company_name.length() == 0) {
            et_company_name.requestFocus();
            et_company_name.setError(getResources().getString(R.string.txt_entercompnyname));
            valid = false;
        } else if (et_company_city.length() == 0) {
            et_company_city.requestFocus();
            et_company_city.setError(getResources().getString(R.string.txt_enteraddress));
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


        new android.support.v7.app.AlertDialog.Builder(getActivity())
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
                            et_selectedcity.setText(selectedcity.trim());
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
                        // MyApplication.list.clear();
                        // MyApplication.list2.clear();
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
                        if (items != null) {
                            Citylist = new ArrayList<String>(Arrays.asList(items));
                            if (Citylist.size() > 0) {
                                for (int j = 0; j < Citylist.size(); j++) {
                                    mapcity.put(Citylist.get(j), true);
                                }
                            }
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

    private void showAreaSelectionDialog() {

        final Dialog customDialog;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View customView = inflater.inflate(R.layout.area_list_profile, null);
        customDialog = new Dialog(getActivity());
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(customView);
        ImageView filterCancelIv = customDialog.findViewById(R.id.filter_cancelIv);


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


        setLeadCityValue();
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

                System.out.println("jsonString: " + jsonObject.toString());
                customDialog.dismiss();
                et_lead_city.setText(String.valueOf(selectedRadioButton.getText()));
                jsonStr = jsonObject.toString();
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

    private void setLeadCityValue() {
        switch (selectRegion) {
            case 1:
                //jsonStr = "{\"name\":\"bangalore\",\"latitude\":\"12.972442\",\"longitude\":\"77.580643\"}";
                jsonStr = "{\"name\":\"bengaluru\",\"latitude\":\"12.972442\",\"longitude\":\"77.580643\"}";
                break;
            case 2:
                jsonStr = "{\"name\":\"chennai\",\"latitude\":\"13.067439\",\"longitude\":\"80.237617\"}";
                break;
            case 3:
                jsonStr = "{\"name\":\"delhi\",\"latitude\":\"28.644800\",\"longitude\":\"77.216721\"}";
                break;
            case 4:
                jsonStr = "{\"name\":\"hyderabad\",\"latitude\":\"17.387140\",\"longitude\":\"78.491684\"}";
                break;
            case 5:
                jsonStr = "{\"name\":\"kolkata\",\"latitude\":\"22.572645\",\"longitude\":\"88.363892\"}";
                break;
            case 6:
                jsonStr = "{\"name\":\"mumbai\",\"latitude\":\"19.228825\",\"longitude\":\"72.854118\"}";
                break;
            case 7:
                jsonStr = "{\"name\":\"pune\",\"latitude\":\"18.516726\",\"longitude\":\"73.856255\"}";
                break;
            case 8:
                jsonStr = "other";
            case 9:
                jsonStr = "{\"name\":\"Chandigarh\",\"latitude\":\"30.741482\",\"longitude\":\"76.768066\"}";
            case 10:
                jsonStr = "{\"name\":\"Jaipur\",\"latitude\":\"26.922070\",\"longitude\":\"75.778885\"}";
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

}
