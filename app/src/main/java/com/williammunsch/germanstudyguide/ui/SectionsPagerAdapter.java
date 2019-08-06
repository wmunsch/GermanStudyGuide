package com.williammunsch.germanstudyguide.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.williammunsch.germanstudyguide.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a VocabFragment (defined as a static inner class below).
        if (position==0){
            return VocabFragment.newInstance(position + 1);
        }else{
            return StoriesFragment.newInstance(position + 1);
        }

    }


    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

}