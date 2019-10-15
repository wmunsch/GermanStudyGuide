package com.williammunsch.germanstudyguide;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.williammunsch.germanstudyguide.ui.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {


    ViewPager viewPager;
    BottomNavigationView navView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO : Bind the views and toolbar

        setContentView(R.layout.activity_main);


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
           // getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch(NullPointerException e){
            throw new Error("Null icon");
        }

        navView = findViewById(R.id.nav_view);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        //viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(sectionsPagerAdapter);



        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        System.out.println("NAVIGATION_HOME 0");
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_dashboard:
                        System.out.println("NAVIGATION_dashboard 1");
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_notifications:
                        System.out.println("NAVIGATION_notifacions 2");
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        };

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                navView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        if (item.getItemId() == R.id.action_settings){
           // startSettings();
            return true;
        }

         */
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
