package com.findpackers.android.aap.commanUtill;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.Arrays;

/**
 * Created by Shreya Kotak on 12/05/16.
 */
public class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static String Timeformateanother(String timestring) {

        String CurrentString = timestring;
        // System.out.println(" CurrentString== " + CurrentString);
        String[] separated = CurrentString.split(" ");

        String[] separated2 = separated[0].split("-");
        String[] separated3 = separated[1].split(":");
        //  System.out.println("formated separated[0]== " + separated2[0]);
        //System.out.println("formated separated[1]== " + separated2[1]);
        // System.out.println("formated separated[2]== " + separated2[2]);
        int selectedYear = Integer.parseInt(separated2[0]);
        int selectedDay = Integer.parseInt(separated2[2]);
        int selectedMonth = Integer.parseInt(separated2[1]);


        String array1[] = separated[1].split(":");

        int hour = Integer.parseInt(array1[0]);
        int minutes = Integer.parseInt(array1[1]);
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min = "";
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hour).append(':')
                .append(min).append(" ").append(timeSet).toString();

        //   String dateString = separated2[1]+"/"+separated2[2]+"/"+separated2[0]+ " "+separated3[0]+":"+separated3[1]+aTime;
        String dateString = separated2[2] + "/" + separated2[1] + "/" + separated2[0].substring(2) + " " + aTime;


        //  System.out.println("formated day time == " + dateString);
        return dateString;
    }


    public static String Timeformatewithouttime(String timestring) {

        String CurrentString = timestring;
        // System.out.println(" CurrentString== " + CurrentString);
        String[] separated2 = CurrentString.split("-");

        // System.out.println("formated separated[0]== " + separated2[0]);
        //  System.out.println("formated separated[1]== " + separated2[1]);
        //  System.out.println("formated separated[2]== " + separated2[2]);
        int selectedYear = Integer.parseInt(separated2[0]);
        int selectedDay = Integer.parseInt(separated2[2]);
        int selectedMonth = Integer.parseInt(separated2[1]);

        String dateString = separated2[2] + "/" + separated2[1] + "/" + separated2[0].substring(2);
        //  System.out.println("formated day time == " + dateString);
        return dateString;
    }

    public static String chkstringnull(String shkstring) {
        // System.out.println("shkstring :"+shkstring);
        if (shkstring.equalsIgnoreCase("null") || shkstring.equalsIgnoreCase("")) {
            return "N/A";
        } else {
            return shkstring;
        }

    }

    public static String chkstringnull2(String shkstring) {
        // System.out.println("shkstring :"+shkstring);
        if (shkstring.equalsIgnoreCase("null") || shkstring.equalsIgnoreCase(" ")) {
            return " ";
        } else {
            return shkstring;
        }

    }


    public static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToString((Bundle) value));
                } else {
                    out.append(value);
                }

                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }


}
