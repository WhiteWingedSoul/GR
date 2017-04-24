package com.hedspi.hoangviet.eslrecom.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.hedspi.hoangviet.eslrecom.fragments.DateStartLearningFragment;
import com.hedspi.hoangviet.eslrecom.fragments.ResultImageFragment;
import com.hedspi.hoangviet.eslrecom.fragments.TestFragment;
import com.hedspi.hoangviet.eslrecom.models.AdapterItem;
import com.hedspi.hoangviet.eslrecom.models.MatchResult;
import com.hedspi.hoangviet.eslrecom.models.Question;

import java.util.List;

public class ResultViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<AdapterItem> mItems;

    public ResultViewPagerAdapter(FragmentManager fragmentManager, List<AdapterItem> items) {
        super(fragmentManager);
        mItems = items;
    }

    public void addData(AdapterItem item){
        mItems.set(mItems.size()-1, item);
        mItems.add(new AdapterItem() {});

        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Yet another bug in FragmentStatePagerAdapter that destroyItem is called on fragment that hasnt been added. Need to catch
        try {
            super.destroyItem(container, position, object);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Fragment getItem(int position) {
        AdapterItem adapterItem = mItems.get(position);

        if (adapterItem instanceof MatchResult) {
            MatchResult result = (MatchResult) mItems.get(position);

            ResultImageFragment fragment = ResultImageFragment.newInstance(result.getMaterial().getCoverLink());

            return fragment;
        }else{
            return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }
}