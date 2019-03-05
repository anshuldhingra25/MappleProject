package com.findpackers.android.aap.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findpackers.android.aap.AboutUs;
import com.findpackers.android.aap.BuildConfig;
import com.findpackers.android.aap.ChangePasswordActivity;
import com.findpackers.android.aap.ContactUs;
import com.findpackers.android.aap.Disclamer;
import com.findpackers.android.aap.HowItWorkActivity;
import com.findpackers.android.aap.MainActivity;
import com.findpackers.android.aap.PrivacyPloicy;
import com.findpackers.android.aap.R;
import com.findpackers.android.aap.Refund_cancelation_policy;
import com.findpackers.android.aap.TermsAndConditions;

/**
 * Created by narendra on 2/16/2018.
 */

public class SettingFragment extends Fragment {


    public static SettingFragment newInstance() {

        return new SettingFragment();
    }

    Button layout_change_password;
    LinearLayout btnChangePassword, btnHowItWork, btnTermsConditions, layout_privacy_policy;
    LinearLayout layou_return_refund, layou_diclamer, layou_about, layou_contactus, layout_notification;
    TextView tvVersionInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        tvVersionInfo = view.findViewById(R.id.tvVersionInfo);
        tvVersionInfo.setText("Version : " + BuildConfig.VERSION_NAME);
        btnChangePassword = (LinearLayout) view.findViewById(R.id.layout_change_password);
        btnHowItWork = (LinearLayout) view.findViewById(R.id.layout_howit_works);
        btnTermsConditions = (LinearLayout) view.findViewById(R.id.layout_terms_conditions);
        layout_privacy_policy = (LinearLayout) view.findViewById(R.id.layout_privacy_policy);
        layou_return_refund = (LinearLayout) view.findViewById(R.id.layou_return_refund);
        layou_diclamer = (LinearLayout) view.findViewById(R.id.layou_diclamer);
        layou_about = (LinearLayout) view.findViewById(R.id.layou_about);
        layou_contactus = (LinearLayout) view.findViewById(R.id.layou_contactus);

        layout_notification = (LinearLayout) view.findViewById(R.id.layout_notification);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(in);
            }
        });
        btnHowItWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), HowItWorkActivity.class);
                startActivity(in);
            }
        });

        btnTermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), TermsAndConditions.class);
                startActivity(in);
            }
        });
        layout_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), PrivacyPloicy.class);
                startActivity(in);
            }
        });
        layou_return_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), Refund_cancelation_policy.class);
                startActivity(in);
            }
        });
        layou_diclamer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), Disclamer.class);
                startActivity(in);
            }
        });
        layou_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AboutUs.class);
                startActivity(in);
            }
        });
        layou_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), ContactUs.class);
                startActivity(in);
            }
        });

        layout_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Intent intent = new Intent();
//                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
//
////for Android 5-7
//                intent.putExtra("app_package", getActivity().getPackageName());
//                intent.putExtra("app_uid", getActivity().getApplicationInfo().uid);
//
//// for Android 8 and above
//                intent.putExtra("android.provider.extra.APP_PACKAGE", getActivity().getPackageName());
//
//                startActivity(intent);

//                Intent intent = new Intent();
//                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
//                    intent.setAction("android.settings.ACTION_APPLICATION_DETAILS_SETTINGS");
//                    intent.putExtra("android.provider.extra.APP_PACKAGE", getActivity().getPackageName());
//                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, "com.findpackers.android.aap.ANDROID");
//                } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    intent.setAction("android.settings.ACTION_APPLICATION_DETAILS_SETTINGS");
//                    intent.putExtra("app_package", getActivity().getPackageName());
//                    intent.putExtra("app_uid", getActivity().getApplicationInfo().uid);
//                } else {
//                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    intent.addCategory(Intent.CATEGORY_DEFAULT);
//                    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
//                }
//
//                getActivity().startActivity(intent);
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("navin", "onKey Back listener is working!!!");
                    Intent in = new Intent(getActivity(), MainActivity.class);
                    startActivity(in);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });


        return view;
    }

}
