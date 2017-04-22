package com.hedspi.hoangviet.eslrecom.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;

/**
 * Created by hoangviet on 11/20/16.
 */

public class Survey1Fragment extends Fragment {
    private View view;
    private UserProfile profile;

    public static Survey1Fragment newInstance(){
        Survey1Fragment fragment = new Survey1Fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profile = new UserProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_survey1, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.answerGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
//                    case R.id.answer_a:
//                        profile.setLearnTimeScore(UserProfile.BEGINNER);
//                        break;
//                    case R.id.answer_b:
//                        profile.setLearnTimeScore(UserProfile.BASIC);
//                        break;
//                    case R.id.answer_c:
//                        profile.setLearnTimeScore(UserProfile.INTERMEDIATE);
//                        break;
//                    case R.id.answer_d:
//                        profile.setLearnTimeScore(UserProfile.UPPER_INTERMEDIATE);
//                        break;
//                    case R.id.answer_e:
//                        profile.setLearnTimeScore(UserProfile.ADVANCED);
//                        break;
                }
            }
        });

        TextView nextButton = (TextView) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioGroup.getCheckedRadioButtonId() != -1){
                    if (radioGroup.getCheckedRadioButtonId() == R.id.answer_a){
                        profile.calculateReadingScore(0);
//                        profile.setListeningScore(0);
//                        profile.setWrittingScore(0);
//                        profile.setSpeakingScore(0);
//                        profile.setTestPreference(Preference.NONE);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().setCustomAnimations(
                                R.anim.slide_in_right, R.anim.slide_out_left,
                                R.anim.slide_in_left, R.anim.slide_out_right)
                                .replace(R.id.fragment, Survey8Fragment.newInstance(profile)) //Survey7Fragment.newInstance(profile))
                                .addToBackStack(null)
                                .commit();
                    }else{
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().setCustomAnimations(
                                R.anim.slide_in_right, R.anim.slide_out_left,
                                R.anim.slide_in_left, R.anim.slide_out_right)
                                .replace(R.id.fragment, Survey2Fragment.newInstance(profile))
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


}
