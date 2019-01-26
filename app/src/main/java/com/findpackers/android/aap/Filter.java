package com.findpackers.android.aap;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findpackers.android.aap.commanUtill.MyApplication;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;

public class Filter extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbarTitle, Reset;
    ImageView IvBack;
    ProgressDialog prgDialog;

    MultiSpinnerSearch searchMultiSpinnerUnlimited;
    Button btn_cancelfilter, btn_applyfilter;
    CheckBox mcheck_hot, mcheck_active, mcheck_sold, mcheck_myleads, mcheck_allleads, mcheck_withincity, mcheck_outsidecity, mcheck_international2;
    CheckBox mcheck_3bhk, mcheck_1bhk, mcheck_2bhk, mcheck_lesthen1bhk, mcheck_above3bhk;
    LinearLayout daynamicallyaddtext;
    com.bskim.maxheightscrollview.widgets.MaxHeightScrollView sc_flow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filter);


        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_titlefilter);
        Reset = (TextView) findViewById(R.id.txt_reset);
        btn_cancelfilter = (Button) findViewById(R.id.btn_cancelfilter);
        btn_applyfilter = (Button) findViewById(R.id.btn_applyfilter);
        IvBack = (ImageView) findViewById(R.id.back_icon);
        daynamicallyaddtext = (LinearLayout) findViewById(R.id.daynamicallyaddtext);

        mcheck_hot = (CheckBox) findViewById(R.id.check_hot);
        mcheck_active = (CheckBox) findViewById(R.id.check_active);
        mcheck_sold = (CheckBox) findViewById(R.id.check_sold);
        mcheck_myleads = (CheckBox) findViewById(R.id.check_myleads);
        mcheck_allleads = (CheckBox) findViewById(R.id.check_allleads);

        mcheck_withincity = (CheckBox) findViewById(R.id.check_withincity);
        mcheck_outsidecity = (CheckBox) findViewById(R.id.check_outsidecity);
        mcheck_international2 = (CheckBox) findViewById(R.id.check_international2);

        mcheck_1bhk = (CheckBox) findViewById(R.id.check_1bhk);
        mcheck_2bhk = (CheckBox) findViewById(R.id.check_2bhk);
        mcheck_3bhk = (CheckBox) findViewById(R.id.check_3bhk);
        mcheck_lesthen1bhk = (CheckBox) findViewById(R.id.check_lesthen1bhk);
        mcheck_above3bhk = (CheckBox) findViewById(R.id.check_above3bhk);

        setSupportActionBar(toolbar);
        toolbarTitle.setText("FILTER");

        prgDialog = new ProgressDialog(Filter.this);
        prgDialog.setMessage("Please wait..");
        prgDialog.setCancelable(false);

        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        final int N = 2; // total number of textviews to add

        final TextView[] myTextViews = new TextView[N]; // create an empty array;

        for (int i = 0; i < N; i++) {
            // create a new textview
            final TextView rowTextView = new TextView(this);

            // set some properties of rowTextView or something
            rowTextView.setText("This is row #" + i);
            rowTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.bid_icon, 0);

            rowTextView.setId(i);
            rowTextView.setOnClickListener(mThisButtonListener);


            // add the textview to the linearlayout
            daynamicallyaddtext.addView(rowTextView);


            // save a reference to the textview for later
            myTextViews[i] = rowTextView;
        }

        searchMultiSpinnerUnlimited = (MultiSpinnerSearch) findViewById(R.id.searchMultiSpinnerUnlimited);

       /* searchMultiSpinnerUnlimited.setItems(MyApplication.listArray0, -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                MyApplication.mcities = "";
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        mcheck_allleads.setChecked(false);
                        //MyApplication.mcities = MyApplication.mcities + "," + MyApplication.map.get(items.get(i).getName());
                        System.out.println("mcities :" + MyApplication.mcities);

                        Log.i("navin ", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                    } else {

                    }
                }
                if (MyApplication.mcities.equalsIgnoreCase("")) {
                    System.out.println("not selected :" + MyApplication.mcities);
                }
            }

        });*/

        if (MyApplication.bydefaultfeilter) {
            mcheck_allleads.setChecked(true);
        }

        if (MyApplication.strcheck_hot.equalsIgnoreCase("y")) {
            mcheck_hot.setChecked(true);
        }
        if (MyApplication.strcheck_active.equals("y")) {
            mcheck_active.setChecked(true);
        }
        if (MyApplication.strcheck_sold.equals("y")) {
            mcheck_sold.setChecked(true);
        }
        if (MyApplication.strcheck_myleads.equals("y")) {
            mcheck_myleads.setChecked(true);
        }
        if (MyApplication.strcheck_allleads.equals("y")) {
            mcheck_allleads.setChecked(true);
        }


        if (MyApplication.strcheck_withincity.equals("y")) {
            mcheck_withincity.setChecked(true);
        }
        if (MyApplication.strcheck_outsidecity.equals("y")) {
            mcheck_outsidecity.setChecked(true);
        }
        if (MyApplication.strcheck_international2.equals("y")) {
            mcheck_international2.setChecked(true);
        }
        if (MyApplication.strcheck_1bhk.equals("y")) {
            mcheck_1bhk.setChecked(true);
        }
        if (MyApplication.strcheck_2bhk.equals("y")) {
            mcheck_2bhk.setChecked(true);
        }
        if (MyApplication.strcheck_3bhk.equals("y")) {
            mcheck_3bhk.setChecked(true);
        }
        if (MyApplication.strcheck_lesthen1bhk.equals("y")) {
            mcheck_lesthen1bhk.setChecked(true);
        }
        if (MyApplication.strcheck_above3bhk.equals("y")) {
            mcheck_above3bhk.setChecked(true);
        }


        mcheck_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mcheck_hot.isChecked()) {

                    mcheck_allleads.setChecked(false);

                } else {
                }
            }
        });
        mcheck_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mcheck_active.isChecked()) {

                    mcheck_allleads.setChecked(false);


                } else {
                }

            }

        });
        mcheck_sold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mcheck_sold.isChecked()) {

                    mcheck_allleads.setChecked(false);

                } else {
                }

            }
        });
        mcheck_myleads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mcheck_myleads.isChecked()) {

                    mcheck_allleads.setChecked(false);

                } else {
                }
            }
        });
        mcheck_allleads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mcheck_allleads.isChecked()) {

                    //mcheck_allleads.setChecked(false);
                    mcheck_hot.setChecked(false);
                    mcheck_active.setChecked(false);
                    mcheck_sold.setChecked(false);
                    mcheck_myleads.setChecked(false);
                    mcheck_withincity.setChecked(false);
                    mcheck_outsidecity.setChecked(false);
                    mcheck_international2.setChecked(false);
                    mcheck_3bhk.setChecked(false);
                    mcheck_1bhk.setChecked(false);
                    mcheck_2bhk.setChecked(false);
                    mcheck_lesthen1bhk.setChecked(false);
                    mcheck_above3bhk.setChecked(false);

                } else {
                }
            }
        });
        mcheck_withincity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mcheck_withincity.isChecked()) {

                    mcheck_allleads.setChecked(false);

                } else {
                }

            }
        });
        mcheck_outsidecity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mcheck_outsidecity.isChecked()) {

                    mcheck_allleads.setChecked(false);


                } else {

                }

            }
        });
        mcheck_international2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mcheck_international2.isChecked()) {

                    mcheck_allleads.setChecked(false);


                } else {

                }
            }
        });

        mcheck_1bhk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mcheck_1bhk.isChecked()) {

                    mcheck_allleads.setChecked(false);

                } else {

                }

            }
        });
        mcheck_2bhk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mcheck_2bhk.isChecked()) {

                    mcheck_allleads.setChecked(false);

                } else {

                }
            }
        });
        mcheck_3bhk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mcheck_3bhk.isChecked()) {

                    mcheck_allleads.setChecked(false);

                } else {

                }
            }
        });
        mcheck_lesthen1bhk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mcheck_lesthen1bhk.isChecked()) {

                    mcheck_allleads.setChecked(false);

                } else {

                }
            }
        });
        mcheck_above3bhk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mcheck_above3bhk.isChecked()) {
                    mcheck_allleads.setChecked(false);

                } else {

                }
            }
        });


        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyApplication.bydefaultfeilter = true;
                mcheck_allleads.setChecked(true);
                MyApplication.list.clear();
                MyApplication.listArray0.clear();

                MyApplication.mcities = "";


                MyApplication.filtercount = 1;
                mcheck_hot.setChecked(false);
                mcheck_active.setChecked(false);
                mcheck_sold.setChecked(false);
                mcheck_myleads.setChecked(false);
                mcheck_allleads.setChecked(false);
                mcheck_withincity.setChecked(false);
                mcheck_outsidecity.setChecked(false);
                mcheck_international2.setChecked(false);
                mcheck_3bhk.setChecked(false);
                mcheck_1bhk.setChecked(false);
                mcheck_2bhk.setChecked(false);
                mcheck_lesthen1bhk.setChecked(false);
                mcheck_above3bhk.setChecked(false);
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
                finish();
            }
        });
        btn_cancelfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        btn_applyfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.filterlead = true;

                int filtercount = 0;

                System.out.println("mcities :" + MyApplication.mcities);

                if (!MyApplication.mcities.equalsIgnoreCase("")) {
                    filtercount = filtercount + 1;

                }

                if (mcheck_allleads.isChecked()) {
                    MyApplication.bydefaultfeilter = true;
                    filtercount = 1;
                } else {
                    MyApplication.bydefaultfeilter = false;
                }

                if (mcheck_hot.isChecked()) {

                    MyApplication.strcheck_hot = "y";
                } else {
                    MyApplication.strcheck_hot = "n";
                }

                if (mcheck_active.isChecked()) {
                    MyApplication.strcheck_active = "y";
                } else {
                    MyApplication.strcheck_active = "n";
                }
                if (mcheck_sold.isChecked()) {
                    MyApplication.strcheck_sold = "y";
                } else {
                    MyApplication.strcheck_sold = "n";
                }
                if (mcheck_myleads.isChecked()) {

                    MyApplication.strcheck_myleads = "y";
                } else {
                    MyApplication.strcheck_myleads = "n";
                }
                if (mcheck_withincity.isChecked()) {
                    MyApplication.strcheck_withincity = "y";
                } else {
                    MyApplication.strcheck_withincity = " ";
                }
                if (mcheck_outsidecity.isChecked()) {

                    MyApplication.strcheck_outsidecity = "y";
                } else {
                    MyApplication.strcheck_outsidecity = " ";
                }
                if (mcheck_international2.isChecked()) {

                    MyApplication.strcheck_international2 = "y";
                } else {
                    MyApplication.strcheck_international2 = " ";
                }
                if (mcheck_1bhk.isChecked()) {

                    MyApplication.strcheck_1bhk = "y";
                } else {
                    MyApplication.strcheck_1bhk = "n";
                }
                if (mcheck_2bhk.isChecked()) {

                    MyApplication.strcheck_2bhk = "y";
                } else {
                    MyApplication.strcheck_2bhk = "n";
                }
                if (mcheck_3bhk.isChecked()) {

                    MyApplication.strcheck_3bhk = "y";
                } else {
                    MyApplication.strcheck_3bhk = "n";
                }

                if (mcheck_lesthen1bhk.isChecked()) {

                    MyApplication.strcheck_lesthen1bhk = "y";
                } else {
                    MyApplication.strcheck_lesthen1bhk = "n";
                }
                if (mcheck_above3bhk.isChecked()) {

                    MyApplication.strcheck_above3bhk = "y";
                } else {
                    MyApplication.strcheck_above3bhk = "n";
                }


                if (MyApplication.strcheck_hot.equals("y")) {
                    MyApplication.strcheck_hot = "y";
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;

                }
                if (MyApplication.strcheck_active.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;

                }
                if (MyApplication.strcheck_sold.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_myleads.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_allleads.equals("y")) {
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_allleads.equals("y")) {
                    filtercount = filtercount + 1;
                }


                if (MyApplication.strcheck_withincity.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_outsidecity.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_international2.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_1bhk.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_2bhk.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_3bhk.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_lesthen1bhk.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }
                if (MyApplication.strcheck_above3bhk.equals("y")) {
                    MyApplication.bydefaultfeilter = false;
                    filtercount = filtercount + 1;
                }


                if (filtercount == 0) {
                    MyApplication.bydefaultfeilter = true;
                    filtercount = 1;
                }
                MyApplication.filtercount = filtercount;
                finish();
            }
        });
    }

    private View.OnClickListener mThisButtonListener = new View.OnClickListener() {
        public void onClick(View view) {


            findViewById(view.getId()).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    final int DRAWABLE_LEFT = 0;

                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (motionEvent.getAction() == MotionEvent.ACTION_BUTTON_RELEASE) {
                        {

                            daynamicallyaddtext.removeView(findViewById(view.getId()));

                            System.out.println("clicked item" + view.getId());

                        }
                    }
                    return false;
                }
            });


        }
    };


}
