package com.hedspi.hoangviet.eslrecom.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by hoangviet on 11/20/16.
 */

public class SettingFragment extends Fragment {
    private View view;

    public static SettingFragment newInstance(){
        SettingFragment fragment = new SettingFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_setting, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final MaterialEditText levelScore = (MaterialEditText) view.findViewById(R.id.levelScore);
        final MaterialEditText testScore = (MaterialEditText) view.findViewById(R.id.testScore);
        final MaterialEditText timeScore = (MaterialEditText) view.findViewById(R.id.timeScore);
        final MaterialEditText learnPreferenceScore = (MaterialEditText) view.findViewById(R.id.preferenceScore);
        final MaterialEditText decisionBoundary = (MaterialEditText) view.findViewById(R.id.decisionScore);

        final Preference preference = DatabaseManager.getPreference();
        levelScore.setText(""+preference.getOverallScore());
        timeScore.setText(""+preference.getTimeScore());
        testScore.setText(""+preference.getTestScore());
        learnPreferenceScore.setText(""+preference.getLearnPreferenceScore());
        decisionBoundary.setText(""+preference.getDecisionBoundary());

        Button saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String levelScoreStr = levelScore.getText().toString();
                String timeScoreStr = timeScore.getText().toString();
                String testScoreStr = testScore.getText().toString();
                String learnPreferenceScoreStr = learnPreferenceScore.getText().toString();
                String decisionBoundaryStr = decisionBoundary.getText().toString();
                if(levelScoreStr.matches("0\\.[0-9]+") &&
                        timeScoreStr.matches("0\\.[0-9]+") &&
                        testScoreStr.matches("0\\.[0-9]+") &&
                        learnPreferenceScoreStr.matches("0\\.[0-9]+") &&
                        decisionBoundaryStr.matches("0\\.[0-9]+")){
                    preference.setOverallScore(Double.parseDouble(levelScoreStr));
                    preference.setTimeScore(Double.parseDouble(timeScoreStr));
                    preference.setTestScore(Double.parseDouble(testScoreStr));
                    preference.setLearnPreferenceScore(Double.parseDouble(learnPreferenceScoreStr));
                    preference.setDecisionBoundary(Double.parseDouble(decisionBoundaryStr));

                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    database.child("preference").setValue(preference);

                    Toast.makeText(getActivity(), getResources().getString(R.string.updated), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


}
