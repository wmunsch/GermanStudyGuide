package com.williammunsch.germanstudyguide.viewmodels;

import android.app.Application;
import android.net.TrafficStats;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.transition.Visibility;

import com.williammunsch.germanstudyguide.datamodels.VocabModel;
import com.williammunsch.germanstudyguide.datamodels.Word;
import com.williammunsch.germanstudyguide.repositories.FlashcardRepository;
import com.williammunsch.germanstudyguide.repositories.Repository;
import com.williammunsch.germanstudyguide.repositories.WordRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class FlashcardViewModel extends ViewModel implements Observable {
    private FlashcardRepository mFlashcardRepository;
    private LiveData<List<VocabModel>> vocabList;

   // private String currentNode = "heehehehe";
    private String currentNodeAnswer = "lalala";
   // private String hintVisibility = "invisible";
   // private Visibility hintVisibility;
    //private boolean mHintVisibility = true;

   // private MutableLiveData<Integer> hintVisibility = new MutableLiveData<>();




  //  private Integer hintVisibility = VISIBLE;
    //private String hintVisibility = "VISIBLE";
   // private LiveData<Integer> hintVisibility;

    //TODO : Move all of these databinding values into the repository so it saves as a singleton
    //Keep track of hint sentence visibility in a LiveData for data binding.
    /*
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
    private ArrayList<VocabModel> finishedList = new ArrayList<>();

*/
    //private MutableLiveData<VocabModel> currentNode = new MutableLiveData<>();


    private PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();



    private String answer = "";
    private boolean finished = false;
    private boolean finishedWithActivity = false;

    private Random random = new Random();
    private boolean correct;

    //TODO : reset edittext to ""


    /**
     * Keeps track of the order of flashcards in the live data here,
     * moves the index positions around and removes them when necessary.
     */
    @Inject
    public FlashcardViewModel(FlashcardRepository flashcardRepository) {
        this.mFlashcardRepository = flashcardRepository;

        //vocabList = mFlashcardRepository.getVocabData();

       //Livedata does not get updated when removing a node from mediatorlivedata, until the activity is recreated ?
        //Needs to be observed or it does not get updated.
      // mediatorVocabList.addSource(vocabList, value -> mediatorVocabList.setValue(value));
        //currentNode.setValue(mediatorVocabList.getValue().get(0));

        /*
         * Allows the textview to use databinding without having to call observe in the activity.
         * First argument is the livedata where the value is coming from.
         * Second argument (return value) is the object that the currentNode livedata needs to hold (a VocabModel in this case).
         */
        //setUpViewsForNewCard();
       // checkButtonText.setValue("TUNAKTUNAKTAUNK");
        /*
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

         */


/*
        if (currentNode.getValue()!= null && currentNode.getValue().getStudying()==0){
            setUpViewsForNewCard();
        }else if (currentNode.getValue()!= null && currentNode.getValue().getStudying()==1){
            setUpViewsForOldCard();
        }else{
            setUpViewsForNewCard();
        }

        //setUpViewsForNewCard();

        /*
        englishTextVisibility.setValue(VISIBLE);
        mHintVisibility.setValue(VISIBLE);
        checkmarkVisibility.setValue(INVISIBLE);
        xmarkVisibility.setValue(INVISIBLE);
        checkButtonText.setValue("Next");
        iwasrightVisibility.setValue(GONE);
        correctLayoutVisibility.setValue(GONE);
        editTextVisibility.setValue(INVISIBLE);
        hintButtonVisibility.setValue(INVISIBLE);

         */


    }
    /*
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
        setAnswer("");
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

     */


    public LiveData<Integer> getEnglishTextVisibility(){
        return mFlashcardRepository.getEnglishTextVisibility();
    }
    public LiveData<Integer> getHintButtonVisibility(){
        return mFlashcardRepository.getHintButtonVisibility();
    }
    public LiveData<Integer> getEditTextVisibility(){
        return mFlashcardRepository.getEditTextVisibility();
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


    public void showSentence(){
        mFlashcardRepository.showSentence();
    }

    public LiveData<List<VocabModel>> getMediatorVocabList(){
        return mFlashcardRepository.getMediatorVocabList();
    }



    public LiveData<VocabModel> getCurrentNode(){
        return mFlashcardRepository.getCurrentNode();
    }



    public void checkAnswer(){
       // moveToNextNode();
       // System.out.println("currentNode = " + currentNode.getValue());
            if (!mFlashcardRepository.isFinished()&& mFlashcardRepository.getCurrentNode().getValue()!=null && mFlashcardRepository.getCurrentNode().getValue().getStudying() != 0){
                System.out.println("Checking answer");
                System.out.println("ANSWERtext IS : " + answer + "   currect answer: " + mFlashcardRepository.getCurrentNode().getValue().getEnglish());
                //System.out.println("ANSWERtext IS : " + answer + "   currect answer: " + mediatorVocabList.getValue().get(0).getEnglish());
                boolean test = false;

                //test entered answer against all possible answers in the VocabModel
                for (String s : mFlashcardRepository.getCurrentNode().getValue().getEnglishStringsArray()){//mediatorVocabList.getValue().get(0).getEnglishStringsArray()){
                    if (s.equalsIgnoreCase(answer)){
                        test = true;
                    }
                }

                if (test && !answer.equals("")) {
                    System.out.println("CORRECT");
                    // mediatorVocabList.getValue().get(0).setScore(mediatorVocabList.getValue().get(0).getScore()+5);
                   // currentNode.getValue().setScore(currentNode.getValue().getScore()+5);
                    mFlashcardRepository.getCurrentNode().getValue().increaseScore();
                    //mFlashcardRepository.updateNode(mediatorVocabList.getValue().get(0));
                    mFlashcardRepository.setCorrect(true);
                    setUpCorrectAnswerViews();

                } else if (!test && !answer.equals("")) {
                    System.out.println("INCORRECT");
                    //currentNode.getValue().setScore(currentNode.getValue().getScore()-5);
                    mFlashcardRepository.getCurrentNode().getValue().decreaseScore();
                    //mediatorVocabList.getValue().get(0).setScore(mediatorVocabList.getValue().get(0).getScore()-5);
                    // mFlashcardRepository.updateNode(mediatorVocabList.getValue().get(0));
                    mFlashcardRepository.setCorrect(false);//correct = false;
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
        mFlashcardRepository.setFinished(true);//finished = true;

    }

    private void moveToNextNode(){
        if (finishedWithActivity){
           // finish();
        }
        System.out.println("is it correct?" + correct);
        //popNode();
        if (mFlashcardRepository.getCurrentNode().getValue() != null && mFlashcardRepository.getCurrentNode().getValue().getStudying()==0){
            mFlashcardRepository.getCurrentNode().getValue().setStudying(1);
        }

        if (mFlashcardRepository.isCorrect()){
            popNode();
        }else{
            moveNode();
        }



        if (mFlashcardRepository.getMediatorVocabList().getValue()!= null &&mFlashcardRepository.getMediatorVocabList().getValue().size()<=0){
            /*System.out.println("FINISHED WITH ACTIVITY");
            for (int i = 0 ; i < finishedList.size();i++){
                System.out.println(finishedList.get(i).toString() + " " + finishedList.get(i).getScore());
            }*/
            mFlashcardRepository.setEditTextVisibility(GONE);//editTextVisibility.setValue(GONE);
            mFlashcardRepository.setHintButtonVisibility(GONE);//hintButtonVisibility.setValue(GONE);
            mFlashcardRepository.setCheckButtonText("Finish");//checkButtonText.setValue("Finish");
            mFlashcardRepository.setFinishedWithActivity(true);//finishedWithActivity = true;
            mFlashcardRepository.removeMediatorSource();//mediatorVocabList.removeSource(vocabList);
            mFlashcardRepository.updateAllNodes();
            //TODO : Display stats from the activity.
        }
    }


    /**
     * Removes the top item from the mediatorLiveData list
     */

    //TODO : fix this giving indexoutofbounds exception when lists are size 0
    private void popNode(){
        List<VocabModel> list = mFlashcardRepository.getMediatorVocabList().getValue();//mediatorVocabList.getValue();
        try {
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

    public void moveNode(){
        List<VocabModel> list =mFlashcardRepository.getMediatorVocabList().getValue();// mediatorVocabList.getValue();
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

        mFlashcardRepository.setMediatorVocabListValue(list);//mediatorVocabList.setValue(list);
    }
/*
    private void updateAllNodes(){
        System.out.println("UPDATING ALL NODES");
        for (int i = 0 ; i < finishedList.size();i++){
            mFlashcardRepository.updateNode(finishedList.get(i));
        }
    }

 */

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


/*

   /* Using lambda for simplicity instead of the code below
        mediatorVocabList.addSource(vocabList, new Observer<List<VocabModel>>() {
            @Override
            public void onChanged(List<VocabModel> vocabModels) {
                mediatorVocabList.setValue(vocabModels);
            }
        });

    public void setCurrentNode(){
      //  this.currentNode.setValue(mediatorVocabList.getValue().get(0));
    }

        /**
     *
     * @return The MediatorLiveData as LiveData so it cant be changed from the activity

    public LiveData<List<VocabModel>> getMediatorVocabList(){
    //return vocabList2;
    return mediatorVocabList;
}

    public LiveData<List<VocabModel>> getVocabList(){
        return vocabList;
    }

 */
