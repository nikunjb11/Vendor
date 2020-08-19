package com.softhub.vendor;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;
    public Context cxt;

    public Session(Context cxt){
        this.cxt = cxt;
        prefs = cxt.getSharedPreferences("myapp",Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedIn(Boolean loggedIn){

        editor.putBoolean("loggedInmode",loggedIn);
        editor.commit();
    }

    public void setPrefix(String prefix){

        editor.putString("prefix",prefix);
        editor.commit();
    }

    public void setUserName(String userName){

        editor.putString("username",userName);
        editor.commit();
    }

    public void setMobileNumber(String mobileNumber){

        editor.putString("mobileNumber",mobileNumber);
        editor.commit();
    }

    public void tiffinCount(String tiffinCount){

        editor.putString("tiffinCount",tiffinCount);
        editor.commit();
    }

    public boolean loggedIn(){
        return prefs.getBoolean("loggedInmode",false);
    }

}
