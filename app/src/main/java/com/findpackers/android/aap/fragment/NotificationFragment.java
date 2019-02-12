package com.findpackers.android.aap.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findpackers.android.aap.AboutUs;
import com.findpackers.android.aap.AvlableBlance;
import com.findpackers.android.aap.ChangePasswordActivity;
import com.findpackers.android.aap.ContactUs;
import com.findpackers.android.aap.Disclamer;
import com.findpackers.android.aap.HowItWorkActivity;
import com.findpackers.android.aap.MainActivity;
import com.findpackers.android.aap.PrivacyPloicy;
import com.findpackers.android.aap.R;
import com.findpackers.android.aap.Refund_cancelation_policy;
import com.findpackers.android.aap.TermsAndConditions;
import com.findpackers.android.aap.adapter.Adapter_notifications;
import com.findpackers.android.aap.adapter.Adapter_resentadded;
import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.Utility;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.pojo.NotificationResource;
import com.findpackers.android.aap.pojo.RecentAddedResource;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by narendra on 2/16/2018.
 */

public class NotificationFragment extends Fragment implements Adapter_notifications.Readnotifications {

    LinearLayoutManager llm;
    RecyclerView recycler_view;
    ProgressDialog prgDialog;
    private Adapter_notifications mAdapter;
    private ArrayList<NotificationResource> myRechargeList = new ArrayList<NotificationResource>();

    public static NotificationFragment newInstance() {

        return new NotificationFragment();
    }

    @Override
    public void readnotifications(String notificationId) {

        if (CommanMethod.isOnline(getActivity())) {
            RequestParams params = new RequestParams();
            params.put("user_id", MyPreferences.getActiveInstance(getActivity()).getUserId());
            params.put("notification_id", notificationId);
            NotificationReadWebServices(params);
        } else {

            CommanMethod.showAlert("Network Error,Please try again", getActivity());
        }


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);


        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait..");
        prgDialog.setCancelable(false);

        llm = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(llm);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setHasFixedSize(true);

        if (CommanMethod.isOnline(getActivity())) {
            RequestParams params = new RequestParams();
            params.put("user_id", MyPreferences.getActiveInstance(getActivity()).getUserId());
            NotificationWebServices(params);
        } else {

            CommanMethod.showAlert("Network Error,Please try again", getActivity());
        }


        return view;
    }

    public void NotificationWebServices(RequestParams params) {
        String Url = WebserviceUrlClass.notifications;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(15000);
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "get notifcation  Response is>>>>" + response.toString());

                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {

                        myRechargeList.clear();
                        NotificationResource mrecharge = null;
                        JSONArray jsonArray = null;
                        jsonArray = response.getJSONArray("data");
                        String unreadMsg = response.getString("unread_msg");
                        if (!unreadMsg.equalsIgnoreCase("0"))
                            MainActivity.txt_notification.setText(unreadMsg);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            mrecharge = new NotificationResource();


                            mrecharge.setId(obj.getString("id"));
                            mrecharge.setNotificationText(obj.getString("notificationText").replace("\u00a0",""));
                            mrecharge.setNotificationTitle(obj.getString("notificationTitle").replace("\u00a0",""));
                            // mrecharge.setCreated_at(obj.getString("created_at"));
                            mrecharge.setCreated_at(Utility.Timeformateanother(obj.getString("created_at").toString().trim()));

                            //  mrecharge.setCreated_at(obj.getString(Utility.Timeformateanother(obj.getString("created_at"))));
                            mrecharge.setIs_read(obj.getString("is_read"));


                            myRechargeList.add(mrecharge);
                        }


                        mAdapter = new Adapter_notifications(getActivity(), myRechargeList, NotificationFragment.this);
                        recycler_view.setAdapter(mAdapter);


                    } else if (respcode.equals("300")) {

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
                final Dialog dialog = new Dialog(getActivity());
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

    public void NotificationReadWebServices(RequestParams params) {
        String Url = WebserviceUrlClass.notificationsread;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(15000);
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "get notifcation read Response is>>>>" + response.toString());

                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {

                        RequestParams params = new RequestParams();
                        params.put("user_id", MyPreferences.getActiveInstance(getActivity()).getUserId());
                        NotificationWebServices(params);


                    } else if (respcode.equals("300")) {

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
                final Dialog dialog = new Dialog(getActivity());
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

}
