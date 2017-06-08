package com.hedspi.hoangviet.eslrecom.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Linh on 9/2/2016.
 */
public class SettingHelper {
    private static SettingHelper instance;
    private static final String PROFILE = "profile";
    private static final String SETTING = "setting";
    private static final String UID = "uid";
    private static final String LANGUAGE = "languageString";
    private static final String EN = "en";

    private SharedPreferences profilePreference;
    private SharedPreferences settingPreference;
    private String uid;
    private String language;
    private Context mContext;

    private SettingHelper(Context context) {
        profilePreference = context.getSharedPreferences(PROFILE, 0);
        settingPreference = context.getSharedPreferences(SETTING, 0);

        uid = profilePreference.getString(UID, null);
        language = settingPreference.getString(LANGUAGE,EN);
        mContext = context;
    }

    public static SettingHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SettingHelper(context);
        }
        return instance;
    }

    public String getUid() {
        return uid;
    }

    public void removeUid(){
        uid = null;
        profilePreference.edit().remove(UID).apply();
    }

    public void setUid(String uid) {
        profilePreference.edit().putString(UID, uid).apply();
        this.uid = uid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        settingPreference.edit().putString(LANGUAGE, language).apply();
        this.language = language;
    }

    public void updateLocale() {
        Resources res = mContext.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language);
        res.updateConfiguration(conf, dm);
    }
}
