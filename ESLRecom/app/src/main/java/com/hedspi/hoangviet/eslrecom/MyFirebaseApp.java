package com.hedspi.hoangviet.eslrecom;

import com.google.firebase.database.FirebaseDatabase;
import com.hedspi.hoangviet.eslrecom.helpers.SettingHelper;

public class MyFirebaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persisaddListenerForSingleValueEventtence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference().keepSynced(true);

        SettingHelper.getInstance(this).updateLocale();
    }
}