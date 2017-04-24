package com.hedspi.hoangviet.eslrecom;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hedspi.hoangviet.eslrecom.adapters.ResultViewPagerAdapter;
import com.hedspi.hoangviet.eslrecom.adapters.TestViewPagerAdapter;
import com.hedspi.hoangviet.eslrecom.commons.Common;
import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.helpers.TestHelper;
import com.hedspi.hoangviet.eslrecom.libraries.NonSwipeableViewPager;
import com.hedspi.hoangviet.eslrecom.managers.AnimationHelper;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.AdapterItem;
import com.hedspi.hoangviet.eslrecom.models.MatchResult;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.hedspi.hoangviet.eslrecom.models.Question;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;

public class ResultActivity extends AppCompatActivity {
    private List<Material> listMaterialProfile;
    private List<MatchResult> listMatchResult;
    private List<AdapterItem> mItems;
    private UserProfile profile;
    private ProgressDialog progress;

    private NonSwipeableViewPager viewPager;
    private ResultViewPagerAdapter adapter;
    private View viewLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        progress = ProgressDialog.show(this, "", getResources().getString(R.string.caculating), false);
        profile = DatabaseManager.getUserProfile();

        viewLayout = findViewById(R.id.viewLayout);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    getAllMaterials();
                }catch (IOException e){
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();


    }

    private void runFirstMatching() {
        listMatchResult = new ArrayList<>();
        for (Material material : listMaterialProfile) {
            double matchScore = contextMatching(material);
            if (matchScore > DatabaseManager.getPreference().getDecisionBoundary()) {
                MatchResult matchResult = new MatchResult();
                matchResult.setMaterial(material);
                matchResult.setMatchScore(matchScore);

                listMatchResult.add(matchResult);
            }
        }

        Collections.sort(listMatchResult, new Comparator<MatchResult>() {
            @Override
            public int compare(MatchResult result2, MatchResult result1) {
                return Double.compare(result1.getMatchScore(), result2.getMatchScore());
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                adapter.updateAdapter(listMatchResult);
//                adapter.notifyDataSetChanged();

                mItems.addAll(listMatchResult);
                adapter = new ResultViewPagerAdapter(getSupportFragmentManager(), mItems);
                viewPager.setAdapter(adapter);
                viewLayout.setVisibility(View.VISIBLE);
                progress.hide();
            }
        });
    }

    private void getAllMaterials() throws IOException {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        listMaterialProfile = new ArrayList<>();


        database.child(Common.MATERIAL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {

                    Map<String, Object> datas = (Map<String, Object>) dataSnapshot.getValue();
                    for (Map.Entry<String, Object> entry : datas.entrySet()) {
                        //Get user map
                        Map singleData = (Map) entry.getValue();
                        Material material = new Material();
                        material.setName((String) singleData.get("name"));
                        material.setAbstractString((String) singleData.get("abstractString"));
                        material.setAuthor((String) singleData.get("author"));
                        material.setContent((String) singleData.get("content"));
                        material.setCoverLink((String) singleData.get("coverLink"));
                        material.setDescription((String) singleData.get("description"));
                        material.setDocumentType((String) singleData.get("documentType"));
                        material.setEdition_format((String) singleData.get("edition_format"));
                        material.setGenre_form((String) singleData.get("genre_form"));
                        material.setNote((String) singleData.get("note"));
                        material.setOnlineLink((String) singleData.get("onlineLink"));
                        material.setOnlineName((String) singleData.get("onlineName"));
                        material.setPublisher((String) singleData.get("publisher"));
                        material.setSellerLink((String) singleData.get("sellerLink"));
                        material.setSellerName((String) singleData.get("sellerName"));
                        material.setSellerPrice((String) singleData.get("sellerPrice"));
                        material.setSubjects((String) singleData.get("subjects"));
                        material.setSummary((String) singleData.get("summary"));
                        material.setTag((String) singleData.get("tag"));

                        listMaterialProfile.add(material);
                    }
//
//                    for(DataSnapshot child : dataSnapshot.getChildren()){
//                        listMaterialProfile.add(child.getValue(Material.class));
//                    }

                    runFirstMatching();

                    String a = "hihi";
                    //TODO
                    //for now let's just focus on kansei on the tags
                    // build User Preference: Material class with List<Object> attributes
                    // choose between showing 1 or 10 at a time

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private double contextMatching(Material material){
        double matchRate = 0;

        double levelMatchScore = matchOverall(material);
//        double testMatchScore = matchTestPrefer(bookProfile);
//        double timeMatchScore = matchTimeSpend(bookProfile);
        double learnListMatchScore = matchLearnList(material);

        matchRate = levelMatchScore + learnListMatchScore; //+ testMatchScore + timeMatchScore ;

        return matchRate/DatabaseManager.getPreference().getBestMatch();
    }

    private double matchOverall(Material material){
        String level = profile.returnOverallPreference();
        if (material.getName().contains(level) || material.getTag().contains(level))
            return DatabaseManager.getPreference().getOverallScore();
        else
            return 0;
    }

    /*private double matchTestPrefer(BookProfile bookProfile){
        if(bookProfile.getTestPreference() == profile.getTestPreference())
            return DatabaseManager.getPreference().getTestScore();
        else
            return 0;
    }

    private double matchTimeSpend(BookProfile bookProfile){
        if(bookProfile.getTimePreference() == profile.getTimeCanSpend())
            return DatabaseManager.getPreference().getTimeScore();
        else
            return 0;
    }*/

    private double matchLearnList(Material material){
        //First test try: use only word people input to compare
        // if word exists in material tag or name -> its matched!
        double learnlistScore = 0;
        int userLearnListCount = profile.getLearnList().size();
        int matchCount = 0;
        learnlistScore = ((double)matchCount/userLearnListCount);
        for(String userLearnType : profile.getLearnList()){
            if (material.getName().contains(userLearnType) || material.getTag().contains(userLearnType)) {
                matchCount++;
                learnlistScore += material.getKeywordImportantScore(userLearnType);
            }
        }

        learnlistScore = learnlistScore* DatabaseManager.getPreference().getLearnPreferenceScore()/2;
        return learnlistScore;
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    private void toPreviousFragment() {
        int currentPosition = viewPager.getCurrentItem();

        if (currentPosition == 0) {
            viewPager.setCurrentItem(currentPosition, true);
        } else {
            viewPager.setCurrentItem(currentPosition - 1, true);
        }
    }

    public void toNextFragment() {
        int currentPosition = viewPager.getCurrentItem();

        if ((currentPosition + 1) == mItems.size()) {
            finish();
        } else {
            viewPager.setCurrentItem(currentPosition + 1, true);
        }
    }

//    public boolean isChangingFragment() {
//        return changingFragment;
//    }

}
