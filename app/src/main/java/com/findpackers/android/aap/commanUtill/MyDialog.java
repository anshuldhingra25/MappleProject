package com.findpackers.android.aap.commanUtill;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by navin on 4/7/2018.
 */

public class MyDialog extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("your title");
        alertDialog.setMessage("your message");

        alertDialog.show();
    }
}
