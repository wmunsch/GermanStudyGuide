package com.williammunsch.germanstudyguide;

import androidx.annotation.NonNull;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.williammunsch.germanstudyguide.databinding.ActivityMainBinding;
import com.williammunsch.germanstudyguide.ui.LoginDialogFragment;
import com.williammunsch.germanstudyguide.ui.SectionsPagerAdapter;
import com.williammunsch.germanstudyguide.activitiesviewmodels.MainActivityViewModel;
import com.williammunsch.germanstudyguide.viewmodelhelpers.ViewModelFactory;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    MainActivityViewModel mainActivityViewModel;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        setSupportActionBar(binding.toolbar);
        ((GermanApp) getApplicationContext()).getAppComponent().inject(this);

        mainActivityViewModel = new ViewModelProvider(this,viewModelFactory).get(MainActivityViewModel.class);
        binding.setMainactivityviewmodel(mainActivityViewModel);
        binding.viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        binding.loginClickable.setOnClickListener((View view)->
                binding.drawerLayout.openDrawer(GravityCompat.START));


        fm = getSupportFragmentManager();

        //Decide what text to display in a dialog fragment when there is an error logging in
        mainActivityViewModel.getErrorCode().observe(this, code ->{
            if (code==1){
                LoginDialogFragment loginDialogFragment = LoginDialogFragment.newInstance("Account not found","The username you provided is not registered.\nPlease make sure you entered the\ncorrect username and try again.");
                loginDialogFragment.show(fm,"loginDialogFragment");
            }else if (code==2){
                LoginDialogFragment loginDialogFragment = LoginDialogFragment.newInstance("Incorrect password","The password you entered was incorrect.\nPlease make sure you entered the\ncorrect password and try again.");
                loginDialogFragment.show(fm,"loginDialogFragment");
                binding.etPassword.setText("");
                binding.etPassword.requestFocus();
            }else if (code==3){
                //login
                binding.etPassword.setText("");
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                closeKeyboard();
            }else if (code==4){
                //registered an account, close the drawer and show verification sent popup.
                LoginDialogFragment loginDialogFragment = LoginDialogFragment.newInstance("Account Created","Welcome to your German study guide!\nGet started by studying vocabulary,\nthen test your skills by reading.");
                loginDialogFragment.show(fm,"loginDialogFragment");

                binding.etPasswordR.setText("");
                binding.etUsernameR.setText("");

                binding.drawerLayout.closeDrawer(GravityCompat.START);
                closeKeyboard();

            }else if (code==5){
                LoginDialogFragment loginDialogFragment = LoginDialogFragment.newInstance("Error connecting","Check to see if you're connected\nto the internet and try again.");
                loginDialogFragment.show(fm,"loginDialogFragment");
            }
        });


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


    public void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
