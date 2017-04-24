package com.hedspi.hoangviet.eslrecom.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.squareup.picasso.Picasso;

/**
 * Created by hoangviet on 11/20/16.
 */

public class ResultImageFragment extends Fragment {
    private View view;
    private String url;

    public static ResultImageFragment newInstance(String url){
        ResultImageFragment fragment = new ResultImageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("url", url);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_result_image, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        url = (String) getArguments().getSerializable("url");
        ImageView bookIcon = (ImageView) view.findViewById(R.id.bookIcon);
        Picasso.with(getActivity()).load(url)
                .fit()
                .into(bookIcon);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

}
