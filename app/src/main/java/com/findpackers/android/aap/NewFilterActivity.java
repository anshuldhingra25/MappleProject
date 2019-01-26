package com.findpackers.android.aap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.findpackers.android.aap.commanUtill.GpsTracker;
import com.findpackers.android.aap.commanUtill.MyApplication;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.notification.MyFirebaseMessagingService;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.androidbuts.multispinnerfilter.MultiSpinnerSearch.Citylist;
import static com.findpackers.android.aap.commanUtill.MyApplication.bydefaulselectedcity;
import static com.findpackers.android.aap.commanUtill.MyApplication.selectedRegion;

public class NewFilterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    String jsonStr = "";
    private int selectRegion;
    Toolbar toolbar;
    TextView toolbarTitle, Reset;
    ImageView IvBack;
    ProgressDialog prgDialog;
    FlowLayout daynamicallyaddtext;
    LinearLayout lnspineer;
    MultiSpinnerSearch searchMultiSpinnerUnlimited;
    int dynamicId = 101;
    Spinner spn_leadtype;
    LinearLayout ln_leadtype;
    Button btn_todayhotlead, btn_active, btn_sold, btn_mylead;
    Button btn_remaninglead, btn_alllead, btn_withincity, btn_outsidecity, btn_international, btn_nearby;
    Button btn_1bhk, btn_2bhk, btn_3bhk, btn_lessthenibhk, btn_above3bhk, btn_cancel, btn_apply, btn_bike, btn_car, btn_applycites;

    boolean bbtn_todayhotlead, bbtn_active, bbtn_sold, bbtn_mylead, bbtn_remaninglead, bbtn_alllead, bbtn_withincity, bbtn_outsidecity, bbtn_international, bbtn_nearby, bbtn_selectcity = true;
    boolean bbtn_1bhk, bbtn_2bhk, bbtn_3bhk, bbtn_lessthenibhk, bstrsnleadtype, bbtn_above3bhk, bbtn_bike, bbtn_car, bbtn_cancel, bbtn_apply;


    public String cstrcheck_3bhk = "", cstrcheck_1bhk = "", cstrcheck_2bhk = "", cstrcheck_lesthen1bhk = "", cstrcheck_above3bhk = "";
    public String cstrcheck_hot = "", cstrcheck_active = "", cstrcheck_sold = "", cstrcheck_myleads = "", cstrcheck_remainlead = "", cstrcheck_allleads = "", cstrcheck_withincity = "", cstrcheck_outsidecity = "", cstrcheck_international2 = "", cstrcheck_nearby = "", cstrcheck_selectcity = "y";
    public String cstrcheck_car = "", cstrcheck_bike = "", selectedleadtype = "";

    public boolean setpreviousdata;

    ArrayAdapter<String> spinnerArrayAdapter;
    public Set mselectdcity = new HashSet();
    com.toptoche.searchablespinnerlibrary.SearchableSpinner spinner;
    List<KeyPairBoolData> listArray0 = new ArrayList<>();
    String[] leadtype = {"Select lead type", "Standard", "Premium", "Luxury"};
    String[] leadtype1 = {"Select lead type", "Standard", "Premium"};
    String[] leadtype2 = {"Select lead type", "Standard"};
    RadioGroup area_check_listRB;
    AppCompatRadioButton RBBangloreRegion, RBChennaiRegion, RBDelhiRegion, RBHyderabadRegion, RBKolkataRegion,
            RBMumbaiRegion, RBPuneRegion, RBOtherRegion, RBChandigarhRegion, RBJaipurRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_filter);

        initializeViews();

        // btn_applyfilter = (Button) findViewById(R.id.btn_applyfilter);
        performSelectRegionOperation();

        setClickListenersOnViews();

        System.out.println("strcheck_myleads :" + MyApplication.strcheck_myleads.equals("y"));

        searchMultiSpinnerUnlimited = (MultiSpinnerSearch) findViewById(R.id.searchMultiSpinnerUnlimited);

        MultiSpinnerSearch.staticmselectdcity.clear();
        ;
        ArrayAdapter aa;

        System.out.println("category_status" + MyApplication.category_status);
        if (MyApplication.spl_status.equalsIgnoreCase("1")) {
            if (MyApplication.category_status.equalsIgnoreCase("3")) {
                aa = new ArrayAdapter(NewFilterActivity.this, android.R.layout.simple_spinner_item, leadtype);

            } else if (MyApplication.category_status.equalsIgnoreCase("2")) {
                aa = new ArrayAdapter(NewFilterActivity.this, android.R.layout.simple_spinner_item, leadtype1);

            } else {
                aa = new ArrayAdapter(NewFilterActivity.this, android.R.layout.simple_spinner_item, leadtype2);

            }
        } else {
            aa = new ArrayAdapter(NewFilterActivity.this, android.R.layout.simple_spinner_item, leadtype);
        }
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_leadtype.setAdapter(aa);


        spn_leadtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedleadtype = String.valueOf(position);

                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#8f8d8d"));
                    ln_leadtype.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners_normal));
                    MyApplication.strspiner_leadtype = "n";

                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#2e5d97"));
                    ln_leadtype.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners));


                }

                if (position == 1) {
                    MyApplication.strspiner_leadtype = "s";
                }
                if (position == 2) {
                    MyApplication.strspiner_leadtype = "p";
                }
                if (position == 3) {
                    MyApplication.strspiner_leadtype = "l";
                }
                System.out.println("selectedleadtype :" + MyApplication.strspiner_leadtype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        additeminspinner();


        Iterator it = MyApplication.mapselectdcity.iterator();

        while (it.hasNext()) {
            String value = (String) it.next();
            mselectdcity.add(value);
            // MultiSpinnerSearch.staticmselectdcity.add(value);
            lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners));
            addtext(value);
            System.out.println("Value :" + value);
        }

        String[] items = MyApplication.usercities.split(",");
        Citylist = new ArrayList<String>(Arrays.asList(items));

        System.out.println("Citylist :" + Citylist);

        if (!bydefaulselectedcity) {
            bydefaulselectedcity = true;
            for (int ii = 0; ii < Citylist.size(); ii++) {
                String value = Citylist.get(ii);
                mselectdcity.add(value);
                // MultiSpinnerSearch.staticmselectdcity.add(value);
                lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners_normal));
                addtextdefaultselected(value);
                System.out.println("Value :" + value);
            }
        } else if (MyApplication.mapselectdcity.size() == 0) {
            for (int ii = 0; ii < Citylist.size(); ii++) {
                String value = Citylist.get(ii);
                mselectdcity.add(value);
                // MultiSpinnerSearch.staticmselectdcity.add(value);
                lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners));
                addtextdefaultselected(value);
                System.out.println("Value :" + value);
            }
        }


        if (MyApplication.bydefaultfeilter) {

            bbtn_alllead = true;
            btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_alllead.setTextColor(Color.parseColor("#2e5d97"));
            // MyApplication.strcheck_allleads="y";
            cstrcheck_allleads = "n";
        }

        if (MyApplication.strcheck_hot.equalsIgnoreCase("y")) {
            bbtn_todayhotlead = true;
            btn_todayhotlead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_todayhotlead.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_hot = "y";
        }
        if (MyApplication.strcheck_active.equals("y")) {
            bbtn_active = true;
            btn_active.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_active.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_active = "y";
        }
        if (MyApplication.strcheck_sold.equals("y")) {
            bbtn_sold = true;
            btn_sold.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_sold.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_sold = "y";
        }
        if (MyApplication.strcheck_myleads.equals("y")) {
            bbtn_mylead = true;
            btn_mylead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_mylead.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_myleads = "y";
        }
        if (MyApplication.strcheck_remainlead.equals("y")) {
            bbtn_remaninglead = true;
            btn_remaninglead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_remaninglead.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_remainlead = "y";

        }
        if (MyApplication.strcheck_allleads.equals("y")) {
            bbtn_alllead = true;
            btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_alllead.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_allleads = "y";
        }


        if (MyApplication.strcheck_withincity.equals("y")) {
            bbtn_withincity = true;
            btn_withincity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_withincity.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_withincity = "y";
        }
        if (MyApplication.strcheck_outsidecity.equals("y")) {
            bbtn_outsidecity = true;
            btn_outsidecity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_outsidecity.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_outsidecity = "y";
        }
        if (MyApplication.strcheck_international2.equals("y")) {
            bbtn_international = true;
            btn_international.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_international.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_international2 = "y";
        }
        if (MyApplication.strcheck_nearby.equals("y")) {
            bbtn_nearby = true;
            btn_nearby.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_nearby.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_nearby = "y";
        }
        if (MyApplication.strcheck_selectcity.equals("y")) {
            bbtn_selectcity = true;
            cstrcheck_selectcity = "y";
        }
        if (MyApplication.strcheck_1bhk.equals("y")) {
            bbtn_1bhk = true;
            btn_1bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_1bhk.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_1bhk = "y";
        }
        if (MyApplication.strcheck_2bhk.equals("y")) {
            bbtn_2bhk = true;
            btn_2bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_2bhk.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_2bhk = "y";
        }
        if (MyApplication.strcheck_3bhk.equals("y")) {
            bbtn_3bhk = true;
            btn_3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_3bhk.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_3bhk = "y";
        }
        if (MyApplication.strcheck_lesthen1bhk.equals("y")) {
            bbtn_lessthenibhk = true;
            btn_lessthenibhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_lessthenibhk.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_lesthen1bhk = "y";
        }
        if (MyApplication.strcheck_above3bhk.equals("y")) {
            bbtn_above3bhk = true;
            btn_above3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_above3bhk.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_above3bhk = "y";
        }

        if (MyApplication.strcheck_bike.equals("y")) {
            bbtn_bike = true;
            btn_bike.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_bike.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_bike = "y";
        }
        if (MyApplication.strcheck_car.equals("y")) {
            bbtn_car = true;
            btn_car.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
            btn_car.setTextColor(Color.parseColor("#2e5d97"));
            cstrcheck_car = "y";
        }

        if (MyApplication.strspiner_leadtype.equals("n")) {
            bstrsnleadtype = false;

        }
        if (MyApplication.strspiner_leadtype.equals("s")) {

            bstrsnleadtype = true;
            spn_leadtype.setSelection(1);

        }
        if (MyApplication.strspiner_leadtype.equals("p")) {
            bstrsnleadtype = true;
            spn_leadtype.setSelection(2);


        }
        if (MyApplication.strspiner_leadtype.equals("l")) {
            bstrsnleadtype = true;
            spn_leadtype.setSelection(3);


        }
        if (!MyApplication.mcities.equals("")) {
            bbtn_selectcity = true;


        }

        // activateActiveLeads();
    }

    private void initializeViews() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_titlefilter);
        Reset = (TextView) findViewById(R.id.txt_reset);
        IvBack = (ImageView) findViewById(R.id.back_icon);
        spn_leadtype = (Spinner) findViewById(R.id.spn_leadtype);
        ln_leadtype = (LinearLayout) findViewById(R.id.ln_leadtype);


        btn_todayhotlead = (Button) findViewById(R.id.btn_todayhotlead);
        btn_active = (Button) findViewById(R.id.btn_active);

        btn_sold = (Button) findViewById(R.id.btn_sold);
        btn_mylead = (Button) findViewById(R.id.btn_mylead);
        btn_remaninglead = (Button) findViewById(R.id.btn_remaninglead);
        btn_alllead = (Button) findViewById(R.id.btn_alllead);

        btn_withincity = (Button) findViewById(R.id.btn_withincity);
        btn_outsidecity = (Button) findViewById(R.id.btn_outsidecity);
        btn_international = (Button) findViewById(R.id.btn_international);
        btn_nearby = (Button) findViewById(R.id.btn_nearby);


        lnspineer = (LinearLayout) findViewById(R.id.lnspineer);


        btn_1bhk = (Button) findViewById(R.id.btn_1bhk);
        btn_2bhk = (Button) findViewById(R.id.btn_2bhk);
        btn_3bhk = (Button) findViewById(R.id.btn_3bhk);
        btn_lessthenibhk = (Button) findViewById(R.id.btn_lessthenibhk);

        btn_above3bhk = (Button) findViewById(R.id.btn_above3bhk);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_apply = (Button) findViewById(R.id.btn_apply);
        btn_bike = (Button) findViewById(R.id.btn_bike);
        btn_car = (Button) findViewById(R.id.btn_car);
        btn_applycites = (Button) findViewById(R.id.btn_applycites);
        daynamicallyaddtext = (FlowLayout) findViewById(R.id.add_city);
    }

    private void performSelectRegionOperation() {
        selectRegion = MyPreferences.getActiveInstance(NewFilterActivity.this).getLeadCityValue();
        System.out.println("Region :" + selectRegion);
        area_check_listRB = findViewById(R.id.area_check_listRB);
        RBBangloreRegion = findViewById(R.id.rbBangloreRegion);

        RBChennaiRegion = findViewById(R.id.rbChennaiRegion);

        RBDelhiRegion = findViewById(R.id.rbDelhiRegion);

        RBHyderabadRegion = findViewById(R.id.rbHyderabadRegion);

        RBKolkataRegion = findViewById(R.id.rbKolkataRegion);

        RBMumbaiRegion = findViewById(R.id.rbMumbaiRegion);

        RBPuneRegion = findViewById(R.id.rbPuneRegion);

        RBOtherRegion = findViewById(R.id.rbOtherRegion);

        RBChandigarhRegion = findViewById(R.id.rbChandigarhRegion);
        RBJaipurRegion = findViewById(R.id.rbJaipurRegion);

        RBBangloreRegion.setChecked(selectRegion == 1);
        RBChennaiRegion.setChecked(selectRegion == 2);
        RBDelhiRegion.setChecked(selectRegion == 3);
        RBHyderabadRegion.setChecked(selectRegion == 4);
        RBKolkataRegion.setChecked(selectRegion == 5);
        RBMumbaiRegion.setChecked(selectRegion == 6);
        RBPuneRegion.setChecked(selectRegion == 7);
        RBOtherRegion.setChecked(selectRegion == 8);
        RBChandigarhRegion.setChecked(selectRegion == 9);
        RBJaipurRegion.setChecked(selectRegion == 10);
        if (selectedRegion.equalsIgnoreCase("")) {
            setLeadCityValue();
        } else {
            setLeadCityValueSelected(selectedRegion);
        }
    }

    private void setClickListenersOnViews() {
        btn_todayhotlead.setOnClickListener(this);
        btn_active.setOnClickListener(this);
        btn_sold.setOnClickListener(this);
        btn_mylead.setOnClickListener(this);
        btn_remaninglead.setOnClickListener(this);
        btn_alllead.setOnClickListener(this);
        btn_withincity.setOnClickListener(this);
        btn_outsidecity.setOnClickListener(this);
        btn_international.setOnClickListener(this);
        btn_nearby.setOnClickListener(this);
        btn_applycites.setOnClickListener(this);


        btn_1bhk.setOnClickListener(this);
        btn_2bhk.setOnClickListener(this);
        btn_3bhk.setOnClickListener(this);
        btn_lessthenibhk.setOnClickListener(this);
        btn_above3bhk.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
        btn_bike.setOnClickListener(this);
        btn_car.setOnClickListener(this);
        Reset.setOnClickListener(this);
        IvBack.setOnClickListener(this);
    }


    public void additeminspinner() {

        String[] items = MyApplication.usercities.split(",");
        Citylist = new ArrayList<String>(Arrays.asList(items));
        listArray0.clear();
        for (int i = 0; i < MyApplication.list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(MyApplication.list.get(i));
            if (Citylist.contains(MyApplication.list.get(i))) {
                h.setSelected(true);
            } else {

            }
            // h.setSelected(true);
            listArray0.add(h);
            // MyApplication.mapselectdcity.add(MyApplication.list.get(i));
        }


        searchMultiSpinnerUnlimited.setItems(listArray0, -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                if (!MultiSpinnerSearch.cancelflag) {
                    MyApplication.mcities = "";
                    setpreviousdata = true;

                    mselectdcity.clear();
                    daynamicallyaddtext.removeAllViews();
                    System.out.println("selectedItem" + items);


                    if (setpreviousdata) {
                        setpreviousdata = false;
                        Iterator it = MyApplication.mapselectdcity.iterator();

                        while (it.hasNext()) {
                            String value = (String) it.next();
                            mselectdcity.add(value);
                            lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners));
                            addtext(value);

                            System.out.println("Value :" + value);
                        }

                        if (mselectdcity.size() <= 0) {
                            lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners_normal));

                        }
                    }


                    for (int i = 0; i < items.size(); i++) {

                        System.out.println(" all items.get(i).getName()" + items.get(i).getName());
                        if (items.get(i).isSelected()) {
                            System.out.println(" all items.get(i).getName()" + items.get(i).getName());

                            btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                            btn_alllead.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners_normal));
                            bbtn_alllead = false;
                            // MyApplication.strcheck_allleads="n";
                            cstrcheck_allleads = "n";
                            if (mselectdcity.contains(items.get(i).getName())) {
                                System.out.println("map" + "coming in map");


                            } else {
                                lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners));


                                addtextfromlocal(items.get(i).getName());

                            }

                        } else {

                        }
                    }

                } else {

                    // searchMultiSpinnerUnlimited.setItemsclear(listArray0);
                }
            }

        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
        btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_alllead = false;
        System.out.println("selected default city selected top " + selectedItem);
        //MyApplication.strcheck_allleads="n";
        cstrcheck_allleads = "n";
        if (mselectdcity.contains(selectedItem)) {
            System.out.println("map" + "coming in map");

            Toast.makeText(getApplicationContext(), "already selected .", Toast.LENGTH_LONG).show();


        } else {

            System.out.println("selected default city selected " + selectedItem);
            for (int ii = 0; ii < Citylist.size(); ii++) {
                String value = Citylist.get(ii);
                System.out.println("selected default city" + value);

                addtextfromlocal(value);

            }
            addtextfromlocal(selectedItem);
        }


        //addtext(selectedItem);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        System.out.println("selectedItem" + "no data");


    }

    private void setLeadCityValueSelected(String area) {
        if (area.toLowerCase().contains("bengaluru".toLowerCase())) {
            RBBangloreRegion.setChecked(true);
            selectRegion = 1;

        } else if (area.toLowerCase().contains("chennai".toLowerCase())) {
            RBChennaiRegion.setChecked(true);
            selectRegion = 2;
        } else if (area.toLowerCase().contains("delhi".toLowerCase())) {
            RBDelhiRegion.setChecked(true);
            selectRegion = 3;
        } else if (area.toLowerCase().contains("hyderabad".toLowerCase())) {
            RBHyderabadRegion.setChecked(true);
            selectRegion = 4;
        } else if (area.toLowerCase().contains("kolkata".toLowerCase())) {
            RBKolkataRegion.setChecked(true);
            selectRegion = 5;
        } else if (area.toLowerCase().contains("mumbai".toLowerCase())) {
            RBMumbaiRegion.setChecked(true);
            selectRegion = 6;
        } else if (area.toLowerCase().contains("pune".toLowerCase())) {
            RBPuneRegion.setChecked(true);
            selectRegion = 7;
        } else if (area.toLowerCase().contains("other".toLowerCase())) {
            RBOtherRegion.setChecked(true);
            selectRegion = 8;
        } else if (area.toLowerCase().contains("Chandigarh".toLowerCase())) {
            RBChandigarhRegion.setChecked(true);
            selectRegion = 9;
        } else if (area.toLowerCase().contains("Jaipur".toLowerCase())) {
            RBJaipurRegion.setChecked(true);
            selectRegion = 10;
        }
    }

    private View.OnClickListener mThisButtonListener = new View.OnClickListener() {
        public void onClick(View view) {


            if (Citylist.contains(String.valueOf(view.getTag()))) {

                Toast.makeText(getApplicationContext(), "You can not remove primary city .", Toast.LENGTH_LONG).show();
            } else {
                daynamicallyaddtext.removeView(findViewById(view.getId()));
                System.out.println("clicked item" + view.getId());
                System.out.println("clicked item vlaue " + String.valueOf(view.getTag()));

                MyApplication.mapselectdcity.remove(String.valueOf(view.getTag()));
                mselectdcity.remove(String.valueOf(view.getTag()));
                // MultiSpinnerSearch.staticmselectdcity.remove(String.valueOf(view.getTag()));

                MultiSpinnerSearch.staticmselectdcity.add(String.valueOf(view.getTag()));
            /*if(MyApplication.list.contains(String.valueOf(view.getTag())))
            {

            }else{
                MyApplication.list.add(String.valueOf(view.getTag()));
            }*/


                System.out.println("seleted city count " + daynamicallyaddtext.getChildCount());
                if (daynamicallyaddtext.getChildCount() <= 0) {
                    lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners_normal));
                    mselectdcity.clear();
                    //MultiSpinnerSearch.staticmselectdcity.clear();
                }

                String.valueOf(view.getTag());
            }
        }
    };

    public void addtext(String selectedItem) {
        // create a new textview
        final TextView rowTextView = new TextView(this);
        // set some properties of rowTextView or something
        rowTextView.setText(selectedItem);
        rowTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.cancelcity, 0);
        rowTextView.setBackgroundResource(R.drawable.button_bg_rounded_corners);
        rowTextView.setId(dynamicId);
        rowTextView.setCompoundDrawablePadding(10);
        rowTextView.setPadding(10, 5, 10, 5);
        rowTextView.setTextSize(12);
        rowTextView.setTextColor(Color.parseColor("#2e5d97"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(7, 7, 7, 7);
        rowTextView.setLayoutParams(params);
        rowTextView.setTag(selectedItem);
        rowTextView.setOnClickListener(mThisButtonListener);
        daynamicallyaddtext.addView(rowTextView);


        //  MyApplication.mapselectdcity.add(selectedItem);


        dynamicId++;


        // MyApplication.list.remove(selectedItem);


    }

    public void addtextdefaultselected(String selectedItem) {
        // create a new textview
        final TextView rowTextView = new TextView(this);
        // set some properties of rowTextView or something
        rowTextView.setText(selectedItem);
        rowTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        // rowTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.cancelcity, 0);
        rowTextView.setBackgroundResource(R.drawable.button_bg_rounded_corners);
        rowTextView.setId(dynamicId);
        rowTextView.setCompoundDrawablePadding(10);
        rowTextView.setPadding(10, 5, 10, 5);
        rowTextView.setTextSize(12);
        rowTextView.setTextColor(Color.parseColor("#8f8d8d"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(7, 7, 7, 7);
        rowTextView.setLayoutParams(params);
        rowTextView.setTag(selectedItem);
        rowTextView.setOnClickListener(mThisButtonListener);
        daynamicallyaddtext.addView(rowTextView);


        //  MyApplication.mapselectdcity.add(selectedItem);


        dynamicId++;


        // MyApplication.list.remove(selectedItem);


    }

    public void addtextfromlocal(String selectedItem) {
        // create a new textview


        final TextView rowTextView = new TextView(this);
        // set some properties of rowTextView or something
        rowTextView.setText(selectedItem);
        rowTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.cancelcity, 0);
        rowTextView.setBackgroundResource(R.drawable.button_bg_rounded_corners_normal);
        rowTextView.setId(dynamicId);
        rowTextView.setCompoundDrawablePadding(10);
        rowTextView.setPadding(10, 5, 10, 5);
        rowTextView.setTextSize(12);
        rowTextView.setTextColor(Color.parseColor("#8f8d8d"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(7, 7, 7, 7);
        rowTextView.setLayoutParams(params);
        rowTextView.setTag(selectedItem);
        rowTextView.setOnClickListener(mThisButtonListener);
        daynamicallyaddtext.addView(rowTextView);

        System.out.println("selectedItem" + selectedItem);

        mselectdcity.add(selectedItem);
        // MultiSpinnerSearch.staticmselectdcity.add(selectedItem);

        dynamicId++;


        // MyApplication.list.remove(selectedItem);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_todayhotlead:
                // do your code
                if (bbtn_todayhotlead) {
                    deactivateHotLeads();
                    //  MyApplication.strcheck_hot="n";
                } else {

                    activateHotLeads();
                }

                break;
            case R.id.btn_active:
                // do your code
                if (bbtn_active) {
                    deactivateActiveLeads();
                } else {
                    activateActiveLeads();
                }


                break;

            case R.id.btn_sold:
                // do your code
                if (bbtn_sold) {
                    deactivateSoldLeads();
                } else {
                    activateSoldLeads();

                }

                break;

            case R.id.btn_mylead:
                if (bbtn_mylead) {
                    deactivatemyleads();
                } else {
                    activateMyLeads();
                    // MyApplication.strcheck_allleads="n";

                }

                // do your code
                break;

            case R.id.btn_remaninglead:

                if (bbtn_remaninglead) {
                    btn_remaninglead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_remaninglead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_remaninglead = false;
                    // MyApplication.strcheck_allleads="n";
                    cstrcheck_remainlead = "n";
                } else {
                    bbtn_remaninglead = true;
                    btn_remaninglead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_remaninglead.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_allleads="y";
                    cstrcheck_remainlead = "y";


                    btn_todayhotlead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_todayhotlead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_todayhotlead = false;

                    btn_active.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_active.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_active = false;

                    btn_sold.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_sold.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_sold = false;

                    btn_mylead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_mylead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_mylead = false;

                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;


                    btn_withincity.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_withincity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_withincity = false;

                    btn_outsidecity.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_outsidecity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_outsidecity = false;

                    btn_international.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_international.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_international = false;

                    btn_nearby.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_nearby.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_nearby = false;


                    btn_1bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_1bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_1bhk = false;

                    btn_2bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_2bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_2bhk = false;

                    btn_3bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_3bhk = false;

                    btn_lessthenibhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_lessthenibhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_lessthenibhk = false;

                    btn_above3bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_above3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_above3bhk = false;

                    btn_bike.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_bike.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_bike = false;

                    btn_car.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_car.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_car = false;

                    MyApplication.strcheck_3bhk = "n";
                    MyApplication.strcheck_1bhk = "n";
                    MyApplication.strcheck_2bhk = "n";
                    MyApplication.strcheck_lesthen1bhk = "n";
                    MyApplication.strcheck_above3bhk = "n";
                    MyApplication.strcheck_hot = "n";
                    MyApplication.strcheck_active = "n";
                    MyApplication.strcheck_sold = "n";
                    MyApplication.strcheck_myleads = "n";
                    MyApplication.strcheck_allleads = "n";
                    MyApplication.strcheck_withincity = "n";
                    MyApplication.strcheck_outsidecity = "n";
                    MyApplication.strcheck_international2 = "n";
                    MyApplication.strcheck_car = "n";
                    MyApplication.strcheck_bike = "n";

                    cstrcheck_3bhk = "n";
                    cstrcheck_1bhk = "n";
                    cstrcheck_2bhk = "n";
                    cstrcheck_lesthen1bhk = "n";
                    cstrcheck_above3bhk = "n";
                    cstrcheck_hot = "n";
                    cstrcheck_active = "n";
                    cstrcheck_sold = "n";
                    cstrcheck_myleads = "n";
                    cstrcheck_allleads = "n";
                    cstrcheck_withincity = "n";
                    cstrcheck_outsidecity = "n";
                    cstrcheck_international2 = "n";
                    cstrcheck_nearby = "n";
                    cstrcheck_car = "n";
                    cstrcheck_bike = "n";


                }


                break;
            case R.id.btn_alllead:

                if (bbtn_alllead) {
                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    // MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";
                } else {
                    bbtn_alllead = true;
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_alllead.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_allleads="y";
                    cstrcheck_allleads = "y";


                    btn_todayhotlead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_todayhotlead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_todayhotlead = false;

                    btn_active.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_active.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_active = false;

                    btn_sold.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_sold.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_sold = false;

                    btn_mylead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_mylead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_mylead = false;

                    btn_remaninglead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_remaninglead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_remaninglead = false;


                    btn_withincity.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_withincity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_withincity = false;

                    btn_outsidecity.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_outsidecity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_outsidecity = false;

                    btn_international.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_international.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_international = false;

                    btn_nearby.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_nearby.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_nearby = false;


                    btn_1bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_1bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_1bhk = false;

                    btn_2bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_2bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_2bhk = false;

                    btn_3bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_3bhk = false;

                    btn_lessthenibhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_lessthenibhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_lessthenibhk = false;
                    bstrsnleadtype = false;

                    btn_above3bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_above3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_above3bhk = false;

                    btn_bike.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_bike.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_bike = false;

                    btn_car.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_car.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_car = false;

                    MyApplication.strcheck_3bhk = "n";
                    MyApplication.strcheck_1bhk = "n";
                    MyApplication.strcheck_2bhk = "n";
                    MyApplication.strcheck_lesthen1bhk = "n";
                    MyApplication.strcheck_above3bhk = "n";
                    MyApplication.strcheck_hot = "n";
                    MyApplication.strcheck_active = "n";
                    MyApplication.strcheck_sold = "n";
                    MyApplication.strcheck_myleads = "n";
                    MyApplication.strcheck_remainlead = "n";
                    MyApplication.strcheck_withincity = "n";
                    MyApplication.strcheck_outsidecity = "n";
                    MyApplication.strcheck_international2 = "n";
                    MyApplication.strcheck_nearby = "n";
                    MyApplication.strcheck_car = "n";
                    MyApplication.strcheck_bike = "n";

                    cstrcheck_3bhk = "n";
                    cstrcheck_1bhk = "n";
                    cstrcheck_2bhk = "n";
                    cstrcheck_lesthen1bhk = "n";
                    cstrcheck_above3bhk = "n";
                    cstrcheck_hot = "n";
                    cstrcheck_active = "n";
                    cstrcheck_sold = "n";
                    cstrcheck_myleads = "n";
                    cstrcheck_remainlead = "n";
                    cstrcheck_withincity = "n";
                    cstrcheck_outsidecity = "n";
                    cstrcheck_international2 = "n";
                    cstrcheck_nearby = "n";
                    cstrcheck_car = "n";
                    cstrcheck_bike = "n";


                }

                break;

            case R.id.btn_withincity:
                if (bbtn_withincity) {
                    btn_withincity.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_withincity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_withincity = false;
                    // MyApplication.strcheck_withincity="n";
                    cstrcheck_withincity = "n";

                } else {
                    bbtn_withincity = true;
                    btn_withincity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_withincity.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_withincity="y";
                    cstrcheck_withincity = "y";


                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    //MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";

/*
                    btn_outsidecity.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_outsidecity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_outsidecity=false;
                    //MyApplication.strcheck_outsidecity="n";
                    cstrcheck_outsidecity="n";

                    btn_international.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_international.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_international=false;
                   // MyApplication.strcheck_international2="n";
                    cstrcheck_international2="n";*/

                }

                break;

            case R.id.btn_outsidecity:
                if (bbtn_outsidecity) {
                    btn_outsidecity.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_outsidecity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_outsidecity = false;
                    // MyApplication.strcheck_outsidecity="n";
                    cstrcheck_outsidecity = "n";
                } else {
                    bbtn_outsidecity = true;
                    btn_outsidecity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_outsidecity.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_outsidecity="y";
                    cstrcheck_outsidecity = "y";
                   /* daynamicallyaddtext.removeAllViews();
                    mselectdcity.clear();

                    for (int i = 0; i < MyApplication.list.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();
                        h.setId(i + 1);
                        h.setName(MyApplication.list.get(i));
                        h.setSelected(true);
                        listArray0.add(h);
                    }
                    lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners_normal));
                    searchMultiSpinnerUnlimited.setItemsclear(listArray0);
                    additeminspinner();*/

                    //  MyApplication.list.clear();
                    // MyApplication.list=MyApplication.list2;
                    searchMultiSpinnerUnlimited.setSelection(0);


                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    // MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";
/*

                    btn_withincity.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_withincity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_withincity=false;
                    //MyApplication.strcheck_withincity="n";
                    cstrcheck_withincity="n";

                    btn_international.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_international.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));

                    bbtn_international=false;
                   // MyApplication.strcheck_international2="n";
                    cstrcheck_international2="n";
*/


                }

                break;

            case R.id.btn_international:
                if (bbtn_international) {
                    btn_international.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_international.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_international = false;
                    // MyApplication.strcheck_international2="n";
                    cstrcheck_international2 = "n";
                } else {
                    bbtn_international = true;
                    btn_international.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_international.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_international2="y";
                    cstrcheck_international2 = "y";


                    // MyApplication.list.clear();
                    //MyApplication.list=MyApplication.list2;


                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    // MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";

                 /*   btn_withincity.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_withincity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_withincity=false;
                  //  MyApplication.strcheck_withincity="n";
                    cstrcheck_withincity="n";

                    btn_outsidecity.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_outsidecity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_outsidecity=false;
                   // MyApplication.strcheck_outsidecity="n";
                    cstrcheck_outsidecity="n";*/


                }

                break;
            case R.id.btn_nearby:


                try {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GpsTracker gpsTracker = new GpsTracker(NewFilterActivity.this);
                if (gpsTracker.canGetLocation()) {

                } else {
                    gpsTracker.showSettingsAlert();
                }


                if (bbtn_nearby) {
                    btn_nearby.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_nearby.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_nearby = false;
                    // MyApplication.strcheck_international2="n";
                    cstrcheck_nearby = "n";
                } else {
                    bbtn_nearby = true;
                    btn_nearby.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_nearby.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_international2="y";
                    cstrcheck_nearby = "y";
                    cstrcheck_selectcity = "n";
                    lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_selectcity = false;


                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    // MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";


                }

                break;
            case R.id.btn_applycites:
                if (bbtn_selectcity) {
                    lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners_normal));
                    cstrcheck_selectcity = "n";
                    bbtn_selectcity = false;
                } else {
                    lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners));
                    bbtn_selectcity = true;

                    System.out.println("Apply city :" + bbtn_selectcity);
                    btn_nearby.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_nearby.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_nearby = false;
                    // MyApplication.strcheck_international2="n";
                    cstrcheck_nearby = "n";
                    cstrcheck_selectcity = "y";


                    //  finish();


                }


                break;

            case R.id.btn_1bhk:
                if (bbtn_1bhk) {
                    btn_1bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_1bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_1bhk = false;
                    // MyApplication.strcheck_1bhk="n";
                    cstrcheck_1bhk = "n";
                } else {
                    bbtn_1bhk = true;
                    btn_1bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_1bhk.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_1bhk="y";
                    cstrcheck_1bhk = "y";

                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    //MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";
                    enable1bhk();


                }

                break;

            case R.id.btn_2bhk:
                if (bbtn_2bhk) {
                    btn_2bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_2bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_2bhk = false;
                    //MyApplication.strcheck_2bhk="n";
                    cstrcheck_2bhk = "n";
                } else {
                    bbtn_2bhk = true;
                    btn_2bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_2bhk.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_2bhk="y";
                    cstrcheck_2bhk = "y";

                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    // MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";
                    enable2bhk();


                }

                break;

            case R.id.btn_3bhk:
                if (bbtn_3bhk) {
                    btn_3bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_3bhk = false;
                    //MyApplication.strcheck_3bhk="n";
                    cstrcheck_3bhk = "n";
                } else {
                    bbtn_3bhk = true;
                    btn_3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_3bhk.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_3bhk="y";
                    cstrcheck_3bhk = "y";

                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    //MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";
                    enable3bhk();


                }

                break;

            case R.id.btn_lessthenibhk:
                if (bbtn_lessthenibhk) {
                    btn_lessthenibhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_lessthenibhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_lessthenibhk = false;
                    //MyApplication.strcheck_lesthen1bhk="n";
                    cstrcheck_lesthen1bhk = "n";
                } else {
                    bbtn_lessthenibhk = true;
                    btn_lessthenibhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_lessthenibhk.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_lesthen1bhk="y";
                    cstrcheck_lesthen1bhk = "y";

                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    //MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";
                    enablelessthan1bhk();

                }

                break;

            case R.id.btn_above3bhk:
                if (bbtn_above3bhk) {
                    btn_above3bhk.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_above3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_above3bhk = false;
                    // MyApplication.strcheck_above3bhk="n";
                    cstrcheck_above3bhk = "n";
                } else {
                    bbtn_above3bhk = true;
                    btn_above3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_above3bhk.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_above3bhk="y";
                    cstrcheck_above3bhk = "y";

                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    //MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";

                    enablemorethan3bhk();
                }

                break;

            case R.id.btn_bike:
                if (bbtn_bike) {
                    btn_bike.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_bike.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_bike = false;
                    // MyApplication.strcheck_bike="n";
                    cstrcheck_bike = "n";
                } else {
                    bbtn_bike = true;
                    btn_bike.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_bike.setTextColor(Color.parseColor("#2e5d97"));
                    //  MyApplication.strcheck_bike="y";
                    cstrcheck_bike = "y";

                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    // MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";

                }
                break;
            case R.id.btn_car:
                if (bbtn_car) {
                    btn_car.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_car.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_car = false;
                    // MyApplication.strcheck_car="n";
                    cstrcheck_car = "n";
                } else {
                    bbtn_car = true;
                    btn_car.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                    btn_car.setTextColor(Color.parseColor("#2e5d97"));
                    // MyApplication.strcheck_car="y";
                    cstrcheck_car = "y";

                    btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
                    btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                    bbtn_alllead = false;
                    // MyApplication.strcheck_allleads="n";
                    cstrcheck_allleads = "n";

                }
                break;

            case R.id.btn_cancel:
                finish();
                break;

            case R.id.txt_reset:

                bbtn_alllead = true;
                btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
                btn_alllead.setTextColor(Color.parseColor("#2e5d97"));
                // MyApplication.strcheck_allleads="y";
                cstrcheck_allleads = "y";


                btn_todayhotlead.setTextColor(Color.parseColor("#8f8d8d"));
                btn_todayhotlead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_todayhotlead = false;

                btn_active.setTextColor(Color.parseColor("#8f8d8d"));
                btn_active.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_active = false;
                btn_sold.setTextColor(Color.parseColor("#8f8d8d"));
                btn_sold.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_sold = false;

                btn_mylead.setTextColor(Color.parseColor("#8f8d8d"));
                btn_mylead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_mylead = false;

                btn_remaninglead.setTextColor(Color.parseColor("#8f8d8d"));
                btn_remaninglead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_remaninglead = false;


                btn_withincity.setTextColor(Color.parseColor("#8f8d8d"));
                btn_withincity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_withincity = false;

                btn_outsidecity.setTextColor(Color.parseColor("#8f8d8d"));
                btn_outsidecity.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_outsidecity = false;

                btn_international.setTextColor(Color.parseColor("#8f8d8d"));
                btn_international.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_international = false;


                btn_1bhk.setTextColor(Color.parseColor("#8f8d8d"));
                btn_1bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_1bhk = false;

                btn_2bhk.setTextColor(Color.parseColor("#8f8d8d"));
                btn_2bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_2bhk = false;

                btn_3bhk.setTextColor(Color.parseColor("#8f8d8d"));
                btn_3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_3bhk = false;

                btn_lessthenibhk.setTextColor(Color.parseColor("#8f8d8d"));
                btn_lessthenibhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_lessthenibhk = false;

                btn_above3bhk.setTextColor(Color.parseColor("#8f8d8d"));
                btn_above3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_above3bhk = false;

                btn_bike.setTextColor(Color.parseColor("#8f8d8d"));
                btn_bike.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_bike = false;

                btn_car.setTextColor(Color.parseColor("#8f8d8d"));
                btn_car.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
                bbtn_car = false;

                lnspineer.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners_normal));

                ln_leadtype.setBackground(ContextCompat.getDrawable(NewFilterActivity.this, R.drawable.button_bg_rounded_corners_normal));
                MyApplication.strspiner_leadtype = "n";


                MyApplication.bydefaultfeilter = true;
                MyApplication.listArray0.clear();
                MyApplication.filtercount = 2;
                MyApplication.strcheck_3bhk = "";
                MyApplication.strcheck_1bhk = "";
                MyApplication.strcheck_2bhk = "";
                MyApplication.strcheck_lesthen1bhk = "";
                MyApplication.strcheck_above3bhk = "";
                MyApplication.strcheck_hot = "";
                MyApplication.strcheck_active = "";
                MyApplication.strcheck_sold = "";
                MyApplication.strcheck_myleads = "";
                MyApplication.strcheck_withincity = "";
                MyApplication.strcheck_outsidecity = "";
                MyApplication.strcheck_international2 = "";
                MyApplication.strcheck_car = "";
                MyApplication.strcheck_bike = "";
                MyApplication.mapselectdcity.clear();
                MyApplication.mcities = "";
                // MyApplication.list.clear();
                MyApplication.list = MyApplication.list2;
                finish();

                break;

            case R.id.back_icon:

                MyApplication.bck_filter = true;
                onBackPressed();


                break;
            case R.id.btn_apply:
                setRegionCheck();
                MyApplication.list = MyApplication.list2;

                MyApplication.filterlead = true;
                MyApplication.mapselectdcity.clear();
                int filtercount = 0;
                MyApplication.mcities = "";

                Iterator it = mselectdcity.iterator();
                int i = 0;

                while (it.hasNext()) {
                    String value = (String) it.next();
                    if (cstrcheck_selectcity.equalsIgnoreCase("y")) {
                        MyApplication.mapselectdcity.add(value);
                    }
                    if (i == 0) {
                        if (cstrcheck_selectcity.equalsIgnoreCase("y")) {
                            MyApplication.mcities = MyApplication.mcities + value;
                        } else {
                            MyApplication.mcities = "";
                        }
                    } else {
                        if (cstrcheck_selectcity.equalsIgnoreCase("y")) {
                            MyApplication.mcities = MyApplication.mcities + "," + value;
                        } else {
                            MyApplication.mcities = "";
                        }
                    }
                    i++;

                    System.out.println("Value :" + value);


                }


                if (cstrcheck_allleads.equals("y")) {

                    MyApplication.strcheck_allleads = "y";
                } else {
                    MyApplication.strcheck_allleads = "n";
                }
                if (cstrcheck_hot.equals("y")) {

                    MyApplication.strcheck_hot = "y";
                } else {
                    MyApplication.strcheck_hot = "n";
                }
                if (cstrcheck_active.equals("y")) {
                    MyApplication.strcheck_active = "y";

                } else {
                    MyApplication.strcheck_active = "n";
                }
                if (cstrcheck_sold.equals("y")) {
                    MyApplication.strcheck_sold = "y";
                } else {
                    MyApplication.strcheck_sold = "n";
                }
                if (cstrcheck_myleads.equals("y")) {
                    MyApplication.strcheck_myleads = "y";
                } else {
                    MyApplication.strcheck_myleads = "n";
                }
                if (cstrcheck_remainlead.equals("y")) {
                    MyApplication.strcheck_remainlead = "y";
                } else {
                    MyApplication.strcheck_remainlead = "n";
                }

                if (cstrcheck_withincity.equals("y")) {
                    MyApplication.strcheck_withincity = "y";
                } else {
                    MyApplication.strcheck_withincity = "n";
                }
                if (cstrcheck_outsidecity.equals("y")) {
                    MyApplication.strcheck_outsidecity = "y";
                } else {
                    MyApplication.strcheck_outsidecity = "n";
                }
                if (cstrcheck_international2.equals("y")) {
                    MyApplication.strcheck_international2 = "y";
                } else {
                    MyApplication.strcheck_international2 = "n";
                }
                if (cstrcheck_nearby.equals("y")) {
                    MyApplication.strcheck_nearby = "y";
                } else {
                    MyApplication.strcheck_nearby = "n";
                }
                if (cstrcheck_1bhk.equals("y")) {
                    MyApplication.strcheck_1bhk = "y";
                } else {
                    MyApplication.strcheck_1bhk = "n";
                }
                if (cstrcheck_2bhk.equals("y")) {
                    MyApplication.strcheck_2bhk = "y";
                } else {
                    MyApplication.strcheck_2bhk = "n";
                }
                if (cstrcheck_3bhk.equals("y")) {
                    MyApplication.strcheck_3bhk = "y";
                } else {
                    MyApplication.strcheck_3bhk = "n";
                }
                if (cstrcheck_lesthen1bhk.equals("y")) {
                    MyApplication.strcheck_lesthen1bhk = "y";
                } else {
                    MyApplication.strcheck_lesthen1bhk = "n";
                }
                if (cstrcheck_above3bhk.equals("y")) {
                    MyApplication.strcheck_above3bhk = "y";
                } else {
                    MyApplication.strcheck_above3bhk = "n";
                }
                if (cstrcheck_bike.equals("y")) {
                    MyApplication.strcheck_bike = "y";
                } else {
                    MyApplication.strcheck_bike = "n";
                }
                if (cstrcheck_car.equals("y")) {
                    MyApplication.strcheck_car = "y";
                } else {
                    MyApplication.strcheck_car = "n";
                }


                System.out.println("mcities :" + MyApplication.mcities);

                System.out.println("MyApplication.strcheck_3bhk :" + MyApplication.strcheck_3bhk);
                System.out.println("MyApplication.strcheck_1bhk :" + MyApplication.strcheck_1bhk);
                System.out.println("MyApplication.strcheck_2bhk :" + MyApplication.strcheck_2bhk);
                System.out.println("MyApplication.strcheck_3bhk :" + MyApplication.strcheck_3bhk);
                System.out.println("MyApplication.strcheck_lesthen1bhk :" + MyApplication.strcheck_lesthen1bhk);
                System.out.println("MyApplication.strcheck_above3bhk :" + MyApplication.strcheck_above3bhk);

                System.out.println("MyApplication.strcheck_hot :" + MyApplication.strcheck_hot);
                System.out.println("MyApplication.strcheck_active :" + MyApplication.strcheck_active);
                System.out.println("MyApplication.strcheck_sold :" + MyApplication.strcheck_sold);
                System.out.println("MyApplication.strcheck_myleads :" + MyApplication.strcheck_myleads);
                System.out.println("MyApplication.strcheck_myleads :" + MyApplication.strcheck_remainlead);
                System.out.println("MyApplication.strcheck_withincity :" + MyApplication.strcheck_withincity);
                System.out.println("MyApplication.strcheck_outsidecity :" + MyApplication.strcheck_outsidecity);

                System.out.println("MyApplication.strcheck_international2 :" + MyApplication.strcheck_international2);
                System.out.println("MyApplication.strcheck_car :" + MyApplication.strcheck_car);
                System.out.println("MyApplication.strcheck_bike :" + MyApplication.strcheck_bike);
                System.out.println("MyApplication.strcheck_allleads :" + MyApplication.strcheck_allleads);


                if (!MyApplication.mcities.equalsIgnoreCase("")) {
                    filtercount = filtercount + 1;

                }

                if (MyApplication.strcheck_allleads.equals("y")) {
                    MyApplication.bydefaultfeilter = true;
                    filtercount = 1;
                } else {
                    MyApplication.bydefaultfeilter = false;
                }


                if (MyApplication.strspiner_leadtype.equals("s")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;

                }
                if (MyApplication.strspiner_leadtype.equals("p")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;

                }
                if (MyApplication.strspiner_leadtype.equals("l")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;

                }


                if (MyApplication.strcheck_hot.equals("y")) {
                    MyApplication.strcheck_hot = "y";
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;

                }
                if (MyApplication.strcheck_active.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;

                }
                if (MyApplication.strcheck_sold.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_myleads.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_remainlead.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }


                if (MyApplication.strcheck_withincity.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_outsidecity.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_international2.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_nearby.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_1bhk.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_2bhk.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_3bhk.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_lesthen1bhk.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_above3bhk.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_bike.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_car.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }


                if (filtercount == 0) {
                    MyApplication.bydefaultfeilter = true;
                    filtercount = 0;
                }
                MyApplication.filtercount = filtercount;
                finish();

                break;


            default:
                break;
        }

    }

    private void enable1bhk() {

        btn_2bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_2bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_2bhk = false;
        //MyApplication.strcheck_2bhk="n";
        cstrcheck_2bhk = "n";

        btn_3bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_3bhk = false;
        //MyApplication.strcheck_3bhk="n";
        cstrcheck_3bhk = "n";

        btn_lessthenibhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_lessthenibhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_lessthenibhk = false;
        //MyApplication.strcheck_lesthen1bhk="n";
        cstrcheck_lesthen1bhk = "n";

        btn_above3bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_above3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_above3bhk = false;
        // MyApplication.strcheck_above3bhk="n";
        cstrcheck_above3bhk = "n";
    }

    private void enable2bhk() {
        btn_1bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_1bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_1bhk = false;
        // MyApplication.strcheck_1bhk="n";
        cstrcheck_1bhk = "n";


        btn_3bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_3bhk = false;
        //MyApplication.strcheck_3bhk="n";
        cstrcheck_3bhk = "n";

        btn_lessthenibhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_lessthenibhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_lessthenibhk = false;
        //MyApplication.strcheck_lesthen1bhk="n";
        cstrcheck_lesthen1bhk = "n";

        btn_above3bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_above3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_above3bhk = false;
        // MyApplication.strcheck_above3bhk="n";
        cstrcheck_above3bhk = "n";
    }

    private void enable3bhk() {
        btn_1bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_1bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_1bhk = false;
        // MyApplication.strcheck_1bhk="n";
        cstrcheck_1bhk = "n";

        btn_2bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_2bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_2bhk = false;
        //MyApplication.strcheck_2bhk="n";
        cstrcheck_2bhk = "n";


        btn_lessthenibhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_lessthenibhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_lessthenibhk = false;
        //MyApplication.strcheck_lesthen1bhk="n";
        cstrcheck_lesthen1bhk = "n";

        btn_above3bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_above3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_above3bhk = false;
        // MyApplication.strcheck_above3bhk="n";
        cstrcheck_above3bhk = "n";
    }

    private void enablemorethan3bhk() {
        btn_1bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_1bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_1bhk = false;
        // MyApplication.strcheck_1bhk="n";
        cstrcheck_1bhk = "n";

        btn_2bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_2bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_2bhk = false;
        //MyApplication.strcheck_2bhk="n";
        cstrcheck_2bhk = "n";

        btn_3bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_3bhk = false;
        //MyApplication.strcheck_3bhk="n";
        cstrcheck_3bhk = "n";

        btn_lessthenibhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_lessthenibhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_lessthenibhk = false;
        //MyApplication.strcheck_lesthen1bhk="n";
        cstrcheck_lesthen1bhk = "n";


    }

    private void enablelessthan1bhk() {
        btn_1bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_1bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_1bhk = false;
        // MyApplication.strcheck_1bhk="n";
        cstrcheck_1bhk = "n";

        btn_2bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_2bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_2bhk = false;
        //MyApplication.strcheck_2bhk="n";
        cstrcheck_2bhk = "n";

        btn_3bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_3bhk = false;
        //MyApplication.strcheck_3bhk="n";
        cstrcheck_3bhk = "n";


        btn_above3bhk.setTextColor(Color.parseColor("#8f8d8d"));
        btn_above3bhk.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_above3bhk = false;
        // MyApplication.strcheck_above3bhk="n";
        cstrcheck_above3bhk = "n";
    }

    private void setRegionCheck() {
        JSONObject jsonObject = new JSONObject();
        int areaId = area_check_listRB.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(areaId);

        if (area_check_listRB.getCheckedRadioButtonId() != -1) {
            Log.e("radiogroup==", String.valueOf(selectedRadioButton.getText()));

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
        jsonStr = jsonObject.toString();
        selectedRegion = jsonStr;
        System.out.println("jsonString: " + selectedRegion);
    }

    private void deactivatemyleads() {
        btn_mylead.setTextColor(Color.parseColor("#8f8d8d"));
        btn_mylead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_mylead = false;
        // MyApplication.strcheck_myleads="n";
        cstrcheck_myleads = "n";
    }

    private void deactivateSoldLeads() {
        btn_sold.setTextColor(Color.parseColor("#8f8d8d"));
        btn_sold.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_sold = false;
        //  MyApplication.strcheck_sold="n";
        cstrcheck_sold = "n";
    }

    private void deactivateActiveLeads() {
        btn_active.setTextColor(Color.parseColor("#8f8d8d"));
        btn_active.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_active = false;
        // MyApplication.strcheck_active="n";
        cstrcheck_active = "n";
    }

    private void deactivateHotLeads() {
        btn_todayhotlead.setTextColor(Color.parseColor("#8f8d8d"));
        btn_todayhotlead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_todayhotlead = false;
        cstrcheck_hot = "n";
    }

    private void activateHotLeads() {
        disableOtherLeadsExceptHotLeads();
        bbtn_todayhotlead = true;
        btn_todayhotlead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
        btn_todayhotlead.setTextColor(Color.parseColor("#2e5d97"));
        cstrcheck_hot = "y";
        // MyApplication.strcheck_hot="y";

        btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
        btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_alllead = false;
        // MyApplication.strcheck_allleads="n";
        cstrcheck_allleads = "n";

        btn_remaninglead.setTextColor(Color.parseColor("#8f8d8d"));
        btn_remaninglead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_remaninglead = false;
        cstrcheck_remainlead = "n";
    }

    private void activateMyLeads() {
        disableOtherLeadsExceptMyLeads();
        bbtn_mylead = true;
        btn_mylead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
        btn_mylead.setTextColor(Color.parseColor("#2e5d97"));
        //MyApplication.strcheck_myleads="y";
        cstrcheck_myleads = "y";

        btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
        btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_alllead = false;
        cstrcheck_allleads = "n";

        btn_remaninglead.setTextColor(Color.parseColor("#8f8d8d"));
        btn_remaninglead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_remaninglead = false;
        cstrcheck_remainlead = "n";
    }

    private void activateSoldLeads() {
        disableOtherLeadsExceptSoldLeads();
        bbtn_sold = true;
        btn_sold.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
        btn_sold.setTextColor(Color.parseColor("#2e5d97"));
        //MyApplication.strcheck_sold="y";
        cstrcheck_sold = "y";

        btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
        btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_alllead = false;
        // MyApplication.strcheck_allleads="n";
        cstrcheck_allleads = "n";


        btn_remaninglead.setTextColor(Color.parseColor("#8f8d8d"));
        btn_remaninglead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_remaninglead = false;
        cstrcheck_remainlead = "n";
    }

    private void activateActiveLeads() {
        disableOtherLeadsExceptActiveLeads();
        bbtn_active = true;
        btn_active.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners));
        btn_active.setTextColor(Color.parseColor("#2e5d97"));
        // MyApplication.strcheck_active="y";
        cstrcheck_active = "y";

        btn_alllead.setTextColor(Color.parseColor("#8f8d8d"));
        btn_alllead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_alllead = false;
        // MyApplication.strcheck_allleads="n";
        cstrcheck_allleads = "n";

        btn_remaninglead.setTextColor(Color.parseColor("#8f8d8d"));
        btn_remaninglead.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_rounded_corners_normal));
        bbtn_remaninglead = false;
        cstrcheck_remainlead = "n";
    }

    private void disableOtherLeadsExceptHotLeads() {
        deactivateActiveLeads();
        deactivateSoldLeads();

        deactivatemyleads();
    }

    private void disableOtherLeadsExceptActiveLeads() {
        deactivateSoldLeads();

        deactivatemyleads();
        deactivateHotLeads();
    }

    private void disableOtherLeadsExceptSoldLeads() {
        deactivateActiveLeads();

        deactivatemyleads();
        deactivateHotLeads();

    }

    private void disableOtherLeadsExceptMyLeads() {
        deactivateActiveLeads();
        deactivateSoldLeads();
        deactivateHotLeads();

    }

    @Override
    protected void onPause() {

        // hide the keyboard in order to avoid getTextBeforeCursor on inactive InputConnection
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //inputMethodManager.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);

        super.onPause();
    }

    private void setLeadCityValue() {
        switch (selectRegion) {
            case 1:
              //  jsonStr = "{\"name\":\"bangalore\",\"latitude\":\"12.972442\",\"longitude\":\"77.580643\"}";
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
                break;
            case 9:
                jsonStr = "{\"name\":\"Chandigarh\",\"latitude\":\"30.741482\",\"longitude\":\"76.768066\"}";
            case 10:
                jsonStr = "{\"name\":\"Jaipur\",\"latitude\":\"26.922070\",\"longitude\":\"75.778885\"}";
                break;
            default:
                break;
        }
        selectedRegion = jsonStr;
    }

    private void createJson(JSONObject jsonObject, String name, String lat, String lng) throws JSONException {
        jsonObject.put("name", name);
        jsonObject.put("latitude", lat);
        jsonObject.put("longitude", lng);
    }
}