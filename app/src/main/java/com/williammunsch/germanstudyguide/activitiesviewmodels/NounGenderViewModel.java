package com.williammunsch.germanstudyguide.activitiesviewmodels;

import android.os.CountDownTimer;
import android.view.View;

import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;
import com.williammunsch.germanstudyguide.repositories.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
/**
 * Handles all of the livedata objects and logic for the noun gender activity.
 */
public class NounGenderViewModel extends ViewModel implements Observable {
   // private NounGenderRepository nounGenderRepository;
    private PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();
    private Random random = new Random();
    private final MutableLiveData<Integer> checkDerVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> checkDieVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> checkDasVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> exDerVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> exDieVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> exDasVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> articleVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> dasButtonVisibility = new MutableLiveData<>(View.VISIBLE);
    private final MutableLiveData<Integer> derButtonVisibility = new MutableLiveData<>(View.VISIBLE);
    private final MutableLiveData<Integer> dieButtonVisibility = new MutableLiveData<>(View.VISIBLE);
    int numberOfWordsTried =0;

    private final MutableLiveData<String> accuracyText = new MutableLiveData<>();
    private final LiveData<List<VocabModelA1>> vocabList;
    private final MediatorLiveData<List<VocabModelA1>> mediatorVocabList = new MediatorLiveData<>();
    private Repository mRepository;
    private final LiveData<VocabModelA1> currentNode;
    private boolean setAtBeginning = false;
    private double totalWords = 20;
    private double wordsCorrect =0;
    private double wordsTotal = 0;

    private boolean checkedCorrect = false;
    private boolean gotItWrong = false;

    @Inject
    public NounGenderViewModel(Repository repository){
        this.mRepository = repository;
        vocabList = mRepository.getNounQueue();
         /*
        Every time the mediatorVocabList changes, the currentNode is set to the first entry,
        and the values for the top progress bar and text are set,
        and the visibility for the different card types are set up.
         */
        currentNode = Transformations.map(mediatorVocabList, value -> {
            if (mediatorVocabList.getValue() != null){
                if (!setAtBeginning && mediatorVocabList.getValue().size() !=0){
                    totalWords =mediatorVocabList.getValue().size();
                    List<VocabModelA1> list = mediatorVocabList.getValue();
                    Collections.shuffle(list);
                    mediatorVocabList.setValue(list);
                    setAtBeginning=true;
                }

                accuracyText.setValue("Accuracy: " + (int)((wordsCorrect/wordsTotal)*100) +"%"); //This determines the cards left number
                if (mediatorVocabList.getValue().size()>0){
                    return mediatorVocabList.getValue().get(0);
                }
                return null;
            }else{
                return null;
            }
        });

        addSource();
    }

    public void addSource(){
        if (mediatorVocabList.getValue()==null || mediatorVocabList.getValue().isEmpty()){
            try {
                mediatorVocabList.addSource(vocabList, value -> mediatorVocabList.setValue(value));
            }catch(Exception e){
                //  System.out.println("Error: " + e);

            }
        }
    }

    /**
     * Checks to see if Der was the correct choice for the given noun,
     * then hides the other buttons for 1 sec if correct and pops the node
     */
    public void checkAnswerDer(){
        if (currentNode!= null && currentNode.getValue()!= null && currentNode.getValue().getArticle() != null && !checkedCorrect){
            String der = "der";
            if (der.equalsIgnoreCase(currentNode.getValue().getArticle())){
                checkedCorrect = true;
               // System.out.println("CORRECT!");
                checkDerVisibility.setValue(View.VISIBLE);
                dieButtonVisibility.setValue(View.INVISIBLE);
                dasButtonVisibility.setValue(View.INVISIBLE);
                exDieVisibility.setValue(View.GONE);
                exDasVisibility.setValue(View.GONE);
                articleVisibility.setValue(View.VISIBLE);
                new CountDownTimer(1000, 1000) {
                    public void onFinish() {
                        popNode();
                        dieButtonVisibility.setValue(View.VISIBLE);
                        dasButtonVisibility.setValue(View.VISIBLE);
                        articleVisibility.setValue(View.GONE);
                    }

                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                    }
                }.start();
            }else{
               // System.out.println("WRONG!");
                exDerVisibility.setValue(View.VISIBLE);
                gotItWrong = true;
            }
        }
    }

    /**
     * Checks to see if Die was the correct choice for the given noun,
     * then hides the other buttons for 1 sec if correct and pops the node
     */
    public void checkAnswerDie(){
        if (currentNode!= null && currentNode.getValue()!= null && currentNode.getValue().getArticle() != null  && !checkedCorrect){
            String der = "die";
            if (der.equalsIgnoreCase(currentNode.getValue().getArticle())){
                 checkedCorrect = true;
              //  System.out.println("CORRECT!");
                checkDieVisibility.setValue(View.VISIBLE);
                derButtonVisibility.setValue(View.INVISIBLE);
                dasButtonVisibility.setValue(View.INVISIBLE);
                exDerVisibility.setValue(View.GONE);
                exDasVisibility.setValue(View.GONE);
                articleVisibility.setValue(View.VISIBLE);
                new CountDownTimer(1000, 1000) {
                    public void onFinish() {
                        popNode();
                        derButtonVisibility.setValue(View.VISIBLE);
                        dasButtonVisibility.setValue(View.VISIBLE);
                        articleVisibility.setValue(View.GONE);
                    }

                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                    }
                }.start();
            }else{
              //  System.out.println("WRONG!");
                exDieVisibility.setValue(View.VISIBLE);
                gotItWrong = true;
            }
        }
    }

    /**
     * Checks to see if Das was the correct choice for the given noun,
     * then hides the other buttons for 1 sec if correct and pops the node
     */
    public void checkAnswerDas(){
        if (currentNode!= null && currentNode.getValue()!= null && currentNode.getValue().getArticle() != null  && !checkedCorrect){
            String der = "das";
            if (der.equalsIgnoreCase(currentNode.getValue().getArticle())){
                checkedCorrect = true;
               // System.out.println("CORRECT!");
                checkDasVisibility.setValue(View.VISIBLE);
                derButtonVisibility.setValue(View.INVISIBLE);
                dieButtonVisibility.setValue(View.INVISIBLE);
                exDieVisibility.setValue(View.GONE);
                exDerVisibility.setValue(View.GONE);
                articleVisibility.setValue(View.VISIBLE);
                new CountDownTimer(1000, 1000) {
                    public void onFinish() {
                        popNode();
                        derButtonVisibility.setValue(View.VISIBLE);
                        dieButtonVisibility.setValue(View.VISIBLE);
                        articleVisibility.setValue(View.GONE);
                    }

                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                    }
                }.start();
            }else{
               // System.out.println("WRONG!");
                exDasVisibility.setValue(View.VISIBLE);
                gotItWrong = true;
            }
        }
    }



    private void popNode(){
        List<VocabModelA1> list = mediatorVocabList.getValue();
        try {
            VocabModelA1 temp = list.get(0);
            list.remove(0);
            list.add(temp);
        }catch(NullPointerException e){
           // System.out.println("List is null");
        }
        resetViews();

        numberOfWordsTried++;
        if (numberOfWordsTried >=50){
            Collections.shuffle(list);
        }

        if (!gotItWrong) {
            wordsCorrect++;
        }
        wordsTotal++;
        mediatorVocabList.setValue(list);
        checkedCorrect = false;
        gotItWrong = false;

    }

    //Move the node down a random number
    private void moveNode(){
        List<VocabModelA1> list = mediatorVocabList.getValue();
        try{
            if (list.size()>1){
                VocabModelA1 temp = list.get(0);
                list.remove(0);
                //int randomNum = random.nextInt(4)+4;
                list.add(temp);
            }

        }catch(Exception e){
          //  System.out.println("null");
        }
        mediatorVocabList.setValue(list);
    }

    public void resetViews(){
        checkDasVisibility.setValue(View.GONE);
        checkDieVisibility.setValue(View.GONE);
        checkDerVisibility.setValue(View.GONE);
        exDasVisibility.setValue(View.GONE);
        exDerVisibility.setValue(View.GONE);
        exDieVisibility.setValue(View.GONE);
    }

    //Getters and setters

    public MutableLiveData<Integer> getCheckDerVisibility() {
        return checkDerVisibility;
    }
    public LiveData<VocabModelA1> getCurrentNode(){
        return currentNode;
    }
    public LiveData<String> getAccuracyText() {
        return accuracyText;
    }

    public MutableLiveData<Integer> getCheckDieVisibility() {
        return checkDieVisibility;
    }

    public MutableLiveData<Integer> getCheckDasVisibility() {
        return checkDasVisibility;
    }

    public MutableLiveData<Integer> getExDerVisibility() {
        return exDerVisibility;
    }

    public MutableLiveData<Integer> getExDieVisibility() {
        return exDieVisibility;
    }

    public MutableLiveData<Integer> getExDasVisibility() {
        return exDasVisibility;
    }
    public MutableLiveData<Integer> getDasButtonVisibility() {
        return dasButtonVisibility;
    }

    public MutableLiveData<Integer> getDerButtonVisibility() {
        return derButtonVisibility;
    }

    public MutableLiveData<Integer> getDieButtonVisibility() {
        return dieButtonVisibility;
    }
    public MutableLiveData<Integer> getArticleVisibility() {
        return articleVisibility;
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
