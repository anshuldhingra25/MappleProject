package com.findpackers.android.aap.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.findpackers.android.aap.R;
import com.findpackers.android.aap.adapter.Adapter_resentcredited;
import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.Utility;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.pojo.ResourceDebited;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Anu on 22/04/17.
 */



public class FragmentTwo extends Fragment {
    LinearLayoutManager llm;
    RecyclerView recycler_view;
    ProgressDialog prgDialog;
    private Adapter_resentcredited mAdapter;
    private ArrayList<ResourceDebited> resourceDebited = new ArrayList<ResourceDebited>();
    public FragmentTwo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_two, container, false);

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
            params.put("userId", MyPreferences.getActiveInstance(getActivity()).getUserId());
            recentdebitedWebServices(params);
        } else {

            CommanMethod.showAlert("Network Error,Please try again", getActivity());
        }



        return view;
    }
    public void recentdebitedWebServices(RequestParams params) {
        String Url = WebserviceUrlClass.getdebit;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(15000);
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "get wallet debit Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {

                        resourceDebited.clear();
                        ResourceDebited mrecharge=null;
                        JSONArray jsonArray = null;
                        jsonArray = response.getJSONArray("data");
                        String textslug;
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            mrecharge = new ResourceDebited();


                            mrecharge.setPropertySize("Paid for "+ Utility.chkstringnull(obj.getString("propertySize")));
                            mrecharge.setPurchase_date(Utility.Timeformateanother(obj.getString("purchase_date")));
                            mrecharge.setLeadAmount(obj.getString("leadAmount"));
                            mrecharge.setCar(obj.getString("car"));
                            mrecharge.setBike(obj.getString("bike"));
                            resourceDebited.add(mrecharge);
                        }

                        mAdapter = new Adapter_resentcredited(getActivity(), resourceDebited);
                        recycler_view.setAdapter(mAdapter);


                    } else if (respcode.equals("300")) {
                        CommanMethod.showAlert(respMessage,getActivity());
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