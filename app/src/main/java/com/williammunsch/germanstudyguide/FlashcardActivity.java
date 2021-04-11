package com.williammunsch.germanstudyguide;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.view.Menu;
import android.view.MenuItem;

import com.williammunsch.germanstudyguide.databinding.ActivityLearnBinding;
import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;
import com.williammunsch.germanstudyguide.activitiesviewmodels.FlashcardViewModel;
import com.williammunsch.germanstudyguide.viewmodelhelpers.ViewModelFactory;

import java.util.List;

import javax.inject.Inject;


/**
 * Activity for the vocabulary flashcards.
 *
 * The queue for the flashcards will need to be stored in the repository,
 * and observed from a view model.  (notifydatasetchanged)?
 * After testing a flashcard, the entity's value in the database will be
 * changed directly, and observed by the view model.
 * Queue item removal and move will be called from the activity -> viewmodel -> repository
 */
public class FlashcardActivity extends AppCompatActivity {

    @Inject ViewModelFactory viewModelFactory;

    FlashcardViewModel flashcardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLearnBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_learn);
        binding.setLifecycleOwner(this);

        setSupportActionBar(binding.toolbar);
        ((GermanApp) getApplicationContext()).getAppComponent().inject(this);
        flashcardViewModel =  new ViewModelProvider(this,viewModelFactory).get(FlashcardViewModel.class);
        binding.setFlashcardviewmodel(flashcardViewModel);

       // Intent bIntent = getIntent();



        flashcardViewModel.getNavigateToMainActivity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                finish();
            }
        });

        flashcardViewModel.getMediatorVocabList().observe(this, new Observer<List<VocabModelA1>>() {
            @Override
            public void onChanged(List<VocabModelA1> vocabModelA1s) {
                if (vocabModelA1s != null) {
                    for (int i = 0; i < vocabModelA1s.size(); i++){
                       // System.out.println(vocabModelA1s.get(i).getId() + " " + vocabModelA1s.get(i).getGerman() + " " + vocabModelA1s.get(i).getScore() + " " + vocabModelA1s.get(i).getGsentence());// + " " + vocabModelA1s.get(i).getScore()+" " + vocabModelA1s.get(i).getStudying());

                    }
                }
            }
        });


    }

    /*
    private void delayButtonCheck(){
        buttonCheck.setEnabled(false);
        new CountDownTimer(2100, 10) { //Set Timer for 5 seconds
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                buttonCheck.setEnabled(true);
            }
        }.start();
    }


    private void fadeIn(View view, int time){
        view.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null)
                .setStartDelay(time);
    }

    //Unused useful methods
    private void fadeOut(View view){
        view.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(null)
                .setStartDelay(0);
    }
    private void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(entryText.getWindowToken(), 0);
    }

     */

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

}
