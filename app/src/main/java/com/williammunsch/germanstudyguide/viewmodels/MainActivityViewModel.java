package com.williammunsch.germanstudyguide.viewmodels;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.databinding.BindingAdapter;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.repositories.Repository;
import com.williammunsch.germanstudyguide.ui.SectionsPagerAdapter;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {
    private Repository mRepository;//injected
    public SectionsPagerAdapter pageAdapter;
    //public ViewPager viewPager;
    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    public ViewPager.OnPageChangeListener onPageChangeListener;


    //public BottomNavigationView navView;

    @Inject
    public MainActivityViewModel(Repository repository){
        this.mRepository = repository;


    }

    public void openLoginScreen(DrawerLayout mDrawer){
        mDrawer.openDrawer(GravityCompat.START);
        System.out.println("Pressed user icon button");
    }
}
