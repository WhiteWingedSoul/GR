package com.hedspi.hoangviet.eslrecom.fragments;

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
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.BookProfile;
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

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final List<MatchResult> listMatchResult = new ArrayList<>();
        final List<BookProfile> listBookProfile = new ArrayList<>();

        database.child("book-profiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {

                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        listBookProfile.add(child.getValue(BookProfile.class));
                    }

                    for(BookProfile bookProfile:listBookProfile){

                        double matchScore = contextMatching(bookProfile);
                        if(matchScore>DatabaseManager.getPreference().getDecisionBoundary()){
                            MatchResult matchResult = new MatchResult();
                            matchResult.setBook(bookProfile.getBook());
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
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private double contextMatching(BookProfile bookProfile){
        double matchRate = 0;

        double levelMatchScore = matchOverall(bookProfile);
        double testMatchScore = matchTestPrefer(bookProfile);
        double timeMatchScore = matchTimeSpend(bookProfile);
        double learnListMatchScore = matchLearnList(bookProfile);

        matchRate = levelMatchScore + testMatchScore + timeMatchScore + learnListMatchScore;

        return matchRate*100/DatabaseManager.getPreference().getBestMatch();
    }

    private double matchOverall(BookProfile bookProfile){
        if(bookProfile.getLevelPreference() == profile.getOverallPreference())
            return DatabaseManager.getPreference().getOverallScore();
        else
            return 0;
    }

    private double matchTestPrefer(BookProfile bookProfile){
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
    }

    private double matchLearnList(BookProfile bookProfile){
        double learnlistScore = 0;
        int userLearnListCount = profile.getLearnList().size();
        int bookLearnListCount = bookProfile.getLearnSubjectPreference().size();
        int matchCount = 0;
        for(String userLearnType : profile.getLearnList()){
            for(String bookLearnType : bookProfile.getLearnSubjectPreference()){
                if (userLearnType.equals(bookLearnType)){
                    matchCount++;
                    break;
                }
            }
        }

        learnlistScore = ((double)matchCount/userLearnListCount)+((double)matchCount/bookLearnListCount);
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
            Picasso.with(getActivity()).load(matchResult.getBook().getCoverLink())
                    .fit()
                    .into(vh.bookIcon);
            vh.title.setText(matchResult.getBook().getName());
            vh.description.setText(matchResult.getBook().getSummary());

            vh.matchRate.setText(getResources().getString(R.string.match)+String.format("%.3f", matchResult.getMatchScore())+" %");

            vh.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("book", matchResult.getBook());
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
