package com.hedspi.hoangviet.eslrecom;

import android.animation.Animator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.hedspi.hoangviet.eslrecom.helpers.ResultHelper2Test;
import com.hedspi.hoangviet.eslrecom.helpers.TestHelper;
import com.hedspi.hoangviet.eslrecom.libraries.NonSwipeableViewPager;
import com.hedspi.hoangviet.eslrecom.managers.AnimationHelper;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.AdapterItem;
import com.hedspi.hoangviet.eslrecom.models.KanseiPreferences;
import com.hedspi.hoangviet.eslrecom.models.MatchResult;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.hedspi.hoangviet.eslrecom.models.Question;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;
import com.squareup.picasso.Picasso;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;

public class ResultActivity extends AppCompatActivity implements DataDownloadListener {
    private List<Material> listMaterialProfile;
    private List<MatchResult> listMatchResult;
    private List<AdapterItem> mItems = new ArrayList<>();
    private ProgressDialog progress;
    private Material currentMaterial;

    private NonSwipeableViewPager viewPager;
    private ResultViewPagerAdapter adapter;
    private View viewLayout;
    private View smallInfoLayout;
    private View detailLayout;
    private View buyerLayout;
    private View subjectLayout;
    private View contentLayout;
    private View externalLinkLayout;

    private TextView title;
    private TextView author;
    private TextView publisher;
    private TextView editionformat;
    private TextView priceTag;
    private TextView genreform;
    private TextView docType;
    private TextView content;
    private TextView summary;
    private TextView subjects;
    private TextView buyerName;
    private TextView buyerPrice;
    private TextView buyerLink;
    private DiscreteSeekBar kanseiScale;
    private Button buyButton;
    private Button viewOnlineButton;

    private Button rateButton;


    @Override
    public void onBackPressed() {
        KanseiPreferences userKanseiPreference;
        if ((userKanseiPreference = ResultHelper2Test.getUserKanseiPreferences()) != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.dialog_test, null);
            final Dialog dialog = builder.setView(dialogView).create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
            TextView testText = (TextView) dialogView.findViewById(R.id.testText);
            testText.setText(Html.fromHtml(userKanseiPreference.retrieveKanseiTagResultTest()));

        }
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        progress = ProgressDialog.show(this, "", getResources().getString(R.string.caculating), false);

        viewLayout = findViewById(R.id.viewLayout);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        detailLayout = findViewById(R.id.detailLayout);
        buyerLayout = findViewById(R.id.buyerLayout);
        subjectLayout = findViewById(R.id.subjectLayout);
        contentLayout = findViewById(R.id.contentLayout);
        smallInfoLayout = findViewById(R.id.smallInfoLayout);
        externalLinkLayout = findViewById(R.id.externalLinkLayout);

        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        publisher = (TextView) findViewById(R.id.publisher);
        editionformat = (TextView) findViewById(R.id.editionformat);
        priceTag = (TextView) findViewById(R.id.priceTag);
        genreform = (TextView) findViewById(R.id.genreform);
        docType = (TextView) findViewById(R.id.docType);
        content = (TextView) findViewById(R.id.content);
        summary = (TextView) findViewById(R.id.summary);
        subjects = (TextView) findViewById(R.id.subjects);
        buyerName = (TextView) findViewById(R.id.buyerName);
        buyerPrice = (TextView) findViewById(R.id.buyerPrice);
        buyerLink = (TextView) findViewById(R.id.buyerLink);

        kanseiScale = (DiscreteSeekBar) findViewById(R.id.kanseiScale);
        rateButton = (Button) findViewById(R.id.rateButton);
        buyButton = (Button) findViewById(R.id.buyButton);
        viewOnlineButton = (Button) findViewById(R.id.viewOnlineButton);

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reevalute(kanseiScale.getProgress());
//                toNextFragment();
            }
        });


        ResultHelper2Test.initResultHelper(DatabaseManager.getMaterialListFromServer(this), DatabaseManager.getUserProfile());

    }

    @Override
    public void onDataDownloaded(String result) {
        Log.d("LOG data downloaded: ", result);
        progress.hide();
        if (result == Common.SUCCESS){
            startInitialMatching();

            currentMaterial = ((MatchResult)mItems.get(0)).getMaterial();
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToUrl(currentMaterial.getSellerLink());
                }
            });
            viewOnlineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToUrl(currentMaterial.getOnlineLink());
                }
            });
            updateViews();

            viewLayout.setVisibility(View.VISIBLE);
        }
    }

    private void reevalute(int score){
        switch (ResultHelper2Test.reevaluate((double)score/10)){
            case ResultHelper2Test.STATUS_CONTINUE:
                toNextFragment();
                break;
            case ResultHelper2Test.STATUS_END:
                startMatchingKansei();
                toNextFragment();
                break;
        }
    }

    private void startInitialMatching(){
        List<MatchResult> first5 = ResultHelper2Test.initFirstMatching();

        mItems.addAll(first5);
        adapter = new ResultViewPagerAdapter(getSupportFragmentManager(), mItems);
        viewPager.setAdapter(adapter);
    }

    private void startMatchingKansei(){
        List<MatchResult> next5 = ResultHelper2Test.matchingWithKansei();
        mItems.addAll(next5);
        adapter.notifyDataSetChanged();
    }

    private void updateViews(){

        AnimationHelper.playAppearAnimation(detailLayout, 0, null);
        AnimationHelper.playAppearAnimation(smallInfoLayout, 0, null);
        AnimationHelper.playAppearAnimation(externalLinkLayout, 0, null);

        if (currentMaterial.getName() == null || currentMaterial.getName().equals("")){
            title.setText("");
        }else{
            title.setText(currentMaterial.getName());
        }
        if (currentMaterial.getAuthor() == null || currentMaterial.getAuthor().equals("")){
            author.setText("");
        }else{
            author.setText(getResources().getString(R.string.by)+ currentMaterial.getAuthor());
        }
        if (currentMaterial.getPublisher() == null || currentMaterial.getPublisher().equals("")){
            publisher.setText("");
        }else{
            publisher.setText(currentMaterial.getPublisher());
        }
        if (currentMaterial.getEdition_format() == null || currentMaterial.getEdition_format().equals("")){
            editionformat.setText("");
        }else{
            editionformat.setText(currentMaterial.getEdition_format());
        }

        //TODO FORMAT
        if (currentMaterial.getGenre_form() == null || currentMaterial.getGenre_form().equals("")){
            genreform.setText("");
        }else{
            genreform.setText(getResources().getString(R.string.genreform)+ currentMaterial.getGenre_form());
        }
        if (currentMaterial.getDocumentType() == null || currentMaterial.getDocumentType().equals("")){
            docType.setText("");
        }else{
            docType.setText(getResources().getString(R.string.docType)+ currentMaterial.getDocumentType());
        }
        // -------------------

        if (currentMaterial.getContent() == null || currentMaterial.getContent().equals("")){
            contentLayout.setVisibility(View.GONE);
        }else{
            contentLayout.setVisibility(View.VISIBLE);
            content.setText(currentMaterial.getContent());
        }
        if (currentMaterial.getSummary() == null || currentMaterial.getSummary().equals("")){
            summary.setText("");
        }else{
            summary.setText(getResources().getString(R.string.summary)+ currentMaterial.getSummary());
        }
        if (currentMaterial.getSubjects() == null || currentMaterial.getSubjects().equals("")){
            subjectLayout.setVisibility(View.GONE);
        }else{
            subjectLayout.setVisibility(View.VISIBLE);
            subjects.setText(currentMaterial.getSubjects());
        }
        if (currentMaterial.getSellerName() == null || currentMaterial.getSellerName().equals("")){
            buyerLayout.setVisibility(View.GONE);
            buyButton.setVisibility(View.GONE);

            if (currentMaterial.getOnlineName() == null || currentMaterial.getOnlineName().equals("")){
                viewOnlineButton.setVisibility(View.GONE);
            }else{
                priceTag.setVisibility(View.VISIBLE);
                viewOnlineButton.setVisibility(View.VISIBLE);
                priceTag.setText(currentMaterial.getOnlineName());

            }
        }else{
            viewOnlineButton.setVisibility(View.GONE);
            buyerLayout.setVisibility(View.VISIBLE);
            priceTag.setVisibility(View.VISIBLE);
            buyButton.setVisibility(View.VISIBLE);
            buyerName.setText(currentMaterial.getSellerName());
            buyerPrice.setText(currentMaterial.getSellerPrice());
            buyerLink.setText(currentMaterial.getSellerLink());
            priceTag.setText(currentMaterial.getSellerPrice());

        }

    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void runFirstMatching() {
        listMatchResult = new ArrayList<>();
        for (Material material : listMaterialProfile) {
            double matchScore =0; //= contextMatching(material);
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
                List<MatchResult> first5 = listMatchResult.size() > 5 ? listMatchResult.subList(0,5) : listMatchResult;

                mItems.addAll(first5);
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
            currentMaterial = ((MatchResult)mItems.get(currentPosition+1)).getMaterial();
            updateViews();

            viewPager.setCurrentItem(currentPosition + 1, true);
        }
    }

//    public boolean isChangingFragment() {
//        return changingFragment;
//    }

}
