package com.williammunsch.germanstudyguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
        setContentView(R.layout.activity_learn);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ((GermanApp) getApplicationContext()).getAppComponent().inject(this);

        if (viewModelFactory == null){System.out.println("NULL FACTORY IN FLASHCARDACTIVITY");}
        else{ System.out.println("NONNULL FACTORY IN FLASHCARDACTIVITY"); }
        flashcardViewModel = ViewModelProviders.of(this,viewModelFactory).get(FlashcardViewModel.class);


        //dbManager = new DBManager(this);
        Intent bIntent = getIntent();
        tableName =bIntent.getStringExtra("table");
        //bindViews();
        topTestWord = findViewById(R.id.textView_germanWord);

        buttonHint = findViewById(R.id.button_seeSentence);
        buttonHint.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("PRESSED BUTTON");
               // flashcardViewModel.removeFlashcard(0);

                //can put observe in onclicks!
                flashcardViewModel.popNode();


            }
        });


        shortAnimationDuration = 500;




        flashcardViewModel.getMediatorVocabList().observe(this, new Observer<List<VocabModel>>() {
            @Override
            public void onChanged(List<VocabModel> vocabModels) {
                System.out.println("PRINTING MODELLIST IN ACTIVITY   MEDIATOR ");
                if (vocabModels != null) {
                    for (int i = 0 ; i < vocabModels.size();i++){
                        System.out.println(vocabModels.get(i));

                    }
                    topTestWord.setText(vocabModels.get(0).toString());
                   // topTestWord.setText(vocabModels.get(0).toString());
                }
            }
        });


/*
        flashcardViewModel.getVocabList().observe(this, new Observer<List<VocabModel>>() {
            @Override
            public void onChanged(List<VocabModel> vocabModels) {
                System.out.println("PRINTING MODELLIST IN ACTIVITY     LIVE ");
                if (vocabModels != null) {
                    for (int i = 0 ; i < vocabModels.size();i++){
                        //System.out.println(vocabModels.get(flashcardViewModel.getFlashcardOrderList().get(i)));
                        System.out.println(vocabModels.get(i));

                    }
                   // topTestWord.setText(vocabModels.get(0).toString());
                    //topTestWord.setText(vocabModels.get(0).toString());
                   // topTestWord.setText(vocabModels.get(flashcardViewModel.getFlashcardOrderList().get(0)).toString());
                }
            }
        });
*/













        /*
        flashcardViewModel.getModelList().observe(this, new Observer<List<VocabModel>>() {
            @Override
            public void onChanged(List<VocabModel> vocabModels) {
                System.out.println("PRINTING MODELLIST IN ACTIVITY");
                if (vocabModels != null) {
                    for (int i = 0 ; i < vocabModels.size();i++){
                        System.out.println(vocabModels.get(i));
                    }
                    topTestWord.setText(vocabModels.get(0).toString());
                }


            }
        });







        /*
        flashcardViewModel.getWordQueue().observe(this, new Observer<Queue<VocabModel>>() {
            @Override
            public void onChanged(Queue<VocabModel> vocabModels) {
                System.out.println(vocabModels.peek());
            }
        });
*/


    }







    //REPLACE ALL OF THIS WITH DATABINDING
    /*
    private void bindViews(){
        topTestWord = findViewById(R.id.textView_germanWord);
        answerWord = findViewById(R.id.textView_englishWord);
        englishSentence = findViewById(R.id.textView_englishSentence);
        germanSentence = findViewById(R.id.textView_germanSentence);
        entryText = findViewById(R.id.editText_entry);
        entryText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled =false;
                if (i == EditorInfo.IME_ACTION_DONE){
                    test();
                    handled = true;
                }
                return handled;
            }
        });

        checkmark=findViewById(R.id.imageView_checkmark);
        xmark=findViewById(R.id.imageView_xmark);
        correctLayout=findViewById(R.id.linearLayout_correct);
        correctAnswer=findViewById(R.id.textView_correctWord);
        buttonCheck = findViewById(R.id.button_next);
        buttonCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                test();

            }
        });


        buttonHint = findViewById(R.id.button_seeSentence);
        buttonHint.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                germanSentence.setVisibility(View.VISIBLE);
                buttonHint.setVisibility(View.INVISIBLE);
                fadeIn(germanSentence,0);
            }
        });
        buttoniwasright = findViewById(R.id.button_iwasright);
        buttoniwasright.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                test=true;
                test();
            }
        });
    }
*/




    //These need to be in the viewmodel?   can i use databinding to replace these?
    private void setUpNextWord(){
        englishSentence.setVisibility(View.INVISIBLE);
        germanSentence.setVisibility(View.INVISIBLE);
        buttonHint.setVisibility(View.VISIBLE);
        germanSentence.setText(head.getNext().getGSentence());
        englishSentence.setText(head.getNext().getEsentence());
        buttoniwasright.setVisibility(View.GONE);
        xmark.setVisibility(View.INVISIBLE);
        checkmark.setVisibility(View.INVISIBLE);
        correctLayout.setVisibility(View.GONE);
        buttonCheck.setText("Check");
        topTestWord.setAlpha(0f);
        answerWord.setAlpha(0f);
        germanSentence.setAlpha(0f);
        englishSentence.setAlpha(0f);
        entryText.setAlpha(0f);
        buttonHint.setAlpha(0f);
        buttonCheck.setAlpha(0f);
    }
    private void setUpType0(){
        topTestWord.setText(head.getNext().getGerman());
        answerWord.setText(head.getNext().getEnglish());
        answerWord.setVisibility(View.VISIBLE);
        entryText.setVisibility(View.INVISIBLE);
        buttonHint.setVisibility(View.INVISIBLE);
        germanSentence.setVisibility(View.VISIBLE);
        englishSentence.setVisibility(View.VISIBLE);
        delayButtonCheck();
        fadeIn(buttonCheck,2100);
        fadeIn(topTestWord,100);
        fadeIn(answerWord,1100);
        fadeIn(germanSentence,2100);
        fadeIn(englishSentence,2100);
        buttonCheck.setText("Next");
        entryText.setText("");
    }
    private void setUpType1(){
        topTestWord.setText(head.getNext().getGerman());
        answerWord.setVisibility(View.INVISIBLE);
        entryText.setVisibility(View.VISIBLE);
        buttonCheck.setText("Check");
        entryText.setText("");
        fadeIn(topTestWord,50);
        fadeIn(entryText,50);
        fadeIn(buttonHint,50);
        fadeIn(buttonCheck,50);
    }
    private void setUpType2(){
        topTestWord.setText(head.getNext().getEnglish());
        englishSentence.setText(head.getNext().getGSentence());
        germanSentence.setText(head.getNext().getEsentence());
        answerWord.setVisibility(View.INVISIBLE);
        entryText.setVisibility(View.VISIBLE);
        buttonCheck.setText("Check");
        entryText.setText("");
        fadeIn(topTestWord,50);
        fadeIn(entryText,50);
        fadeIn(buttonHint,50);
        fadeIn(buttonCheck,50);
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



    private void setUpCorrectAnswerViews(){
        germanSentence.setText(head.getNext().getGSentence());
        englishSentence.setText(head.getNext().getEsentence());
        checkmark.setVisibility(View.VISIBLE);
        checkmark.setAlpha(0f);
        fadeIn(checkmark,0);
        buttonCheck.setText("Next");
        buttonHint.setVisibility(View.INVISIBLE);
        germanSentence.setVisibility(View.VISIBLE);
        englishSentence.setVisibility(View.VISIBLE);
        fadeIn(germanSentence,0);
        fadeIn(englishSentence,0);
        testing=false;
    }

    private void setUpIncorrectType1AnswerViews(){
        xmark.setVisibility(View.VISIBLE);
        xmark.setAlpha(0f);
        correctLayout.setAlpha(0f);
        fadeIn(xmark,0);
        fadeIn(correctLayout,0);
        correctLayout.setVisibility(View.VISIBLE);
        if (head.getNext().getType()==1){
            correctAnswer.setText(head.getNext().getEnglish());
        }else{
            correctAnswer.setText(head.getNext().getGerman());
        }
        buttonCheck.setText("Next");
        buttonHint.setVisibility(View.INVISIBLE);
        germanSentence.setVisibility(View.VISIBLE);
        fadeIn(germanSentence,0);
        fadeIn(englishSentence,0);
        englishSentence.setVisibility(View.VISIBLE);
        buttoniwasright.setVisibility(View.VISIBLE);
        testing=false;
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
