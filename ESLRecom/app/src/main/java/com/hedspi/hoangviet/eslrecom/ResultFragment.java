package com.hedspi.hoangviet.eslrecom;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hedspi.hoangviet.eslrecom.libraries.GooglePlayElement;
import com.hedspi.hoangviet.eslrecom.libraries.GooglePlayWrapper;
import com.hedspi.hoangviet.eslrecom.models.MatchResult;
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

        profile = (UserProfile) getArguments().getSerializable("profile");

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    letDoTheGodWork();
                }catch (IOException e){
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();

    }

    private void letDoTheGodWork() throws IOException{
        GooglePlayWrapper manager = new GooglePlayWrapper();
        List<GooglePlayElement> listResult = new ArrayList<>();
        final List<MatchResult> listMatchResult = new ArrayList<>();
        for(String query : profile.getSearchQuery()) {
            Log.d("LOG","query: "+query);
            List<GooglePlayElement> result = manager.searchBook(query);
            listResult.addAll(result);
        }

        for(GooglePlayElement result : listResult){
            MatchResult matchResult = new MatchResult();
            matchResult.setElement(result);
            matchResult.setMatchScore(contextMatching(matchResult, result));

            listMatchResult.add(matchResult);
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
            }
        });
    }

    private double contextMatching(MatchResult matchResult, GooglePlayElement result){
        double matchRate = 0;

        matchResult.setOverallMatch(matchOverall(result));
        matchResult.setReadingMatch(matchReading(result));
        matchResult.setSpeakingMatch(matchSpeaking(result));
        matchResult.setWrittingMatch(matchWritting(result));
        matchResult.setListeningMatch(matchListening(result));
        //matchResult.setTestPreferMatch(matchTestPrefer(result));
        //matchResult.setTimeSpendMatch(matchTimeSpend(result));
        matchResult.setLearnListMatch(matchLearnList(result));

        matchRate = matchResult.getOverallMatch() +
                matchResult.getReadingMatch() +
                matchResult.getWrittingMatch() +
                matchResult.getListeningMatch() +
                matchResult.getSpeakingMatch() +
                matchResult.getLearnListMatch();

        return matchRate*100/profile.getBestMatch();
    }

    private double matchOverall(GooglePlayElement result){
        if(result.getelementDescription().contains(profile.getOverallPreference()) ||
                result.getElementName().contains(profile.getOverallPreference()))
            return UserProfile.MATCH_SCORE_OVERALL;
        else
            return 0;
    }

    private double matchReading(GooglePlayElement result){
        if( (result.getelementDescription().contains(profile.getOverallPreference()) && result.getelementDescription().contains("read"))  ||
                (result.getElementName().contains(profile.getOverallPreference()) && result.getElementName().contains("read")) )
            return UserProfile.MATCH_SCORE_READING;
        else
            return 0;
    }

    private double matchWritting(GooglePlayElement result){
        if( (result.getelementDescription().contains(profile.getOverallPreference()) && result.getelementDescription().contains("writing"))  ||
                (result.getElementName().contains(profile.getOverallPreference()) && result.getElementName().contains("writing")) )
            return UserProfile.MATCH_SCORE_READING;
        else
            return 0;
    }
    private double matchListening(GooglePlayElement result){
        if( (result.getelementDescription().contains(profile.getOverallPreference()) && result.getelementDescription().contains("listen"))  ||
                (result.getElementName().contains(profile.getOverallPreference()) && result.getElementName().contains("listen")) )
            return UserProfile.MATCH_SCORE_READING;
        else
            return 0;
    }
    private double matchSpeaking(GooglePlayElement result){
        if( (result.getelementDescription().contains(profile.getOverallPreference()) && result.getelementDescription().contains("speak"))  ||
                (result.getElementName().contains(profile.getOverallPreference()) && result.getElementName().contains("speak")) )
            return UserProfile.MATCH_SCORE_READING;
        else
            return 0;
    }
    private double matchLearnList(GooglePlayElement result){
        double score = 0;
        for(String learnType : profile.getLearnList()){
            if(result.getelementDescription().contains(learnType) ||
                    result.getElementName().contains(learnType))
                return UserProfile.MATCH_SCORE_LEARNLIST;
        }
        return 0;
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
            Picasso.with(getActivity()).load(matchResult.getElement().getElementImgUrl())
                    .fit()
                    .into(vh.bookIcon);
            vh.title.setText(matchResult.getElement().getElementName());
            vh.description.setText(matchResult.getElement().getelementDescription());

            vh.matchRate.setText("Match: "+String.format("%.3f", matchResult.getMatchScore())+" %");
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
        public ImageView bookIcon;
        public TextView matchRate;
        public TextView title;
        public TextView description;

        public MatchResultHolder(View view){
            super(view);

//            frameLayout = (CardView) view.findViewById(R.id.frameLayout);
            bookIcon = (ImageView) view.findViewById(R.id.bookIcon);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            matchRate = (TextView) view.findViewById(R.id.matchRate);
        }

    }

}
