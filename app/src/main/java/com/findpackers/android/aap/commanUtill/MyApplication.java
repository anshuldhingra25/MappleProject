package com.findpackers.android.aap.commanUtill;

import android.app.Application;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by navin on 3/14/2018.
 */

public class MyApplication extends Application {
    public static String strcheck_3bhk = "", strcheck_1bhk = "", strcheck_2bhk = "", strcheck_lesthen1bhk = "", strcheck_above3bhk = "";
    public static String strcheck_hot = "", strcheck_active = "", strcheck_sold = "", strcheck_myleads = "", strcheck_remainlead = "", strcheck_allleads = "", strcheck_withincity = "", strcheck_outsidecity = "", strcheck_international2 = "", strcheck_nearby = "", strcheck_selectcity = "";
    public static String strcheck_car = "", strcheck_bike = "", strspiner_leadtype = "";
    public static boolean filterlead;
    public static String selectedRegion = "";
    public static boolean bck_filter;
    public static boolean bydefaultfeilter = true;
    public static boolean bydefaultsort = true;
    public static boolean bydefaulselectedcity = false;
    public static boolean sortlead = true;
    public static String showthankyoumsg = " ";
    public static int filtercount;
    public static int totalLeads;
    public static boolean notificatonother = false;

    public static String mcities = "";
    public static String activefregment = "";
    public static String category_status = "";
    public static String spl_status = "";
    public static String usercities = "";

    public static ArrayList<String> list = new ArrayList<String>();
    public static ArrayList<String> list2 = new ArrayList<String>();

    public static Set mapselectdcity = new HashSet();

    public static final List<KeyPairBoolData> listArray0 = new ArrayList<>();

    public static String mstr_low_high = "", mstr_high_low = "", mstr_latest = "", mstr_oldest = "", mstr_nearshiftingdate = "";


}
