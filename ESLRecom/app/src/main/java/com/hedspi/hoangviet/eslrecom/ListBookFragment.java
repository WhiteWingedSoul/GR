package com.hedspi.hoangviet.eslrecom;

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
import com.hedspi.hoangviet.eslrecom.models.Book;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangviet on 11/20/16.
 */

public class ListBookFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private MatchResultAdapter adapter;

    private final ValueEventListener readBookData = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //GenericTypeIndicator<ArrayList<Book>> t = new GenericTypeIndicator<ArrayList<Book>>() {};
            //listBook.clear();
            //listBook.addAll((List<Book>)dataSnapshot.getValue(t));

            final Book book = dataSnapshot.getValue(Book.class);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.updateAdapter(book);
                }
            });

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public static ListBookFragment newInstance(){
        ListBookFragment fragment = new ListBookFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void letDoTheGodWork() throws IOException{
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("books-count").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clearData();
                for(int i=0;i<(long)dataSnapshot.getValue();i++){
                    database.child("books").child(""+i).removeEventListener(readBookData);
                    database.child("books").child(""+i).addValueEventListener(readBookData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        private List<Book> mList = new ArrayList<>();

        public MatchResultAdapter(Context context){
            super();
            mContext = context;
        }

        public void updateAdapter(Book book){
            mList.add(book);
            notifyItemInserted(book.getId());
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
            final Book book = mList.get(position);
            Picasso.with(getActivity()).load(book.getCoverLink())
                    .fit()
                    .into(vh.bookIcon);
            vh.title.setText(book.getName());
            vh.description.setText(book.getSummary());

            //vh.matchRate.setText("Match: "+String.format("%.3f", book.getMatchScore())+" %");
            vh.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().setCustomAnimations(
                            R.anim.slide_in_right, R.anim.slide_out_left,
                            R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.fragment, SetBookProfileFragment.newInstance(book))
                            .addToBackStack(null)
                            .commit();
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