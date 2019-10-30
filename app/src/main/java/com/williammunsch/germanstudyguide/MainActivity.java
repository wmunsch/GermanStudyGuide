package com.williammunsch.germanstudyguide;

import androidx.annotation.NonNull;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
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
import com.williammunsch.germanstudyguide.viewmodels.MainActivityViewModel;
import com.williammunsch.germanstudyguide.viewmodels.ViewModelFactory;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    MainActivityViewModel mainActivityViewModel;
    FragmentManager fm;

    //TODO  : slide down with animation when clicking on recyclerview list item
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

        //TODO : Close the keyboard when clicking off of the login page drawer
        fm = getSupportFragmentManager();
        mainActivityViewModel.getErrorCode().observe(this, code ->{
            if (code==1){
                LoginDialogFragment loginDialogFragment = LoginDialogFragment.newInstance("Account not found","The email you provided is not registered.\nPlease make sure you entered the\ncorrect email and try again.");
                loginDialogFragment.show(fm,"loginDialogFrament");
            }else if (code==2){
                LoginDialogFragment loginDialogFragment = LoginDialogFragment.newInstance("Incorrect password","The password you entered was incorrect.\nPlease make sure you entered the\ncorrect password and try again.");
                loginDialogFragment.show(fm,"loginDialogFrament");
                binding.etPassword.setText("");
               // binding.etPassword.setBackground(getDrawable(R.drawable.edittext_background_wrong));
                binding.etPassword.requestFocus();
            }else if (code==3){
                //login
                //TODO : Handle successful login (change fragment?)
                System.out.println("LOGGING IN");
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                closeKeyboard();
            }
        });

        mainActivityViewModel.getUserName().observe(this, name ->
                binding.textViewUsername.setText(name));


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
