package com.hedspi.hoangviet.eslrecom.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hoangviet on 11/20/16.
 */

public class ExperFragment extends Fragment {
    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    public static ExperFragment newInstance(){
        ExperFragment fragment = new ExperFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_expert, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        ExpertViewPagerAdapter adapter = new ExpertViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("bookProfiles-count").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {

                    ArrayList<Long> bookProfileIDsList = new ArrayList<Long>();
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        bookProfileIDsList.add((long)child.getValue());
                    }
//                    GenericTypeIndicator<ArrayList<Long>> t = new GenericTypeIndicator<ArrayList<Long>>() {};
//                    bookProfileIDsList.clear();
//                    bookProfileIDsList.addAll(dataSnapshot.getValue(t));
                    DatabaseManager.setBookProfilesCount(bookProfileIDsList.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    class ExpertViewPagerAdapter extends FragmentStatePagerAdapter {

        public ExpertViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 1:
                    return ListAddedBookFragment.newInstance();
                case 2:
                    return SettingFragment.newInstance();
                default:
                    return ListTobeAddedBookFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 1:
                    return getResources().getString(R.string.updateBookProfile);
                case 2:
                    return getResources().getString(R.string.setting);
                default:
                    return getResources().getString(R.string.addBookProfile);
            }
        }
    }

}
