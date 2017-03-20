package com.hedspi.hoangviet.eslrecom;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hedspi.hoangviet.eslrecom.commons.Common;
import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.fragments.MainFragment;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static final int FRAG_MAIN = 1;
    public static final int ADDED = 1;
    public static final int TO_BE_ADDED = 2;
    public static final int VIEW = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().add(R.id.fragment, Survey1Fragment.newInstance()).commit();
        fragmentManager.beginTransaction().add(R.id.fragment, MainFragment.newInstance(), ""+FRAG_MAIN).commit();

////         DUMP DATA TO FIREBASE
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//        database.removeValue();
//        for(Material material:DatabaseManager.getInstance(this).getListLevelTest()) {
//            database.child(Common.MATERIAL).child(""+material.getId()).setValue(material);
//            database.child(Common.MATERIAL_COUNT).child(""+material.getId()).setValue(material.getId());
//        }

//        INIT FIRST WEIGHT/VALUE
//        Preference preference = new Preference();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        preference.setLearnPreferenceScore(UserProfile.MATCH_SCORE_LEARNLIST);
//        preference.setTimeScore(UserProfile.MATCH_SCORE_TIMESPEND);
//        preference.setTestScore(UserProfile.MATCH_SCORE_TESTPREFER);
//        preference.setOverallScore(UserProfile.MATCH_SCORE_OVERALL);
//        preference.setDecisionBoundary(0.7);
//        databaseReference.child("preference").setValue(preference);



        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("preference").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Preference preference = dataSnapshot.getValue(Preference.class);

                    DatabaseManager.setPreference(preference);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void startActivity(int type, Bundle bundle){
        Intent intent = new Intent(this, DetailActivity.class);
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
