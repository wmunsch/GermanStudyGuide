package com.williammunsch.germanstudyguide.viewmodels;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;

import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;
import com.williammunsch.germanstudyguide.repositories.NounGenderRepository;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
/**
 * Handles all of the view data for the noun gender activity.
 */
public class NounGenderViewModel extends ViewModel implements Observable {
    private NounGenderRepository nounGenderRepository;
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



    private boolean checkedCorrect = false;
    private boolean gotItWrong = false;

    @Inject
    public NounGenderViewModel(NounGenderRepository nounGenderRepository){
        this.nounGenderRepository = nounGenderRepository;
    }

    public void checkAnswerDer(){
        if (nounGenderRepository.getCurrentNode()!= null && nounGenderRepository.getCurrentNode().getValue()!= null && nounGenderRepository.getCurrentNode().getValue().getArticle() != null && !checkedCorrect){
            String der = "der";
            if (der.equalsIgnoreCase(nounGenderRepository.getCurrentNode().getValue().getArticle())){
                checkedCorrect = true;
                System.out.println("CORRECT!");
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
                System.out.println("WRONG!");
                exDerVisibility.setValue(View.VISIBLE);
                gotItWrong = true;
            }
        }
    }

    public void checkAnswerDie(){
        if (nounGenderRepository.getCurrentNode()!= null && nounGenderRepository.getCurrentNode().getValue()!= null && nounGenderRepository.getCurrentNode().getValue().getArticle() != null  && !checkedCorrect){
            String der = "die";
            if (der.equalsIgnoreCase(nounGenderRepository.getCurrentNode().getValue().getArticle())){
                 checkedCorrect = true;
                System.out.println("CORRECT!");
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
                System.out.println("WRONG!");
                exDieVisibility.setValue(View.VISIBLE);
                gotItWrong = true;
            }
        }
    }

    public void checkAnswerDas(){
        if (nounGenderRepository.getCurrentNode()!= null && nounGenderRepository.getCurrentNode().getValue()!= null && nounGenderRepository.getCurrentNode().getValue().getArticle() != null  && !checkedCorrect){
            String der = "das";
            if (der.equalsIgnoreCase(nounGenderRepository.getCurrentNode().getValue().getArticle())){
                checkedCorrect = true;
                System.out.println("CORRECT!");
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
                System.out.println("WRONG!");
                exDasVisibility.setValue(View.VISIBLE);
                gotItWrong = true;
            }
        }
    }


    private void popNode(){
        List<VocabModelA1> list = nounGenderRepository.getMediatorVocabList().getValue();
        try {
            VocabModelA1 temp = list.get(0);
            list.remove(0);
            list.add(temp);
        }catch(NullPointerException e){
            System.out.println("List is null");
        }
        resetViews();

        numberOfWordsTried++;
        if (numberOfWordsTried >=50){
            Collections.shuffle(list);
        }

        if (!gotItWrong) {
            nounGenderRepository.addWordCorrect();
        }
        nounGenderRepository.addWordTotal();
        nounGenderRepository.setMediatorVocabListValue(list);
        checkedCorrect = false;
        System.out.println("Size is : " + list.size());
        gotItWrong = false;

    }

    //Move the node down a random number
    private void moveNode(){
        List<VocabModelA1> list = nounGenderRepository.getMediatorVocabList().getValue();
        try{
            if (list.size()>1){
                VocabModelA1 temp = list.get(0);
                list.remove(0);
                //int randomNum = random.nextInt(4)+4;
                list.add(temp);
            }

        }catch(Exception e){
            System.out.println("null");
        }
        nounGenderRepository.setMediatorVocabListValue(list);
    }

    public void resetViews(){
        checkDasVisibility.setValue(View.GONE);
        checkDieVisibility.setValue(View.GONE);
        checkDerVisibility.setValue(View.GONE);
        exDasVisibility.setValue(View.GONE);
        exDerVisibility.setValue(View.GONE);
        exDieVisibility.setValue(View.GONE);
    }

    public MutableLiveData<Integer> getCheckDerVisibility() {
        return checkDerVisibility;
    }
    public LiveData<VocabModelA1> getCurrentNode(){
        return nounGenderRepository.getCurrentNode();
    }
    public LiveData<String> getAccuracyText() {
        return nounGenderRepository.getAccuracyText();
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
