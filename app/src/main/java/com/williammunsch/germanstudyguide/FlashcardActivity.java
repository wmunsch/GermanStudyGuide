package com.williammunsch.germanstudyguide;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.williammunsch.germanstudyguide.databinding.ActivityLearnBinding;
import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;
import com.williammunsch.germanstudyguide.activitiesviewmodels.FlashcardViewModel;
import com.williammunsch.germanstudyguide.viewmodelhelpers.ViewModelFactory;

import java.util.List;
import java.util.Objects;

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

        //Set the soft keyboard enter to call checkAnswer() so the user doesn't have to close the keyboard for each card
        EditText editTextEntry = (EditText) findViewById(R.id.editText_entry);
        editTextEntry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    flashcardViewModel.checkAnswer();
                    hideKeyboard();
                    handled=true;
                }
                return handled;
            }
        });




        flashcardViewModel.getNavigateToMainActivity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                finish();
            }
        });

        flashcardViewModel.getMediatorVocabList().observe(this, new Observer<List<VocabModelA1>>() {
            @Override
            public void onChanged(List<VocabModelA1> vocabModelA1s) {
                //System.out.println("Changed mediatorvocab list");
            }
        });
        flashcardViewModel.getShowKeyBoard().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    //Only show keyboard on new cards after the initial new 5
                    showKeyboard();
                    flashcardViewModel.setShowKeyBoard(false);
                }
            }
        });


    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
    }

    private void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        //EditText editTextEntry = (EditText) findViewById(R.id.editText_entry);
       // editTextEntry.requestFocus();
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

}
