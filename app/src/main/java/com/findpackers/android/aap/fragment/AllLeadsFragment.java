package com.findpackers.android.aap.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.findpackers.android.aap.LoginActivity;
import com.findpackers.android.aap.MainActivity;
import com.findpackers.android.aap.NewFilterActivity;
import com.findpackers.android.aap.R;
import com.findpackers.android.aap.SpleshActivity;
import com.findpackers.android.aap.adapter.LeadsRecyclerAdapter;
import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.GpsTracker;
import com.findpackers.android.aap.commanUtill.MyApplication;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.Utility;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.interfaces.OnLoadMoreListener;
import com.findpackers.android.aap.pojo.LeadsResources;
import com.findpackers.android.aap.util.Logger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static com.findpackers.android.aap.commanUtill.MyApplication.selectedRegion;

/**
 * Created by narendra on 2/16/2018.
 */

public class AllLeadsFragment extends Fragment implements LeadsRecyclerAdapter.SendWhatsApp_Sms {
    private int selectRegion = 0;
    String jsonStr = "";
    private RecyclerView mRecyclerView, mfilterTypeRecyclerView;
    public ArrayList<LeadsResources> myLeadsList = new ArrayList<LeadsResources>();
    private LeadsRecyclerAdapter mAdapter;
    TextView txtAllLeads, mtxt_filter, mtxt_sort;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog prgDialog;
    LinearLayout mrl_filter;
    Button mbtn_cancelfilter, mbtn_applyfilter;
    private boolean dialogcheck;

    private BroadcastReceiver registerReceiver;
    LinearLayoutManager llm;
    public GpsTracker gpsTracker;
    String notificationCount = "";
    LinearLayout mln_filter, ln_sort;
    private int visibleThreshold = 8;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
    SimpleDateFormat format2 = new SimpleDateFormat("dd/mm/yy hh:mm");

    int mlastVisibleItem = -2;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    public static AllLeadsFragment newInstance() {

        return new AllLeadsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_leads, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swifeRefresh);
        mrl_filter = (LinearLayout) view.findViewById(R.id.rl_filter);
        mtxt_filter = (TextView) view.findViewById(R.id.txt_filter);
        mln_filter = (LinearLayout) view.findViewById(R.id.ln_filter);
        ln_sort = (LinearLayout) view.findViewById(R.id.ln_sort);
        mtxt_sort = (TextView) view.findViewById(R.id.txt_sort);

        // txtAllLeads = (TextView) view.findViewById(R.id.txt_all_leads);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        getLeadsnotifications();

        llm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        System.out.println("UserId :" + MyPreferences.getActiveInstance(getActivity()).getUserId());


        mtxt_filter.setTextColor(Color.parseColor("#ffffff"));
        if (MyApplication.filtercount > 0) {
            mtxt_filter.setText("Filter" + "   " + MyApplication.filtercount);
        } else {
            mtxt_filter.setText("Filter" + "   ");
        }

        mln_filter.setBackgroundDrawable((ContextCompat.getDrawable(getActivity(), R.drawable.new_edittext_selected_bg)));

        mtxt_sort.setTextColor(Color.parseColor("#ffffff"));
        ln_sort.setBackgroundDrawable((ContextCompat.getDrawable(getActivity(), R.drawable.new_edittext_selected_bg)));

        selectRegion = MyPreferences.getActiveInstance(getActivity()).getLeadCityValue();
        System.out.println("selectRegionLead===" + selectRegion);
        if (selectRegion == 0) {
            selectRegion = 3;
        }


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dialogcheck = true;

                if (MyApplication.filterlead) {

                    setCityToNo();
                    RequestParams params = new RequestParams();
                    params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
                    params.put("area", selectedRegion.equalsIgnoreCase("") ? setLeadcityValue() : selectedRegion);
                    params.put("outSideCity", MyApplication.strcheck_outsidecity);
                    params.put("inSideCity", MyApplication.strcheck_withincity);
                    params.put("international", MyApplication.strcheck_international2);
                    if (MyApplication.strcheck_nearby.equalsIgnoreCase("y")) {

                        gpsTracker = new GpsTracker(getActivity());
                        if (gpsTracker.canGetLocation()) {
                            double latitude = gpsTracker.getLatitude();
                            double longitude = gpsTracker.getLongitude();

//                            params.put("nearBy", MyApplication.strcheck_nearby);
//                            params.put("lattitude", latitude);
//                            params.put("longitude", longitude);

                        }


                    }
                    params.put("allLead", MyApplication.strcheck_allleads);
                    params.put("myLead", MyApplication.strcheck_myleads);
                    params.put("remainingLead", MyApplication.strcheck_remainlead);
                    params.put("hot", MyApplication.strcheck_hot);
                    params.put("sold", MyApplication.strcheck_sold);
                    params.put("active", MyApplication.strcheck_active);
                    params.put("1BHK", MyApplication.strcheck_1bhk);
                    params.put("2BHK", MyApplication.strcheck_2bhk);
                    params.put("3BHK", MyApplication.strcheck_3bhk);
                    params.put("LESS_THAN_1BHK", MyApplication.strcheck_lesthen1bhk);
                    params.put("ABOVE_3BHK", MyApplication.strcheck_above3bhk);
                    //      params.put("city", MyApplication.mcities);
                    params.put("car", MyApplication.strcheck_car);
                    params.put("bike", MyApplication.strcheck_bike);
                    params.put("leadType", MyApplication.strspiner_leadtype);

                    if (MyApplication.sortlead) {
                        params.put("lowToHigh", MyApplication.mstr_low_high);
                        params.put("highToLow", MyApplication.mstr_high_low);
                        params.put("latest", MyApplication.mstr_latest);
                        params.put("oldest", MyApplication.mstr_oldest);
                        params.put("nearest", MyApplication.mstr_nearshiftingdate);
                    }


                    allLeadsWebservices(params, true);

                } else {


                    RequestParams params = new RequestParams();
                    params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
                    if (MyApplication.sortlead) {
                        params.put("lowToHigh", MyApplication.mstr_low_high);
                        params.put("highToLow", MyApplication.mstr_high_low);
                        params.put("latest", MyApplication.mstr_latest);
                        params.put("oldest", MyApplication.mstr_oldest);
                        params.put("nearest", MyApplication.mstr_nearshiftingdate);
                    }

                    allLeadsWebservices(params, true);
                }
            }
        });


        mln_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // startActivity(new Intent(getActivity(), Filter.class));
                startActivity(new Intent(getActivity(), NewFilterActivity.class));

            }
        });

        ln_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // startActivity(new Intent(getActivity(), Sort.class));

                RadioButton mrd_low_high, mrd_high_low, mrd_latest, mrd_oldest, mrd_nearshiftingdate;
                RadioGroup radioGroup;

                view = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheet, null);
                radioGroup = (RadioGroup) view.findViewById(R.id.mytypeRadioGroup);
                mrd_low_high = (RadioButton) view.findViewById(R.id.rd_low_high);
                mrd_high_low = (RadioButton) view.findViewById(R.id.rd_high_low);
                mrd_latest = (RadioButton) view.findViewById(R.id.rd_latest);
                mrd_oldest = (RadioButton) view.findViewById(R.id.rd_oldest);
                mrd_nearshiftingdate = (RadioButton) view.findViewById(R.id.rd_nearshiftingdate);
                final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

                System.out.println("MyApplication.bydefaultsort :" + MyApplication.bydefaultsort);
                System.out.println("MyApplication.mstr_low_high :" + MyApplication.mstr_low_high);
                System.out.println("MyApplication.mstr_high_low :" + MyApplication.mstr_high_low);
                System.out.println("MyApplication.mstr_latest :" + MyApplication.mstr_latest);
                System.out.println("MyApplication.mstr_oldest :" + MyApplication.mstr_oldest);
                System.out.println("MyApplication.mstr_nearshiftingdate :" + MyApplication.mstr_nearshiftingdate);


                applySortingFilter(mrd_low_high, mrd_high_low, mrd_latest, mrd_oldest, mrd_nearshiftingdate);


                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        RadioButton rb = (RadioButton) group.findViewById(checkedId);
                        if (null != rb && checkedId > -1) {


                            if (rb.getText().equals("Latest")) {
                                MyApplication.mstr_latest = "y";
                                MyApplication.bydefaultsort = false;
                                MyApplication.sortlead = true;

                            } else {
                                MyApplication.mstr_latest = "n";

                            }
                            if (rb.getText().equals("Oldest")) {
                                MyApplication.mstr_oldest = "y";
                                MyApplication.bydefaultsort = false;
                                MyApplication.sortlead = true;
                            } else {
                                MyApplication.mstr_oldest = "n";
                            }
                            if (rb.getText().equals("Low To High Credit")) {
                                MyApplication.mstr_low_high = "y";
                                MyApplication.bydefaultsort = false;
                                MyApplication.sortlead = true;
                            } else {
                                MyApplication.mstr_low_high = "n";
                            }
                            if (rb.getText().equals("High To Low Credit")) {
                                MyApplication.mstr_high_low = "y";
                                MyApplication.bydefaultsort = false;
                                MyApplication.sortlead = true;
                            } else {
                                MyApplication.mstr_high_low = "n";
                            }
                            if (rb.getText().equals("Nearing Shifting Date")) {
                                MyApplication.mstr_nearshiftingdate = "y";
                                MyApplication.bydefaultsort = false;
                                MyApplication.sortlead = true;
                            } else {
                                MyApplication.mstr_nearshiftingdate = "n";
                            }

                            applysorting();
                            dialog.dismiss();

                        }
                    }
                });


                dialog.setContentView(view);
                dialog.show();


                // mSwipeRefreshLayout.setVisibility(View.GONE);
                // mrl_filter.setVisibility(View.VISIBLE);
            }
        });

       /* mbtn_cancelfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mrl_filter.setVisibility(View.GONE);
            }
        });

        mbtn_applyfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mrl_filter.setVisibility(View.GONE);
            }
        });*/
        mAdapter = new LeadsRecyclerAdapter(getActivity(), myLeadsList, AllLeadsFragment.this, mRecyclerView);


        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int lastVisibleItem, int itemzize) {

                System.out.println("calling onscrollview listner ");
                System.out.println("scrool chk item  size " + itemzize);
                System.out.println("scrool chk item  lastVisibleItem " + lastVisibleItem);

                Log.e("Load more : ", "lastVisibleItemlocal==" + lastVisibleItem);
                Log.e("Load more : ", "itemzize==" + itemzize);
                Log.e("Load more : ", "mlastVisibleItemglobal==" + mlastVisibleItem);

                if (mlastVisibleItem != lastVisibleItem) {
                    System.out.println("scrool chk mlastVisibleItem not equal  " + mlastVisibleItem);
                    if (lastVisibleItem == itemzize - 1) {
                        mlastVisibleItem = lastVisibleItem;


                        System.out.println("scroll chk now webservice will called ");


                        if (MyApplication.filterlead) {
                            setCityToNo();
                            RequestParams params = new RequestParams();
                            params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
                            params.put("area", selectedRegion.equalsIgnoreCase("") ? setLeadcityValue() : selectedRegion);
                            params.put("'current_page'", MyPreferences.getActiveInstance(getActivity()).getcurrent_page());
                            params.put("'next_page_url'", MyPreferences.getActiveInstance(getActivity()).getnextpageUrl());


                            params.put("outSideCity", MyApplication.strcheck_outsidecity);
                            params.put("inSideCity", MyApplication.strcheck_withincity);
                            params.put("international", MyApplication.strcheck_international2);
                            if (MyApplication.strcheck_nearby.equalsIgnoreCase("y")) {

                                gpsTracker = new GpsTracker(getActivity());
                                if (gpsTracker.canGetLocation()) {
                                    double latitude = gpsTracker.getLatitude();
                                    double longitude = gpsTracker.getLongitude();
//
//                                    params.put("nearBy", MyApplication.strcheck_nearby);
//                                    params.put("lattitude", latitude);
//                                    params.put("longitude", longitude);

                                }


                            }
                            params.put("allLead", MyApplication.strcheck_allleads);
                            params.put("myLead", MyApplication.strcheck_myleads);
                            params.put("remainingLead", MyApplication.strcheck_remainlead);
                            params.put("hot", MyApplication.strcheck_hot);
                            params.put("sold", MyApplication.strcheck_sold);
                            params.put("active", MyApplication.strcheck_active);
                            params.put("1BHK", MyApplication.strcheck_1bhk);
                            params.put("2BHK", MyApplication.strcheck_2bhk);
                            params.put("3BHK", MyApplication.strcheck_3bhk);
                            params.put("LESS_THAN_1BHK", MyApplication.strcheck_lesthen1bhk);
                            params.put("ABOVE_3BHK", MyApplication.strcheck_above3bhk);
                            //         params.put("city", MyApplication.mcities);
                            params.put("car", MyApplication.strcheck_car);
                            params.put("bike", MyApplication.strcheck_bike);
                            params.put("leadType", MyApplication.strspiner_leadtype);
                            if (MyApplication.sortlead) {
                                params.put("lowToHigh", MyApplication.mstr_low_high);
                                params.put("area", selectedRegion.equalsIgnoreCase("") ? setLeadcityValue() : selectedRegion);
                                params.put("highToLow", MyApplication.mstr_high_low);
                                params.put("latest", MyApplication.mstr_latest);
                                params.put("oldest", MyApplication.mstr_oldest);
                                params.put("nearest", MyApplication.mstr_nearshiftingdate);
                            }
                            allLeadsWebservicesscrroled(params, false, MyPreferences.getActiveInstance(getActivity()).getnextpageUrl());


                        } else {

                            RequestParams params = new RequestParams();
                            params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
                            params.put("area", selectedRegion.equalsIgnoreCase("") ? setLeadcityValue() : selectedRegion);
                            params.put("'current_page'", MyPreferences.getActiveInstance(getActivity()).getcurrent_page());
                            params.put("'next_page_url'", MyPreferences.getActiveInstance(getActivity()).getnextpageUrl());

//                            params.put("myLead", MyApplication.strcheck_myleads);
//                            params.put("remainingLead", MyApplication.strcheck_remainlead);
//                            params.put("hot", MyApplication.strcheck_hot);
//                            params.put("sold", MyApplication.strcheck_sold);
                            params.put("active", "y");


                            if (MyApplication.sortlead) {
                                params.put("lowToHigh", MyApplication.mstr_low_high);
                                params.put("highToLow", MyApplication.mstr_high_low);
                                params.put("latest", MyApplication.mstr_latest);
                                params.put("oldest", MyApplication.mstr_oldest);
                                params.put("nearest", MyApplication.mstr_nearshiftingdate);
                                params.put("area", selectedRegion.equalsIgnoreCase("") ? setLeadcityValue() : selectedRegion);
                            }
                            allLeadsWebservicesscrroled(params, false, MyPreferences.getActiveInstance(getActivity()).getnextpageUrl());


                        }
                    }
                } else {
                    System.out.println("Not load more");
                }


            }
        });

        return view;
    }

    private void applySortingFilter(RadioButton mrd_low_high, RadioButton mrd_high_low, RadioButton mrd_latest, RadioButton mrd_oldest, RadioButton mrd_nearshiftingdate) {
        if (MyApplication.bydefaultsort) {
            mrd_latest.setChecked(true);
        }

        if (MyApplication.mstr_low_high.equals("y")) {
            mrd_low_high.setChecked(true);

        }
        if (MyApplication.mstr_high_low.equals("y")) {
            mrd_high_low.setChecked(true);

        }
        if (MyApplication.mstr_latest.equals("y")) {
            mrd_latest.setChecked(true);

        }
        if (MyApplication.mstr_oldest.equals("y")) {
            mrd_oldest.setChecked(true);

        }
        if (MyApplication.mstr_nearshiftingdate.equals("y")) {
            mrd_nearshiftingdate.setChecked(true);

        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        mlastVisibleItem = -2;
        loadAllLeads();

    }

    private void loadAllLeads() {
        System.out.println("MyApplication.bck_filter ::" + MyApplication.bck_filter);
        if (MyApplication.bck_filter) {
            MyApplication.bck_filter = false;
        } else {


            if (MyApplication.filtercount > 0) {
                mtxt_filter.setTextColor(Color.parseColor("#ffffff"));
                mtxt_filter.setText("Filter" + "   " + MyApplication.filtercount);
                mln_filter.setBackgroundDrawable((ContextCompat.getDrawable(getActivity(), R.drawable.new_edittext_selected_bg)));
            } else {

                mtxt_filter.setText("Filter" + "   ");
            }
            if (MyApplication.sortlead) {

                mtxt_sort.setTextColor(Color.parseColor("#ffffff"));
                ln_sort.setBackgroundDrawable((ContextCompat.getDrawable(getActivity(), R.drawable.new_edittext_selected_bg)));


            } else {
                mtxt_sort.setTextColor(Color.parseColor("#2e5d97"));
                ln_sort.setBackgroundDrawable((ContextCompat.getDrawable(getActivity(), R.drawable.new_edittext_bg)));

            }

            if ((MyApplication.strcheck_hot.equals("n") || MyApplication.strcheck_hot.equals(""))
                    && ((MyApplication.strcheck_sold.equals("n") || MyApplication.strcheck_sold.equals("")))
                    && ((MyApplication.strcheck_myleads.equals("n") || MyApplication.strcheck_myleads.equals("")))) {
                System.out.println("check for HotLeads");
                MyApplication.filterlead = true;
                MyApplication.strcheck_active = "y";
            }


            if (MyApplication.filterlead) {
                setCityToNo();
                RequestParams params = new RequestParams();
                params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
                params.put("area", selectedRegion.equalsIgnoreCase("") ? setLeadcityValue() : selectedRegion);
                params.put("outSideCity", MyApplication.strcheck_outsidecity);
                params.put("inSideCity", MyApplication.strcheck_withincity);
                params.put("international", MyApplication.strcheck_international2);

                Log.e("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
                Log.e("outSideCity", MyApplication.strcheck_outsidecity);
                Log.e("inSideCity", MyApplication.strcheck_withincity);
                Log.e("international", MyApplication.strcheck_international2);
                if (MyApplication.strcheck_nearby.equalsIgnoreCase("y")) {

                    gpsTracker = new GpsTracker(getActivity());
                    if (gpsTracker.canGetLocation()) {
                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();

//                        params.put("nearBy", MyApplication.strcheck_nearby);
//                        params.put("lattitude", latitude);
//                        params.put("longitude", longitude);

                        Log.e("nearBy", MyApplication.strcheck_nearby);
                        Log.e("lattitude", String.valueOf(latitude));
                        Log.e("longitude", String.valueOf(longitude));

                    }


                }
                params.put("allLead", MyApplication.strcheck_allleads);
                params.put("myLead", MyApplication.strcheck_myleads);
                params.put("remainingLead", MyApplication.strcheck_remainlead);
                params.put("hot", MyApplication.strcheck_hot);
                params.put("sold", MyApplication.strcheck_sold);
                params.put("active", MyApplication.strcheck_active);
                params.put("1BHK", MyApplication.strcheck_1bhk);
                params.put("2BHK", MyApplication.strcheck_2bhk);
                params.put("3BHK", MyApplication.strcheck_3bhk);
                params.put("LESS_THAN_1BHK", MyApplication.strcheck_lesthen1bhk);
                params.put("ABOVE_3BHK", MyApplication.strcheck_above3bhk);
                //      params.put("city", MyApplication.mcities);
                params.put("car", MyApplication.strcheck_car);
                params.put("bike", MyApplication.strcheck_bike);
                params.put("leadType", MyApplication.strspiner_leadtype);

                Log.e("allLead", MyApplication.strcheck_allleads);
                Log.e("myLead", MyApplication.strcheck_myleads);
                Log.e("remainingLead", MyApplication.strcheck_remainlead);
                Log.e("hot", MyApplication.strcheck_hot);
                Log.e("sold", MyApplication.strcheck_sold);
                Log.e("active", MyApplication.strcheck_active);
                Log.e("1BHK", MyApplication.strcheck_1bhk);
                Log.e("2BHK", MyApplication.strcheck_2bhk);
                Log.e("3BHK", MyApplication.strcheck_3bhk);
                Log.e("LESS_THAN_1BHK", MyApplication.strcheck_lesthen1bhk);
                Log.e("ABOVE_3BHK", MyApplication.strcheck_above3bhk);
                Log.e("city", MyApplication.mcities);
                Log.e("car", MyApplication.strcheck_car);
                Log.e("bike", MyApplication.strcheck_bike);
                Log.e("leadType", MyApplication.strspiner_leadtype);


                if (MyApplication.sortlead) {
                    params.put("lowToHigh", MyApplication.mstr_low_high);
                    params.put("highToLow", MyApplication.mstr_high_low);
                    params.put("latest", MyApplication.mstr_latest);
                    params.put("oldest", MyApplication.mstr_oldest);
                    params.put("nearest", MyApplication.mstr_nearshiftingdate);


                    Log.e("lowToHigh", MyApplication.mstr_low_high);
                    Log.e("highToLow", MyApplication.mstr_high_low);
                    Log.e("latest", MyApplication.mstr_latest);
                    Log.e("oldest", MyApplication.mstr_oldest);
                    Log.e("nearest", MyApplication.mstr_nearshiftingdate);

                }

                allLeadsWebservices(params, false);


            } else {

                RequestParams params = new RequestParams();
                params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
                params.put("area", selectedRegion.equalsIgnoreCase("") ? setLeadcityValue() : selectedRegion);
                if (MyApplication.sortlead) {
                    params.put("lowToHigh", MyApplication.mstr_low_high);
                    params.put("highToLow", MyApplication.mstr_high_low);
                    params.put("latest", MyApplication.mstr_latest);
                    params.put("oldest", MyApplication.mstr_oldest);
                    params.put("nearest", MyApplication.mstr_nearshiftingdate);
                }

                allLeadsWebservices(params, false);

            }

        }
    }

    public void applysorting() {

        if (MyApplication.filterlead) {
            setCityToNo();
            RequestParams params = new RequestParams();
            params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
            params.put("area", selectedRegion.equalsIgnoreCase("") ? setLeadcityValue() : selectedRegion);
            Log.e("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());

            params.put("outSideCity", MyApplication.strcheck_outsidecity);
            Log.e("outSideCity", MyApplication.strcheck_outsidecity);

            params.put("outSideCity", MyApplication.strcheck_outsidecity);
            Log.e("outSideCity", MyApplication.strcheck_outsidecity);

            params.put("inSideCity", MyApplication.strcheck_withincity);
            Log.e("inSideCity", MyApplication.strcheck_withincity);

            params.put("international", MyApplication.strcheck_international2);
            Log.e("international", MyApplication.strcheck_international2);

            if (MyApplication.strcheck_nearby.equalsIgnoreCase("y")) {

                if (gpsTracker.canGetLocation()) {
                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();


//                    params.put("nearBy", MyApplication.strcheck_nearby);
//                    Log.e("nearBy", MyApplication.strcheck_nearby);
//
//                    params.put("lattitude", latitude);
//                    Log.e("lattitude", String.valueOf(latitude));
//
//                    params.put("longitude", longitude);
//                    Log.e("longitude", String.valueOf(longitude));

                }


            }


            params.put("allLead", MyApplication.strcheck_allleads);
            Log.e("allLead", MyApplication.strcheck_allleads);

            params.put("myLead", MyApplication.strcheck_myleads);
            Log.e("myLead", MyApplication.strcheck_myleads);

            params.put("remainingLead", MyApplication.strcheck_remainlead);
            Log.e("remainingLead", MyApplication.strcheck_remainlead);

            params.put("hot", MyApplication.strcheck_hot);
            Log.e("hot", MyApplication.strcheck_hot);

            params.put("sold", MyApplication.strcheck_sold);
            Log.e("sold", MyApplication.strcheck_sold);

            params.put("active", MyApplication.strcheck_active);
            Log.e("active", MyApplication.strcheck_active);

            params.put("1BHK", MyApplication.strcheck_1bhk);
            Log.e("1BHK", MyApplication.strcheck_1bhk);

            params.put("2BHK", MyApplication.strcheck_2bhk);
            Log.e("2BHK", MyApplication.strcheck_2bhk);

            params.put("3BHK", MyApplication.strcheck_3bhk);
            Log.e("3BHK", MyApplication.strcheck_3bhk);

            params.put("LESS_THAN_1BHK", MyApplication.strcheck_lesthen1bhk);
            Log.e("LESS_THAN_1BHK", MyApplication.strcheck_lesthen1bhk);

            params.put("ABOVE_3BHK", MyApplication.strcheck_above3bhk);
            Log.e("ABOVE_3BHK", MyApplication.strcheck_above3bhk);

            //    params.put("city", MyApplication.mcities);
            Log.e("city", MyApplication.mcities);

            params.put("car", MyApplication.strcheck_car);
            Log.e("car", MyApplication.strcheck_car);

            params.put("bike", MyApplication.strcheck_bike);
            Log.e("bike", MyApplication.strcheck_bike);

            params.put("leadType", MyApplication.strspiner_leadtype);
            if (MyApplication.sortlead) {

                params.put("lowToHigh", MyApplication.mstr_low_high);
                Log.e("lowToHigh", MyApplication.mstr_low_high);

                params.put("highToLow", MyApplication.mstr_high_low);
                Log.e("highToLow", MyApplication.mstr_high_low);

                params.put("latest", MyApplication.mstr_latest);
                Log.e("latest", MyApplication.mstr_latest);

                params.put("oldest", MyApplication.mstr_oldest);
                Log.e("oldest", MyApplication.mstr_oldest);

                params.put("nearest", MyApplication.mstr_nearshiftingdate);
                Log.e("nearest", MyApplication.mstr_nearshiftingdate);
            }
            allLeadsWebservices(params, false);

        } else {

            RequestParams params = new RequestParams();
            params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
            params.put("area", selectedRegion.equalsIgnoreCase("") ? setLeadcityValue() : selectedRegion);
            if (MyApplication.sortlead) {
                params.put("lowToHigh", MyApplication.mstr_low_high);
                params.put("highToLow", MyApplication.mstr_high_low);
                params.put("latest", MyApplication.mstr_latest);
                params.put("oldest", MyApplication.mstr_oldest);
                params.put("nearest", MyApplication.mstr_nearshiftingdate);
            }
            allLeadsWebservices(params, false);
        }

    }

    private void setCityToNo() {
        if (!MyApplication.strcheck_myleads.equalsIgnoreCase("y")) {
            MyApplication.strcheck_myleads = "n";
        }
        if (!MyApplication.strcheck_withincity.equalsIgnoreCase("y")) {
            MyApplication.strcheck_withincity = "n";
        }
        if (!MyApplication.strcheck_outsidecity.equalsIgnoreCase("y")) {
            MyApplication.strcheck_outsidecity = "n";
        }
        if (!MyApplication.strcheck_international2.equalsIgnoreCase("y")) {
            MyApplication.strcheck_international2 = "n";
        }
    }

    public void allLeadsWebservices(RequestParams params, final boolean progress) {
        Log.d("FirebaseToken", "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
        Logger.d("Filer Params :", params.toString());
        System.out.println(" get all leads params :" + params.toString());
        String Url = WebserviceUrlClass.getAllLeads;
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "get All leads Response is 11 >>>>" + response.toString());
                    System.out.println(" get all leads Response :" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");


                    if (respcode.equals("200")) {


                        if (respMessage.equalsIgnoreCase("No Leads Found")) {
                            final Dialog dialog = new Dialog(getActivity());
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

                            myLeadsList.clear();
                            //  mAdapter = new LeadsRecyclerAdapter(getActivity(), myLeadsList, AllLeadsFragment.this,mRecyclerView);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {


                            if (getActivity() != null)
                                // Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();
                                myLeadsList.clear();
                            MyApplication.totalLeads = 0;
                            LeadsResources mleads = null;
                            LeadsResources smleads = null;
                            JSONArray jsonArray = null;
                            String availableCredits = response.getString("availableCredits");
                            String activestatus = response.getString("status");
                            String isDelete = response.getString("isDelete");
                            String current_page = response.getString("current_page");
                            String next_page_url = response.getString("next_page_url");
                            String prev_page_url = response.getString("prev_page_url");
                            String count = response.getString("count");


                            MyPreferences.getActiveInstance(getActivity()).setCreditbalance(availableCredits);
                            MyPreferences.getActiveInstance(getActivity()).setnextpageUrl(next_page_url);
                            MyPreferences.getActiveInstance(getActivity()).setcurrent_page(current_page);

                            MainActivity.avlable_creditbalance.setText(availableCredits);
                            myLeadsList.add(0, smleads);
                            jsonArray = response.getJSONArray("leads");

                            MyApplication.totalLeads = Integer.parseInt(count);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mleads = new LeadsResources();
                                JSONObject obj = (JSONObject) jsonArray.get(i);

                                mleads.setLead_id(Utility.chkstringnull(obj.getString("lead_id").toString().trim()));
                                mleads.setLead_title(Utility.chkstringnull(obj.getString("firstName").toString().trim() + " " + Utility.chkstringnull2(obj.getString("lastName").toString().trim())));
                                mleads.setLeadamount(Utility.chkstringnull(obj.getString("leadAmount").toString().trim()));
                                // mleads.setClintName(obj.getString("clintName").toString().trim());
                                mleads.setMobileNo(Utility.chkstringnull(obj.getString("mobileNo").toString().trim()));
                                mleads.setEmailId(Utility.chkstringnull(obj.getString("emailId").toString().trim()));
                                mleads.setBids(Utility.chkstringnull(obj.getString("bids").toString().trim()));
                                mleads.setPickuplocation(Utility.chkstringnull(obj.getString("pickupLocation").toString().trim()));
                                mleads.setDroplocation(Utility.chkstringnull(obj.getString("dropLocation").toString().trim()));

                                mleads.setCar(Utility.chkstringnull(obj.getString("car").toString().trim()));
                                mleads.setBike(Utility.chkstringnull(obj.getString("bike").toString().trim()));
                                //  mleads.setDistance(obj.getString("distance").toString().trim());
                                //  mleads.setLeadtime(obj.getString("leadtime").toString().trim());
                                mleads.setMaxVendors(Utility.chkstringnull(obj.getString("maxVendors").toString().trim()));
                                mleads.setLeadType(Utility.chkstringnull(obj.getString("propertySize").toString().trim()));
                                mleads.setPicupdate(Utility.Timeformatewithouttime(obj.getString("pickupDate").toString().trim()));
                                //  mleads.setBuyStatus(obj.getString("buyStatus").toString().trim());
                                mleads.setStatus(Utility.chkstringnull(obj.getString("status").toString().trim()));
                                mleads.setBuyLead(Utility.chkstringnull(obj.getString("buyLead").toString().trim()));
                                mleads.setCreated_at(Utility.Timeformateanother(obj.getString("created_at").toString().trim()));
                                mleads.setUpdated_at(Utility.chkstringnull(obj.getString("updated_at").toString().trim()));
                                mleads.setLevel(Utility.chkstringnull(obj.getString("level").toString().trim()));
                                mleads.setCategory_status(Utility.chkstringnull(response.getString("category_status").toString().trim()));
                                mleads.setSpl_status(Utility.chkstringnull(response.getString("spl_status").toString().trim()));


                                MyApplication.category_status = Utility.chkstringnull(response.getString("category_status").toString().trim());
                                MyApplication.spl_status = Utility.chkstringnull(response.getString("spl_status").toString().trim());
                                MyApplication.usercities = Utility.chkstringnull(response.getString("userCities").toString().trim());
                                // notificationCount =Utility.chkstringnull(response.getString("notificationCount").toString().trim());
                                if (!notificationCount.equalsIgnoreCase("0"))
                                    MainActivity.txt_notification.setText(notificationCount);

                                String bidstatus = Utility.chkstringnull(obj.getString("bids").toString().trim());
                                String maxvendors = Utility.chkstringnull(obj.getString("maxVendors").toString().trim());
                                String buylead = Utility.chkstringnull(obj.getString("buyLead").toString().trim());


                                //String dateString = "04/04/2018 11:49:00 AM";
                                //String dateString = "2018-04-02 21:36:55";

                                try {
                                    Date date = format.parse(Timeformate(obj.getString("created_at").toString().trim()));
                                    System.out.println(date);
                                    System.out.println(getTimeAgo(date));

                                    mleads.setLeadtime(getTimeAgo(date));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                if (bidstatus.equalsIgnoreCase(maxvendors)) {

                                    System.out.println(" control when max vendors and bidsttus :" + obj.getString("bids").toString() + obj.getString("maxVendors").toString());
                                    if (buylead.equalsIgnoreCase("y")) {

                                        if (MyApplication.strcheck_sold.equalsIgnoreCase("y") && MyApplication.strcheck_myleads.equalsIgnoreCase("y")) {
                                            System.out.println(" control when buylead status true :" + obj.getString("buyLead").toString());
                                            myLeadsList.add(mleads);
                                        } else if ((MyApplication.strcheck_sold.equalsIgnoreCase("y") && MyApplication.strcheck_hot.equalsIgnoreCase("y"))) {


                                        } else if ((MyApplication.strcheck_sold.equalsIgnoreCase("y") && MyApplication.strcheck_active.equalsIgnoreCase("y"))) {


                                        } else {
                                            myLeadsList.add(mleads);
                                        }

                                    } else {
                                        myLeadsList.add(mleads);
                                    }

                                } else {
                                    myLeadsList.add(mleads);
                                }

                            }

                            if (activestatus.equalsIgnoreCase("0")) {


                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Sorry your account has deactivated. Please contact to admin.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.setTitle("Account deactivated");
                                alert.show();
                            } else if (isDelete.equalsIgnoreCase("1")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Your account has been deleted.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
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
                                                MyApplication.strcheck_nearby = "";
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
                                                // get prompts.xml view
                                            }
                                        });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("Account deleted");
                                alert.show();

                            } else {
                                //   mAdapter = new LeadsRecyclerAdapter(getActivity(), myLeadsList, AllLeadsFragment.this,mRecyclerView);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }
                    } else if (respcode.equals("300")) {
                        if (getActivity() != null)
                            Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();
                    } else {
                        if (getActivity() != null)
                            Toast.makeText(getActivity(), "Please check your login details", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStart() {
                super.onStart();
                if (dialogcheck) {
                    dialogcheck = false;

                } else {
                    prgDialog.show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (dialogcheck) {
                    dialogcheck = false;

                } else {
                    prgDialog.dismiss();
                }
                CommanMethod.showAlert("Unable to connect to the server, please try again later.", getActivity());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (dialogcheck) {
                    dialogcheck = false;

                } else {
                    prgDialog.dismiss();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

        });
    }


    public void allLeadsWebservicesscrroled(RequestParams params, final boolean progress, String url) {

        System.out.println(" get all leads params scrolled:" + params.toString());
        String Url = url;
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(15000);
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "get All leads Response is 22 >>>>" + response.toString());
                    System.out.println(" get all leads Response :" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");


                    if (respcode.equals("200")) {


                        if (respMessage.equalsIgnoreCase("No Leads Found")) {
                            final Dialog dialog = new Dialog(getActivity());
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

                            myLeadsList.clear();
                            //  mAdapter = new LeadsRecyclerAdapter(getActivity(), myLeadsList, AllLeadsFragment.this,mRecyclerView);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {


                            if (getActivity() != null)
                                // Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();
                                // myLeadsList.clear();
                                MyApplication.totalLeads = 0;
                            LeadsResources mleads = null;
                            LeadsResources smleads = null;
                            JSONArray jsonArray = null;
                            String availableCredits = response.getString("availableCredits");
                            String activestatus = response.getString("status");
                            String isDelete = response.getString("isDelete");
                            String current_page = response.getString("current_page");
                            String next_page_url = response.getString("next_page_url");
                            String prev_page_url = response.getString("prev_page_url");
                            String count = response.getString("count");


                            MyPreferences.getActiveInstance(getActivity()).setCreditbalance(availableCredits);

                            MyPreferences.getActiveInstance(getActivity()).setCreditbalance(availableCredits);
                            MyPreferences.getActiveInstance(getActivity()).setnextpageUrl(next_page_url);
                            MyPreferences.getActiveInstance(getActivity()).setcurrent_page(current_page);

                            MainActivity.avlable_creditbalance.setText(availableCredits);
                            // myLeadsList.add(0, smleads);
                            jsonArray = response.getJSONArray("leads");

                            MyApplication.totalLeads = Integer.parseInt(count);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mleads = new LeadsResources();
                                JSONObject obj = (JSONObject) jsonArray.get(i);

                                mleads.setLead_id(Utility.chkstringnull(obj.getString("lead_id").toString().trim()));
                                mleads.setLead_title(Utility.chkstringnull(obj.getString("firstName").toString().trim() + " " + Utility.chkstringnull2(obj.getString("lastName").toString().trim())));
                                mleads.setLeadamount(Utility.chkstringnull(obj.getString("leadAmount").toString().trim()));
                                // mleads.setClintName(obj.getString("clintName").toString().trim());
                                mleads.setMobileNo(Utility.chkstringnull(obj.getString("mobileNo").toString().trim()));
                                mleads.setEmailId(Utility.chkstringnull(obj.getString("emailId").toString().trim()));
                                mleads.setBids(Utility.chkstringnull(obj.getString("bids").toString().trim()));
                                mleads.setPickuplocation(Utility.chkstringnull(obj.getString("pickupLocation").toString().trim()));
                                mleads.setDroplocation(Utility.chkstringnull(obj.getString("dropLocation").toString().trim()));

                                mleads.setCar(Utility.chkstringnull(obj.getString("car").toString().trim()));
                                mleads.setBike(Utility.chkstringnull(obj.getString("bike").toString().trim()));
                                //  mleads.setDistance(obj.getString("distance").toString().trim());
                                //  mleads.setLeadtime(obj.getString("leadtime").toString().trim());
                                mleads.setMaxVendors(Utility.chkstringnull(obj.getString("maxVendors").toString().trim()));
                                mleads.setLeadType(Utility.chkstringnull(obj.getString("propertySize").toString().trim()));
                                mleads.setPicupdate(Utility.Timeformatewithouttime(obj.getString("pickupDate").toString().trim()));
                                //  mleads.setBuyStatus(obj.getString("buyStatus").toString().trim());
                                mleads.setStatus(Utility.chkstringnull(obj.getString("status").toString().trim()));
                                mleads.setBuyLead(Utility.chkstringnull(obj.getString("buyLead").toString().trim()));
                                mleads.setCreated_at(Utility.Timeformateanother(obj.getString("created_at").toString().trim()));
                                mleads.setUpdated_at(Utility.chkstringnull(obj.getString("updated_at").toString().trim()));
                                mleads.setLevel(Utility.chkstringnull(obj.getString("level").toString().trim()));
                                mleads.setCategory_status(Utility.chkstringnull(response.getString("category_status").toString().trim()));
                                mleads.setSpl_status(Utility.chkstringnull(response.getString("spl_status").toString().trim()));


                                MyApplication.category_status = Utility.chkstringnull(response.getString("category_status").toString().trim());
                                MyApplication.spl_status = Utility.chkstringnull(response.getString("spl_status").toString().trim());
                                MyApplication.usercities = Utility.chkstringnull(response.getString("userCities").toString().trim());
                                //  notificationCount =Utility.chkstringnull(response.getString("notificationCount").toString().trim());
                                if (!notificationCount.equalsIgnoreCase("0"))
                                    MainActivity.txt_notification.setText(notificationCount);

                                String bidstatus = Utility.chkstringnull(obj.getString("bids").toString().trim());
                                String maxvendors = Utility.chkstringnull(obj.getString("maxVendors").toString().trim());
                                String buylead = Utility.chkstringnull(obj.getString("buyLead").toString().trim());


                                //String dateString = "04/04/2018 11:49:00 AM";
                                //String dateString = "2018-04-02 21:36:55";

                                try {
                                    Date date = format.parse(Timeformate(obj.getString("created_at").toString().trim()));
                                    System.out.println(date);
                                    System.out.println(getTimeAgo(date));

                                    mleads.setLeadtime(getTimeAgo(date));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                if (bidstatus.equalsIgnoreCase(maxvendors)) {

                                    System.out.println(" control when max vendors and bidsttus :" + obj.getString("bids").toString() + obj.getString("maxVendors").toString());
                                    if (buylead.equalsIgnoreCase("y")) {

                                        if (MyApplication.strcheck_sold.equalsIgnoreCase("y") && MyApplication.strcheck_myleads.equalsIgnoreCase("y")) {
                                            System.out.println(" control when buylead status true :" + obj.getString("buyLead").toString());
                                            myLeadsList.add(mleads);
                                        } else if ((MyApplication.strcheck_sold.equalsIgnoreCase("y") && MyApplication.strcheck_hot.equalsIgnoreCase("y"))) {


                                        } else if ((MyApplication.strcheck_sold.equalsIgnoreCase("y") && MyApplication.strcheck_active.equalsIgnoreCase("y"))) {


                                        } else {
                                            myLeadsList.add(mleads);
                                        }

                                    } else {
                                        myLeadsList.add(mleads);
                                    }

                                } else {
                                    myLeadsList.add(mleads);
                                }

                            }

                            if (activestatus.equalsIgnoreCase("0")) {


                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Sorry your account has deactivated. Please contact to admin.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.setTitle("Account deactivated");
                                alert.show();
                            } else if (isDelete.equalsIgnoreCase("1")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Your account has been deleted.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
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
                                                MyApplication.strcheck_nearby = "";
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
                                                // get prompts.xml view
                                            }
                                        });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("Account deleted");
                                alert.show();

                            } else {
                                // mAdapter = new LeadsRecyclerAdapter(getActivity(), myLeadsList, AllLeadsFragment.this,mRecyclerView);
                                // mRecyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    } else if (respcode.equals("300")) {
                        if (getActivity() != null)
                            Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();
                    } else {
                        if (getActivity() != null)
                            Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStart() {
                super.onStart();
                if (dialogcheck) {
                    dialogcheck = false;

                } else {
                    prgDialog.show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (dialogcheck) {
                    dialogcheck = false;

                } else {
                    prgDialog.dismiss();
                }
                //    CommanMethod.showAlert("Unable to connect to the server, please try again later.", getActivity());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (dialogcheck) {
                    dialogcheck = false;

                } else {
                    prgDialog.dismiss();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

        });
    }


    @Override
    public void methodSendWhatsApp_Sms(String leadId, String mobileNo, String emailId, String requestType) {
        switch (requestType) {
            case "whatsapp":
                boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
                if (isWhatsappInstalled) {
                    String smsNumber = "91" + mobileNo;
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix

                    startActivity(sendIntent);
                } else {
                    Uri uri = Uri.parse("market://details?id=com.whatsapp");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                    startActivity(goToMarket);
                }

                break;

            case "email":

                try {
                    Intent testIntent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:?subject=" + "Packer & mover " + "&body=" + "" + "&to=" + emailId);
                    testIntent.setData(data);
                    startActivity(testIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "sms":

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobileNo));
                intent.putExtra("sms_body", " ");
                startActivity(intent);

                break;
            case "call":
                try {

                    Intent intentcall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mobileNo, null));
                    startActivity(intentcall);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "buyNow":
                RequestParams params = new RequestParams();
                params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
                params.put("leadId", leadId);
                params.put("buyLead", "y");
                BuyLeadWebservices(params);
                break;

            default:
                break;
            // Statements
        }

    }


    public void BuyLeadWebservices(RequestParams params) {

        String Url = WebserviceUrlClass.buyLead;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "leadBuy Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");


                    if (respcode.equals("200")) {

                        RequestParams params = new RequestParams();
                        params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
                        params.put("area", selectedRegion.equalsIgnoreCase("") ? setLeadcityValue() : selectedRegion);
                        allLeadsWebservices(params, true);

                        Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();

                    } else if (respcode.equals("300")) {
                        Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();
                    } else if (respcode.equals("0")) {
                        final Dialog dialog = new Dialog(getActivity());
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
                    if (respcode.equals("300")) {
                        Toast.makeText(getActivity(), respMessage, Toast.LENGTH_LONG).show();
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

    public void getLeadsnotifications() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updateLeadsList");
        registerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");


                if (MyApplication.filterlead) {
                    setCityToNo();
                    //strcheck_withincity = "", strcheck_outsidecity = "", strcheck_international2
                    RequestParams params = new RequestParams();
                    params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
                    params.put("outSideCity", MyApplication.strcheck_outsidecity);
                    params.put("inSideCity", MyApplication.strcheck_withincity);
                    params.put("allLead", MyApplication.strcheck_allleads);
                    params.put("myLead", MyApplication.strcheck_myleads);
                    params.put("remainingLead", MyApplication.strcheck_remainlead);
                    params.put("hot", MyApplication.strcheck_hot);
                    params.put("sold", MyApplication.strcheck_sold);
                    params.put("active", MyApplication.strcheck_active);
                    params.put("1BHK", MyApplication.strcheck_1bhk);
                    params.put("2BHK", MyApplication.strcheck_2bhk);
                    params.put("3BHK", MyApplication.strcheck_3bhk);
                    params.put("LESS_THAN_1BHK", MyApplication.strcheck_lesthen1bhk);
                    params.put("ABOVE_3BHK", MyApplication.strcheck_above3bhk);
                    if (MyApplication.sortlead) {
                        params.put("lowToHigh", MyApplication.mstr_low_high);
                        params.put("highToLow", MyApplication.mstr_high_low);
                        params.put("latest", MyApplication.mstr_latest);
                        params.put("oldest", MyApplication.mstr_oldest);
                        params.put("nearest", MyApplication.mstr_nearshiftingdate);
                    }
                    allLeadsWebservices(params, true);

                } else {

                    RequestParams params = new RequestParams();
                    params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
                    if (MyApplication.sortlead) {
                        params.put("lowToHigh", MyApplication.mstr_low_high);
                        params.put("highToLow", MyApplication.mstr_high_low);
                        params.put("latest", MyApplication.mstr_latest);
                        params.put("oldest", MyApplication.mstr_oldest);
                        params.put("nearest", MyApplication.mstr_nearshiftingdate);
                    }
                    allLeadsWebservices(params, true);
                }
            }
        };
        if (getActivity() != null)
            try {
                getActivity().registerReceiver(registerReceiver, intentFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    private static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(Date date) {
        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return "moments ago";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String Timeformate(String timestring) {
        String CurrentString = timestring;
        //System.out.println(" CurrentString== " + CurrentString);
        String[] separated = CurrentString.split(" ");

        String[] separated2 = separated[0].split("-");
        String[] separated3 = separated[1].split(":");
        // System.out.println("formated separated[0]== " + separated2[0]);
        // System.out.println("formated separated[1]== " + separated2[1]);
        // System.out.println("formated separated[2]== " + separated2[2]);
        int selectedYear = Integer.parseInt(separated2[0]);
        int selectedDay = Integer.parseInt(separated2[2]);
        int selectedMonth = Integer.parseInt(separated2[1]);
        String array1[] = separated[1].split(":");

        int hour = Integer.parseInt(array1[0]);
        int minutes = Integer.parseInt(array1[1]);
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }


        String dateString = separated2[1] + "/" + separated2[2] + "/" + separated2[0] + " " + separated3[0] + ":" + separated3[1] + ":00 " + timeSet;
        System.out.println("dateString== " + dateString);
        return dateString;
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public void onStop() {
        super.onStop();


        try {
            if (registerReceiver != null) {
                getActivity().unregisterReceiver(registerReceiver);
            }
        } catch (Exception e) {
            Log.i("", "broadcastReceiver is already unregistered");
            registerReceiver = null;
        }


    }

    private String setLeadcityValue() {
        if (selectRegion == 1) {
            //  jsonStr = "{\"name\":\"bangalore\",\"latitude\":\"12.972442\",\"longitude\":\"77.580643\"}";
            jsonStr = "{\"name\":\"bengaluru\",\"latitude\":\"12.972442\",\"longitude\":\"77.580643\"}";
        } else if (selectRegion == 2) {
            jsonStr = "{\"name\":\"chennai\",\"latitude\":\"13.067439\",\"longitude\":\"80.237617\"}";
        } else if (selectRegion == 3) {
            jsonStr = "{\"name\":\"delhi\",\"latitude\":\"28.644800\",\"longitude\":\"77.216721\"}";
        } else if (selectRegion == 4) {
            jsonStr = "{\"name\":\"hyderabad\",\"latitude\":\"17.387140\",\"longitude\":\"78.491684\"}";
        } else if (selectRegion == 5) {
            jsonStr = "{\"name\":\"kolkata\",\"latitude\":\"22.572645\",\"longitude\":\"88.363892\"}";
        } else if (selectRegion == 6) {
            jsonStr = "{\"name\":\"mumbai\",\"latitude\":\"19.228825\",\"longitude\":\"72.854118\"}";
        } else if (selectRegion == 7) {
            jsonStr = "{\"name\":\"pune\",\"latitude\":\"18.516726\",\"longitude\":\"73.856255\"}";
        } else if (selectRegion == 8) {
            jsonStr = "other";
        }

        return jsonStr;
    }
}


