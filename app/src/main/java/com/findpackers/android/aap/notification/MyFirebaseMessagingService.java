package com.findpackers.android.aap.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.findpackers.android.aap.LoginActivity;
import com.findpackers.android.aap.MainActivity;
import com.findpackers.android.aap.R;
import com.findpackers.android.aap.commanUtill.MyApplication;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import static com.findpackers.android.aap.notification.Config.PUSH_NOTIFICATION;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    final int NOTIFY_ID = 1; // any integer number
    int count = 0;
    String userId;
    String idChannel = "com.findpackers.android.aap.ANDROID";
    NotificationChannel mChannel = null;
    int importance = NotificationManager.IMPORTANCE_HIGH;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        System.out.println("calling on onMessageReceived  ");


        Log.d("navin", "Notification Message Body: " + remoteMessage.getNotification().getBody());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            Log.e(TAG, "Notification title: " + remoteMessage.getNotification().getTitle());

            handleNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }





   /* private void handleNotification(String message) {

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }*/

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("imageUrl");
            String timestamp = data.getString("timestamp");
            String notificationType = data.getString("type");
            // JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "message: " + message);
            Log.e(TAG, "title: " + title);
            Log.e(TAG, "isBackground: " + isBackground);
            // Logger.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            sendNotification(message, notificationType);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private void sendNotification(String message, String Type) {
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle("Findpackers")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setNumber(count)

                .setContentIntent(pendingIntent);

//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notificationBuilder.build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription(getString(R.string.app_name));
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            notificationBuilder.setContentTitle(getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(Color.TRANSPARENT)
                    .setVibrate(new long[]{100, 250})
                    .setLights(Color.YELLOW, 500, 5000)
                    .setAutoCancel(true);
        }
        mNotificationManager.notify(1, notificationBuilder.build());
        //find the home launcher Package


        count++;


        BadgeUtils.setBadge(MyFirebaseMessagingService.this, (int) count);
        //  ShortcutBadger.applyCount(this, count);


    }

    private void handleNotification(String message, String title) {


        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            Intent intent;

            if (message.equalsIgnoreCase("Your account has been deactivated, by admin so please contact to admin for any query.")) {


                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("sessionOut");
                MyFirebaseMessagingService.this.sendBroadcast(broadcastIntent);

            } else if (title.equalsIgnoreCase("Credit Wallet")) {

                MyApplication.showthankyoumsg = message;


            } else if (message.startsWith("Your account has been deleted")) {

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("accountdelet");
                MyFirebaseMessagingService.this.sendBroadcast(broadcastIntent);


            } else {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("updateLeadsList");
                MyFirebaseMessagingService.this.sendBroadcast(broadcastIntent);
            }


            if (message.startsWith("Your account has been deleted")) {
                MyPreferences.getActiveInstance(MyFirebaseMessagingService.this).setUserId(" ");
                MyPreferences.getActiveInstance(MyFirebaseMessagingService.this).setIsLoggedIn(false);

                MyApplication.filterlead = false;
                MyApplication.filtercount = 0;
                MyApplication.sortlead = false;

                MyApplication.strcheck_3bhk = "";
                MyApplication.strcheck_1bhk = "";
                MyApplication.strcheck_2bhk = "";
                MyApplication.strcheck_lesthen1bhk = "";
                MyApplication.strcheck_above3bhk = "";
                MyApplication.strcheck_hot = "";
                MyApplication.strcheck_active = "";
                MyApplication.strcheck_sold = "";
                MyApplication.strcheck_myleads = "";
                MyApplication.strcheck_allleads = "";
                MyApplication.strcheck_withincity = "";
                MyApplication.strcheck_outsidecity = "";
                MyApplication.strcheck_international2 = "";
                MyApplication.mcities = "";

                MyApplication.mstr_low_high = "";
                MyApplication.mstr_high_low = "";
                MyApplication.mstr_latest = "";
                MyApplication.mstr_oldest = "";
                MyApplication.mstr_nearshiftingdate = "";
                intent = new Intent(this, LoginActivity.class);

            } else if (message.equalsIgnoreCase("Your account has been deactivated, by admin so please contact to admin for any query.")) {
                //MyPreferences.getActiveInstance(MyFirebaseMessagingService.this).setUserId(" ");
                //MyPreferences.getActiveInstance(MyFirebaseMessagingService.this).setIsLoggedIn(false);

              /*  MyApplication.filterlead=false;
                MyApplication.filtercount=0;
                MyApplication.sortlead=false;

                MyApplication.strcheck_3bhk="";
                MyApplication. strcheck_1bhk="";
                MyApplication.strcheck_2bhk="";
                MyApplication.strcheck_lesthen1bhk="";
                MyApplication.strcheck_above3bhk="";
                MyApplication.strcheck_hot="";
                MyApplication.strcheck_active="";
                MyApplication.strcheck_sold="";
                MyApplication.strcheck_myleads="";
                MyApplication.strcheck_allleads="";
                MyApplication.strcheck_withincity="";
                MyApplication.strcheck_outsidecity="";
                MyApplication.strcheck_international2="";
                MyApplication.mcities="";

                MyApplication.mstr_low_high="";
                MyApplication.mstr_high_low="";
                MyApplication.mstr_latest="";
                MyApplication.mstr_oldest="";
                MyApplication.mstr_nearshiftingdate="";*/
                intent = new Intent(this, MainActivity.class);


            } else {
                intent = new Intent(this, MainActivity.class);
            }


            intent.putExtra("message", message);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle("Findpackers")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(notificationSoundURI)
                    .setContentIntent(resultIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setNumber(count);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mChannel = new NotificationChannel(idChannel, getString(R.string.app_name), importance);
                // Configure the notification channel.
                mChannel.setDescription(getString(R.string.app_name));
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);
            } else {
                mNotificationBuilder.setContentTitle(getString(R.string.app_name))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setColor(Color.TRANSPARENT)
                        .setVibrate(new long[]{100, 250})
                        .setLights(Color.YELLOW, 500, 5000)
                        .setAutoCancel(true);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify(1, mNotificationBuilder.build());
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(0, mNotificationBuilder.build());


            count++;
            //ShortcutBadger.applyCount(this, count);


            BadgeUtils.setBadge(MyFirebaseMessagingService.this, (int) count);

        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }


}
