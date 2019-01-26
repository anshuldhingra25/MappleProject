package com.findpackers.android.aap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.findpackers.android.aap.commanUtill.MyApplication;

public class Sort extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbarTitle,Reset;
    ImageView IvBack;
    Button btn_cancelfilter,btn_applyfilter;
    RadioButton mrd_low_high,mrd_high_low,mrd_latest,mrd_oldest,mrd_nearshiftingdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_titlefilter);
        Reset = (TextView) findViewById(R.id.txt_reset);

        IvBack = (ImageView) findViewById(R.id.back_icon);
        btn_cancelfilter = (Button) findViewById(R.id.btn_cancelfilter);
        btn_applyfilter = (Button) findViewById(R.id.btn_applyfilter);

        mrd_low_high = (RadioButton) findViewById(R.id.rd_low_high);
        mrd_high_low = (RadioButton) findViewById(R.id.rd_high_low);
        mrd_latest = (RadioButton) findViewById(R.id.rd_latest);
        mrd_oldest = (RadioButton) findViewById(R.id.rd_oldest);
        mrd_nearshiftingdate = (RadioButton) findViewById(R.id.rd_nearshiftingdate);



       setSupportActionBar(toolbar);
        toolbarTitle.setText("SORT");

        if(MyApplication.bydefaultsort)
        {
            mrd_latest.setChecked(true);

        }




        if(MyApplication.mstr_low_high.equals("y"))
        {
            mrd_low_high.setChecked(true);

        }  if(MyApplication.mstr_high_low.equals("y"))
        {
            mrd_high_low.setChecked(true);

        }  if(MyApplication.mstr_latest.equals("y"))
        {
            mrd_latest.setChecked(true);

        }  if(MyApplication.mstr_oldest.equals("y"))
        {
            mrd_oldest.setChecked(true);

        }  if(MyApplication.mstr_nearshiftingdate.equals("y"))
        {
            mrd_nearshiftingdate.setChecked(true);

        }

        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_cancelfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               MyApplication.sortlead=false;
                MyApplication.bydefaultsort=true;
                MyApplication.sortlead=true;


                mrd_low_high.setChecked(false);
                 mrd_high_low.setChecked(false);
                 mrd_latest.setChecked(false);
                 mrd_oldest.setChecked(false);
                 mrd_nearshiftingdate.setChecked(false);
                MyApplication.mstr_low_high="";
                MyApplication.mstr_high_low="";
                MyApplication.mstr_latest="";
                MyApplication.mstr_oldest="";
                MyApplication.mstr_nearshiftingdate="";



                finish();
            }
        });

        btn_applyfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyApplication.sortlead=true;

                MyApplication.bydefaultsort=false;

                if(mrd_low_high.isChecked())
                {
                    MyApplication.mstr_low_high="y";
                } else{
                    MyApplication.mstr_low_high="n";
                }
                if(mrd_high_low.isChecked())
                {
                    MyApplication.mstr_high_low="y";
                } else{
                    MyApplication.mstr_high_low="n";
                }
                if(mrd_latest.isChecked())
                {
                    MyApplication.mstr_latest="y";
                }else{
                    MyApplication.mstr_latest="n";
                }
                if(mrd_oldest.isChecked())
                {
                    MyApplication.mstr_oldest="y";
                }else{
                    MyApplication.mstr_oldest="n";
                }
                if(mrd_nearshiftingdate.isChecked())
                {
                    MyApplication.mstr_nearshiftingdate="y";
                }else {
                    MyApplication.mstr_nearshiftingdate="n";
                }
                finish();
            }
        });
    }

}
