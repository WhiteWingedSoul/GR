package com.hedspi.hoangviet.eslrecom.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
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
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.ChildTag;
import com.hedspi.hoangviet.eslrecom.models.MatchResult;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.hedspi.hoangviet.eslrecom.models.PreviewMaterial;
import com.hedspi.hoangviet.eslrecom.models.Tag;
import com.hedspi.hoangviet.eslrecom.models.UsageLog;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hoangviet on 11/20/16.
 */

public class HistoryMaterialFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private HistoryMaterialAdapter adapter;
    private ProgressDialog progress;
    private List<PreviewMaterial> listMaterials;

    public static HistoryMaterialFragment newInstance(UsageLog log){
        HistoryMaterialFragment fragment = new HistoryMaterialFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("log", log);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onResume() {
        ((HistoryActivity)getActivity()).switchToggle(false);
        super.onResume();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listMaterials = ((UsageLog) getArguments().getSerializable("log")).getGoodRatingMaterialIds();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_history_material, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HistoryMaterialAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        adapter.updateAdapter(listMaterials);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    class HistoryMaterialAdapter extends RecyclerView.Adapter{
        private Context mContext;
        private List<PreviewMaterial> mList = new ArrayList<>();

        public HistoryMaterialAdapter(Context context){
            super();
            mContext = context;
        }

        public void updateAdapter(List<PreviewMaterial> list){
            mList.clear();
            mList.addAll(list);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            HistoryMaterialHolder vh = (HistoryMaterialHolder) holder;
            final PreviewMaterial material = mList.get(position);
            Picasso.with(getActivity()).load(material.getMaterialIconLink())
                    .fit()
                    .placeholder(R.drawable.placeholder)
                    .into(vh.bookIcon);
            vh.title.setText(material.getMaterialName());
            vh.description.setText(material.getMaterialDescription());

            vh.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().setCustomAnimations(
                            R.anim.slide_in_right, R.anim.slide_out_left,
                            R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.fragment, DetailBookFragment.newInstance(material.getMaterialId()))
                            .addToBackStack(null)
                            .commit();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("book", matchResult.getMaterial());
//                    ((MainActivity)mContext).startActivity(MainActivity.VIEW, bundle);
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_result, parent, false);
            RecyclerView.ViewHolder viewHolder = new HistoryMaterialHolder(view);
            return viewHolder;
        }
    }

    class HistoryMaterialHolder extends RecyclerView.ViewHolder{
        //public CardView frameLayout;
        public View itemLayout;
        public ImageView bookIcon;
        public TextView title;
        public TextView description;

        public HistoryMaterialHolder(View view){
            super(view);

//            frameLayout = (CardView) view.findViewById(R.id.frameLayout);
            itemLayout = view.findViewById(R.id.itemLayout);
            bookIcon = (ImageView) view.findViewById(R.id.bookIcon);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
        }

    }
}
