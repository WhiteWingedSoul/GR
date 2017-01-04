package com.hedspi.hoangviet.eslrecom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hedspi.hoangviet.eslrecom.models.UserProfile;

/**
 * Created by hoangviet on 11/20/16.
 */

public class Survey8Fragment extends Fragment {
    private View view;
    private UserProfile profile;

    public static Survey8Fragment newInstance(UserProfile profile){
        Survey8Fragment fragment = new Survey8Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("profile", profile);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profile = (UserProfile) getArguments().getSerializable("profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_survey8, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CheckBox answer_a = (CheckBox) view.findViewById(R.id.answer_a);
        final CheckBox answer_b = (CheckBox) view.findViewById(R.id.answer_b);
        final CheckBox answer_c = (CheckBox) view.findViewById(R.id.answer_c);
        final CheckBox answer_d = (CheckBox) view.findViewById(R.id.answer_d);
        final CheckBox answer_e = (CheckBox) view.findViewById(R.id.answer_e);
        final CheckBox answer_f = (CheckBox) view.findViewById(R.id.answer_f);
        final CheckBox answer_g = (CheckBox) view.findViewById(R.id.answer_g);

        answer_a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    profile.getLearnList().add("reading");
                }else{
                    profile.getLearnList().remove("reading");
                }
            }
        });
        answer_b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    profile.getLearnList().add("listening");
                }else{
                    profile.getLearnList().remove("listening");
                }
            }
        });
        answer_c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    profile.getLearnList().add("speaking");
                }else{
                    profile.getLearnList().remove("speaking");
                }
            }
        });
        answer_d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    profile.getLearnList().add("pronounce");
                }else{
                    profile.getLearnList().remove("pronounce");
                }
            }
        });
        answer_e.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    profile.getLearnList().add("vocabulary");
                }else{
                    profile.getLearnList().remove("vocabulary");
                }
            }
        });
        answer_f.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    profile.getLearnList().add("grammar");
                }else{
                    profile.getLearnList().remove("grammar");
                }
            }
        });
        answer_g.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    profile.getLearnList().add("writting");
                }else{
                    profile.getLearnList().remove("writting");
                }
            }
        });

        TextView nextButton = (TextView) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answer_a.isChecked() || answer_b.isChecked() || answer_b.isChecked() || answer_c.isChecked()
                        || answer_d.isChecked() || answer_e.isChecked() || answer_f.isChecked() || answer_g.isChecked()){
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().setCustomAnimations(
                            R.anim.slide_in_right, R.anim.slide_out_left,
                            R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.fragment, ResultFragment.newInstance(profile))
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


}
