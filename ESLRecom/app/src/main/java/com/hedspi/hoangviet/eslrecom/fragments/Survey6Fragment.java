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
import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;

/**
 * Created by hoangviet on 11/20/16.
 */

public class Survey6Fragment extends Fragment {
//    private View view;
//    private UserProfile profile;
//
//    public static Survey6Fragment newInstance(UserProfile profile){
//        Survey6Fragment fragment = new Survey6Fragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("profile", profile);
//        fragment.setArguments(bundle);
//
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        profile = (UserProfile) getArguments().getSerializable("profile");
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
//        if(view == null)
//            view = inflater.inflate(R.layout.fragment_survey6, viewGroup, false);
//
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.answerGroup);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                switch(i) {
//                    case R.id.answer_a:
//                        profile.setTestPreference(Preference.NONE);
//                        break;
//                    case R.id.answer_b:
//                        profile.setTestPreference(Preference.IELTS);
//                        break;
//                    case R.id.answer_c:
//                        profile.setTestPreference(Preference.TOEFL);
//                        break;
//                    case R.id.answer_d:
//                        profile.setTestPreference(Preference.TOEIC);
//                        break;
//                    case R.id.answer_e:
//                        profile.setTestPreference(Preference.iTEC);
//                        break;
//                }
//            }
//        });
//
//        TextView nextButton = (TextView) view.findViewById(R.id.nextButton);
//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(radioGroup.getCheckedRadioButtonId() != -1){
//                    getActivity().getSupportFragmentManager()
//                            .beginTransaction().setCustomAnimations(
//                            R.anim.slide_in_right, R.anim.slide_out_left,
//                            R.anim.slide_in_left, R.anim.slide_out_right)
//                            .replace(R.id.fragment, Survey7Fragment.newInstance(profile))
//                            .addToBackStack(null)
//                            .commit();
//                }
//            }
//        });
//    }
//
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//

}
