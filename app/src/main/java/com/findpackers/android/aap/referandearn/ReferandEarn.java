package com.findpackers.android.aap.referandearn;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.findpackers.android.aap.BuildConfig;
import com.findpackers.android.aap.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ReferandEarn extends AppCompatActivity implements RecyclerAdapter.onItemClick {
    ArrayList<Contacts> selectUsers;
    private RecyclerView recyclerView;
    RecyclerAdapter adapter;
    Cursor phones;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    android.support.v7.widget.Toolbar toolbar;
    ImageView back_icon;
    private EditText searchEt;
    JSONObject jsonObject = new JSONObject();
    JsonArray contactsArray = new JsonArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referand_earn);
        toolbar = findViewById(R.id.my_toolbar);
        back_icon = toolbar.findViewById(R.id.back_icon);
        recyclerView = (RecyclerView) findViewById(R.id.contacts_list_refer_earn);
        recyclerView.setHasFixedSize(true);
        searchEt = findViewById(R.id.searchEt);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        selectUsers = new ArrayList<>();
        showContacts();
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            phones = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            LoadContact loadContact = new LoadContact();
            loadContact.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onInviteClick(String name, String phone) {
        System.out.println("" + phone);
//        Toast.makeText(this,"Name=="+name+"Phone=="+phone,Toast.LENGTH_SHORT).show();
        String shareMessage = "\n" + getResources().getString(R.string.share_message) + "\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone));
        intent.putExtra("sms_body", shareMessage);
        startActivity(intent);
       /* try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "FindPackers");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }*/
    }

    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone

            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                HashSet<String> normalizedNumbersAlreadyFound = new HashSet<>();
                int indexOfNormalizedNumber = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                int indexOfDisplayName = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indexOfDisplayNumber = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                if (phones.getCount() == 0) {

                }

                while (phones.moveToNext()) {

                    String normalizedNumber = phones.getString(indexOfNormalizedNumber);
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        String displayName = phones.getString(indexOfDisplayName);
                        String displayNumber = phones.getString(indexOfDisplayNumber);
                        Contacts selectUser = new Contacts();
                        selectUser.setName(displayName);
                        selectUser.setPhone(displayNumber);
                        selectUsers.add(selectUser);
                        //haven't seen this number yet: do something with this contact!
                    } else {
                        //don't do anything with this contact because we've already found this number
                    }


                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // sortContacts();
            int count = selectUsers.size();
            ArrayList<Contacts> removed = new ArrayList<>();
            ArrayList<Contacts> contacts = new ArrayList<>();
            for (int i = 0; i < selectUsers.size(); i++) {
                Contacts inviteFriendsProjo = selectUsers.get(i);

                if (inviteFriendsProjo.getName().matches("\\d+(?:\\.\\d+)?") || inviteFriendsProjo.getName().trim().length() == 0) {
                    removed.add(inviteFriendsProjo);
                    Log.d("Removed Contact", new Gson().toJson(inviteFriendsProjo));
                } else {
                    contacts.add(inviteFriendsProjo);
                }
            }
            contacts.addAll(removed);


            selectUsers = contacts;
            adapter = new RecyclerAdapter(inflater, selectUsers, ReferandEarn.this);
            recyclerView.setAdapter(adapter);

            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(selectUsers).getAsJsonArray();


        }
    }

    void filter(String text) {
        List<Contacts> temp = new ArrayList();
        for (Contacts filterText : selectUsers) {
            if (filterText.getName().toLowerCase().contains(text)) {
                temp.add(filterText);
            }
        }
        adapter.updateList((ArrayList<Contacts>) temp);
    }


}

