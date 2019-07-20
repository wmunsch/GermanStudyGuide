package com.williammunsch.germanstudyguide;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class LearnActivity extends AppCompatActivity {

    private TextView answerWord, topTestWord, englishSentence, germanSentence, correctAnswer;
    private EditText entryText;
    private Button buttonCheck, buttonHint,buttoniwasright;
    private ImageView checkmark, xmark;
    private LinearLayout correctLayout;
    private Word head, tail;
    DBManager dbManager;
    private ArrayList<Word> wordList;
    private AlertDialog  wrongAnswer, englishSentenceAlert;
    private static final int newWords = 5; //use preferences to set this in options
    private String tableName;
    private int nodeCount;
    private boolean testing = true, test=false;
    private int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        dbManager = new DBManager(this);
        Intent bIntent = getIntent();
        tableName =bIntent.getStringExtra("table");
        bindViews();
        createAlertDialogues();
        wordList = dbManager.getWordList(tableName);
        nodeCount = wordList.size();
        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);


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
                germanSentence.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
            }
        });
        buttoniwasright = findViewById(R.id.button_iwasright);
    }
    private void createAlertDialogues(){
        //Create correct alert window
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.alert_window);

        TextView title = new TextView(this);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        builder.setCustomTitle(title);

        title.setText("Wrong!");
        wrongAnswer=builder.create();
        wrongAnswer.setCanceledOnTouchOutside(false);

        builder.setView(R.layout.alert_correct);
        TextView title2 = new TextView(this);
        title2.setPadding(10, 10, 10, 10);
        title2.setGravity(Gravity.CENTER);
        title2.setTextColor(Color.BLACK);
        title2.setTextSize(20);
        builder.setCustomTitle(title2);
        title2.setText("English Translation:");
        englishSentenceAlert=builder.create();
        englishSentenceAlert.setCanceledOnTouchOutside(false);

        //sentence.setVisibility(View.INVISIBLE);
        //reveal.setVisibility(View.INVISIBLE);
    }

    private void createQueue(){
        head.setNext(wordList.get(0));
        tail = head.getNext();System.out.println(tail.getGerman());
        for (int i = 1; i < newWords; i++){
            tail.setNext(wordList.get(i));
            tail=tail.getNext();
            System.out.println(tail.getGerman());
        }
        for (int i = newWords; i < wordList.size();i++){
            if (wordList.get(i).getScore()>0){wordList.get(i).setType(2);}
            else{wordList.get(i).setType(1);}
            tail.setNext(wordList.get(i));
            tail=tail.getNext();
            System.out.println(tail.getGerman());
        }
        tail.setNext(null);
    }
    private void nextTest(){
        testing = true;
        System.out.println("NODE COUNT" + nodeCount);
        System.out.println("*********printing queue************");
        Word temp1 = head.getNext();
        while (temp1!=null){
            System.out.println(temp1.getGerman() + " " + temp1.getType());
            temp1 = temp1.getNext();
        }

        try{
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
            if (head.getNext().getType()==0){
                topTestWord.setText(head.getNext().getGerman());
                answerWord.setText(head.getNext().getEnglish());
                answerWord.setVisibility(View.VISIBLE);
                entryText.setVisibility(View.INVISIBLE);
                buttonHint.setVisibility(View.INVISIBLE);
                germanSentence.setVisibility(View.VISIBLE);
                englishSentence.setVisibility(View.VISIBLE);
                buttonCheck.setText("Next");
                entryText.setText("");
            }else if (head.getNext().getType()==1){
                topTestWord.setText(head.getNext().getGerman());
                answerWord.setVisibility(View.INVISIBLE);
                entryText.setVisibility(View.VISIBLE);
                buttonCheck.setText("Check");
                entryText.setText("");
            }else if (head.getNext().getType()==2){
                topTestWord.setText(head.getNext().getEnglish());
                answerWord.setVisibility(View.INVISIBLE);
                entryText.setVisibility(View.VISIBLE);
                buttonCheck.setText("Check");
                entryText.setText("");
            }
        }catch(Exception e){
            backToMainActivity();
        }


    }

    private void nextWord(){
         updateWord(10);
         removeNode();
         nextTest();
    }

    private void test(){
        //test the correctness on first button press
        if (testing) {
            if (head.getNext().getType() == 0) {
                head.getNext().setType(1);
                moveNode(false);
                nextTest();
            } else if (head.getNext().getType() == 1) {
                if (head.getNext().getStudying() != 1) {
                    nowStudying();
                }

                //test
                test = false;
                String s = entryText.getText().toString();
                if (s.equalsIgnoreCase(head.getNext().getEnglish())) {
                    test = true;
                }

                if (test && !s.equals("")) {
                    setUpCorrectAnswerViews();
                } else if (!test && !s.equals("")) {
                    setUpIncorrectType1AnswerViews();
                }else{
                    //Do nothing because the edit text is empty. Prevents misclicks.
                }

            } else if (head.getNext().getType() == 2) {

                //test
                test = false;
                String s = entryText.getText().toString();
                if (s.equalsIgnoreCase(head.getNext().getGerman())) {
                    test = true;
                }


                if (test) {
                    checkmark.setVisibility(View.VISIBLE);
                    checkmark.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
                    buttonCheck.setText("Next");
                    buttonHint.setVisibility(View.INVISIBLE);
                    if(germanSentence.getVisibility()!=View.VISIBLE){germanSentence.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));}
                    germanSentence.setVisibility(View.VISIBLE);
                    englishSentence.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
                    englishSentence.setVisibility(View.VISIBLE);
                    testing=false;
                } else {
                    correctLayout.setAlpha(0f);
                    correctLayout.setVisibility(View.VISIBLE);
                    correctAnswer.setText(head.getNext().getGerman());
                    correctLayout.animate()
                            .alpha(1f)
                            .setDuration(shortAnimationDuration)
                            .setListener(null);

                    buttonCheck.setText("Next");
                    xmark.setVisibility(View.VISIBLE);
                    xmark.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
                    buttonHint.setVisibility(View.INVISIBLE);
                    if(germanSentence.getVisibility()!=View.VISIBLE){germanSentence.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));}
                    germanSentence.setVisibility(View.VISIBLE);
                    englishSentence.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
                    englishSentence.setVisibility(View.VISIBLE);
                    testing=false;
                }
            }
        }
        //After showing whether the answer was correct or not, move to next activity
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
    private void setUpCorrectAnswerViews(){
        checkmark.setVisibility(View.VISIBLE);
        checkmark.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        buttonCheck.setText("Next");
        buttonHint.setVisibility(View.INVISIBLE);
        if(germanSentence.getVisibility()!=View.VISIBLE){germanSentence.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));}
        germanSentence.setVisibility(View.VISIBLE);
        englishSentence.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        englishSentence.setVisibility(View.VISIBLE);
        testing=false;
    }

    private void setUpIncorrectType1AnswerViews(){
        correctLayout.setAlpha(0f);
        correctLayout.setVisibility(View.VISIBLE);
        correctAnswer.setText(head.getNext().getEnglish());
        correctLayout.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        buttonCheck.setText("Next");
        xmark.setVisibility(View.VISIBLE);
        xmark.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        buttonHint.setVisibility(View.INVISIBLE);
        if(germanSentence.getVisibility()!=View.VISIBLE){germanSentence.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));}
        germanSentence.setVisibility(View.VISIBLE);
        englishSentence.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        englishSentence.setVisibility(View.VISIBLE);
        buttoniwasright.setVisibility(View.VISIBLE);
        testing=false;
    }

    private void showEnglishSentence(){
        testing = false;
        englishSentence.setVisibility(View.VISIBLE);
        englishSentence.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        englishSentence.setText(head.getNext().getEsentence());
        buttonHint.setVisibility(View.INVISIBLE);
        germanSentence.setVisibility(View.VISIBLE);
    }

    public void wasCorrect(View view){
        updateWord(10);
        wrongAnswer.dismiss();
        removeNode();
        nextTest();
    }

    public void unloadAlert(View view){
        wrongAnswer.dismiss();
        moveNode(true);
        nextTest();
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
            if (nodeCount == 0){System.out.println("GOING BACK TO MAIN)");backToMainActivity();}
        }catch(Exception e){
            System.out.println("removeNode failed.");
            backToMainActivity();
        }

    }

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
            temp.setNext(head.getNext().getNext().getNext().getNext());
            head.getNext().getNext().getNext().setNext(temp);
        }
        else if (nodeCount == 4){
            head.setNext(head.getNext().getNext());
            temp.setNext(head.getNext().getNext().getNext());
            head.getNext().getNext().setNext(temp);
        }else if (nodeCount == 3){
            head.setNext(head.getNext().getNext());
            temp.setNext(null);
            head.getNext().getNext().setNext(temp);
        }
        else if (nodeCount == 2){
            head.setNext(head.getNext().getNext());
            temp.setNext(null);
            head.getNext().setNext(temp);
        }else{

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
