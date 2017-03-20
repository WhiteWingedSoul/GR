package com.hedspi.hoangviet.eslrecom.libraries;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.models.Tag;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;

/**
 * Created by viet on 3/18/17.
 */

public class TagCompletionView extends TokenCompleteTextView<String> {
    public TagCompletionView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(String tag) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.tag_token, (ViewGroup) getParent(), false);
        view.setText(tag);

        return view;
    }

    @Override
    protected String defaultObject(String completionText) {
        return completionText;
    }
}
