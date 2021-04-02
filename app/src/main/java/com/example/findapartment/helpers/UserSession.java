package com.example.findapartment.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserSession {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    private static final String KEY_USER_TOKEN = "KEY_USER_TOKEN";
    private static final String KEY_USER_ROLE = "KEY_USER_ROLE";

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";


    public UserSession(Context context) {
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = sharedPreferences.edit();
    }

    public void createSession(String userEmail, String accessToken, String role) {
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putString(KEY_USER_TOKEN, accessToken);
        editor.putString(KEY_USER_ROLE, role);

        editor.commit();

        String user_name_new=sharedPreferences.getString(KEY_USER_EMAIL, null);
        Log.d("TAG","successfully logged in:"+user_name_new);
    }

    public String getLoggedInUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public String getLoggedInUserToken() {
        return sharedPreferences.getString(KEY_USER_TOKEN, null);
    }

    public String getLoggedInUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, null);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getString(KEY_USER_TOKEN, null) != null;
    }

    public void deleteSession() {
        editor.clear();
        editor.commit();
    }

}
