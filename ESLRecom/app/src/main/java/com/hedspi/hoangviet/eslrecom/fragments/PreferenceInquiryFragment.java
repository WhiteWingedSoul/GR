package com.hedspi.hoangviet.eslrecom.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hedspi.hoangviet.eslrecom.MainActivity;
import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.libraries.TagCompletionView;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.Tag;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangviet on 11/20/16.
 */

public class PreferenceInquiryFragment extends Fragment {
    private UserProfile profile;

    public static PreferenceInquiryFragment newInstance(UserProfile profile){
        PreferenceInquiryFragment fragment = new PreferenceInquiryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("profile", profile);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profile = DatabaseManager.getUserProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey8, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TagCompletionView autoCompleteTextView = (TagCompletionView) view.findViewById(R.id.autoCompleteText);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setSplitChar(' ');
        autoCompleteTextView.setTokenLimit(5);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        List<String> tagName = new ArrayList<>();
        for(Tag tag: DatabaseManager.getTagList()){
            tagName.add(tag.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, tagName);
        autoCompleteTextView.setAdapter(adapter);

        TextView nextButton = (TextView) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.setLearnList(autoCompleteTextView.getObjects());
                DatabaseManager.setUserProfile(profile);
                ((MainActivity) getActivity()).startResultActivity(MainActivity.ADDED, new Bundle());
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction().setCustomAnimations(
//                        R.anim.slide_in_right, R.anim.slide_out_left,
//                        R.anim.slide_in_left, R.anim.slide_out_right)
//                        .replace(R.id.fragment, ResultFragment.newInstance(profile))
//                        .addToBackStack(null)
//                        .commit();
            }
        });

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


}
