package com.findpackers.android.aap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.findpackers.android.aap.R;
import com.findpackers.android.aap.pojo.RechargeResource;

import java.util.List;

public class LeadsRecyclerAdapter_recharge extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RechargeResource> itemList;
    private Context context;
    RadioButton rb;
    private RadioButton lastCheckedRB = null;
    private static int lastCheckedPos = 0;
    Callback callback;
    int oldposition;
    int radioclickedpostion;
    private RadioGroup lastCheckedRadioGroup = null;
    public LeadsRecyclerAdapter_recharge(Context context, List<RechargeResource> itemList, Callback callback) {
        this.itemList = itemList;
        this.context = context;
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recharge, parent, false);
            return new TransactionHistoryViewHolder(layoutView);

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

       // int id = (position+1)*100;
        int id = position;

        rb = new RadioButton(LeadsRecyclerAdapter_recharge.this.context);
        rb.setId(id++);
        if(position==0){
            rb.setChecked(true);
            callback.callbackrecharge(itemList.get(position).getRechargeID(),itemList.get(position).getRechargeAmount(),itemList.get(position).getRechargPoint());

        }
        rb.setText("");

        ((TransactionHistoryViewHolder) holder).rg_selectedradigroup.addView(rb);

            ((TransactionHistoryViewHolder) holder).txt_amount.setText(itemList.get(position).getRechargeAmount());
            ((TransactionHistoryViewHolder) holder).txt_points.setText(itemList.get(position).getRechargPoint());
            ((TransactionHistoryViewHolder) holder).txt_buyNow.setText(itemList.get(position).getExtra()+"%");
         /* ((TransactionHistoryViewHolder) holder).layout_all_details.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  rb.setChecked(true);
              }
          });*/
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView txt_buyNow, txt_points, txt_amount;
        public RadioGroup rg_selectedradigroup;
        LinearLayout layout_all_details;
        RadioButton rd_selectplan;

        public TransactionHistoryViewHolder(View itemView) {
            super(itemView);

            txt_buyNow = (TextView) itemView.findViewById(R.id.txt_buyNow);
            txt_points = (TextView) itemView.findViewById(R.id.txt_points);
            txt_amount = (TextView) itemView.findViewById(R.id.txt_amount);
            rd_selectplan = (RadioButton) itemView.findViewById(R.id.rd_selectplan);
            layout_all_details = (LinearLayout) itemView.findViewById(R.id.layout_all_details);
            rg_selectedradigroup = (RadioGroup) itemView.findViewById(R.id.rg_selectedradigroup);

            txt_buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* oldposition = getAdapterPosition();
                    callback.callbackrecharge(itemList.get(oldposition).getRechargeID(),itemList.get(oldposition).getRechargeAmount(),itemList.get(oldposition).getRechargPoint());*/
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oldposition = getAdapterPosition();

                    ((RadioButton)rg_selectedradigroup.getChildAt(1)).setChecked(true);



                }
            });

            txt_buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oldposition = getAdapterPosition();

                    ((RadioButton)rg_selectedradigroup.getChildAt(1)).setChecked(true);



                }
            });
            txt_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oldposition = getAdapterPosition();

                    ((RadioButton)rg_selectedradigroup.getChildAt(1)).setChecked(true);



                }
            });
            txt_points.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oldposition = getAdapterPosition();

                    ((RadioButton)rg_selectedradigroup.getChildAt(1)).setChecked(true);



                }
            });


            rg_selectedradigroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    //since only one package is allowed to be selected
                    //this logic clears previous selection
                    //it checks state of last radiogroup and
                    // clears it if it meets conditions
                    if (lastCheckedRadioGroup != null
                            && lastCheckedRadioGroup.getCheckedRadioButtonId()
                            != radioGroup.getCheckedRadioButtonId()
                            && rg_selectedradigroup.getCheckedRadioButtonId() != -1) {
                        lastCheckedRadioGroup.clearCheck();


                        radioclickedpostion=radioGroup.getCheckedRadioButtonId();
                        callback.callbackrecharge(itemList.get(radioclickedpostion).getRechargeID(),itemList.get(radioclickedpostion).getRechargeAmount(),itemList.get(radioclickedpostion).getRechargPoint());

/*
                        Toast.makeText(LeadsRecyclerAdapter_recharge.this.context,
                                "Radio button clicked " + radioclickedpostion,
                                Toast.LENGTH_SHORT).show();*/

                    }
                    lastCheckedRadioGroup = radioGroup;

                }
            });


        }
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView headerTitle;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView) itemView.findViewById(R.id.header_id);
        }
    }

    public interface Callback {
        void callbackrecharge(String planid, String amount, String planName);
    }

}
