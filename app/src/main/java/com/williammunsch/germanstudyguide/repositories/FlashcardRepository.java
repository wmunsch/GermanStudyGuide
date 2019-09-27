package com.williammunsch.germanstudyguide.repositories;


import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

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
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * Handles the data and queue operations for the flashcard activity.
 *
 */
@Singleton
public class FlashcardRepository {

    GermanDatabase db;

    private List<VocabModel> modelList; //The list holding data that can be manipulated
    private MutableLiveData<List<VocabModel>> mAllVocab = new MutableLiveData<>();    //The live data that the activity observes

    private List<VocabModel> vocabQueue;

    private LiveData<List<VocabModel>> vocabList;

   // private MutableLiveData<List<VocabModel>> vocabQueue = new MutableLiveData<>();

    private VocabDao mVocabDao;


    private final int newWords = 5;



    private MediatorLiveData<List<VocabModel>> mediatorVocabList = new MediatorLiveData<>();
    private LiveData<VocabModel> currentNode;// = new MutableLiveData<>();
    private MutableLiveData<Integer> mHintVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> checkmarkVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> xmarkVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> iwasrightVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> correctLayoutVisibility = new MutableLiveData<>();
    private MutableLiveData<String> checkButtonText = new MutableLiveData<>();
    private MutableLiveData<Integer> editTextVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> hintButtonVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> englishTextVisibility = new MutableLiveData<>();
    private String answer = "";


    private boolean finished = false;



    private boolean finishedWithActivity = false;



    private boolean correct = false;
    private Random random = new Random();



    private ArrayList<VocabModel> finishedList = new ArrayList<>();


    /**
     * Need to inject context to get access to the live data to create the queue.
     * Queue needs to be stored here so it will save throughout changes in the lifecycle.
     */
    @Inject
    public FlashcardRepository(GermanDatabase db){
        this.db = db;
        mVocabDao = db.vocabDao();

        vocabList = mVocabDao.getVocabQueue();
        mediatorVocabList.addSource(vocabList, value -> mediatorVocabList.setValue(value));


        currentNode = Transformations.map(mediatorVocabList, value -> {
            if (mediatorVocabList.getValue() != null){
                System.out.println("calling transformations.map currentNode = " + currentNode.getValue());
                if (mediatorVocabList.getValue().size()>0){
                    if (mediatorVocabList.getValue().get(0).getStudying()==0){
                        setUpViewsForNewCard();
                    }else if (mediatorVocabList.getValue().get(0).getStudying()==1){
                        setUpViewsForOldCard();
                    }
                    return mediatorVocabList.getValue().get(0);
                }
                return null;
            }else{
                return null;//new VocabModel(0,null,null,null,null,0,0,0);
            }
        });
       // vocabQueue = mVocabDao.getVocabQueue();
      //  mAllVocab = mVocabDao.getFiveNewVocab();

      //  vocabQueue.setValue(mAllVocab.getValue());


        //getVocabList();



        for(int i = 0 ; i < newWords ; i++){
            //wordQueue.add(modelList.getValue().get(i));
        }
        for(int i = 5; i < 10; i++){
           // wordQueue.add(modelOldList.getValue().get(i));
        }

    }

    /**
     * Removes the top item from the mediatorLiveData list
     */
    private void popNode(){
        List<VocabModel> list = mediatorVocabList.getValue();
        try {
            // finishedList.add(list.get(0));
            finishedList.add(currentNode.getValue());
            list.remove(0);
        }catch(NullPointerException e){
            System.out.println("List is null");
        }
        mediatorVocabList.setValue(list);
    }

    /**
     * Moves the item in the mediatorLiveData list down a random number of indexes
     */
    public void moveNode(){
        List<VocabModel> list =mediatorVocabList.getValue();
        System.out.println("Size is " + list.size());

        VocabModel temp = list.get(0);
        int ranNum = 0;

        if (list.size() > 5){
            ranNum = random.nextInt(3)+3;  //index 0-5+  between 3 and 5
        }
        else if (list.size() == 5){
            ranNum = random.nextInt(2)+3;   //index 0-4 between 3 and 4
        }
        else if (list.size() == 4){
            ranNum = random.nextInt(2)+2; //index 0-3   between 2 and 3
        }
        else if (list.size() == 3){
            ranNum = random.nextInt(2)+1;  //index 0-2  between 1 and 2
        }
        else if (list.size() == 2){
            ranNum = 1;
        }

        //Move every index between 0 and where the currentNode will go down one position
        for (int i = 0; i < ranNum; i++){
            list.set(i, list.get(i+1));
        }

        list.set(ranNum,temp); //move the currentNode to the random position chosen earlier

        mediatorVocabList.setValue(list);
    }

    public void updateAllNodes(){
        System.out.println("UPDATING ALL NODES");
        for (int i = 0 ; i < finishedList.size();i++){
            updateNode(finishedList.get(i));
        }
    }

    private void setUpViewsForNewCard(){
        System.out.println("CALLING setupviewsforNEWcard");
        englishTextVisibility.setValue(VISIBLE);
        mHintVisibility.setValue(VISIBLE);
        checkmarkVisibility.setValue(INVISIBLE);
        xmarkVisibility.setValue(INVISIBLE);
        checkButtonText.setValue("Next");
        iwasrightVisibility.setValue(GONE);
        correctLayoutVisibility.setValue(GONE);
        editTextVisibility.setValue(INVISIBLE);
        hintButtonVisibility.setValue(INVISIBLE);
    }

    private void setUpViewsForOldCard(){
        System.out.println("CALLING setupviewsforOLDcard");
        checkButtonText.setValue("Check");
        finished = false;
        //setAnswer("");
        checkmarkVisibility.setValue(INVISIBLE);
        xmarkVisibility.setValue(INVISIBLE);
        correctLayoutVisibility.setValue(INVISIBLE);
        mHintVisibility.setValue(INVISIBLE);
        hintButtonVisibility.setValue(VISIBLE);
        editTextVisibility.setValue(VISIBLE);
        englishTextVisibility.setValue(INVISIBLE);
        correctLayoutVisibility.setValue(INVISIBLE);
        iwasrightVisibility.setValue(GONE);
    }

    private void setUpIncorrectAnswerViews(){
        System.out.println("calling setupincorrectanswerviews");
        xmarkVisibility.setValue(VISIBLE);
        iwasrightVisibility.setValue(VISIBLE);
        correctLayoutVisibility.setValue(VISIBLE);

        checkButtonText.setValue("Next");
        //finished = true;
    }

    private void setUpCorrectAnswerViews(){
        System.out.println("calling setupcorrectanswerviews");
        checkmarkVisibility.setValue(VISIBLE);
        //TODO : set linearLayout_correct to visible to show other answers?

        checkButtonText.setValue("Next");
       // finished = true;

    }

    public boolean isFinishedWithActivity() {
        return finishedWithActivity;
    }

    public void setFinishedWithActivity(boolean finishedWithActivity) {
        this.finishedWithActivity = finishedWithActivity;
    }

    public void removeMediatorSource(){
        mediatorVocabList.removeSource(vocabList);
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setMediatorVocabListValue(List<VocabModel> list) {
        this.mediatorVocabList.setValue(list);
    }


    public ArrayList<VocabModel> getFinishedList() {
        return finishedList;
    }

    public void setFinishedList(ArrayList<VocabModel> finishedList) {
        this.finishedList = finishedList;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    public void setEnglishTextVisibility(int i){
        englishTextVisibility.setValue(i);
    }
    public void setHintButtonVisibility(int i){
        hintButtonVisibility.setValue(i);
    }
    public void setEditTextVisibility(int i){
        editTextVisibility.setValue(i);
    }
    public void setCheckButtonText(String s){
        checkButtonText.setValue(s);
    }
    public void setCorrectLayoutVisibility(int i){
        correctLayoutVisibility.setValue(i);
    }
    public void setIwasrightVisibility(int i){
        iwasrightVisibility.setValue(i);
    }
    public void setCheckmarkVisibility(int i){
        checkmarkVisibility.setValue(i);
    }
    public void setmHintVisibility(int i){
        mHintVisibility.setValue(i);
    }

    public void setXmarkVisibility(int i){
        xmarkVisibility.setValue(i);
    }

    public List<VocabModel> getVocabQueue(){
        return vocabQueue;
    }

    public LiveData<List<VocabModel>> getVocabData(){
        return vocabList;
    }
    public LiveData<Integer> getEnglishTextVisibility(){
        return englishTextVisibility;
    }
    public LiveData<Integer> getHintButtonVisibility(){
        return hintButtonVisibility;
    }
    public LiveData<Integer> getEditTextVisibility(){
        return editTextVisibility;
    }

    public LiveData<String> getCheckButtonText(){
        return checkButtonText;
    }

    public LiveData<Integer> getIwasrightVisibility(){
        return iwasrightVisibility;
    }
    public LiveData<Integer> getCorrectLayoutVisibility(){
        return correctLayoutVisibility;
    }

    public LiveData<Integer> getXmarkVisibility(){
        return xmarkVisibility;
    }
    public LiveData<Integer> getCheckmarkVisibility(){
        return checkmarkVisibility;
    }

    public LiveData<Integer> getHintVisibility(){
        return mHintVisibility;
    }


    public void showSentence(){
        if (mHintVisibility.getValue() != null && mHintVisibility.getValue() == VISIBLE){mHintVisibility.setValue(INVISIBLE);}
        else{mHintVisibility.setValue(VISIBLE);}
    }

    public LiveData<List<VocabModel>> getMediatorVocabList(){
        return mediatorVocabList;
    }

    public LiveData<VocabModel> getCurrentNode(){
        return currentNode;
    }

    public void updateNode(VocabModel vocabModel){
        new updateNodeAsyncTask(mVocabDao).execute(vocabModel);
    }


    private static class updateNodeAsyncTask extends AsyncTask<VocabModel, Void, Void> {

        private VocabDao mAsyncTaskDao;

        updateNodeAsyncTask(VocabDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final VocabModel... params) {
            mAsyncTaskDao.updateNode(params[0]);
            System.out.println("Updating node to " + params[0].getScore());
            return null;
        }
    }


/*
    private class GetVocabListAsyncTask extends AsyncTask<List<VocabModel>, Void, Void>{
        private VocabDao vocabDao;
        //private List<VocabModel> modelList;
        //FlashcardRepository repo;

        GetVocabListAsyncTask(VocabDao dao){
            vocabDao = dao;
           // this.repo = repo;
        }

        @Override
        protected Void doInBackground(List<VocabModel>... vocabModels) {
            modelList = vocabDao.getFiveNewVocab();
            mAllVocab.postValue(modelList);
            //create queue here
            System.out.println("PRINTING MODELLIST");
            System.out.println(modelList.get(0));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //mAllVocab.postValue(modelList);
           // repo.updateList(modelList);
           // super.onPostExecute(aVoid);
        }


    }

    public void updateList(List<VocabModel> list){
        modelList = list;
    }

    public void getVocabList(){
        new GetVocabListAsyncTask(mVocabDao).execute();

    }

    public MutableLiveData<List<VocabModel>> getModelList(){
        //MutableLiveData data = new MutableLiveData();
        //data.postValue(modelList);
        return mAllVocab;
    }

    public void removeFlashcard(int i){
        modelList.remove(i);
        mAllVocab.setValue(modelList);
    }

/*
public LiveData<LinkedList<VocabModel>> getModelQueue(){
    return modelQueue;
}

    public LiveData<Queue<VocabModel>> getA1Queue(){
        return liveDataQueue;
    }


*/


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
