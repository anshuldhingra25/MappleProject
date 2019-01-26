package com.findpackers.android.aap.commanUtill;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by narendra on 1/29/2018.
 */

public class MyPreferences {

    private static MyPreferences preferences = null;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;

    private String userId1 = "userId1";
    private String userId = "userId";
    private String nextpageUrl = "nextpageUrl";
    private String current_page = "current_page";
    private String firstName = "firstName";
    private String lastName = "lastName";
    private String username = "username";
    private String emailid = "email";
    private String address = "address";
    private String password = "password";
    private String status = "status";
    private String education = "education";
    private String university = "university";
    private String company = "company";
    private String designation = "designation";
    private String image = "image";
    private String mobile = "mobile";
    private String submitdate = "submitdate";
    private String updatedate = "updatedate";
    private String fbid = "fbid";
    private String gpid = "gpid";
    private String isLogedIn = "isLogedIn";
    private String howTowork = "howtowork";
    private String creditBalance = "creditBalance";
    private String leadCityValue = "leadcityvalue";
    private String updateValue = "updatevalue";


    public MyPreferences(Context context) {
        setmPreferences(PreferenceManager.getDefaultSharedPreferences(context));
    }


    public void setmPreferences(SharedPreferences mPreferences) {
        this.mPreferences = mPreferences;
    }


    public static MyPreferences getActiveInstance(Context context) {
        if (preferences == null) {
            preferences = new MyPreferences(context);
        }
        return preferences;
    }


    /*public String getUserId() {
        return mPreferences.getString(this.userId,"");
    }

    public void setUserId(String userId) {
        editor = mPreferences.edit();
        editor.putString(this.userId, userId);
        editor.commit();
    }*/

    public String getUserId() {
        return mPreferences.getString(this.userId, "");
    }

    public void setUserId(String userId) {
        editor = mPreferences.edit();
        editor.putString(this.userId, userId);
        editor.commit();
    }

    public String getnextpageUrl() {
        return mPreferences.getString(this.nextpageUrl, "");
    }

    public void setnextpageUrl(String nextpageurl) {
        editor = mPreferences.edit();
        editor.putString(this.nextpageUrl, nextpageurl);
        editor.commit();
    }

    public String getcurrent_page() {
        return mPreferences.getString(this.nextpageUrl, "");
    }

    public void setcurrent_page(String current_page) {
        editor = mPreferences.edit();
        editor.putString(this.current_page, current_page);
        editor.commit();
    }

    public String getemailId() {
        return mPreferences.getString(this.emailid, "");
    }

    public void setemailId(String emailid) {
        editor = mPreferences.edit();
        editor.putString(this.emailid, emailid);
        editor.commit();
    }


    public void setLeadCityValue(int value) {
        editor = mPreferences.edit();
        editor.putInt(this.leadCityValue, value);
        editor.commit();
    }

    public int getLeadCityValue() {
        return mPreferences.getInt(this.leadCityValue, 0);
    }


    public void setUpdateValue(int value) {
        editor = mPreferences.edit();
        editor.putInt(this.updateValue, value);
        editor.commit();
    }

    public int getUpdateValue() {
        return mPreferences.getInt(this.updateValue, 0);
    }


    public String getUsername() {
        return mPreferences.getString(this.username, "");
    }

    public void setUsername(String username) {
        editor = mPreferences.edit();
        editor.putString(this.username, username);
        editor.commit();
    }

    public String getAddress() {
        return mPreferences.getString(this.address, "");
    }

    public void setAddress(String address) {
        editor = mPreferences.edit();
        editor.putString(this.address, address);
        editor.commit();
    }

    public String getLastName() {
        return mPreferences.getString(this.lastName, "");
    }

    public void setLastName(String lastName) {
        editor = mPreferences.edit();
        editor.putString(this.lastName, lastName);
        editor.commit();
    }

    public String getFirstName() {
        return mPreferences.getString(this.firstName, "");
    }

    public void setFirstName(String firstName) {
        editor = mPreferences.edit();
        editor.putString(this.firstName, firstName);
        editor.commit();
    }


    public String getPassword() {
        return mPreferences.getString(this.password, "");
    }

    public void setPassword(String password) {
        editor = mPreferences.edit();
        editor.putString(this.password, password);
        editor.commit();
    }

    public String getStatus() {
        return mPreferences.getString(this.status, "");
    }

    public void setStatus(String status) {
        editor = mPreferences.edit();
        editor.putString(this.status, status);
        editor.commit();
    }

    public String getEducation() {
        return mPreferences.getString(this.education, "");
    }

    public void setEducation(String education) {
        editor = mPreferences.edit();
        editor.putString(this.education, education);
        editor.commit();
    }

    public String getUniversity() {
        return mPreferences.getString(this.university, "");
    }

    public void setUniversity(String university) {
        editor = mPreferences.edit();
        editor.putString(this.university, university);
        editor.commit();
    }

    public String getCompany() {
        return mPreferences.getString(this.company, "");
    }

    public void setCompany(String company) {
        editor = mPreferences.edit();
        editor.putString(this.company, company);
        editor.commit();
    }

    public String getDesignation() {
        return mPreferences.getString(this.designation, "");
    }

    public void setDesignation(String designation) {
        editor = mPreferences.edit();
        editor.putString(this.designation, designation);
        editor.commit();
    }

    public String getImage() {
        return mPreferences.getString(this.image, "");
    }

    public void setImage(String image) {
        editor = mPreferences.edit();
        editor.putString(this.image, image);
        editor.commit();
    }

    public String getMobile() {
        return mPreferences.getString(this.mobile, "");
    }

    public void setMobile(String mobile) {
        editor = mPreferences.edit();
        editor.putString(this.mobile, mobile);
        editor.commit();
    }

    public String getSubmitdate() {
        return mPreferences.getString(this.submitdate, "");
    }

    public void setSubmitdate(String submitdate) {
        editor = mPreferences.edit();
        editor.putString(this.submitdate, submitdate);
        editor.commit();
    }

    public String getUpdatedate() {
        return mPreferences.getString(this.updatedate, "");
    }

    public void setUpdatedate(String updatedate) {
        editor = mPreferences.edit();
        editor.putString(this.updatedate, updatedate);
        editor.commit();
    }

    public String getFbid() {
        return mPreferences.getString(this.fbid, "");
    }

    public void setFbid(String fbid) {
        editor = mPreferences.edit();
        editor.putString(this.fbid, fbid);
        editor.commit();
    }

    public String getGpid() {
        return mPreferences.getString(this.gpid, "");
    }

    public void setGpid(String gpid) {
        editor = mPreferences.edit();
        editor.putString(this.gpid, gpid);
        editor.commit();
    }

    public boolean getIsLoggedIn() {
        return mPreferences.getBoolean(this.isLogedIn, false);
    }

    public void setIsLoggedIn(boolean isLoggedin) {
        editor = mPreferences.edit();
        editor.putBoolean(this.isLogedIn, isLoggedin);
        editor.commit();
    }

    public boolean gethowTowork() {
        return mPreferences.getBoolean(this.howTowork, false);
    }

    public void sethowTowork(boolean isLoggedin) {
        editor = mPreferences.edit();
        editor.putBoolean(this.howTowork, isLoggedin);
        editor.commit();
    }


    public String getCreditbalance() {
        return mPreferences.getString(this.creditBalance, "N/A");
    }

    public void setCreditbalance(String creditbalance) {
        editor = mPreferences.edit();
        editor.putString(this.creditBalance, creditbalance);
        editor.commit();
    }
}
