package com.hedspi.hoangviet.eslrecom.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.TestActivity;
import com.hedspi.hoangviet.eslrecom.commons.Common;
import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.models.Question;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class DateStartLearningFragment extends Fragment {

    private boolean loading = false;

    public static DateStartLearningFragment newInstance() {
        DateStartLearningFragment fragment = new DateStartLearningFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learningstartdate, viewGroup, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.answerGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!loading) {
                    loading = true;
                    switch (i) {
                        case R.id.answer_a:
                            ((TestActivity) getActivity()).initInputProfile(Preference.BEGINNER);
                            break;
                        case R.id.answer_b:
                            ((TestActivity) getActivity()).initInputProfile(Preference.ELEMENTARY);
                            break;
                        case R.id.answer_c:
                            ((TestActivity) getActivity()).initInputProfile(Preference.INTERMEDIATE);
                            break;
                        case R.id.answer_d:
                            ((TestActivity) getActivity()).initInputProfile(Preference.UPPER_INTERMEDIATE);
                            break;
                        case R.id.answer_e:
                            ((TestActivity) getActivity()).initInputProfile(Preference.ADVANCE);
                            break;
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
