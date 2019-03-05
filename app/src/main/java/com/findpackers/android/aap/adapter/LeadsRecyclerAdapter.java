package com.findpackers.android.aap.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.findpackers.android.aap.MainActivity;
import com.findpackers.android.aap.R;
import com.findpackers.android.aap.RechargeWalletActivity;
import com.findpackers.android.aap.commanUtill.MyApplication;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.interfaces.OnLoadMoreListener;
import com.findpackers.android.aap.pojo.LeadsResources;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by narendra on 2/20/2018.
 */

public class LeadsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> { //RecyclerView.Adapter<LeadsRecyclerAdapter.TransactionHistoryViewHolder> {


    private int visibleThreshold = 10;
    private List<LeadsResources> itemList;
    private Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    SendWhatsApp_Sms sendWhatsApp_Sms;
    int oldposition;
    ProgressDialog prgDialog;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;

    public LeadsRecyclerAdapter(Context context, List<LeadsResources> itemList, SendWhatsApp_Sms sendWhatsApp_Sms, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.context = context;
        this.sendWhatsApp_Sms = sendWhatsApp_Sms;

        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    onLoadMoreListener.onLoadMore(lastVisibleItem, totalItemCount);
                    Log.e("Load more : ", "totalItemCount==" + totalItemCount);
                    Log.e("Load more : ", "lastVisibleItem==" + lastVisibleItem);

                }

            });
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
            return new HeaderViewHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_leads, parent, false);
            return new TransactionHistoryViewHolder(layoutView);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //  ItemObject mObject = itemList.get(position);


        if (holder instanceof HeaderViewHolder) {
            // ((HeaderViewHolder) holder).headerTitle.setText(mObject.getContents());
            ((HeaderViewHolder) holder).txt_totalleads.setText(String.valueOf(MyApplication.totalLeads));


        } else if (holder instanceof TransactionHistoryViewHolder) {
            // ((ItemViewHolder) holder).itemContent.setText(mObject.getContents());
            String staust = itemList.get(position).getStatus();

            ((TransactionHistoryViewHolder) holder).txtcall_time.setText(itemList.get(position).getLeadtime() + " min");
            ((TransactionHistoryViewHolder) holder).txtcradit_value.setText(itemList.get(position).getLeadamount());
            ((TransactionHistoryViewHolder) holder).txtdrop1address.setText(itemList.get(position).getDetailedpickaddress());
            ((TransactionHistoryViewHolder) holder).txtmobile_no.setText(itemList.get(position).getMobileNo());
            ((TransactionHistoryViewHolder) holder).txtdate.setText(itemList.get(position).getCreated_at());
            ((TransactionHistoryViewHolder) holder).txthtype.setText(itemList.get(position).getLeadType());
            ((TransactionHistoryViewHolder) holder).txtdrop_address.setText(itemList.get(position).getDroplocation());
            ((TransactionHistoryViewHolder) holder).txtpickup_address.setText(itemList.get(position).getPickuplocation());
            ((TransactionHistoryViewHolder) holder).txtmaxvendors.setText(itemList.get(position).getBids() + "/" + itemList.get(position).getMaxVendors());
            ((TransactionHistoryViewHolder) holder).txttime.setText(itemList.get(position).getLeadtime());
            ((TransactionHistoryViewHolder) holder).txtleadtitle.setText(itemList.get(position).getLead_title());
            ((TransactionHistoryViewHolder) holder).txtemail_id.setText(itemList.get(position).getEmailId());
            ((TransactionHistoryViewHolder) holder).txt_pickupdate.setText(itemList.get(position).getPicupdate());

            if (itemList.get(position).getSpl_status().equalsIgnoreCase("1")) {

                if (itemList.get(position).getCategory_status().equalsIgnoreCase("1")) {

                    ((TransactionHistoryViewHolder) holder).leadcategory.setVisibility(View.VISIBLE);
                    ((TransactionHistoryViewHolder) holder).leadcategory2.setVisibility(View.INVISIBLE);
                    ((TransactionHistoryViewHolder) holder).leadcategory3.setVisibility(View.INVISIBLE);

                } else if (itemList.get(position).getCategory_status().equalsIgnoreCase("2")) {
                    ((TransactionHistoryViewHolder) holder).leadcategory.setVisibility(View.VISIBLE);
                    ((TransactionHistoryViewHolder) holder).leadcategory2.setVisibility(View.VISIBLE);
                    ((TransactionHistoryViewHolder) holder).leadcategory3.setVisibility(View.INVISIBLE);
                } else if (itemList.get(position).getCategory_status().equalsIgnoreCase("3")) {
                    ((TransactionHistoryViewHolder) holder).leadcategory.setVisibility(View.VISIBLE);
                    ((TransactionHistoryViewHolder) holder).leadcategory2.setVisibility(View.VISIBLE);
                    ((TransactionHistoryViewHolder) holder).leadcategory3.setVisibility(View.VISIBLE);
                }


            }

            if (itemList.get(position).getCar().equalsIgnoreCase("1")) {
                ((TransactionHistoryViewHolder) holder).txt_car.setVisibility(View.VISIBLE);
            } else {
                ((TransactionHistoryViewHolder) holder).txt_car.setVisibility(View.GONE);
            }
            if (itemList.get(position).getBike().equalsIgnoreCase("1")) {
                ((TransactionHistoryViewHolder) holder).txt_bike.setVisibility(View.VISIBLE);
            } else {
                ((TransactionHistoryViewHolder) holder).txt_bike.setVisibility(View.GONE);
            }

            if (itemList.get(position).getLevel().equalsIgnoreCase("1")) {

                ((TransactionHistoryViewHolder) holder).leadcategory.setTextColor(Color.parseColor("#ffffff"));
                ((TransactionHistoryViewHolder) holder).leadcategory2.setTextColor(Color.parseColor("#f3484747"));
                ((TransactionHistoryViewHolder) holder).leadcategory3.setTextColor(Color.parseColor("#f3484747"));
                ((TransactionHistoryViewHolder) holder).leadcategory.setBackground(ContextCompat.getDrawable(context, R.drawable.standardbackground));
                ((TransactionHistoryViewHolder) holder).leadcategory2.setBackground(ContextCompat.getDrawable(context, R.drawable.premium));
                ((TransactionHistoryViewHolder) holder).leadcategory3.setBackground(ContextCompat.getDrawable(context, R.drawable.premium));

            } else if (itemList.get(position).getLevel().equalsIgnoreCase("2")) {

                ((TransactionHistoryViewHolder) holder).leadcategory2.setTextColor(Color.parseColor("#ffffff"));
                ((TransactionHistoryViewHolder) holder).leadcategory.setTextColor(Color.parseColor("#f3484747"));
                ((TransactionHistoryViewHolder) holder).leadcategory3.setTextColor(Color.parseColor("#f3484747"));
                ((TransactionHistoryViewHolder) holder).leadcategory2.setBackground(ContextCompat.getDrawable(context, R.drawable.standardbackground));
                ((TransactionHistoryViewHolder) holder).leadcategory.setBackground(ContextCompat.getDrawable(context, R.drawable.premium));
                ((TransactionHistoryViewHolder) holder).leadcategory3.setBackground(ContextCompat.getDrawable(context, R.drawable.premium));

            } else if (itemList.get(position).getLevel().equalsIgnoreCase("3")) {

                ((TransactionHistoryViewHolder) holder).leadcategory3.setTextColor(Color.parseColor("#ffffff"));
                ((TransactionHistoryViewHolder) holder).leadcategory.setTextColor(Color.parseColor("#f3484747"));
                ((TransactionHistoryViewHolder) holder).leadcategory2.setTextColor(Color.parseColor("#f3484747"));
                ((TransactionHistoryViewHolder) holder).leadcategory3.setBackground(ContextCompat.getDrawable(context, R.drawable.standardbackground));
                ((TransactionHistoryViewHolder) holder).leadcategory2.setBackground(ContextCompat.getDrawable(context, R.drawable.premium));
                ((TransactionHistoryViewHolder) holder).leadcategory.setBackground(ContextCompat.getDrawable(context, R.drawable.premium));

            }


            String k = itemList.get(position).getBids();
            String k1 = itemList.get(position).getMaxVendors();
            //String k1 = itemList.get(position).getMaxVendors();

            if (Integer.parseInt(k) == Integer.parseInt(k1)) {
                ((TransactionHistoryViewHolder) holder).layoutAllDetais.setBackgroundColor(context.getResources().getColor(R.color.souldout_color));
                ((TransactionHistoryViewHolder) holder).layoutShowDetails.setVisibility(View.GONE);

                if (itemList.get(position).getBuyLead().equalsIgnoreCase("y")) {
                    ((TransactionHistoryViewHolder) holder).txtbuyNow.setText("My Lead  ");
                    ((TransactionHistoryViewHolder) holder).txtmaxvendors.setVisibility(View.INVISIBLE);
                    ((TransactionHistoryViewHolder) holder).txtbuyNow.setBackground(ContextCompat.getDrawable(context, R.drawable.round_button_delete));

                    ((TransactionHistoryViewHolder) holder).txtbuyNow.setTextColor(Color.parseColor("#000000"));
                    ((TransactionHistoryViewHolder) holder).txtbuyNow.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bid_icon, 0, 0, 0);
                    ((TransactionHistoryViewHolder) holder).txtbuyNow.setCompoundDrawablePadding(3);
                    ((TransactionHistoryViewHolder) holder).layoutShowDetails.setVisibility(View.VISIBLE);
                } else {
                    ((TransactionHistoryViewHolder) holder).txtbuyNow.setText("SOLD  ");
                    ((TransactionHistoryViewHolder) holder).txtbuyNow.setBackground(ContextCompat.getDrawable(context, R.drawable.round_button_delete));

                    ((TransactionHistoryViewHolder) holder).txtbuyNow.setTextColor(Color.parseColor("#000000"));
                    ((TransactionHistoryViewHolder) holder).txtbuyNow.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bid_icon, 0, 0, 0);
                    ((TransactionHistoryViewHolder) holder).txtbuyNow.setCompoundDrawablePadding(3);
                }
            } else if (itemList.get(position).getBuyLead().equalsIgnoreCase("y")) {

                ((TransactionHistoryViewHolder) holder).layoutAllDetais.setBackgroundColor(context.getResources().getColor(R.color.buynow_color));
                ((TransactionHistoryViewHolder) holder).layoutShowDetails.setVisibility(View.VISIBLE);
                ((TransactionHistoryViewHolder) holder).txtbuyNow.setText("My Lead  ");
                ((TransactionHistoryViewHolder) holder).txtmaxvendors.setVisibility(View.INVISIBLE);
                ((TransactionHistoryViewHolder) holder).txtbuyNow.setBackground(ContextCompat.getDrawable(context, R.drawable.round_button_delete));
                ((TransactionHistoryViewHolder) holder).txtbuyNow.setTextColor(Color.parseColor("#000000"));
                ((TransactionHistoryViewHolder) holder).txtbuyNow.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bid_icon, 0, 0, 0);
                ((TransactionHistoryViewHolder) holder).txtbuyNow.setCompoundDrawablePadding(3);


            } else {

                ((TransactionHistoryViewHolder) holder).layoutAllDetais.setBackgroundColor(context.getResources().getColor(R.color.nobuy_color));
                ((TransactionHistoryViewHolder) holder).layoutShowDetails.setVisibility(View.GONE);
                ((TransactionHistoryViewHolder) holder).txtbuyNow.setText("BUY      ");
                ((TransactionHistoryViewHolder) holder).txtbuyNow.setBackground(ContextCompat.getDrawable(context, R.drawable.selectbackgroundappcolor));
                ((TransactionHistoryViewHolder) holder).txtbuyNow.setTextColor(Color.parseColor("#ffffff"));
                ((TransactionHistoryViewHolder) holder).txtbuyNow.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bid_iconwhite, 0, 0, 0);
                ((TransactionHistoryViewHolder) holder).txtbuyNow.setCompoundDrawablePadding(3);

            }
            ((TransactionHistoryViewHolder) holder).txtbuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println("BuyStatus : " + itemList.get(position).getBuyStatus());


                    if (!((TransactionHistoryViewHolder) holder).txtbuyNow.getText().toString().trim().equals("My Lead") && !((TransactionHistoryViewHolder) holder).txtbuyNow.getText().toString().trim().equals("SOLD")) {

                        int balance = Integer.parseInt(MyPreferences.getActiveInstance(context).getCreditbalance());
                        if (balance < Integer.parseInt(itemList.get(position).getLeadamount())) {

                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.dialog_sorry_addcredit);
                            Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                            Button btn_addcredit = (Button) dialog.findViewById(R.id.btn_addcredit);
                            TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);
                            // if button is clicked, close the custom dialog

                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;


                            txt_msg.setText("Your Credit Ballance is runing low, Please Add Credit in your wallet");

                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            btn_addcredit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent in = new Intent(context, RechargeWalletActivity.class);
                                    context.startActivity(in);


                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            dialog.getWindow().setAttributes(lp);


                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Confirm and proceed ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();


                                            //sendWhatsApp_Sms.methodSendWhatsApp_Sms(itemList.get(oldposition).getLead_id(), itemList.get(oldposition).getMobileNo(), itemList.get(oldposition).getEmailId(), "buyNow");


                                            RequestParams params = new RequestParams();
                                            params.put("userId", MyPreferences.getActiveInstance(context).getUserId());
                                            params.put("leadId", itemList.get(position).getLead_id());
                                            params.put("buyLead", "y");
                                            BuyLeadWebservices(params, ((TransactionHistoryViewHolder) holder).txtbuyNow, ((TransactionHistoryViewHolder) holder).layoutShowDetails);


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
                            alert.setTitle("Buy lead");
                            alert.show();
                        }

                    } else {

                        Toast.makeText(context, "already purchased ", Toast.LENGTH_LONG).show();
                    }


                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

        public View view;
        ImageView ivwhatsapp, ivmsg, ivcallphone, send_email;
        public TextView txtleadtitle, txttime, txtdate, txtcradit_value, txtmaxvendors, txthtype, txtdrop_address, txtpickup_address, leadcategory, leadcategory2, leadcategory3;
        public TextView txtmobile_no, txtcall_time, txtemail_id, txtdrop1address, txtbuyNow, txt_email_time, txt_pickupdate, txt_bike, txt_car;
        LinearLayout layoutShowDetails, layoutAllDetais, txt_phonecall, txt_email, top_leadtype;


        public TransactionHistoryViewHolder(View itemView) {
            super(itemView);

            layoutShowDetails = (LinearLayout) itemView.findViewById(R.id.layout_contects);
            layoutAllDetais = (LinearLayout) itemView.findViewById(R.id.layout_all_details);

            txtleadtitle = (TextView) itemView.findViewById(R.id.txt_leadtitle);
            txttime = (TextView) itemView.findViewById(R.id.txt_time);
            txtdate = (TextView) itemView.findViewById(R.id.txt_date);
            txtcradit_value = (TextView) itemView.findViewById(R.id.txt_cradit_value);
            txtmaxvendors = (TextView) itemView.findViewById(R.id.txt_maxvendors);
            txthtype = (TextView) itemView.findViewById(R.id.txt_htype);
            txtdrop_address = (TextView) itemView.findViewById(R.id.txt_drop_address);
            txtpickup_address = (TextView) itemView.findViewById(R.id.txt_pickup_address);
            txtmobile_no = (TextView) itemView.findViewById(R.id.txt_mobile_no);
            txt_pickupdate = (TextView) itemView.findViewById(R.id.txt_pickupdate);
            txtcall_time = (TextView) itemView.findViewById(R.id.txt_call_time);
            txtemail_id = (TextView) itemView.findViewById(R.id.txt_email_id);
            txtdrop1address = (TextView) itemView.findViewById(R.id.txt_drop1_address);
            txtbuyNow = (TextView) itemView.findViewById(R.id.txt_buyNow);
            ivwhatsapp = (ImageView) itemView.findViewById(R.id.iv_whatsapp);
            ivmsg = (ImageView) itemView.findViewById(R.id.iv_msg);
            ivcallphone = (ImageView) itemView.findViewById(R.id.iv_callphone);
            send_email = (ImageView) itemView.findViewById(R.id.send_email);
            txt_email_time = (TextView) itemView.findViewById(R.id.txt_email_time);
            txt_bike = (TextView) itemView.findViewById(R.id.txt_bike);
            txt_car = (TextView) itemView.findViewById(R.id.txt_car);
            leadcategory = (TextView) itemView.findViewById(R.id.leadcategory);
            leadcategory2 = (TextView) itemView.findViewById(R.id.leadcategory2);
            leadcategory3 = (TextView) itemView.findViewById(R.id.leadcategory3);
            top_leadtype = (LinearLayout) itemView.findViewById(R.id.top_leadtype);
            txt_phonecall = (LinearLayout) itemView.findViewById(R.id.txt_phonecall);
            txt_email = (LinearLayout) itemView.findViewById(R.id.txt_email);

            ivwhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oldposition = getAdapterPosition();

                    sendWhatsApp_Sms.methodSendWhatsApp_Sms(itemList.get(oldposition).getLead_id(), itemList.get(oldposition).getMobileNo(), itemList.get(oldposition).getEmailId(), "whatsapp");

                }
            });
            txt_phonecall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oldposition = getAdapterPosition();

                    sendWhatsApp_Sms.methodSendWhatsApp_Sms(itemList.get(oldposition).getLead_id(), itemList.get(oldposition).getMobileNo(), itemList.get(oldposition).getEmailId(), "call");

                }
            });

            txt_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oldposition = getAdapterPosition();

                    sendWhatsApp_Sms.methodSendWhatsApp_Sms(itemList.get(oldposition).getLead_id(), itemList.get(oldposition).getMobileNo(), itemList.get(oldposition).getEmailId(), "email");

                }
            });

            ivmsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oldposition = getAdapterPosition();

                    sendWhatsApp_Sms.methodSendWhatsApp_Sms(itemList.get(oldposition).getLead_id(), itemList.get(oldposition).getMobileNo(), itemList.get(oldposition).getEmailId(), "sms");

                }
            });

           /* txtbuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oldposition = getAdapterPosition();

                    System.out.println("BuyStatus : " + itemList.get(oldposition).getBuyStatus());



                    if(!txtbuyNow.getText().toString().trim().equals("My Lead")&&!txtbuyNow.getText().toString().trim().equals("SOLD")) {

                           int balance= Integer.parseInt(MyPreferences.getActiveInstance(context).getCreditbalance());
                            if(balance<Integer.parseInt(itemList.get(oldposition).getLeadamount()))
                            {

                                final Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.dialog_sorry_addcredit);
                                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                                Button btn_addcredit = (Button) dialog.findViewById(R.id.btn_addcredit);
                                TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);
                                // if button is clicked, close the custom dialog

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;


                                txt_msg.setText("Your Credit Ballance is runing low, Please Add Credit in your wallet");

                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                btn_addcredit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent in = new Intent(context, RechargeWalletActivity.class);
                                        context.startActivity(in);


                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                                dialog.getWindow().setAttributes(lp);



                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Confirms and proceed.?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();


                                                //sendWhatsApp_Sms.methodSendWhatsApp_Sms(itemList.get(oldposition).getLead_id(), itemList.get(oldposition).getMobileNo(), itemList.get(oldposition).getEmailId(), "buyNow");

                                                RequestParams params = new RequestParams();
                                                params.put("userId", MyPreferences.getActiveInstance(context).getUserId());
                                                params.put("leadId", itemList.get(oldposition).getLead_id());
                                                params.put("buyLead", "y");
                                                BuyLeadWebservices(params);



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
                                alert.setTitle("Buy lead");
                                alert.show();
                            }

                        }
                    else{

                        Toast.makeText(context, "already purchased ", Toast.LENGTH_LONG).show();
                    }


                }
            });*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent paymentWithService = new Intent(context, QuotationOfferDetailsActivity.class);
                    context.startActivity(paymentWithService);*/
                }
            });

        }


    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView headerTitle, txt_totalleads;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView) itemView.findViewById(R.id.header_id);
            txt_totalleads = (TextView) itemView.findViewById(R.id.txt_totalleads);
        }
    }

    public interface SendWhatsApp_Sms {
        void methodSendWhatsApp_Sms(String leadId, String mobileNo, String emailId, String requestType);
    }


    public void BuyLeadWebservices(RequestParams params, final TextView mytextbuy, final LinearLayout lnviewdetails) {

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
                        String availableCredits = response.getString("availableCredits");
                        MyPreferences.getActiveInstance(context).setCreditbalance(availableCredits);

                        MainActivity.avlable_creditbalance.setText(availableCredits);

                        mytextbuy.setText("My Lead  ");
                        lnviewdetails.setVisibility(View.VISIBLE);

                        Toast.makeText(context, respMessage, Toast.LENGTH_LONG).show();

                    } else if (respcode.equals("300")) {
                        Toast.makeText(context, respMessage, Toast.LENGTH_LONG).show();
                    } else if (respcode.equals("0")) {
                        final Dialog dialog = new Dialog(context);
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
                        Toast.makeText(context, respMessage, Toast.LENGTH_LONG).show();
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

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

}
