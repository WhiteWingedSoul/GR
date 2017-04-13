package com.hedspi.hoangviet.eslrecom.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hedspi.hoangviet.eslrecom.MainActivity;
import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.commons.Common;
import com.hedspi.hoangviet.eslrecom.managers.AnimationHelper;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.Tag;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by hoangviet on 11/20/16.
 */

public class MainFragment extends Fragment {
    private View view;
    private UserProfile profile;

    private SharedPreferences setting;

    public static MainFragment newInstance(){
        MainFragment fragment = new MainFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setting = getActivity().getSharedPreferences("setting", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_main, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SharedPreferences.Editor editor = setting.edit();

        ImageView configButton = (ImageView) view.findViewById(R.id.configButton);
        Button recommendButton = (Button) view.findViewById(R.id.recommendButton);
        Spinner languageSpinner = (Spinner) view.findViewById(R.id.languageSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.language, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final String currentLanguage = setting.getString("languageString","en");
        languageSpinner.setAdapter(adapter);
        if(currentLanguage.equals("en"))
            languageSpinner.setSelection(1);
        else
            languageSpinner.setSelection(0);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if (!currentLanguage.equals("vi")) {
                            editor.putString("languageString", "vi");
                            editor.commit();
                            setLocale("vi");

                            Intent refresh = new Intent(getActivity(), getActivity()
                                    .getClass());
                            startActivity(refresh);
                            getActivity().finish();
                        }
                        break;
                    case 1:
                        if(!currentLanguage.equals("en")) {
                            editor.putString("languageString", "en");
                            editor.commit();
                            setLocale("en");

                            Intent refresh = new Intent(getActivity(), getActivity()
                                    .getClass());
                            startActivity(refresh);
                            getActivity().finish();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHelper.playAssetAnimation(getActivity(), v, R.anim.shakeitoff, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//                        getActivity().getSupportFragmentManager()
//                                .beginTransaction().setCustomAnimations(
//                                R.anim.slide_in_right, R.anim.slide_out_left,
//                                R.anim.slide_in_left, R.anim.slide_out_right)
//                                .replace(R.id.fragment, ExperFragment.newInstance())
//                                .addToBackStack(null)
//                                .commit();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTagListFromServer();

            }
        });

    }

    private void getTagListFromServer(){
        if (DatabaseManager.getTagList() == null) {
            final ProgressDialog progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading), false);
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child(Common.TAG).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        List<Tag> list = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            list.add(child.getValue(Tag.class));
                        }
                        DatabaseManager.setTagList(list);
                        progress.hide();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().setCustomAnimations(
                                R.anim.slide_in_right, R.anim.slide_out_left,
                                R.anim.slide_in_left, R.anim.slide_out_right)
                                .replace(R.id.fragment, Survey1Fragment.newInstance())
                                .addToBackStack(null)
                                .commit();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progress.hide();
                }
            });
        }else{
            getActivity().getSupportFragmentManager()
                    .beginTransaction().setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment, Survey1Fragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        /*Intent refresh = new Intent(this, AndroidLocalize.class);
        startActivity(refresh);
        finish();*/
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


}
