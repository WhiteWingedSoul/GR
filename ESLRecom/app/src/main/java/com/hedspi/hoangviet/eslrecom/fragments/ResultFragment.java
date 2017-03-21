package com.hedspi.hoangviet.eslrecom.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hedspi.hoangviet.eslrecom.MainActivity;
import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.commons.Common;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.MatchResult;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hoangviet on 11/20/16.
 */

public class ResultFragment extends Fragment {
    private View view;
    private UserProfile profile;
    private RecyclerView recyclerView;
    private MatchResultAdapter adapter;
    private ProgressDialog progress;
    private List<Material> listMaterialProfile;
    private List<MatchResult> listMatchResult ;

    public static ResultFragment newInstance(UserProfile profile){
        ResultFragment fragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("profile", profile);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.caculating), false);

        profile = (UserProfile) getArguments().getSerializable("profile");


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

    private void runFirstMatching(){
        listMatchResult = new ArrayList<>();
        for(Material material:listMaterialProfile){
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
            public int compare(MatchResult result2, MatchResult result1)
            {
                return  Double.compare(result1.getMatchScore(), result2.getMatchScore());
            }
        });


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter.updateAdapter(listMatchResult);
                adapter.notifyDataSetChanged();
                progress.hide();
            }
        });
    }

    private void getAllMaterials() throws IOException{

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        listMaterialProfile = new ArrayList<>();


        database.child(Common.MATERIAL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {

                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        listMaterialProfile.add(child.getValue(Material.class));
                    }

                    runFirstMatching();

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
        String level = profile.getOverallPreference();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_result, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        TextView decisionBoundary = (TextView)view.findViewById(R.id.decisionBoundary);
        decisionBoundary.setText(getResources().getString(R.string.decision)+" > "+DatabaseManager.getPreference().getDecisionBoundary());
        adapter = new MatchResultAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    class MatchResultAdapter extends RecyclerView.Adapter{
        private Context mContext;
        private List<MatchResult> mList = new ArrayList<>();

        public MatchResultAdapter(Context context){
            super();
            mContext = context;
        }

        public void updateAdapter(List<MatchResult> list){
            mList.clear();
            mList.addAll(list);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MatchResultHolder vh = (MatchResultHolder) holder;
            final MatchResult matchResult = mList.get(position);
            Picasso.with(getActivity()).load(matchResult.getMaterial().getCoverLink())
                    .fit()
                    .placeholder(R.drawable.placeholder)
                    .into(vh.bookIcon);
            vh.title.setText(matchResult.getMaterial().getName());
            vh.description.setText(matchResult.getMaterial().getSummary());

//            vh.matchRate.setText(getResources().getString(R.string.match)+String.format("%.3f", matchResult.getMatchScore())+" %");
            vh.matchRate.setVisibility(View.GONE);

            vh.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("book", matchResult.getMaterial());
                    ((MainActivity)mContext).startActivity(MainActivity.VIEW, bundle);
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_result, parent, false);
            RecyclerView.ViewHolder viewHolder = new MatchResultHolder(view);
            return viewHolder;
        }
    }

    class MatchResultHolder extends RecyclerView.ViewHolder{
        //public CardView frameLayout;
        public View itemLayout;
        public ImageView bookIcon;
        public TextView matchRate;
        public TextView title;
        public TextView description;

        public MatchResultHolder(View view){
            super(view);

//            frameLayout = (CardView) view.findViewById(R.id.frameLayout);
            itemLayout = view.findViewById(R.id.itemLayout);
            bookIcon = (ImageView) view.findViewById(R.id.bookIcon);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            matchRate = (TextView) view.findViewById(R.id.matchRate);
        }

    }

}
