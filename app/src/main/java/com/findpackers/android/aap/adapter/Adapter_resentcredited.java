package com.findpackers.android.aap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findpackers.android.aap.R;
import com.findpackers.android.aap.pojo.ResourceDebited;

import java.util.List;

public class Adapter_resentcredited extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ResourceDebited> itemList;
    private Context context;
    int oldposition;
    String propertytype;

    public Adapter_resentcredited(Context context, List<ResourceDebited> itemList) {
        this.itemList = itemList;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_debited, parent, false);
            return new TransactionHistoryViewHolder(layoutView);

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            propertytype=itemList.get(position).getPropertySize();
            ((TransactionHistoryViewHolder) holder).txt_txndate.setText(itemList.get(position).getPurchase_date());
            ((TransactionHistoryViewHolder) holder).txt_balence.setText(itemList.get(position).getLeadAmount());

            if(itemList.get(position).getCar().equalsIgnoreCase("1"))
            {
                propertytype=propertytype+", Car";
            } if(itemList.get(position).getBike().equalsIgnoreCase("1"))
            {
                propertytype=propertytype+", Bike";
            }
            ((TransactionHistoryViewHolder) holder).txt_addedby.setText(propertytype);

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
        public TextView  txt_txndate, txt_balence,txt_addedby;

        public TransactionHistoryViewHolder(View itemView) {
            super(itemView);


            txt_txndate = (TextView) itemView.findViewById(R.id.txt_txndate);
            txt_balence = (TextView) itemView.findViewById(R.id.txt_balence);
            txt_addedby = (TextView) itemView.findViewById(R.id.txt_addedby);



        }
    }

}
