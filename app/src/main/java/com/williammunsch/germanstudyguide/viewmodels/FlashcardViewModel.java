package com.williammunsch.germanstudyguide.viewmodels;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.CreateUploadDataResponse;
import com.williammunsch.germanstudyguide.SingleLiveEvent;
import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;
import com.williammunsch.germanstudyguide.repositories.FlashcardRepository;
import com.williammunsch.germanstudyguide.repositories.Repository;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Contains the core business logic for the flashcard activity.
 */
public class FlashcardViewModel extends ViewModel implements Observable {
    private FlashcardRepository mFlashcardRepository;
    private Repository mRepository;

    private PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

    private String answer = "";
    private Random random = new Random();

    private SingleLiveEvent<Boolean> navigateToMainActivity = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> addSourceEvent = new SingleLiveEvent<>();
    private LiveData<Integer> a1Learned;
    private LiveData<String> userName;
    /**
     * Keeps track of the order of flashcards in the live data here,
     * moves the index positions around and removes them when necessary.
     */
    @Inject
    public FlashcardViewModel(FlashcardRepository flashcardRepository, Repository repository) {
        this.mFlashcardRepository = flashcardRepository;
        this.mRepository = repository;
        userName=mRepository.getUserName();

        a1Learned = mFlashcardRepository.getA1Learned();

    }



    public void finishActivity(){
        navigateToMainActivity.call();
        //mFlashcardRepository.addSource();
    }


    public void iWasright(){
        mFlashcardRepository.getCurrentNode().getValue().fixScore();
        mFlashcardRepository.setCorrect(true);
        moveToNextNode();
    }

    public void checkAnswer(){
        //TODO

        if (!mFlashcardRepository.isFinished()&& mFlashcardRepository.getCurrentNode().getValue()!=null && mFlashcardRepository.getCurrentNode().getValue().getStudying() != 0){
            boolean test = false;
            //test entered answer against all possible answers in the VocabModelA1
            if (mFlashcardRepository.getCurrentNode().getValue().getScore()>50){
                //Test english -> german
                if ( mFlashcardRepository.getCurrentNode().getValue().getGerman().equalsIgnoreCase(answer)){
                    test = true;
                }
            }else{
                //Test german -> english
                for (String s : mFlashcardRepository.getCurrentNode().getValue().getEnglishStringsArray()){//mediatorVocabList.getValue().get(0).getEnglishStringsArray()){
                    System.out.println("Testing " + s + " against: "+ answer);
                    if (s.equalsIgnoreCase(answer)) {
                        test = true;
                        break;
                    }
                }
            }



            if (test && !answer.equals("")) {
                System.out.println("CORRECT");
                 mFlashcardRepository.getCurrentNode().getValue().increaseScore();
                mFlashcardRepository.setCorrect(true);
                setUpCorrectAnswerViews();
            } else if (!test && !answer.equals("")) {
                System.out.println("INCORRECT");
                mFlashcardRepository.getCurrentNode().getValue().decreaseScore();
                mFlashcardRepository.setCorrect(false);
                setUpIncorrectAnswerViews();

            }else{
                //Do nothing because the edit text is empty. Prevents misclicks.
            }
        }else{
            System.out.println("Moving to next node");
            moveToNextNode();
        }


    }

    private void setUpIncorrectAnswerViews(){
        System.out.println("calling setupincorrectanswerviews");
        mFlashcardRepository.setXmarkVisibility(VISIBLE);
        mFlashcardRepository.setIwasrightVisibility(VISIBLE);
        mFlashcardRepository.setCorrectLayoutVisibility(VISIBLE);
        mFlashcardRepository.setCheckButtonText("Next");
        mFlashcardRepository.setFinished(true);
    }

    private void setUpCorrectAnswerViews(){
        System.out.println("calling setupcorrectanswerviews");
        mFlashcardRepository.setCheckmarkVisibility(VISIBLE);
        //TODO : set linearLayout_correct to visible to show other answers?

        mFlashcardRepository.setCheckButtonText("Next");
        mFlashcardRepository.setFinished(true);

    }

    private void moveToNextNode(){
        //TODO

        if (mFlashcardRepository.getCurrentNode().getValue() != null && mFlashcardRepository.getCurrentNode().getValue().getStudying()==0){
            mFlashcardRepository.getCurrentNode().getValue().setStudying(1);
        }

        if (mFlashcardRepository.isCorrect()){
            popNode();
        }else{
            moveNode();
        }

        setAnswer("");

        //Activity is finished, update the local ROOM database
        if (mFlashcardRepository.getMediatorVocabList().getValue()!= null &&mFlashcardRepository.getMediatorVocabList().getValue().size()<=0){
            mFlashcardRepository.setEditTextVisibility(GONE);
            mFlashcardRepository.setHintButtonVisibility(GONE);
            mFlashcardRepository.setCheckButtonVisibility(GONE);
            System.out.println("Setting finishedwithactivity to true");
            mFlashcardRepository.setCheckmarkVisibility(GONE);
            mFlashcardRepository.setCorrectLayoutVisibility(GONE);
            mFlashcardRepository.setIwasrightVisibility(GONE);
            mFlashcardRepository.setXmarkVisibility(GONE);
            mFlashcardRepository.removeMediatorSource();
            mFlashcardRepository.setFinishButtonVisibility(VISIBLE);
           // mFlashcardRepository.updateAllNodes();

            //If logged in, update remote database too
            if (mRepository.getCurrentUser().getValue()!=null){
               // update with remote database call
                mFlashcardRepository.updateAllNodes(true);
            }else{
                //update without remote database call
                mFlashcardRepository.updateAllNodes(false);
            }

            //TODO : Display stats from the activity.
            mFlashcardRepository.setGoodJobVisibility(VISIBLE);
            mFlashcardRepository.setTv_wordsLearnedVisibility(VISIBLE);
            mFlashcardRepository.setProgressBar_wordsLearnedVisibility(VISIBLE);
            mFlashcardRepository.setTextView_wordsLearnedOutOfVisibility(VISIBLE);

            mFlashcardRepository.setTextView_wordsMasteredVisibility(VISIBLE);
            mFlashcardRepository.setProgressBar_wordsMasteredVisibility(VISIBLE);
            mFlashcardRepository.setTextView_wordsMasteredOutOfVisibility(VISIBLE);
        }
    }


    /**
     * Removes the top item from the mediatorLiveData list
     */
    private void popNode(){
        List<VocabModelA1> list = mFlashcardRepository.getMediatorVocabList().getValue();//mediatorVocabList.getValue();
        try {
            mFlashcardRepository.getCurrentNode().getValue().increaseFrequency();
            mFlashcardRepository.getFinishedList().add(mFlashcardRepository.getCurrentNode().getValue());//finishedList.add(currentNode.getValue());
            list.remove(0);
        }catch(NullPointerException e){
            System.out.println("List is null");
        }
        mFlashcardRepository.setMediatorVocabListValue(list);
    }

    /**
     * Moves the item in the mediatorLiveData list down a random number of indexes
     */

    private void moveNode(){
        List<VocabModelA1> list =mFlashcardRepository.getMediatorVocabList().getValue();// mediatorVocabList.getValue();
        System.out.println("Size is " + list.size());

        VocabModelA1 temp = list.get(0);
        int ranNum = 0;

        if (list.size() > 5){
           ranNum = 5;// ranNum = random.nextInt(3)+3;  //index 0-5+  between 3 and 5
        }
        else if (list.size() == 5){
           ranNum = 4;// ranNum = random.nextInt(2)+3;   //index 0-4 between 3 and 4
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

        mFlashcardRepository.setMediatorVocabListValue(list);
    }


    public LiveData<Integer> getWordsLearned(){return mFlashcardRepository.getWordsLearned();}
    public LiveData<Integer> getWordsLearnedP(){ return mFlashcardRepository.getWordsLearnedP();}
    public LiveData<Integer> getWordsMasteredP(){ return mFlashcardRepository.getWordsMasteredP();}

    public LiveData<Integer> getWordsMax() {
        return mFlashcardRepository.getWordsMax();
    }
    public LiveData<Integer> getWordsLearnedPercent() {
        return mFlashcardRepository.getWordsLearnedPercent();
    }
    public LiveData<Integer> getWordsMastered() {
        return mFlashcardRepository.getWordsMastered();
    }

    public LiveData<Integer> getCheckButtonVisibility() {
        return mFlashcardRepository.getCheckButtonVisibility();
    }
    public LiveData<Integer> getCardsFinished() {
        return mFlashcardRepository.getCardsFinished();
    }

    public LiveData<String> getCardsFinishedText() {
        return mFlashcardRepository.getCardsFinishedText();
    }
    public LiveData<Boolean> getNavigateToMainActivity(){
        return navigateToMainActivity;
    }
    public LiveData<Integer> getFinishButtonVisibility(){
        return mFlashcardRepository.getFinishButtonVisibility();
    }
    public LiveData<Integer> getTv_wordsLearnedVisibility(){
        return mFlashcardRepository.getTv_wordsLearnedVisibility();
    }
    public LiveData<Integer> getGoodJobVisibility(){
        return mFlashcardRepository.getGoodJobVisibility();
    }
    public LiveData<Integer> getEnglishTextVisibility(){
        return mFlashcardRepository.getEnglishTextVisibility();
    }
    public LiveData<Integer> getHintButtonVisibility(){
        return mFlashcardRepository.getHintButtonVisibility();
    }
    public LiveData<Integer> getEditTextVisibility(){
        return mFlashcardRepository.getEditTextVisibility();
    }
    public LiveData<Integer> getTextView_wordsMasteredVisibility(){
        return mFlashcardRepository.getTextView_wordsMasteredVisibility();
    }
    public LiveData<Integer> getProgressBar_wordsMasteredVisibility(){
        return mFlashcardRepository.getProgressBar_wordsMasteredVisibility();
    }
    public LiveData<Integer> getTextView_wordsMasteredOutOfVisibility(){
        return mFlashcardRepository.getTextView_wordsMasteredOutOfVisibility();
    }

    public LiveData<Integer> getTextView_wordsLearnedOutOfVisibility(){
        return mFlashcardRepository.getTextView_wordsLearnedOutOfVisibility();
    }
    public LiveData<String> getCheckButtonText(){
        return mFlashcardRepository.getCheckButtonText();
    }
    public LiveData<Integer> getIwasrightVisibility(){
        return mFlashcardRepository.getIwasrightVisibility();
    }
    public LiveData<Integer> getCorrectLayoutVisibility(){
        return mFlashcardRepository.getCorrectLayoutVisibility();
    }
    public LiveData<Integer> getXmarkVisibility(){
        return mFlashcardRepository.getXmarkVisibility();
    }
    public LiveData<Integer> getCheckmarkVisibility(){
        return mFlashcardRepository.getCheckmarkVisibility();
    }
    public LiveData<Integer> getHintVisibility(){
        return mFlashcardRepository.getHintVisibility();
    }

    public LiveData<Integer> getProgressBar_wordsLearnedVisibility(){
        return mFlashcardRepository.getProgressBar_wordsLearnedVisibility();
    }

    public void showSentence(){
        mFlashcardRepository.showSentence();
    }

    public LiveData<List<VocabModelA1>> getMediatorVocabList(){
        return mFlashcardRepository.getMediatorVocabList();
    }



    public LiveData<VocabModelA1> getCurrentNode(){
        return mFlashcardRepository.getCurrentNode();
    }

    //Two-way binding to allow reading of the edit text
    @Bindable
    public String getAnswer(){
        return answer;
    }

    public void setAnswer(String a){
        this.answer = a;
        propertyChangeRegistry.notifyChange(this, BR.answer);
    }




    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        propertyChangeRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        propertyChangeRegistry.remove(callback);
    }
}
