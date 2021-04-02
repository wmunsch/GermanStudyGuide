package com.williammunsch.germanstudyguide.ui;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.williammunsch.germanstudyguide.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    // tab titles
    private String[] tabTitles = new String[]{"\nVocabulary", "\nGrammar", "\nStories"};
    private int[] imageResId = {
            R.drawable.ic_vocab,
            R.drawable.ic_settings,
            R.drawable.ic_vocab
    };


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    // overriding getPageTitle()
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a VocabFragment (defined as a static inner class below).
        if (position==0){
            return VocabFragment.newInstance(0);
        }else if (position==1){
            return GrammarFragment.newInstance(1);
        }else{
            return StoriesFragment.newInstance(2);
        }

    }


    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

}