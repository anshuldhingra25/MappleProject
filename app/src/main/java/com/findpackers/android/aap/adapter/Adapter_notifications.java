package com.findpackers.android.aap.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findpackers.android.aap.R;
import com.findpackers.android.aap.pojo.NotificationResource;
import com.findpackers.android.aap.pojo.RecentAddedResource;

import java.util.List;

public class Adapter_notifications extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NotificationResource> itemList;
    private Context context;
    int oldposition;
    Readnotifications readnotifications;
    public Adapter_notifications(Context context, List<NotificationResource> itemList,Readnotifications readnotifications) {
        this.itemList = itemList;
        this.context = context;
        this.readnotifications=readnotifications;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notifications, parent, false);
            return new TransactionHistoryViewHolder(layoutView);

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



            ((TransactionHistoryViewHolder) holder).txt_notificationtitle.setText(itemList.get(position).getNotificationTitle());
            ((TransactionHistoryViewHolder) holder).txt_msg.setText(itemList.get(position).getNotificationText());

            if(itemList.get(position).getIs_read().equalsIgnoreCase("0")){

                ((TransactionHistoryViewHolder) holder).ln_row.setBackgroundColor(context.getResources().getColor(R.color.buynow_color));


            }else{

                ((TransactionHistoryViewHolder) holder).ln_row.setBackgroundColor(context.getResources().getColor(R.color.white));

            }


            ((TransactionHistoryViewHolder) holder).txt_txndate.setText(itemList.get(position).getCreated_at());


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
        public TextView  txt_txndate,txt_notificationtitle,txt_msg,txt_readstatus;
        LinearLayout ln_row;

        public TransactionHistoryViewHolder(View itemView) {
            super(itemView);


            txt_txndate = (TextView) itemView.findViewById(R.id.txt_txndate);
            txt_notificationtitle = (TextView) itemView.findViewById(R.id.txt_notificationtitle);
            txt_msg = (TextView) itemView.findViewById(R.id.txt_msg);
            txt_readstatus = (TextView) itemView.findViewById(R.id.txt_readstatus);
            ln_row = (LinearLayout) itemView.findViewById(R.id.ln_row);

            ln_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    oldposition = getAdapterPosition();
                    readnotifications.readnotifications(itemList.get(oldposition).getId());
                }
            });


        }
    }
    public interface Readnotifications {
        void readnotifications(String notificationId );
    }
}
