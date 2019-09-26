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

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class FlashcardViewModel extends ViewModel implements Observable {
    private FlashcardRepository mFlashcardRepository;
    private LiveData<List<VocabModel>> vocabList;
    private MediatorLiveData<List<VocabModel>> mediatorVocabList = new MediatorLiveData<>();
   // private String currentNode = "heehehehe";
    private String currentNodeAnswer = "lalala";
   // private String hintVisibility = "invisible";
   // private Visibility hintVisibility;
    //private boolean mHintVisibility = true;

   // private MutableLiveData<Integer> hintVisibility = new MutableLiveData<>();

    private LiveData<VocabModel> currentNode;// = new MutableLiveData<>();


  //  private Integer hintVisibility = VISIBLE;
    //private String hintVisibility = "VISIBLE";
   // private LiveData<Integer> hintVisibility;

    //Keep track of hint sentence visibility in a LiveData for data binding.
    private MutableLiveData<Integer> mHintVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> checkmarkVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> xmarkVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> iwasrightVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> correctLayoutVisibility = new MutableLiveData<>();
    private MutableLiveData<String> checkButtonText = new MutableLiveData<>();
    private MutableLiveData<Integer> editTextVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> hintButtonVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> englishTextVisibility = new MutableLiveData<>();


    //private MutableLiveData<VocabModel> currentNode = new MutableLiveData<>();


    private PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

    private ArrayList<VocabModel> finishedList = new ArrayList<>();

    private String answer = "";
    private boolean finished = false;

    private Random random = new Random();
    private boolean correct;
    private String textToShow;

    //TODO : make incorrect stuff invisible when moving to next node

    /**
     * Keeps track of the order of flashcards in the live data here,
     * moves the index positions around and removes them when necessary.
     */
    @Inject
    public FlashcardViewModel(FlashcardRepository flashcardRepository) {
        this.mFlashcardRepository = flashcardRepository;

        vocabList = mFlashcardRepository.getVocabData();

       //Livedata does not get updated when removing a node from mediatorlivedata, until the activity is recreated ?
        //Needs to be observed or it does not get updated.
       mediatorVocabList.addSource(vocabList, value -> mediatorVocabList.setValue(value));
        //currentNode.setValue(mediatorVocabList.getValue().get(0));

        /*
         * Allows the textview to use databinding without having to call observe in the activity.
         * First argument is the livedata where the value is coming from.
         * Second argument (return value) is the object that the currentNode livedata needs to hold (a VocabModel in this case).
         */


        currentNode = Transformations.map(mediatorVocabList, value -> {
            if (mediatorVocabList.getValue() != null){
               //System.out.println("calling transformations.map currentNode = " + currentNode.getValue());
                if (mediatorVocabList.getValue().size()>0){
                    return mediatorVocabList.getValue().get(0);
                }
                    return null;
            }else{
                return null;//new VocabModel(0,null,null,null,null,0,0,0);
            }
        });




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

    public void setCheckmarkVisibility(){
        //checkmarkVisibility;
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



    public String getCurrentNodeAnswer(){
        return currentNodeAnswer;
    }

    public String getTextToShow(){
        return textToShow;
    }


    public LiveData<VocabModel> getCurrentNode(){
        return currentNode;
        /*
        MutableLiveData<VocabModel> node = new MediatorLiveData<>();
        if (mediatorVocabList.getValue() != null){
            node.setValue(mediatorVocabList.getValue().get(0));
        }
        return node;

         */
    }



    public void checkAnswer(){
       // moveToNextNode();
       // System.out.println("currentNode = " + currentNode.getValue());


            if (!finished && currentNode.getValue()!=null && currentNode.getValue().getStudying() != 0){
                System.out.println("Checking answer");
                System.out.println("ANSWERtext IS : " + answer + "   currect answer: " + currentNode.getValue().getEnglish());
                //System.out.println("ANSWERtext IS : " + answer + "   currect answer: " + mediatorVocabList.getValue().get(0).getEnglish());
                boolean test = false;

                //test entered answer against all possible answers in the VocabModel
                for (String s : currentNode.getValue().getEnglishStringsArray()){//mediatorVocabList.getValue().get(0).getEnglishStringsArray()){
                    if (s.equalsIgnoreCase(answer)){
                        test = true;
                    }
                }

                if (test && !answer.equals("")) {
                    System.out.println("CORRECT");
                    // mediatorVocabList.getValue().get(0).setScore(mediatorVocabList.getValue().get(0).getScore()+5);
                   // currentNode.getValue().setScore(currentNode.getValue().getScore()+5);
                    currentNode.getValue().increaseScore();
                    //mFlashcardRepository.updateNode(mediatorVocabList.getValue().get(0));
                    correct = true;
                    setUpCorrectAnswerViews();

                } else if (!test && !answer.equals("")) {
                    System.out.println("INCORRECT");
                    //currentNode.getValue().setScore(currentNode.getValue().getScore()-5);
                    currentNode.getValue().decreaseScore();
                    //mediatorVocabList.getValue().get(0).setScore(mediatorVocabList.getValue().get(0).getScore()-5);
                    // mFlashcardRepository.updateNode(mediatorVocabList.getValue().get(0));
                    correct = false;
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
        xmarkVisibility.setValue(VISIBLE);
        iwasrightVisibility.setValue(VISIBLE);
        correctLayoutVisibility.setValue(VISIBLE);

        checkButtonText.setValue("Next");
        finished = true;
    }

    private void setUpCorrectAnswerViews(){
        checkmarkVisibility.setValue(VISIBLE);
        //TODO : set linearLayout_correct to visible to show other answers?

        checkButtonText.setValue("Next");
        finished = true;

    }

    private void moveToNextNode(){
        System.out.println("is it correct?" + correct);
        //popNode();
        if (currentNode.getValue() != null && currentNode.getValue().getStudying()==0){
            currentNode.getValue().setStudying(1);
        }

        if (correct){
            popNode();
        }else{
            moveNode();
        }


        if (currentNode.getValue()!=null && currentNode.getValue().getStudying()==0){

        }else{
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


        if (mediatorVocabList.getValue()!= null && mediatorVocabList.getValue().size()<=0){
            System.out.println("FINISHED WITH ACTIVITY");
            for (int i = 0 ; i < finishedList.size();i++){
                System.out.println(finishedList.get(i).toString() + " " + finishedList.get(i).getScore());
            }
            editTextVisibility.setValue(GONE);
            hintButtonVisibility.setValue(GONE);
            //TODO : Set edit text to gone, hint button to gone, check button to gone.
            //TODO : Display stats from the activity.
        }
    }


    /**
     * Removes the top item from the mediatorLiveData list
     */
    private void popNode(){
        /*
        System.out.println("Before popping");
       // System.out.println("currentNode = " + currentNode.getValue());
        for (int i = 0 ; i < mediatorVocabList.getValue().size(); i++){
            System.out.println(mediatorVocabList.getValue().get(i));
        }

         */

        List<VocabModel> list = mediatorVocabList.getValue();
        try {
           // finishedList.add(list.get(0));
            finishedList.add(currentNode.getValue());
            list.remove(0);
        }catch(NullPointerException e){
            System.out.println("List is null");
        }
        mediatorVocabList.setValue(list);
       // currentNode

        /*
        System.out.println("After popping");
       // System.out.println("currentNode = " + currentNode.getValue());
        for (int i = 0 ; i < mediatorVocabList.getValue().size(); i++){
            System.out.println(mediatorVocabList.getValue().get(i));
        }

         */

    }

    /**
     * Moves the item in the mediatorLiveData list down a random number of indexes
     */
    public void moveNode(){
        List<VocabModel> list = mediatorVocabList.getValue();
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
