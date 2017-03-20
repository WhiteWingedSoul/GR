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
import com.hedspi.hoangviet.eslrecom.models.BookProfile;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangviet on 11/20/16.
 */

public class ListAddedBookFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private MatchResultAdapter adapter;

    private final ValueEventListener readBookData = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //GenericTypeIndicator<ArrayList<Material>> t = new GenericTypeIndicator<ArrayList<Material>>() {};
            //listBook.clear();
            //listBook.addAll((List<Material>)dataSnapshot.getValue(t));
            if(dataSnapshot.getValue()!=null) {
                final BookProfile bookProfile = dataSnapshot.getValue(BookProfile.class);
                if (bookProfile!=null) {
                    adapter.updateAdapter(bookProfile);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public static ListAddedBookFragment newInstance(){
        ListAddedBookFragment fragment = new ListAddedBookFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ValueEventListener getProfileCount = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                ArrayList<Long> bookIDsList = new ArrayList<Long>();

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    bookIDsList.add((long)child.getValue());
                }

                for (int i=0;i<bookIDsList.size();i++) {
                    database.child("book-profiles").child("" + bookIDsList.get(i)).removeEventListener(readBookData);
                    database.child("book-profiles").child("" + bookIDsList.get(i)).addValueEventListener(readBookData);
                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void letDoTheGodWork() throws IOException{
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("bookProfiles-count").addValueEventListener(getProfileCount);
    }

    @Override
    public void onDestroy() {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("bookProfiles-count").removeEventListener(getProfileCount);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_list_book, viewGroup, false);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    class MatchResultAdapter extends RecyclerView.Adapter{
        private Context mContext;
        private List<BookProfile> mList = new ArrayList<>();

        public MatchResultAdapter(Context context){
            super();
            mContext = context;
        }

        public void updateAdapter(BookProfile bookProfile){
            boolean notAdded = true;
            for(BookProfile profile:mList){
                if (profile.getId() == bookProfile.getId()){
                    notAdded = false;
                    break;
                }
            }
            if (notAdded) {
                mList.add(bookProfile);
                notifyItemInserted(bookProfile.getId());
            }
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
            MatchResultHolder vh = (MatchResultHolder) holder;
            final BookProfile bookProfile = mList.get(position);
            Picasso.with(getActivity()).load(bookProfile.getBook().getCoverLink())
                    .fit()
                    .into(vh.bookIcon);
            vh.title.setText(bookProfile.getBook().getName());
            vh.description.setText(bookProfile.getBook().getSummary());

            //vh.matchRate.setText("Match: "+String.format("%.3f", book.getMatchScore())+" %");
            vh.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bookProfile", bookProfile);
                    ((MainActivity)mContext).startActivity(MainActivity.ADDED, bundle);
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_book, parent, false);
            RecyclerView.ViewHolder viewHolder = new MatchResultHolder(view);


            return viewHolder;
        }
    }

    class MatchResultHolder extends RecyclerView.ViewHolder{
        public View itemLayout;
        public ImageView bookIcon;
        public TextView matchRate;
        public TextView title;
        public TextView description;

        public MatchResultHolder(View view){
            super(view);

            itemLayout = view.findViewById(R.id.itemLayout);
            bookIcon = (ImageView) view.findViewById(R.id.bookIcon);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            matchRate = (TextView) view.findViewById(R.id.matchRate);
        }

    }

}
