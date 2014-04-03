package com.povodev.hemme.android.management;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.povodev.hemme.android.bean.User;

/**
 * Created by Stefano on 03/04/14.
 */
public class SessionManagement {

    /*
     * SharedPreferences for USER bean
     */
    private static final String SESSION_USER_ID     = "id";
    private static final String SESSION_NAME        = "name";
    private static final String SESSION_SURNAME     = "surname";
    private static final String SESSION_EMAIL       = "email";
    private static final String SESSION_PASSWORD    = "password";
    private static final String SESSION_IMEI        = "imei";
    private static final String SESSION_ROLE        = "role";

    // Indicates if a user is logged in or not
    private static final String IS_LOGGED_IN        = "logged_in";


    /*
     * Creates a new kind-of user session.
     * Stores user infos in SharedPreferences
     */
    public static void createLoginSession(Context context, User user){
        SharedPreferences preferences = getPreferences(context);
        editSessionSharedPreferences(preferences,user);
    }

    /*
     * Retrieve SharedPreferences
     */
    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /*
     * Puts user infos in SharedPreferences
     */
    private static void editSessionSharedPreferences(SharedPreferences preferences, User user) {
        preferences.edit()
                .putInt(SESSION_USER_ID,user.getId())
                .putString(SESSION_NAME,user.getName())
                .putString(SESSION_SURNAME,user.getSurname())
                .putString(SESSION_EMAIL,user.getEmail())
                .putString(SESSION_PASSWORD,user.getPassword())
                .putString(SESSION_IMEI,user.getImei())
                .putInt(SESSION_ROLE,user.getRole())
                .putBoolean(IS_LOGGED_IN,true).commit();
    }

    /*
     * Close the session
     */
    public static void closeSession(Context context){
        SharedPreferences preferences = getPreferences(context);
        removeSessionSharedPreferences(preferences);
    }

    /*
     * Remove user infos in SharedPreferences
     */
    private static void removeSessionSharedPreferences(SharedPreferences preferences) {
        preferences.edit()
                .remove(SESSION_USER_ID)
                .remove(SESSION_NAME)
                .remove(SESSION_SURNAME)
                .remove(SESSION_EMAIL)
                .remove(SESSION_PASSWORD)
                .remove(SESSION_IMEI)
                .remove(SESSION_ROLE)
                .putBoolean(IS_LOGGED_IN,false).commit();
    }

    /*
     * Get the user stored in the current session
     */
    public static User getUserInSession(Context context){
        SharedPreferences preferences = getPreferences(context);
        return getUserInSharedPreferences(preferences);
    }

    /*
     * Map User bean with the user stored in SharedPreferences
     */
    private static User getUserInSharedPreferences(SharedPreferences preferences) {
        User user = new User();
        user.setId(preferences.getInt(SESSION_USER_ID,0));
        user.setName(preferences.getString(SESSION_NAME,null));
        user.setSurname(preferences.getString(SESSION_SURNAME,null));
        user.setEmail(preferences.getString(SESSION_EMAIL,null));
        user.setPassword(preferences.getString(SESSION_PASSWORD,null));
        user.setImei(preferences.getString(SESSION_IMEI,null));
        user.setRole(preferences.getInt(SESSION_ROLE,0));
        return user;
    }
}
