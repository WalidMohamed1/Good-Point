package com.helloworld.goodpoint.ui;


import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String IS_LOGINNED = "Token";

    private static final String LINK = "ngrok";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }

    public void setLogin(String token){
        editor.putString(IS_LOGINNED, token);
        editor.commit();
    }

    public void setLogout(){
        editor.putString(IS_LOGINNED, "");
        editor.commit();
    }

    public String isLoginned() {
        return pref.getString(IS_LOGINNED,"");
    }

    public void setNGROKLink(String link){
        editor.putString(LINK, link);
        editor.commit();
    }

    public String getNGROKLink() {
        return pref.getString(LINK,"http://127.0.0.1:8000/");
    }

}






