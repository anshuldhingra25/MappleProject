package com.findpackers.android.aap.commanUtill;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.findpackers.android.aap.BuildConfig;
import com.findpackers.android.aap.R;
import com.findpackers.android.aap.SpleshActivity;
import com.loopj.android.http.RequestParams;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

public class ForceUpdate {

    private Context context;
    private double versionName= Double.valueOf(BuildConfig.VERSION_NAME);
    private double playStoreVersionName;

    public ForceUpdate(Context context){

        this.context = context;
    }


    public void build(){



        LoadUrl loadUrl = new LoadUrl();
        loadUrl.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadUrl extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {

            if (!isConnectivityOk()){
                this.cancel(true);
                return;
            }

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                Document doc = Jsoup.connect(getUrl())
                        .userAgent("Mozilla/4.0")
                        .referrer("https://www.google.com")
                        .timeout(30000)
                        .get();

                String fulldata = doc.select("span[class=htlgb]").text();
                String ver = doc.select("span[class=htlgb]").get(6).text();
                String ver5 = doc.select("span[class=htlgb]").get(5).text();
                String ver3 = doc.select("span[class=htlgb]").get(4).text();
                String ver4 = doc.select("span[class=htlgb]").get(3).text();
                Log.e("From Playstore ", ver);

                Log.e("From fulldata ", fulldata);
                Log.e("From ver3 ", ver3);
                Log.e("From ver4 ", ver4);
                Log.e("From ver5 ", ver5);
                playStoreVersionName = Double.valueOf(ver);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (playStoreVersionName>versionName){
                Log.e("Current Version", String.valueOf(versionName));
                Log.e("Update Version", String.valueOf(playStoreVersionName));
                showUpdateDialog();
            }else{
                Log.e("No Update Available", "Current Version: "+String.valueOf(playStoreVersionName));
            }
        }
    }

    private String getUrl(){
        return "https://play.google.com/store/apps/details?id="+context.getPackageName();
    }

    private void showUpdateDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please, update app to new version ")
                .setCancelable(false)
               /* .setNegativeButton("NO, THANKS ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();


                    }
                }) */.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                       Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(getUrl()));
                        context.startActivity(intent);



                       // uninstallApp();

                    }
                });


        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("New version available");
        alert.show();

    }

    public void uninstallApp(){


       Uri packageURI = Uri.parse("package:" + context.getPackageName()); //App package to //be deleted
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,packageURI);
        context.startActivity(uninstallIntent);
    }


    private boolean isConnectivityOk() {
        try {
            NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            return activeNetwork != null && (activeNetwork.getType() == 1 || activeNetwork.getType() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}
