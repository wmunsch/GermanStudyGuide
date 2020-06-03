package com.williammunsch.germanstudyguide.repositories;



import android.os.AsyncTask;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;
import com.williammunsch.germanstudyguide.room.GermanDatabase;
import com.williammunsch.germanstudyguide.room.VocabDao;

import java.util.ArrayList;
import java.util.List;

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

    private LiveData<List<VocabModelA1>> vocabList;

    private VocabDao mVocabDao;

    private double newWords = 5;
    private boolean setAtBeginning = false;

    private MediatorLiveData<List<VocabModelA1>> mediatorVocabList = new MediatorLiveData<>();
    private LiveData<VocabModelA1> currentNode;
    private MutableLiveData<Integer> mHintVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> checkmarkVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> xmarkVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> iwasrightVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> correctLayoutVisibility = new MutableLiveData<>();
    private MutableLiveData<String> checkButtonText = new MutableLiveData<>();
    private MutableLiveData<Integer> editTextVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> hintButtonVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> englishTextVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> finishButtonVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> checkButtonVisibility = new MutableLiveData<>();
    //End of activity views
    private MutableLiveData<Integer> goodJobVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> tv_wordsLearnedVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> progressBar_wordsLearnedVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> textView_wordsLearnedOutOfVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> textView_wordsMasteredVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> progressBar_wordsMasteredVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> textView_wordsMasteredOutOfVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> cardsFinished = new MutableLiveData<>();
    private MutableLiveData<String> cardsFinishedText = new MutableLiveData<>();

    private LiveData<Integer> wordsMax;
    private LiveData<Integer> wordsLearned;
    private LiveData<Integer> wordsMastered;
    private MutableLiveData<Integer> wordsLearnedPercent = new MutableLiveData<>();
    private LiveData<Integer> wordsLearnedP;
    private LiveData<Integer> wordsMasteredP;
   // private LiveData<Integer> cardsForThisActivity;


    private boolean finished = false;

    private boolean correct = false;

    private ArrayList<VocabModelA1> finishedList = new ArrayList<>();
    //private VocabModelA1[] finishedList = new VocabModelA1[5];


    /**
     * Queue needs to be stored here so it will save throughout changes in the lifecycle.
     */
    @Inject
    public FlashcardRepository(GermanDatabase db){
        this.db = db;
        mVocabDao = db.vocabDao();
        //TODO


        vocabList = mVocabDao.getVocabQueue();
        wordsMax =mVocabDao.count();
        wordsLearned = mVocabDao.countLearned();
        wordsMastered = mVocabDao.countMastered();



        //TODO : Add functionality to the progress bars and animation

       //-- wordsLearnedPercent.setValue(wordsLearned.getValue()*100/wordsMax.getValue());

        /*
        Every time the mediatorVocabList changes, the currentNode is set to the first entry,
        and the values for the top progress bar and text are set,
        and the visibility for the different card types are set up.
         */
        //TODO

        currentNode = Transformations.map(mediatorVocabList, value -> {
            if (mediatorVocabList.getValue() != null){
                //reset the number of total cards for the activity (necessary for the progress bar because 1st time there are only 5 cards, then 10, then 15 then 20)
                if (!setAtBeginning && mediatorVocabList.getValue().size() !=0){newWords =mediatorVocabList.getValue().size(); setAtBeginning=true;}
                cardsFinished.setValue((int)(100 - (((double)mediatorVocabList.getValue().size())/newWords)*100)); //This determines the percentage bar for flashcard actvity
               // wordsLearnedPercent.setValue((int)(((double)wordsLearned.getValue()/700)*100));
                //(int)(((double)a1Learned/a1Max)*100)
               // cardsFinished.setValue(100 - (mediatorVocabList.getValue().size()*100/newWords)); //This determines the percentage bar

                cardsFinishedText.setValue("Cards Remaining: " + mediatorVocabList.getValue().size()); //This determines the cards left number

                if (mediatorVocabList.getValue().size()>0){
                    System.out.println("Studying is equal to " + mediatorVocabList.getValue().get(0).getStudying());
                    if (mediatorVocabList.getValue().get(0).getStudying()==0){
                        setUpViewsForNewCard();
                    }else if (mediatorVocabList.getValue().get(0).getStudying()==1){
                        setUpViewsForOldCard();
                    }
                    return mediatorVocabList.getValue().get(0);
                }
                return null;
            }else{
                return null;
            }
        });


        /*
         * Every time a new word is set to learned in the room database,
         * the wordsLearnedP livedata gets updated to reflect this.
         * wordsLearnedP is the percentage of words learned out of all A1 words.
         */
        wordsLearnedP = Transformations.map(wordsLearned, value -> {
            if (wordsLearned.getValue() != null){
                return (int)(((double)wordsLearned.getValue()/700)*100);
            }
            return 0;

        });

        wordsMasteredP = Transformations.map(wordsMastered, value -> {
            if (wordsMastered.getValue() != null){
                return (int)(((double)wordsMastered.getValue()/700)*100);
            }
            return 0;

        });


    }

    public void addSource(){
        if (mediatorVocabList.getValue()==null || mediatorVocabList.getValue().isEmpty()){
            System.out.println("*\n*\nADDING SOURCE\n*\n*");
            mediatorVocabList.addSource(vocabList, value -> mediatorVocabList.setValue(value));
        }
    }

    /**
     * Updates the scores for every vocabulary word that was in the activity
     * based on a finished List that all words are sent to after being popped from
     * the mediatorVocabList.
     */
    public void updateAllNodes(){
        System.out.println("UPDATING ALL NODES");

        updateNode(finishedList);
        finishedList.clear();
        setAtBeginning = false;

        System.out.println("Finished updating nodes");
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
        finishButtonVisibility.setValue(INVISIBLE);
        checkButtonVisibility.setValue(VISIBLE);
        goodJobVisibility.setValue(GONE);
        tv_wordsLearnedVisibility.setValue(GONE);
        progressBar_wordsLearnedVisibility.setValue(GONE);
        textView_wordsLearnedOutOfVisibility.setValue(GONE);
        textView_wordsMasteredVisibility.setValue(GONE);
        progressBar_wordsMasteredVisibility.setValue(GONE);
        textView_wordsMasteredOutOfVisibility.setValue(GONE);
        correct = false;


        goodJobVisibility.setValue(INVISIBLE);
        tv_wordsLearnedVisibility.setValue(INVISIBLE);
        progressBar_wordsLearnedVisibility.setValue(INVISIBLE);
        textView_wordsLearnedOutOfVisibility.setValue(INVISIBLE);
        textView_wordsMasteredVisibility.setValue(INVISIBLE);
        progressBar_wordsMasteredVisibility.setValue(INVISIBLE);
        textView_wordsMasteredOutOfVisibility.setValue(INVISIBLE);
    }

    private void setUpViewsForOldCard(){
        System.out.println("CALLING setupviewsforOLDcard");
        checkButtonText.setValue("Check");
        finished = false;
        //setAnswer("");
        goodJobVisibility.setValue(INVISIBLE);
        tv_wordsLearnedVisibility.setValue(INVISIBLE);
        progressBar_wordsLearnedVisibility.setValue(INVISIBLE);
        textView_wordsLearnedOutOfVisibility.setValue(INVISIBLE);
        textView_wordsMasteredVisibility.setValue(INVISIBLE);
        progressBar_wordsMasteredVisibility.setValue(INVISIBLE);
        textView_wordsMasteredOutOfVisibility.setValue(INVISIBLE);

        checkmarkVisibility.setValue(INVISIBLE);
        xmarkVisibility.setValue(INVISIBLE);
        correctLayoutVisibility.setValue(INVISIBLE);
        mHintVisibility.setValue(INVISIBLE);
        hintButtonVisibility.setValue(VISIBLE);
        editTextVisibility.setValue(VISIBLE);
        englishTextVisibility.setValue(INVISIBLE);
        correctLayoutVisibility.setValue(INVISIBLE);
        iwasrightVisibility.setValue(GONE);
        finishButtonVisibility.setValue(INVISIBLE);
        checkButtonVisibility.setValue(VISIBLE);
        correct = false;
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

    public void setMediatorVocabListValue(List<VocabModelA1> list) {
        this.mediatorVocabList.setValue(list);
    }


    /**
     * Creates an updateNodeAsyncTask and executes it.
     * @param vocabModelA1s The list of all the nodes that will be updated.
     *  This needs to add every node to the execute portion manually.
     *  Could this be improved by sending the list as a parameter instead of each vocabModel individually?
     */
    private void updateNode(List<VocabModelA1> vocabModelA1s){
        System.out.println("vocabmodea1s sidze = " +vocabModelA1s.size());
        if (vocabModelA1s.size()==3){
            new updateNodeAsyncTask(mVocabDao).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2));
        }else if (vocabModelA1s.size()==4){
            new updateNodeAsyncTask(mVocabDao).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3));
        }else if (vocabModelA1s.size()==5){
            new updateNodeAsyncTask(mVocabDao).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3), vocabModelA1s.get(4));
        }else if (vocabModelA1s.size()==10){
            new updateNodeAsyncTask(mVocabDao).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3), vocabModelA1s.get(4),
                    vocabModelA1s.get(5), vocabModelA1s.get(6), vocabModelA1s.get(7), vocabModelA1s.get(8), vocabModelA1s.get(9));
        }else if (vocabModelA1s.size()==14){
            new updateNodeAsyncTask(mVocabDao).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3), vocabModelA1s.get(4),
                    vocabModelA1s.get(5), vocabModelA1s.get(6), vocabModelA1s.get(7), vocabModelA1s.get(8), vocabModelA1s.get(9),
                    vocabModelA1s.get(10), vocabModelA1s.get(11), vocabModelA1s.get(12), vocabModelA1s.get(13));
        }else if (vocabModelA1s.size()==15){
            new updateNodeAsyncTask(mVocabDao).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3), vocabModelA1s.get(4),
                    vocabModelA1s.get(5), vocabModelA1s.get(6), vocabModelA1s.get(7), vocabModelA1s.get(8), vocabModelA1s.get(9),
                    vocabModelA1s.get(10), vocabModelA1s.get(11), vocabModelA1s.get(12), vocabModelA1s.get(13), vocabModelA1s.get(14));
        }else if (vocabModelA1s.size()==20){
            new updateNodeAsyncTask(mVocabDao).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3), vocabModelA1s.get(4),
                    vocabModelA1s.get(5), vocabModelA1s.get(6), vocabModelA1s.get(7), vocabModelA1s.get(8), vocabModelA1s.get(9),
                    vocabModelA1s.get(10), vocabModelA1s.get(11), vocabModelA1s.get(12), vocabModelA1s.get(13), vocabModelA1s.get(14),
                    vocabModelA1s.get(15), vocabModelA1s.get(16), vocabModelA1s.get(17), vocabModelA1s.get(18), vocabModelA1s.get(19));
        }else{
            System.out.println("ERROR UPDATENODE DIDNT WORK");
        }
        //new updateNodeAsyncTask(mVocabDao).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3), vocabModelA1s.get(4));
    }

    /**
     * AsyncTask to update each vocabulary word in the ROOM database on a separate thread
     * at the end of the activity, once every flashcard has been finished.
     */
    private static class updateNodeAsyncTask extends AsyncTask<VocabModelA1, Void, Void> {

        private VocabDao mAsyncTaskDao;

        updateNodeAsyncTask(VocabDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final VocabModelA1... params) {
            for (VocabModelA1 model : params){
                mAsyncTaskDao.updateNode(model);
                System.out.println("Updating node to " +model.getGerman() + " " +  model.getScore());
            }
            //mAsyncTaskDao.updateNode(params[0]);
            //System.out.println("Updating node to " + params[0].getScore());
            return null;
        }
    }





   /*
   Getters and setters
    */

   public LiveData<Integer> getWordsLearned(){return wordsLearned;}
    public LiveData<Integer> getWordsLearnedP(){return wordsLearnedP;}
    public LiveData<Integer> getWordsMasteredP(){return wordsMasteredP;}

    public LiveData<Integer> getA1Learned(){return mVocabDao.countLearned();}
    public ArrayList<VocabModelA1> getFinishedList() {
        return finishedList;
    }
    public boolean isFinished() {
        return finished;
    }

    public LiveData<Integer> getCheckButtonVisibility() {
        return checkButtonVisibility;
    }

    public void setCheckButtonVisibility(int i) {
        this.checkButtonVisibility.setValue(i);
    }

    public void setFinishButtonVisibility(int i){this.finishButtonVisibility.setValue(i);}
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
    public void setGoodJobVisibility(int i){
        goodJobVisibility.setValue(i);
    }
    public void setTv_wordsLearnedVisibility(int i){
        tv_wordsLearnedVisibility.setValue(i);
    }
    public void setProgressBar_wordsLearnedVisibility(int i){
        progressBar_wordsLearnedVisibility.setValue(i);
    }

    public void setXmarkVisibility(int i){
        xmarkVisibility.setValue(i);
    }
    public void setTextView_wordsLearnedOutOfVisibility(int i){
        textView_wordsLearnedOutOfVisibility.setValue(i);
    }
    public void setTextView_wordsMasteredVisibility(int i){
        textView_wordsMasteredVisibility.setValue(i);
    }
    public void setProgressBar_wordsMasteredVisibility(int i){
        progressBar_wordsMasteredVisibility.setValue(i);
    }
    public void setTextView_wordsMasteredOutOfVisibility(int i){
        textView_wordsMasteredOutOfVisibility.setValue(i);
    }
    public LiveData<Integer> getCardsFinished(){
        return cardsFinished;
    }

    public LiveData<Integer> getWordsLearnedPercent(){return wordsLearnedPercent;}
    public LiveData<Integer> getWordsMax(){return wordsMax;}
    public LiveData<Integer> getWordsMastered(){return wordsMastered;}

    public LiveData<String> getCardsFinishedText(){return cardsFinishedText;}
    public LiveData<Integer> getTextView_wordsMasteredOutOfVisibility(){return  textView_wordsMasteredOutOfVisibility;}
    public LiveData<Integer> getProgressBar_wordsMasteredVisibility(){return  progressBar_wordsMasteredVisibility;}
    public LiveData<Integer> getTextView_wordsMasteredVisibility(){return  textView_wordsMasteredVisibility;}
    public LiveData<Integer> getProgressBar_wordsLearnedVisibility(){return progressBar_wordsLearnedVisibility;}
    public LiveData<Integer> getTextView_wordsLearnedOutOfVisibility(){return  textView_wordsLearnedOutOfVisibility;}
    public LiveData<Integer> getTv_wordsLearnedVisibility(){return tv_wordsLearnedVisibility;}
    public LiveData<Integer> getGoodJobVisibility(){return goodJobVisibility;}
    public LiveData<Integer> getFinishButtonVisibility(){return finishButtonVisibility;}
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

    public LiveData<List<VocabModelA1>> getMediatorVocabList(){
        return mediatorVocabList;
    }

    public LiveData<VocabModelA1> getCurrentNode(){
        return currentNode;
    }


    public void showSentence(){
       if (mHintVisibility.getValue() != null && mHintVisibility.getValue() == VISIBLE){mHintVisibility.setValue(INVISIBLE);}
       else if (mHintVisibility.getValue() != null && mHintVisibility.getValue() == INVISIBLE){mHintVisibility.setValue(VISIBLE);}
    }



}
