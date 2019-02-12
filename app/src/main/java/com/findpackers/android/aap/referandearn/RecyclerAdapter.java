package com.findpackers.android.aap.referandearn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.findpackers.android.aap.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    public List<Contacts> cont;
    Contacts list;
    private ArrayList<Contacts> arraylist;
    boolean checked = false;
    View vv;
    private onItemClick onItemClick;

    public RecyclerAdapter(LayoutInflater inflater, List<Contacts> items, onItemClick onItemClickt) {
        this.layoutInflater = inflater;
        this.cont = items;
        this.arraylist = new ArrayList<Contacts>();
        this.arraylist.addAll(cont);
        this.onItemClick = onItemClickt;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.contactlist_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        list = cont.get(position);
        String name = (list.getName());

        holder.title.setText(name);
        holder.phone.setText(list.getPhone());
        holder.txt_invite_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onInviteClick(list.getName(), holder.phone.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cont.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView phone;
        TextView txt_invite_contact;
        public RelativeLayout contact_select_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            title = (TextView) itemView.findViewById(R.id.tv_contact_name);
            phone = (TextView) itemView.findViewById(R.id.tv_contact_number);
            contact_select_layout = (RelativeLayout) itemView.findViewById(R.id.contact_select_layout);
            txt_invite_contact = (TextView) itemView.findViewById(R.id.txt_invite_contact);

        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface onItemClick {
        void onInviteClick(String name, String phone);

    }

    public void updateList(ArrayList<Contacts> list) {
        cont = list;
        notifyDataSetChanged();
    }

}
 
 