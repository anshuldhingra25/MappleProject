package com.findpackers.android.aap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by narendra on 2/19/2018.
 */

public class ThankYouActivity extends AppCompatActivity {
     Button btn_ok;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        btn_ok = (Button) findViewById(R.id.btn_cradit_rechage);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(ThankYouActivity.this, AvlableBlance.class);
                startActivity(in);
                finish();
            }
        });
    }
}



