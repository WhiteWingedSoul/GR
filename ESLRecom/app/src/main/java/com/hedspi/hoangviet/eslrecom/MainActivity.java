package com.hedspi.hoangviet.eslrecom;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hedspi.hoangviet.eslrecom.commons.Common;
import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.fragments.MainFragment;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.hedspi.hoangviet.eslrecom.models.Question;
import com.hedspi.hoangviet.eslrecom.models.Tag;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
//        new AsyncTask<Void, Void, Void>(){
//            @Override
//            protected Void doInBackground(Void... params) {
//                dumpDataFirebase();
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                Toast.makeText(MainActivity.this, "Data inited!", Toast.LENGTH_LONG).show();
//                super.onPostExecute(aVoid);
//
//            }
//        }.execute();
        /////////////////////////////////////////////

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

    private void dumpDataFirebase(){
        DatabaseManager databaseManager = DatabaseManager.getInstance(this);

        //// DUMP QUESTION
        String json = databaseManager.readJSONFromAsset("catdata.json");
        Gson gson = new Gson();
        List<Question> listQuestion = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject questionJson = jsonArray.getJSONObject(i);
                Question question = gson.fromJson(questionJson.toString(), Question.class);

                if(question != null) {
                    listQuestion.add(question);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //// DUMP TAG
        json = databaseManager.readJSONFromAsset("tagdata.json");
        List<Tag> listTag = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject tagJson = jsonArray.getJSONObject(i);
                Tag tag = gson.fromJson(tagJson.toString(), Tag.class);

                if(tag != null) {
                    listTag.add(tag);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //// DUMP TAG
        json = databaseManager.readJSONFromAsset("materialdata.json");
        List<Material> listMaterial = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject materialJson = jsonArray.getJSONObject(i);
                Material material = gson.fromJson(materialJson.toString(), Material.class);

                if(material != null) {
                    listMaterial.add(material);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("LOG","");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.removeValue();
        for(Material material:listMaterial) {
            database.child(Common.MATERIAL).push().setValue(material);
        }
        for(Question question:listQuestion) {
            database.child(Common.QUESTION).push().setValue(question);
        }
        for(Tag tag:listTag) {
            database.child(Common.TAG).push().setValue(tag);
        }

//        INIT FIRST WEIGHT/VALUE
        Preference preference = new Preference();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        preference.setLearnPreferenceScore(UserProfile.MATCH_SCORE_LEARNLIST);
        preference.setOverallScore(UserProfile.MATCH_SCORE_OVERALL);
        preference.setDecisionBoundary(0.1);
        databaseReference.child("preference").setValue(preference);

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

    public void startTestActivity(int type, Bundle bundle){
        Intent intent = new Intent(this, TestActivity.class);
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void startResultActivity(int type, Bundle bundle){
        Intent intent = new Intent(this, ResultActivity.class);
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
