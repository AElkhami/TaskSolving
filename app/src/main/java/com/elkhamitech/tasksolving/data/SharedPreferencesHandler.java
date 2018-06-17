package com.elkhamitech.tasksolving.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.elkhamitech.tasksolving.presenter.MainContract;

public class SharedPreferencesHandler implements MainContract.PreferenceInteractor {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final String PREF_NAME = "MyPrefsFile";
    private static int PRIVATE_MODE = 0;
    private static String KEY_LAST_UPD = "lastUpd";
    private static String KEY_SELECTED_VIEW = "selectedView";


    public SharedPreferencesHandler(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    @Override
    public void setViewPreference(boolean viewSwitchedFlag) {
        editor.putBoolean(KEY_SELECTED_VIEW, viewSwitchedFlag);
        editor.apply();
    }

    @Override
    public void setLastUpdPreference(String lastUpdated) {
        editor.putString(KEY_LAST_UPD, lastUpdated);
        editor.apply();
    }

    @Override
    public Boolean getViewPreference() {
        return prefs.getBoolean(KEY_SELECTED_VIEW, true);
    }

    @Override
    public String getLastUpdPreference() {
        return prefs.getString(KEY_LAST_UPD, null);
    }

}
