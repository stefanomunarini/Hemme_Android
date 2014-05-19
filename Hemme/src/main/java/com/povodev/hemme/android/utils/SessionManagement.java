package com.povodev.hemme.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.povodev.hemme.android.bean.User;

/**
 * This class helps to manage user infos in session.
 * Created by Stefano on 03/04/14.
 */
public class SessionManagement {

    /*
     * SharedPreferences for USER bean
     */
    private static final String SESSION_USER_ID         = "id";
    private static final String SESSION_NAME            = "name";
    private static final String SESSION_SURNAME         = "surname";
    private static final String SESSION_EMAIL           = "email";
    private static final String SESSION_PASSWORD        = "password";
    private static final String SESSION_IMEI            = "imei";
    private static final String SESSION_ROLE            = "role";

    /*
     * The patient id currently selected in the Home_Activity Spinner
     */
    private static final String CURRENT_PATIENT_ID      = "current_patient_id";

    /*
     * Indicates if a user is logged in or not
     */
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
     * Close the session
     */
    public static void closeSession(Context context){
        SharedPreferences preferences = getPreferences(context);
        removeSessionSharedPreferences(preferences);
    }

    /*
     * Get the user stored in the current session
     */
    public static User getUserInSession(Context context){
        SharedPreferences preferences = getPreferences(context);
        return getUserInSharedPreferences(preferences);
    }

    /*
     * Return true if user is logged, false otherwise
     */
    public static boolean isUserLoggedIn(Context context){
        SharedPreferences preferences = getPreferences(context);
        return preferences.getBoolean(IS_LOGGED_IN,false);
    }

    /*
     * Retrieve SharedPreferences
     */
    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static final String TAG = "SessionManagement";

    /*
     * Puts user infos in SharedPreferences
     */
    private static void editSessionSharedPreferences(SharedPreferences preferences, User user) {
        Log.d(TAG,SESSION_USER_ID + ":" + user.getId());
        Log.d(TAG,SESSION_NAME + ":" + user.getName());
        Log.d(TAG,SESSION_SURNAME + ":" + user.getSurname());
        Log.d(TAG,SESSION_EMAIL + ":" + user.getEmail());
        Log.d(TAG,SESSION_PASSWORD + ":" + user.getPassword());
        Log.d(TAG,SESSION_IMEI + ":" + user.getImei());
        Log.d(TAG,SESSION_ROLE + ":" + user.getRole());
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
                .putBoolean(IS_LOGGED_IN,false)
                .putInt(CURRENT_PATIENT_ID,-1).commit();
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
        user.setRole(preferences.getInt(SESSION_ROLE,-1));
        return user;
    }

    /*
     * Save the currently selected (in Fragment_Home spinner) patient id
     * @param: context
     * @param: patient_id
     */
    public static void editPatientIdInSharedPreferences(Context context, int patient_id){
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putInt(CURRENT_PATIENT_ID, patient_id).commit();
    }

    /*
     * Get the currently selected (in Fragment_Home spinner) patient id
     * @param: context
     */
    public static int getPatientIdInSharedPreferences(Context context){
        SharedPreferences preferences = getPreferences(context);
        return preferences.getInt(CURRENT_PATIENT_ID, -1);
    }
}
