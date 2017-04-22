package com.hedspi.hoangviet.eslrecom;

import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persisaddListenerForSingleValueEventtence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference().keepSynced(true);
    }
}