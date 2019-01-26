package com.findpackers.android.aap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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

import com.findpackers.android.aap.PaytmPayment.JSONParser;
import com.findpackers.android.aap.adapter.LeadsRecyclerAdapter_recharge;
import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.pojo.RechargeResource;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

/**
 * Created by narendra on 2/19/2018.
 */

public class RechargeWalletActivity extends AppCompatActivity implements LeadsRecyclerAdapter_recharge.Callback , PaytmPaymentTransactionCallback {

    Button btnCreditRechage;
    Toolbar toolbar;
    ProgressDialog prgDialog;

    private String userMobile, userEmail;
    private String totalAmount, planId,planName, itemName;

    TextView toolbarTitle,txt_avlableCredit;
    ImageView IvBack;
    private LeadsRecyclerAdapter_recharge mAdapter;
    private ArrayList<RechargeResource> myRechargeList = new ArrayList<RechargeResource>();
    private RecyclerView mRecyclerView_Recharge;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayoutManager llm;
    private boolean dialogcheck;
    String ppoint;
    LinearLayout ln_creditbalance;
    String orderId;
    private String mid, custid;
    String CHECKSUMHASH = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_wallet);
        btnCreditRechage = (Button) findViewById(R.id.btn_cradit_rechage);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        txt_avlableCredit = (TextView) findViewById(R.id.txt_avlableCredit);
        ln_creditbalance = (LinearLayout) findViewById(R.id.ln_creditbalance);
        IvBack = (ImageView) findViewById(R.id.back_icon);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("My Wallet");
        txt_avlableCredit.setText(MyPreferences.getActiveInstance(RechargeWalletActivity.this).getCreditbalance());
        prgDialog = new ProgressDialog(RechargeWalletActivity.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);


        mRecyclerView_Recharge = (RecyclerView) findViewById(R.id.recycler_view_recharge);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swifeRefresh);
        llm = new LinearLayoutManager(RechargeWalletActivity.this);
        mRecyclerView_Recharge.setLayoutManager(llm);
        mRecyclerView_Recharge.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView_Recharge.setHasFixedSize(true);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dialogcheck = true;
                if (CommanMethod.isOnline(RechargeWalletActivity.this)) {
                    RequestParams params = new RequestParams();
                    ActivePlansWebServices(params);
                } else {

                    CommanMethod.showAlert("Network Error,Please try again", RechargeWalletActivity.this);
                }


            }
        });
        if (CommanMethod.isOnline(RechargeWalletActivity.this)) {
            RequestParams params = new RequestParams();
            ActivePlansWebServices(params);
        } else {

            CommanMethod.showAlert("Network Error,Please try again", RechargeWalletActivity.this);
        }

        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ln_creditbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(RechargeWalletActivity.this, AvlableBlance.class);
                startActivity(in);
            }
        });

        btnCreditRechage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                Random rnd = new Random();
                char c = (char) (rnd.nextInt(26) + 'a');

                System.out.println("current ts : "+ts + " "+c);


                orderId="FP"+ts+c;




               // mid = "MoonAr57853311012716";//test sandbox
                mid = "Moonar91949577668913";//production
                custid = MyPreferences.getActiveInstance(RechargeWalletActivity.this).getUserId();

              sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
               dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpUserDetails();
    }

    @Override
    public void callbackrecharge(String planId,String amount,String planName) {

        System.out.println("amount : "+amount);
        System.out.println("planName : "+planName);

         totalAmount=amount;
         itemName="point :"+planName;
         this.planId=planId;
         ppoint=planName;

    }


    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " respon true " + bundle.toString());
        System.out.println("Paytm Response:"+bundle.toString());
        System.out.println("Paytm TXNID:"+bundle.get("TXNID"));
        System.out.println("Paytm STATUS:"+bundle.get("STATUS"));
        System.out.println("Paytm TXNAMOUNT:"+bundle.get("TXNAMOUNT"));
        System.out.println("Paytm BANKNAME:"+bundle.get("BANKNAME"));
        System.out.println("Paytm GATEWAYNAME:"+bundle.get("GATEWAYNAME"));
        System.out.println("Paytm GATEWAYNAME:"+bundle.get("RESPMSG"));
        System.out.println("Paytm GATEWAYNAME:"+bundle.get("CHECKSUMHASH"));


     if(bundle.get("STATUS").toString().equalsIgnoreCase("TXN_SUCCESS")) {
         RequestParams params = new RequestParams();
         params.put("status", bundle.get("STATUS"));
         params.put("order_id", orderId);
         params.put("checksum", bundle.get("CHECKSUMHASH"));
         params.put("txnid", bundle.get("TXNID"));
         params.put("udf1", MyPreferences.getActiveInstance(RechargeWalletActivity.this).getUserId());
         params.put("points", ppoint);
         params.put("amount", bundle.get("TXNAMOUNT"));
         params.put("name_on_card", bundle.get("payTm"));
         params.put("cardnum", bundle.get("GATEWAYNAME"));
         params.put("EMAIL", MyPreferences.getActiveInstance(RechargeWalletActivity.this).getemailId());
         params.put("MOBILE_NO", MyPreferences.getActiveInstance(RechargeWalletActivity.this).getMobile());
         params.put("point_id", planId);

         Log.e("params sucess ", params.toString());
         webServices(params);

     }else if(bundle.get("STATUS").toString().equalsIgnoreCase("PENDING")) {

         RequestParams params = new RequestParams();
         params.put("status", bundle.get("STATUS"));
         params.put("order_id", orderId);
         params.put("checksum", bundle.get("CHECKSUMHASH"));
         params.put("txnid", bundle.get("TXNID"));
         params.put("udf1", MyPreferences.getActiveInstance(RechargeWalletActivity.this).getUserId());
         params.put("points", ppoint);
         params.put("amount", bundle.get("TXNAMOUNT"));
         params.put("name_on_card", bundle.get("payTm"));
         params.put("cardnum", bundle.get("GATEWAYNAME"));
         params.put("EMAIL", MyPreferences.getActiveInstance(RechargeWalletActivity.this).getemailId());
         params.put("MOBILE_NO", MyPreferences.getActiveInstance(RechargeWalletActivity.this).getMobile());
         params.put("point_id", planId);

         Log.e("params sucess ", params.toString());
         webServices(params);


     }else{

         final Dialog dialog = new Dialog(RechargeWalletActivity.this);
         dialog.setContentView(R.layout.dialog_sorry);
         Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
         TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);
         WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
         lp.copyFrom(dialog.getWindow().getAttributes());
         lp.width = WindowManager.LayoutParams.MATCH_PARENT;
         txt_msg.setText(bundle.get("RESPMSG").toString());
         dialogButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.dismiss();
             }
         });
         dialog.show();
         dialog.getWindow().setAttributes(lp);
     }

    }

    @Override
    public void networkNotAvailable() {

    }

    @Override
    public void clientAuthenticationFailed(String s) {

    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  "+ s );
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  " );
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel " );
    }


    private void setUpUserDetails() {
        userEmail = MyPreferences.getActiveInstance(RechargeWalletActivity.this).getemailId();
        userMobile = MyPreferences.getActiveInstance(RechargeWalletActivity.this).getMobile();
        System.out.println("userEmail : "+userEmail);
        System.out.println("userMobile : "+userMobile);

    }


    public void ActivePlansWebServices(RequestParams params) {

        String Url = WebserviceUrlClass.ActivePlans;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("nk", "recharge wallet Response is>>>>" + response.toString());
                    String respMessage = response.getString("responseMessage");
                    String respcode = response.getString("responseCode");

                    if (respcode.equals("200")) {
                        JSONArray jsonArray = null;
                        RechargeResource mrecharge=null;
                        jsonArray = response.getJSONArray("data");
                        myRechargeList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            mrecharge =new RechargeResource();
                            mrecharge.setRechargeSN(obj.getString("id"));
                            mrecharge.setRechargPoint(obj.getString("points"));
                            mrecharge.setRechargeAmount(obj.getString("rs"));
                            mrecharge.setRechargeID(obj.getString("id"));
                            mrecharge.setExtra(obj.getString("extra"));

                            if(obj.getString("rs").startsWith("Free"))
                            {
                                System.out.println("not added in array  : "+obj.getString("extra"));
                            }else{
                                myRechargeList.add(mrecharge);
                            }


                        }
                        mAdapter = new LeadsRecyclerAdapter_recharge(RechargeWalletActivity.this, myRechargeList,RechargeWalletActivity.this);
                        mRecyclerView_Recharge.setAdapter(mAdapter);

                    } else if (respcode.equals("300")) {
                        // Toast.makeText(getApplication(), respMessage, Toast.LENGTH_LONG).show();
                        CommanMethod.showAlert(respMessage,RechargeWalletActivity.this);
                    } else {

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
                    prgDialog.show();
                }
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

    public void webServices(RequestParams params) {
        String url = WebserviceUrlClass.demoPaymentSucess;
        System.out.println("url:"+url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout( 15 * 1000);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                prgDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                prgDialog.hide();
                Log.e("response after payment", "" + response.toString());
                if (response != null) {
                    try {
                        String success = response.getString("responseCode");
                        String respMessage = response.getString("responseMessage");
                        Log.d("object", "" + response);
                        if (success.equals("200")) {
                            JSONObject data = response.getJSONObject("data");
                            String total_points = data.getString("total_points");
                            MyPreferences.getActiveInstance(RechargeWalletActivity.this).setCreditbalance(total_points);
                            MainActivity.avlable_creditbalance.setText(total_points);
                            txt_avlableCredit.setText(total_points);

                            Intent in = new Intent(getApplication(), ThankYouActivity.class);
                            startActivity(in);
                             finish();


                        } else if(success.equals("300")){
                            CommanMethod.showAlert(respMessage,RechargeWalletActivity.this);

                        }else{

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                CommanMethod.showAlert("Unable to connect the server,please try again", RechargeWalletActivity.this);
                prgDialog.dismiss();
            }


            @Override
            public void onFinish() {
                super.onFinish();
                prgDialog.dismiss();
            }
        });
    }


    // Paytm integratins by navin

    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(RechargeWalletActivity.this);

       // String url = "http://www.blueappsoftware.com/payment/payment_paytm/generateChecksum.php";
        String url = WebserviceUrlClass.generatechecksum;

       String varifyurl="https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+orderId; // for test sandbox
      //  String callbackurl="https://securegw.paytm.in/theia/processTransaction";
       // String varifyurl="https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=<ORDERIDVALUE>";


        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();

            mid = "Moonar91949577668913";
            custid = MyPreferences.getActiveInstance(RechargeWalletActivity.this).getUserId();

        }

        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParser jsonParser = new JSONParser(RechargeWalletActivity.this);
            String  param=
                    "MID="+mid+
                            "&ORDER_ID=" + orderId+
                            "&CUST_ID="+custid+
                            "&EMAIL="+ MyPreferences.getActiveInstance(RechargeWalletActivity.this).getemailId()+
                            "&MOBILE_NO="+ MyPreferences.getActiveInstance(RechargeWalletActivity.this).getMobile()+
                            "&TXN_AMOUNT="+totalAmount;



            Log.e("checksum"," param string "+param );

            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);

            Log.e("CheckSum result >>", jsonObject.toString());
            if (jsonObject != null) {
                Log.e("CheckSum result >>", jsonObject.toString());
                try {

                    CHECKSUMHASH = jsonObject.has("CHECKSUMHASH") ? jsonObject.getString("CHECKSUMHASH") : "";

                    String jsonFormattedString = new JSONTokener(CHECKSUMHASH).nextValue().toString();

                    Log.e("CheckSum result >>", CHECKSUMHASH);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e(" setup acc ", "   result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

           // PaytmPGService Service = PaytmPGService.getStagingService();
            PaytmPGService Service = PaytmPGService.getProductionService();
            Map<String, String> paramMap = new HashMap<String, String>();


            paramMap.put("MID", mid);
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", totalAmount);
            paramMap.put("EMAIL", MyPreferences.getActiveInstance(RechargeWalletActivity.this).getemailId());
            paramMap.put("MOBILE_NO", MyPreferences.getActiveInstance(RechargeWalletActivity.this).getMobile());
           // paramMap.put("WEBSITE", "APPSTAGING");// for test sandbox
            paramMap.put("WEBSITE", "APPPROD");
            paramMap.put("CALLBACK_URL",varifyurl);
            paramMap.put("CHECKSUMHASH", CHECKSUMHASH);
           // paramMap.put("INDUSTRY_TYPE_ID", "Retail");//for test sandbox
            paramMap.put("INDUSTRY_TYPE_ID", "Retail109");//for prodction
            PaytmOrder Order = new PaytmOrder(paramMap);



            Log.e("checksum ", paramMap.toString());

            Service.initialize(Order, null);
            // start payment service call here
            Service.startPaymentTransaction(RechargeWalletActivity.this, true, true, RechargeWalletActivity.this);

        }
    }


    public void genrateChksum(RequestParams params) {

        String Url = WebserviceUrlClass.generatechecksum;
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println(" response paytm chksum :"+response);


                try {

                    JSONObject obj = new JSONObject(response.getString("CHECKSUMHASH"));

                    Log.d("My App", obj.toString());

                } catch (Throwable t) {

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










    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

}
