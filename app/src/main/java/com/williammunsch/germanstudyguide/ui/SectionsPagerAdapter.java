package com.williammunsch.germanstudyguide.ui;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
            return VocabFragment.newInstance(0);
        }else if (position==1){
            return StoriesFragment.newInstance(1);
        }else{
            return OptionsFragment.newInstance(2);
        }

    }


    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

}