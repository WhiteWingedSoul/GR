package com.hedspi.hoangviet.eslrecom.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hedspi.hoangviet.eslrecom.DataDownloadListener;
import com.hedspi.hoangviet.eslrecom.HistoryActivity;
import com.hedspi.hoangviet.eslrecom.MainActivity;
import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.commons.Common;
import com.hedspi.hoangviet.eslrecom.libraries.TagCompletionView;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.BookProfile;
import com.hedspi.hoangviet.eslrecom.models.ChildTag;
import com.hedspi.hoangviet.eslrecom.models.Tag;
import com.hedspi.hoangviet.eslrecom.models.UsageLog;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hoangviet on 11/20/16.
 */

public class HistoryFragment extends Fragment implements DataDownloadListener{
    private View view;
    private RecyclerView recyclerView;
    private UsageAdapter adapter;
    private List<UsageLog> mListUsage;

    private ProgressDialog progress;

    public static HistoryFragment newInstance(){
        HistoryFragment fragment = new HistoryFragment();

        return fragment;
    }

    @Override
    public void onResume() {
        ((HistoryActivity)getActivity()).switchToggle(true);
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_history, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UsageAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        try {
            progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading), false);
            progress.show();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            mListUsage = DatabaseManager.getUserUsageFromServer(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        if (progress!=null)
            progress.cancel();
        super.onPause();
    }

    @Override
    public void onDataDownloaded(String result) {
        Log.d("LOG data downloaded: ", result);
        if (progress!=null)
            progress.cancel();
        if (result.equals(Common.SUCCESS))
            adapter.updateAdapter(DatabaseManager.getUserUsageList());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    class UsageAdapter extends RecyclerView.Adapter{
        private Context mContext;
        private List<UsageLog> mList = new ArrayList<>();

        public UsageAdapter(Context context){
            super();
            mContext = context;
        }

        public void updateAdapter(List<UsageLog> list){
            clearData();
            mList.addAll(list);
            notifyDataSetChanged();
        }

        public void clearData(){
            mList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            UsageHolder vh = (UsageHolder) holder;
            final UsageLog log = mList.get(position);

            Date date = new Date(log.getCreatedTime());
            vh.date.setText(DateFormat.getDateTimeInstance().format(date));
            vh.preferences.setText(log.getPreferences());

            vh.results.setText(log.getGoodRatingMaterialIds().size()+" "+getResources().getString(R.string.result));
            vh.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().setCustomAnimations(
                            R.anim.slide_in_right, R.anim.slide_out_left,
                            R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.fragment, HistoryMaterialFragment.newInstance(log))
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);
            RecyclerView.ViewHolder viewHolder = new UsageHolder(view);

            return viewHolder;
        }
    }

    class UsageHolder extends RecyclerView.ViewHolder{
        public View itemLayout;
        public TextView preferences;
        public TextView date;
        public TextView results;

        public UsageHolder(View view){
            super(view);

            itemLayout = view.findViewById(R.id.itemLayout);
            preferences = (TextView) view.findViewById(R.id.preferences);
            date = (TextView) view.findViewById(R.id.date);
            results = (TextView) view.findViewById(R.id.results);
        }

    }
}
