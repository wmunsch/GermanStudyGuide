package com.williammunsch.germanstudyguide.repositories;



import android.os.AsyncTask;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.williammunsch.germanstudyguide.datamodels.VocabModel;
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

    private LiveData<List<VocabModel>> vocabList;

    private VocabDao mVocabDao;

    private final int newWords = 5;

    private MediatorLiveData<List<VocabModel>> mediatorVocabList = new MediatorLiveData<>();
    private LiveData<VocabModel> currentNode;
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
    private MutableLiveData<Integer> goodJobVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> tv_wordsLearnedVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> progressBar_wordsLearnedVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> textView_wordsLearnedOutOfVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> textView_wordsMasteredVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> progressBar_wordsMasteredVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> textView_wordsMasteredOutOfVisibility = new MutableLiveData<>();




    private boolean finished = false;

    private boolean correct = false;

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
                return null;
            }
        });
    }

    public void addSource(){
        if (mediatorVocabList.getValue()==null || mediatorVocabList.getValue().isEmpty()){
            System.out.println("*\n*\nADDING SOURCE\n*\n*");
            mediatorVocabList.addSource(vocabList, value -> mediatorVocabList.setValue(value));
        }
    }

    public void updateAllNodes(){
        System.out.println("UPDATING ALL NODES");
        for (int i = 0 ; i < finishedList.size();i++){
            updateNode(finishedList.get(i));
        }
        finishedList.clear();
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

    public void setMediatorVocabListValue(List<VocabModel> list) {
        this.mediatorVocabList.setValue(list);
    }


    public ArrayList<VocabModel> getFinishedList() {
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
}
