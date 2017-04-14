package com.hedspi.hoangviet.eslrecom.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.hedspi.hoangviet.eslrecom.fragments.DateStartLearningFragment;
import com.hedspi.hoangviet.eslrecom.fragments.TestFragment;
import com.hedspi.hoangviet.eslrecom.models.AdapterItem;
import com.hedspi.hoangviet.eslrecom.models.Question;

import java.util.ArrayList;
import java.util.List;

public class TestViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<AdapterItem> mItems;

    public TestViewPagerAdapter(FragmentManager fragmentManager, List<AdapterItem> items) {
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

        if (adapterItem instanceof Question) {
            Question question = (Question) mItems.get(position);

            TestFragment fragmentWord = TestFragment.newInstance(question);

            return fragmentWord;
        } else /*if (mDetails.get(position) instanceof Summary)*/ {
//            Summary summary = (Summary) mDetails.get(position);
//            SummaryFragment fragmentSummary = SummaryFragment.newInstance(summary);
//
//            return fragmentSummary;

            DateStartLearningFragment fragmentDate = DateStartLearningFragment.newInstance();

            return fragmentDate;
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