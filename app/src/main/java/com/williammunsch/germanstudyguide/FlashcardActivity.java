package com.williammunsch.germanstudyguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.williammunsch.germanstudyguide.databinding.ActivityLearnBinding;
import com.williammunsch.germanstudyguide.datamodels.VocabModel;
import com.williammunsch.germanstudyguide.datamodels.Word;
import com.williammunsch.germanstudyguide.viewmodels.FlashcardViewModel;
import com.williammunsch.germanstudyguide.viewmodels.ViewModelFactory;
import com.williammunsch.germanstudyguide.viewmodels.VocabListViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;


/**
 * The queue for the flashcards will need to be stored in the repository,
 * and observed from a view model.  (notifydatasetchanged)?
 * After testing a flashcard, the entity's value in the database will be
 * changed directly, and observed by the view model.
 * Queue item removal and move will be called from the activity -> viewmodel -> repository
 * All of the findviewbyid stuff needs to be replaced with databinding.
 */
public class FlashcardActivity extends AppCompatActivity {

    private TextView answerWord, topTestWord, englishSentence, germanSentence, correctAnswer;
    private EditText entryText;
    private Button buttonCheck, buttonHint,buttoniwasright;
    private ImageView checkmark, xmark;
    private LinearLayout correctLayout;
    private Word head, tail;
    private ArrayList<Word> wordList;
    private static final int newWords = 5; //use preferences to set this in options
    private String tableName;
    private int nodeCount;
    private boolean testing = true, test=false, finishedWithAll = false, learnedAll = false;
    private int shortAnimationDuration;


    @Inject ViewModelFactory viewModelFactory;


    FlashcardViewModel flashcardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLearnBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_learn);
        binding.setLifecycleOwner(this);

       // Toolbar myToolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(myToolbar);
        setSupportActionBar(binding.toolbar);

        ((GermanApp) getApplicationContext()).getAppComponent().inject(this);

        flashcardViewModel = ViewModelProviders.of(this,viewModelFactory).get(FlashcardViewModel.class);

        binding.setFlashcardviewmodel(flashcardViewModel);

        Intent bIntent = getIntent();
        tableName =bIntent.getStringExtra("table");


        shortAnimationDuration = 500;


        flashcardViewModel.getNavigateToMainActivity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                finish();
            }
        });

        flashcardViewModel.getMediatorVocabList().observe(this, new Observer<List<VocabModel>>() {
            @Override
            public void onChanged(List<VocabModel> vocabModels) {
                if (vocabModels != null) {
                    for (int i = 0 ; i < vocabModels.size();i++){
                        System.out.println(vocabModels.get(i).getId() + " " + vocabModels.get(i) + " " + vocabModels.get(i).getScore()+" " + vocabModels.get(i).getStudying());

                    }
                }
            }
        });
    }

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


    public void backToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtra("flashcard",maxId);
        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings){
            // startSettings();
            return true;
        }
        return true;
    }

}
