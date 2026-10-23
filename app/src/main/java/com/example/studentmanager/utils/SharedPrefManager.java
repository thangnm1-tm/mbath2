package com.example.studentmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String PREF_NAME = "StudentManagerPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USER_CODE = "userCode";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_REMEMBER_ME = "rememberMe";

    private static SharedPrefManager instance;
    private final SharedPreferences sharedPreferences;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveLoginSession(int userId, String username, String role, String userCode, String fullname, boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_USER_CODE, userCode);
        editor.putString(KEY_FULLNAME, fullname);
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isRememberMe() {
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, "");
    }

    public String getUserCode() {
        return sharedPreferences.getString(KEY_USER_CODE, "");
    }

    public String getFullname() {
        return sharedPreferences.getString(KEY_FULLNAME, "");
    }

    public void logout() {
        boolean remember = isRememberMe();
        String savedUsername = getUsername();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        if (remember) {
            editor.putBoolean(KEY_REMEMBER_ME, true);
            editor.putString(KEY_USERNAME, savedUsername);
        }
        editor.apply();
    }
}
