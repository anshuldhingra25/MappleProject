package com.findpackers.android.aap.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findpackers.android.aap.R;
import com.findpackers.android.aap.pojo.RecentAddedResource;

import java.util.List;

public class Adapter_resentadded extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecentAddedResource> itemList;
    private Context context;
    int oldposition;

    public Adapter_resentadded(Context context, List<RecentAddedResource> itemList) {
        this.itemList = itemList;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_wallet, parent, false);
            return new TransactionHistoryViewHolder(layoutView);

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



            ((TransactionHistoryViewHolder) holder).txt_txndate.setText(itemList.get(position).getTxndate());
            ((TransactionHistoryViewHolder) holder).txt_balence.setText(itemList.get(position).getTxnbalance());
            if(itemList.get(position).getRchrgAmount().equalsIgnoreCase("Free Credit"))
            {
                ((TransactionHistoryViewHolder) holder).txt_rchrgamount.setText("free credit");
                ((TransactionHistoryViewHolder) holder).txt_rchrgamount.setTextColor(Color.parseColor("#5fc73b"));



            }else{
                ((TransactionHistoryViewHolder) holder).txt_rchrgamount.setText(" ");
                ((TransactionHistoryViewHolder) holder).txt_rchrgamount.setTextColor(Color.parseColor("#4ab9cf"));

            }
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
        public TextView  txt_txndate, txt_balence,txt_rchrgamount;

        public TransactionHistoryViewHolder(View itemView) {
            super(itemView);


            txt_txndate = (TextView) itemView.findViewById(R.id.txt_txndate);
            txt_balence = (TextView) itemView.findViewById(R.id.txt_balence);
            txt_rchrgamount = (TextView) itemView.findViewById(R.id.txt_rchrgamount);



        }
    }

}
