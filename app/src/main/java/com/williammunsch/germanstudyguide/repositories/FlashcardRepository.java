package com.williammunsch.germanstudyguide.repositories;


import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.williammunsch.germanstudyguide.datamodels.VocabModel;
import com.williammunsch.germanstudyguide.datamodels.Word;
import com.williammunsch.germanstudyguide.room.GermanDatabase;
import com.williammunsch.germanstudyguide.room.VocabDao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Handles the data and queue operations for the flashcard activity.
 *
 */
@Singleton
public class FlashcardRepository {

    GermanDatabase db;
   // private VocabModel head, tail;
    private ArrayList<Word> wordList;
    private LiveData<List<VocabModel>> modelList, modelOldList;
    private LiveData<List<VocabModel>> mAllVocab;
    private VocabDao mVocabDao;
    private Word head, tail;
    private Queue<VocabModel> wordQueue;
    private int nodeCount;
    private final int newWords = 5;
    private MutableLiveData<Queue<VocabModel>> liveDataQueue;

    @Inject
    public FlashcardRepository(GermanDatabase db){
        this.db = db;
        mVocabDao = db.vocabDao();

        wordQueue = new LinkedList<>();
        modelList = mVocabDao.getFiveNewVocab();
        //modelOldList = db.vocabDao().getFiveOldVocab();
        mAllVocab = mVocabDao.getAllVocabs();
       // Collections.shuffle(modelOldList);

        System.out.println("TTTEESSTTTT");
        System.out.println(mAllVocab.getValue().get(0));

        for(int i = 0 ; i < newWords ; i++){
            //wordQueue.add(modelList.getValue().get(i));
        }
        for(int i = 5; i < 10; i++){
           // wordQueue.add(modelOldList.getValue().get(i));
        }

        //liveDataQueue = new MutableLiveData<>();
        //liveDataQueue.setValue(wordQueue);





       // wordList = new ArrayList<>();
        //wordList.add(new Word(modelList.get(0).getId(),modelList.get(0).getGerman(), modelList.get(0).getEnglish(), modelList.get(0).getGsent(),modelList.get(0).getEsent(),modelList.get(0).getScore(), modelList.get(0).getFreq(), modelList.get(0).getStudying()));
        //wordList.add(new Word(modelList.get(1).getId(),modelList.get(1).getGerman(), modelList.get(1).getEnglish(), modelList.get(1).getGsent(),modelList.get(1).getEsent(),modelList.get(1).getScore(), modelList.get(1).getFreq(), modelList.get(1).getStudying()));
        //wordList.add(new Word(modelList.get(2).getId(),modelList.get(2).getGerman(), modelList.get(2).getEnglish(), modelList.get(2).getGsent(),modelList.get(2).getEsent(),modelList.get(2).getScore(), modelList.get(2).getFreq(), modelList.get(2).getStudying()));


        //onvert modelList to words?



        //head = new VocabModel(0, null,null,null,null,0,0,0);
        //tail = new VocabModel(0,null,null,null,null,0,0,0);
       // head = new Word(0,0,0,0,null,null,null,null);
        //tail = new Word(0,0,0,0,null,null,null,null);
        //createQueue();
    }

    public LiveData<Queue<VocabModel>> getA1Queue(){
        return liveDataQueue;
    }





/*
    private void createQueue(){

        if (db.vocabDao().countLearned().getValue() < db.vocabDao().count().getValue()){
            setUpQueueType0();
        }
        else if (db.vocabDao().countLearned().getValue() >= db.vocabDao().count().getValue() && db.vocabDao().countMastered().getValue() < db.vocabDao().count().getValue()){
            setUpQueueType1();
        }
        else {
            setUpQueueType2();
        }
        tail.setNext(null);
    }
    private void setUpQueueType0(){
        //wordList = db.vocabDao().getFiveVocab();
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
    private void moveNode(boolean b){
        //Moves the current first node in the queue either 5 down or to the end.
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

*/

}
