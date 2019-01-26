package com.findpackers.android.aap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findpackers.android.aap.adapter.Adapter_resentadded;
import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.fragment.FragmentOne;
import com.findpackers.android.aap.fragment.FragmentTwo;
import com.findpackers.android.aap.pojo.RecentAddedResource;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AvlableBlance extends AppCompatActivity {
   TextView txt_addcredits;
   public static TextView txt_avlablebalence;
    Toolbar toolbar;
    TextView toolbarTitle,txt_avlableCredit;
    LinearLayout ln_addcredidt;
    RecyclerView recycler_view;
    private Adapter_resentadded mAdapter;
    LinearLayoutManager llm;
    ImageView IvBack;
    ProgressDialog prgDialog;
    private ArrayList<RecentAddedResource> myRechargeList = new ArrayList<RecentAddedResource>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avlable_blance);

        txt_avlablebalence = (TextView) findViewById(R.id.txt_avlablebalence);
        txt_addcredits = (TextView) findViewById(R.id.txt_addcredits);
        ln_addcredidt = (LinearLayout) findViewById(R.id.ln_addcredidt);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);


        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        txt_avlableCredit = (TextView) findViewById(R.id.txt_avlableCredit);
        IvBack = (ImageView) findViewById(R.id.back_icon);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("My Credits");


        prgDialog = new ProgressDialog(AvlableBlance.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOne(), "credited");
        adapter.addFragment(new FragmentTwo(), "debited");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4ab9cf"));
        tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#f3484747"), Color.parseColor("#4ab9cf"));




     /*   llm = new LinearLayoutManager(AvlableBlance.this);
        recycler_view.setLayoutManager(llm);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setHasFixedSize(true);

        if (CommanMethod.isOnline(AvlableBlance.this)) {
            RequestParams params = new RequestParams();
            params.put("userId",MyPreferences.getActiveInstance(AvlableBlance.this).getUserId());
            recentAddedWebServices(params);
        } else {

            CommanMethod.showAlert("Network Error,Please try again", AvlableBlance.this);
        }

*/
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_avlableCredit.setText(MyPreferences.getActiveInstance(AvlableBlance.this).getCreditbalance());

        txt_addcredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(AvlableBlance.this, RechargeWalletActivity.class);
                startActivity(in);


            }
        });


    }
        public void recentAddedWebServices(RequestParams params) {
            String Url = WebserviceUrlClass.getWallet;
            prgDialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(15000);
            client.post(Url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        Log.e("nk", "get wallet Response is>>>>" + response.toString());
                        String respMessage = response.getString("responseMessage");
                        String respcode = response.getString("responseCode");

                        if (respcode.equals("200")) {

                            myRechargeList.clear();
                            RecentAddedResource mrecharge=null;
                            JSONArray jsonArray = null;
                            jsonArray = response.getJSONArray("data");
                            String textslug;
                            txt_avlablebalence.setText(response.getString("myCreditBalance"));
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject obj = (JSONObject) jsonArray.get(i);
                                mrecharge = new RecentAddedResource();


                                mrecharge.setTxnbalance(obj.getString("credit_points"));
                                mrecharge.setTxnId(obj.getString("id"));
                                mrecharge.setTxndate(obj.getString("updated_at"));
                                myRechargeList.add(mrecharge);
                            }

                            mAdapter = new Adapter_resentadded(AvlableBlance.this, myRechargeList);
                            recycler_view.setAdapter(mAdapter);


                        } else if (respcode.equals("300")) {
                            CommanMethod.showAlert(respMessage,AvlableBlance.this);
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
                    final Dialog dialog = new Dialog(AvlableBlance.this);
                    dialog.setContentView(R.layout.dialog_sorry);
                    Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                    TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);
                    // if button is clicked, close the custom dialog

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;


                    txt_msg.setText("Network Error,Please try again");

                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    dialog.getWindow().setAttributes(lp);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    prgDialog.dismiss();
                }

            });
        }




    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
