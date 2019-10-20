package com.williammunsch.germanstudyguide;

import androidx.annotation.NonNull;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.williammunsch.germanstudyguide.databinding.ActivityMainBinding;
import com.williammunsch.germanstudyguide.ui.SectionsPagerAdapter;
import com.williammunsch.germanstudyguide.viewmodels.MainActivityViewModel;
import com.williammunsch.germanstudyguide.viewmodels.ViewModelFactory;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        setSupportActionBar(binding.toolbar);
        ((GermanApp) getApplicationContext()).getAppComponent().inject(this);

        mainActivityViewModel = ViewModelProviders.of(this,viewModelFactory).get(MainActivityViewModel.class);
        binding.setMainactivityviewmodel(mainActivityViewModel);
        binding.viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        binding.loginClickable.setOnClickListener((View view)->
                binding.drawerLayout.openDrawer(GravityCompat.START));



       // FragmentLoginBinding fragmentLoginBinding = DataBindingUtil.setContentView(this,R.layout.fragment_login);
       // FragmentLoginBinding _bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_login, binding.navigationView, false);
       // binding.navigationView.addHeaderView(_bind.getRoot());
     //   _bind.setUser(Session.getUserProfile());

        /*
        binding.navigationView.getHeaderView(0).findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("login button pressed");
            }
        });

         */

        //Allows the bottom navigation bar to change viewPager items.
        binding.navView.setOnNavigationItemSelectedListener((@NonNull MenuItem item) ->{
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    binding.viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    binding.viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    binding.viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        });

        //Allows the viewPager swipe to change the bottom navigation bar.
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {
                binding.navView.getMenu().getItem(i).setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int i) { }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
