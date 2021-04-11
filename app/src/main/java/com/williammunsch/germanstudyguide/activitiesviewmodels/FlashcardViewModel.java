package com.williammunsch.germanstudyguide.activitiesviewmodels;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.SingleLiveEvent;
import com.williammunsch.germanstudyguide.datamodels.User;
import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;
import com.williammunsch.germanstudyguide.repositories.Repository;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Handles all of the livedata objects and logic for the flashcard activity.
 */
public class FlashcardViewModel extends ViewModel implements Observable {
    private final Repository mRepository;

    private final PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

    private String answer = "";
    private final Random random = new Random();

    private final SingleLiveEvent<Boolean> navigateToMainActivity = new SingleLiveEvent<>();

    private final LiveData<String> userName;
    private final LiveData<User> currentUser;

    private LiveData<List<VocabModelA1>> vocabList;
    private final MediatorLiveData<List<VocabModelA1>> mediatorVocabList = new MediatorLiveData<>();
    private final LiveData<VocabModelA1> currentNode;
    private double newWords = 5;

    private final MutableLiveData<Integer> cardsFinished = new MutableLiveData<>();
    private final MutableLiveData<String> cardsFinishedText = new MutableLiveData<>();

    private final MutableLiveData<Integer> mHintVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> checkmarkVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> xmarkVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> iwasrightVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> correctLayoutVisibility = new MutableLiveData<>();
    private final MutableLiveData<String> checkButtonText = new MutableLiveData<>();
    private final MutableLiveData<Integer> editTextVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> hintButtonVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> englishTextVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> finishButtonVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> checkButtonVisibility = new MutableLiveData<>();
    //End of activity views
    private final MutableLiveData<Integer> goodJobVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> tv_wordsLearnedVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> progressBar_wordsLearnedVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> textView_wordsLearnedOutOfVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> textView_wordsMasteredVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> progressBar_wordsMasteredVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> textView_wordsMasteredOutOfVisibility = new MutableLiveData<>();

    private boolean finished = false;
    private boolean correct = false;
    private final LiveData<Integer> wordsMax;
    private final LiveData<Integer> wordsLearned;
    private final LiveData<Integer> wordsMastered;
    private final MutableLiveData<Integer> wordsLearnedPercent = new MutableLiveData<>();
    private final LiveData<Integer> wordsLearnedP;
    private final LiveData<Integer> wordsMasteredP;

    /**
     * Keeps track of the order of flashcards in the live data here,
     * moves the index positions around and removes them when necessary.
     */
    @Inject
    public FlashcardViewModel(Repository repository) {
        this.mRepository = repository;

        currentUser = repository.getUserInfoFromRoom();
        wordsMax = repository.countWordsMax();
        wordsLearned = repository.countLearned();
        wordsMastered = repository.countMastered();

        //map username to currentuser.username to show username in top left of main screen
        userName = Transformations.map(currentUser, name->{
            if (currentUser.getValue() != null){
                return currentUser.getValue().getUsername();
            }
            return "Log In";
        } );

        vocabList = mRepository.getVocabQueue();
        addSource();
        /*
        Every time the mediatorVocabList changes, the currentNode is set to the first entry,
        and the values for the top progress bar and text are set,
        and the visibility for the different card types are set up.
         */
        currentNode = Transformations.map(mediatorVocabList, value -> {
            if (mediatorVocabList.getValue() != null){
                //reset the number of total cards for the activity (necessary for the progress bar because 1st time there are only 5 cards, then 10, then 15 then 20)
                if (!mRepository.isSetAtBeginning() && mediatorVocabList.getValue().size() !=0){
                    newWords =mediatorVocabList.getValue().size();
                    mRepository.setAtBeginning(true);
                }
                cardsFinished.setValue((int)(100 - (((double)mediatorVocabList.getValue().size())/newWords)*100)); //This determines the percentage bar for flashcard actvity

                cardsFinishedText.setValue("Cards Remaining: " + mediatorVocabList.getValue().size()); //This determines the cards left number

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

    /**
     * Observes the vocabList livedata to retrieve values in transformations
     */
    public void addSource(){
        if (mediatorVocabList.getValue()==null || mediatorVocabList.getValue().isEmpty()){
            try {
                mediatorVocabList.addSource(vocabList, value -> mediatorVocabList.setValue(value));
            }catch(Exception e){
                // System.out.println("Error: " + e);

            }
        }
    }

    public void removeMediatorSource(){
        mediatorVocabList.removeSource(vocabList);
    }

    public void finishActivity(){
        navigateToMainActivity.call();
    }

    public void setMediatorVocabListValue(List<VocabModelA1> list) {
        this.mediatorVocabList.setValue(list);
    }


    /**
     * Resets the score for a card that was initially wrong but pressed I was right button
     */
    public void iWasright(){
        if (currentNode.getValue()!=null){
            currentNode.getValue().fixScore();
        }
        correct=true;
        moveToNextNode();
    }

    /**
     * Tests the answer against possible answers, sets the views, and moves to the next card
     */
    public void checkAnswer(){
        if (!finished && currentNode.getValue()!=null && currentNode.getValue().getStudying() != 0){
            boolean test = false;
            //test entered answer against all possible answers in the VocabModelA1
            if (currentNode.getValue().getScore()>50){
                //Test english -> german
                if ( currentNode.getValue().getGerman().equalsIgnoreCase(answer)){
                    test = true;
                }
            }else{
                //Test german -> english
                for (String s : currentNode.getValue().getEnglishStringsArray()){
                    if (s.equalsIgnoreCase(answer)) {
                        test = true;
                        break;
                    }
                }
            }

            if (test && !answer.equals("")) {
                currentNode.getValue().increaseScore();
                correct=true;
                setUpCorrectAnswerViews();
            } else if (!test && !answer.equals("")) {
                currentNode.getValue().decreaseScore();
                correct=false;
                setUpIncorrectAnswerViews();

            }else{
                //Do nothing because the edit text is empty. Prevents misclicks.
            }
        }else{
            moveToNextNode();
        }


    }

    private void setUpIncorrectAnswerViews(){
        xmarkVisibility.setValue(VISIBLE);
        iwasrightVisibility.setValue(VISIBLE);
        correctLayoutVisibility.setValue(VISIBLE);
        checkButtonText.setValue("Next");
        finished=true;
    }

    private void setUpCorrectAnswerViews(){
        checkmarkVisibility.setValue(VISIBLE);
        xmarkVisibility.setValue(GONE);
        //TODO : set linearLayout_correct to visible to show other answers?

        checkButtonText.setValue("Next");
        finished=true;
    }

    private void setUpViewsForNewCard(){
        englishTextVisibility.setValue(VISIBLE);
        mHintVisibility.setValue(VISIBLE);
        checkmarkVisibility.setValue(INVISIBLE);
        xmarkVisibility.setValue(INVISIBLE);
        checkButtonText.setValue("Next");
        iwasrightVisibility.setValue(GONE);
        correctLayoutVisibility.setValue(GONE);
        editTextVisibility.setValue(INVISIBLE);
        hintButtonVisibility.setValue(GONE);
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
        checkButtonText.setValue("Check");
        finished = false;
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

    /**
     * Sets the current word's studying value to 1, then either moves the card down the queue if wrong
     * or pops the node if correct.
     */
    private void moveToNextNode(){
        if (currentNode.getValue() != null && currentNode.getValue().getStudying()==0){
            currentNode.getValue().setStudying(1);
        }

        if (correct){
            popNode();
        }else{
            moveNode();
        }

        setAnswer("");

        //Activity is finished, update the local ROOM database
        if (mediatorVocabList.getValue() != null && mediatorVocabList.getValue().size()<=0){
            editTextVisibility.setValue(GONE);
            hintButtonVisibility.setValue(GONE);
            checkButtonVisibility.setValue(GONE);
            checkmarkVisibility.setValue(GONE);
            correctLayoutVisibility.setValue(GONE);
            iwasrightVisibility.setValue(GONE);
            xmarkVisibility.setValue(GONE);
            removeMediatorSource();
            finishButtonVisibility.setValue(VISIBLE);

            //If logged in, update remote database too
            if (currentUser.getValue()!=null){
               // update with remote database call
                mRepository.updateAllNodes(true,userName);
            }else{
                //update without remote database call
                mRepository.updateAllNodes(false,userName);
            }
            mRepository.setAtBeginning(false);

            goodJobVisibility.setValue(VISIBLE);
            tv_wordsLearnedVisibility.setValue(VISIBLE);
            progressBar_wordsLearnedVisibility.setValue(VISIBLE);
            textView_wordsLearnedOutOfVisibility.setValue(VISIBLE);

            textView_wordsMasteredVisibility.setValue(VISIBLE);
            progressBar_wordsMasteredVisibility.setValue(VISIBLE);
            textView_wordsMasteredOutOfVisibility.setValue(VISIBLE);
        }
    }


    /**
     * Removes the top item from the mediatorLiveData list
     */
    private void popNode(){
        List<VocabModelA1> list = mediatorVocabList.getValue();
        try {
            if (currentNode.getValue()!=null){
                currentNode.getValue().increaseFrequency();
            }

            mRepository.getFinishedList().add(currentNode.getValue());
            assert list != null;
            list.remove(0);
        }catch(NullPointerException e){
         //   System.out.println("List is null");
        }
        mediatorVocabList.setValue(list);
    }

    /**
     * Moves the item in the mediatorLiveData list down a random number of indexes
     */
    private void moveNode(){
        List<VocabModelA1> list =mediatorVocabList.getValue();
        assert list != null;
        VocabModelA1 temp = list.get(0);
        int ranNum = 0;

        if (list.size() > 5){
           ranNum = 5;  //index 0-5+  between 3 and 5
        }
        else if (list.size() == 5){
           ranNum = 4;  //index 0-4 between 3 and 4
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


    //Getters and setters

    public LiveData<Integer> getWordsLearned(){return wordsLearned;}
    public LiveData<Integer> getWordsLearnedP(){ return wordsLearnedP;}
    public LiveData<Integer> getWordsMasteredP(){ return wordsMasteredP;}

    public LiveData<Integer> getWordsMax() {
        return wordsMax;
    }
    public LiveData<Integer> getWordsLearnedPercent() {
        return wordsLearnedPercent;
    }
    public LiveData<Integer> getWordsMastered() {
        return wordsMastered;
    }

    public LiveData<Integer> getCheckButtonVisibility() {
        return checkButtonVisibility;
    }
    public LiveData<Integer> getCardsFinished() {
        return cardsFinished;
    }

    public LiveData<String> getCardsFinishedText() {
        return cardsFinishedText;
    }
    public LiveData<Boolean> getNavigateToMainActivity(){
        return navigateToMainActivity;
    }
    public LiveData<Integer> getFinishButtonVisibility(){
        return finishButtonVisibility;
    }
    public LiveData<Integer> getTv_wordsLearnedVisibility(){
        return tv_wordsLearnedVisibility;
    }
    public LiveData<Integer> getGoodJobVisibility(){
        return goodJobVisibility;
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
    public LiveData<Integer> getTextView_wordsMasteredVisibility(){
        return textView_wordsMasteredVisibility;
    }
    public LiveData<Integer> getProgressBar_wordsMasteredVisibility(){
        return progressBar_wordsMasteredVisibility;
    }
    public LiveData<Integer> getTextView_wordsMasteredOutOfVisibility(){
        return textView_wordsMasteredOutOfVisibility;
    }

    public LiveData<Integer> getTextView_wordsLearnedOutOfVisibility(){
        return textView_wordsLearnedOutOfVisibility;
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

    public LiveData<Integer> getProgressBar_wordsLearnedVisibility(){
        return progressBar_wordsLearnedVisibility;
    }

    public void showSentence(){
        if (mHintVisibility.getValue() != null && mHintVisibility.getValue() == VISIBLE){mHintVisibility.setValue(INVISIBLE);}
        else if (mHintVisibility.getValue() != null && mHintVisibility.getValue() == INVISIBLE){mHintVisibility.setValue(VISIBLE);}
    }

    public LiveData<List<VocabModelA1>> getMediatorVocabList(){
        return mediatorVocabList;
    }



    public LiveData<VocabModelA1> getCurrentNode(){
        return currentNode;
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
