package com.williammunsch.germanstudyguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.williammunsch.germanstudyguide.datamodels.Word;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FlashcardActivity extends AppCompatActivity {

    private TextView answerWord, topTestWord, englishSentence, germanSentence, correctAnswer;
    private EditText entryText;
    private Button buttonCheck, buttonHint,buttoniwasright;
    private ImageView checkmark, xmark;
    private LinearLayout correctLayout;
    private Word head, tail;
    DBManager dbManager;
    private ArrayList<Word> wordList;
    private static final int newWords = 5; //use preferences to set this in options
    private String tableName;
    private int nodeCount;
    private boolean testing = true, test=false, finishedWithAll = false, learnedAll = false;
    private int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        //dbManager = new DBManager(this);
        Intent bIntent = getIntent();
        tableName =bIntent.getStringExtra("table");
        bindViews();
        shortAnimationDuration = 500;

        head = new Word(0,0,0,0,null,null,null,null);
        tail = new Word(0,0,0,0,null,null,null,null);
        createQueue();

        nextTest();
    }

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



    private void createQueue(){
        if (dbManager.getWordsLearned(tableName) < dbManager.getWordsMax(tableName)){
            setUpQueueType0();
        }
        else if (dbManager.getWordsLearned(tableName) >= dbManager.getWordsMax(tableName) && dbManager.getWordsMastered(tableName) < dbManager.getWordsMax(tableName)){
            setUpQueueType1();
        }
        else {
            setUpQueueType2();
        }
        tail.setNext(null);
    }
    private void setUpQueueType0(){
        wordList = dbManager.getWordList(tableName);
        nodeCount = wordList.size();
        head.setNext(wordList.get(0));
        tail = head.getNext();
        //Set up the intro flash cards
        for (int i = 1; i < newWords; i++){
            tail.setNext(wordList.get(i));
            tail=tail.getNext();
        }
        //Set up 15 cards after the new ones
        for (int i = newWords; i < wordList.size();i++){
            if (wordList.get(i).getScore()>0){wordList.get(i).setType(2);}
            else{wordList.get(i).setType(1);}
            tail.setNext(wordList.get(i));
            tail=tail.getNext();
        }
    }
    private void setUpQueueType1(){
        learnedAll = true;
        wordList = dbManager.getWordListAllLearned(tableName);
        nodeCount = wordList.size();
        head.setNext(wordList.get(0));
        tail = head.getNext();
        //Set up 20 cards to review based on score and frequency
        for (int i = 0; i < wordList.size();i++){
            if (wordList.get(i).getScore()>0){wordList.get(i).setType(2);}
            else{wordList.get(i).setType(1);}
            tail.setNext(wordList.get(i));
            tail=tail.getNext();
        }
    }
    private void setUpQueueType2(){
        learnedAll = true;
        wordList = dbManager.getWordListAllMastered(tableName);
        nodeCount = wordList.size();
        head.setNext(wordList.get(0));
        tail = head.getNext();
        //Set up randomized review
        for (int i = 0; i < wordList.size();i++){
            if (wordList.get(i).getScore()>0){wordList.get(i).setType(2);}
            else{wordList.get(i).setType(1);}
            tail.setNext(wordList.get(i));
            tail=tail.getNext();
        }
    }


    private void nextTest(){
        testing = true;
        System.out.println("NODE COUNT" + nodeCount);
        System.out.println("*********printing queue************");
        Word temp1 = head.getNext();
        while (temp1!=null){
            System.out.println(temp1.getGerman() + " " + temp1.getType() + "  score: " + temp1.getScore());
            temp1 = temp1.getNext();
        }
        try{
            setUpNextWord();
            if (head.getNext().getType()==0){
                setUpType0();
            }else if (head.getNext().getType()==1){
                setUpType1();
            }else if (head.getNext().getType()==2){
                setUpType2();
            }
        }catch(Exception e){
            backToMainActivity();
        }
    }
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



    private void nextWord(){
         updateWord(10);
         removeNode();
         if (!finishedWithAll){nextTest();}
    }

    private void testAnswer1(){
        test = false;
        String enteredAnswer = entryText.getText().toString();

        //test entered answer against all possible answers in database
        for (String s : head.getNext().getEnglishStringsArray()){
            if (s.equalsIgnoreCase(enteredAnswer)){
                test = true;
            }
        }

        if (test && !enteredAnswer.equals("")) {
            setUpCorrectAnswerViews();
        } else if (!test && !enteredAnswer.equals("")) {
            setUpIncorrectType1AnswerViews();
        }else{
            //Do nothing because the edit text is empty. Prevents misclicks.
        }
    }
    private void testAnswer2(){
        test = false;
        String enteredAnswer = entryText.getText().toString();
        if (enteredAnswer.equalsIgnoreCase(head.getNext().getGerman())) {
            test = true;
        }

        if (test && !enteredAnswer.equals("")) {
            setUpCorrectAnswerViews();
        } else if (!test && !enteredAnswer.equals("")) {
            setUpIncorrectType1AnswerViews();
        }else{
            //Do nothing because the edit text is empty. Prevents misclicks.
        }
    }

    private void setStudying(){
        if (head.getNext().getStudying() != 1) {
            nowStudying();
        }
    }



    private void test(){
        if (finishedWithAll){backToMainActivity();}
        else{
            //test the correctness on first button press
            if (testing) {
                if (head.getNext().getType() == 0) {
                    head.getNext().setType(1);
                    moveNode(false);
                    nextTest();

                } else if (head.getNext().getType() == 1) {
                    setStudying();
                    testAnswer1();

                } else if (head.getNext().getType() == 2) {
                    testAnswer2();
                }
            }
            //After showing whether the answer was correct or not, move to next word
            else{
                //If answer was correct
                if (test){
                    nextWord();
                }
                //If answer was wrong
                else{
                    moveNode(true);
                    nextTest();
                }
            }

        }

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


    public void updateWord(int s){
        head.getNext().setScore(head.getNext().getScore()+s);
        dbManager.updateWord(head.getNext(),tableName);
    }

    private void nowStudying(){
        dbManager.setStudying(head.getNext(),tableName);
    }


    private void removeNode(){
        try {
            head.setNext(head.getNext().getNext());
            nodeCount--;
            if (nodeCount == 0){
                System.out.println("finished");
                    topTestWord.setText("Finished!");
                    topTestWord.setAlpha(0f);
                    fadeIn(topTestWord,0);

                    if (!learnedAll){
                        int numWordsLearned = dbManager.getWordsLearned(tableName);
                        int totalWords = dbManager.getWordsMax(tableName);
                        double percent = ((double)numWordsLearned/totalWords)*100;
                        DecimalFormat df = new DecimalFormat("#.##");
                        answerWord.setText(df.format(percent)+ "% of " + tableName + " words learned.");
                    }else{
                        int numWordsMastered = dbManager.getWordsMastered(tableName);
                        int totalWords = dbManager.getWordsMax(tableName);
                        double percent = ((double)numWordsMastered/totalWords)*100;
                        DecimalFormat df = new DecimalFormat("#.##");
                        answerWord.setText(df.format(percent)+ "% of " + tableName + " words mastered.");
                    }

                    answerWord.setVisibility(View.VISIBLE);
                    answerWord.setAlpha(0f);
                    fadeIn(answerWord,0);
                    finishedWithAll = true;
                    buttonHint.setVisibility(View.INVISIBLE);
                    englishSentence.setVisibility(View.INVISIBLE);
                    germanSentence.setVisibility(View.INVISIBLE);
                    buttoniwasright.setVisibility(View.GONE);
                    correctLayout.setVisibility(View.GONE);
                    xmark.setVisibility(View.INVISIBLE);
                    checkmark.setVisibility(View.INVISIBLE);
                    entryText.setVisibility(View.INVISIBLE);
                    buttonCheck.setText("Done");
                }
        }catch(Exception e){
            System.out.println("removeNode failed.");
            //backToMainActivity();
        }

    }

    //Moves the current first node in the queue either 5 down or to the end.
    private void moveNode(boolean b){
        if (b) {updateWord(-7);}
        entryText.setText("");
        Word temp = head.getNext();
        if (nodeCount > 5){
            head.setNext(head.getNext().getNext());
            temp.setNext(head.getNext().getNext().getNext().getNext().getNext());
            head.getNext().getNext().getNext().getNext().setNext(temp);
        }
        else if (nodeCount == 5){
            head.setNext(head.getNext().getNext());
            temp.setNext(null);//temp.setNext(head.getNext().getNext().getNext().getNext().getNext());
            head.getNext().getNext().getNext().getNext().setNext(temp);
        }
        else if (nodeCount == 4){
            head.setNext(head.getNext().getNext());
            temp.setNext(null);//temp.setNext(head.getNext().getNext().getNext().getNext());
            head.getNext().getNext().getNext().setNext(temp);
        }
        else if (nodeCount == 3){
            head.setNext(head.getNext().getNext());
            temp.setNext(null);//temp.setNext(head.getNext().getNext().getNext());
            head.getNext().getNext().setNext(temp);
        }else if (nodeCount == 2){
            head.setNext(head.getNext().getNext());
            temp.setNext(null);
            head.getNext().setNext(temp);
        }
        else{
            //Do nothing because its the last node.
        }

System.out.println("*******************");
        Word temp1 = head.getNext();
        while (temp1!=null){
            System.out.println(temp1.getGerman());
            temp1 = temp1.getNext();
        }
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
