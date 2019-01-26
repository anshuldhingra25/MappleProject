package com.findpackers.android.aap.referandearn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.tagmanager.InstallReferrerReceiver;

public class CustomInstallTrackersReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // Implementing Google Referrer tracker
            InstallReferrerReceiver googleReferrerTracking = new InstallReferrerReceiver();
            googleReferrerTracking.onReceive(context, intent);

            // Do something with referrer data to do your own tracker.
            Log.d("CustomInstallTrackers", "Referrer: "+intent.getStringExtra("referrer"));
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}