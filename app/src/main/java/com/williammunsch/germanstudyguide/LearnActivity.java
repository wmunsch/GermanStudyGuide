package com.williammunsch.germanstudyguide;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class LearnActivity extends AppCompatActivity {

    private TextView answerWord, topTestWord, englishSentence, germanSentence;
    private EditText entryText;
    private Button buttonNext, buttonHint;
    private Word head, tail;
    DBManager dbManager;
    private ArrayList<Word> wordList;
    private AlertDialog  wrongAnswer, englishSentenceAlert;
    private static final int newWords = 5; //use preferences to set this in options
    private String tableName;
    private int nodeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        dbManager = new DBManager(this);
        Intent bIntent = getIntent();
        tableName =bIntent.getStringExtra("table");
        bindViews();
        createAlertDialogues();
        wordList = dbManager.getWordList(tableName);
        nodeCount = wordList.size();

        head = new Word(0,0,0,null,null,null,null);
        tail = new Word(0,0,0,null,null,null,null);
        createQueue();

        nextTest();
    }

    private void bindViews(){
        topTestWord = findViewById(R.id.textView_germanWord);
        answerWord = findViewById(R.id.textView_englishWord);
        englishSentence = findViewById(R.id.textView_englishSentence);
        germanSentence = findViewById(R.id.textView_germanSentence);
        entryText = findViewById(R.id.editText_entry);
        buttonNext = findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener(){
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
            }
        });
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
            if (head.getNext().getType()==0){
                topTestWord.setText(head.getNext().getGerman());
                answerWord.setText(head.getNext().getEnglish());
                answerWord.setVisibility(View.VISIBLE);
                entryText.setVisibility(View.INVISIBLE);
                buttonHint.setVisibility(View.INVISIBLE);
                germanSentence.setVisibility(View.VISIBLE);
            }else if (head.getNext().getType()==1){
                topTestWord.setText(head.getNext().getGerman());
                answerWord.setVisibility(View.INVISIBLE);
                entryText.setVisibility(View.VISIBLE);
            }else if (head.getNext().getType()==2){
                topTestWord.setText(head.getNext().getEnglish());
                answerWord.setVisibility(View.INVISIBLE);
                entryText.setVisibility(View.VISIBLE);
            }
        }catch(Exception e){
            finish();
        }


    }

    private void test(){
        if (head.getNext().getType() == 0){
            head.getNext().setType(1);
            moveNode(false);
            nextTest();
        }else if (head.getNext().getType()==1){
            boolean test = false;
            String s = entryText.getText().toString();
            if (s.equalsIgnoreCase(head.getNext().getEnglish())){
                test = true;
            }

            if (test){
                updateWord(10);
                removeNode();
                nextTest();
            }else{
                wrongAnswer.setMessage("\n"+head.getNext().getEnglish());
                wrongAnswer.show();
            }
        }else if (head.getNext().getType()==2){
            boolean test = false;
            String s = entryText.getText().toString();
            if (s.equalsIgnoreCase(head.getNext().getGerman())){
                test = true;
            }

            if (test){
                updateWord(10);
                removeNode();
                nextTest();
            }else{
                wrongAnswer.setMessage("\n"+head.getNext().getGerman());
                wrongAnswer.show();
            }
        }
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


    private void removeNode(){
        try {
            head.setNext(head.getNext().getNext());
            nodeCount--;
            if (nodeCount == 0){System.out.println("GOING BACK TO MAIN)");finish();}
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

}
