package com.williammunsch.germanstudyguide.repositories;

import android.graphics.Color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;

import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;
import javax.inject.Inject;
/**
 * Stores the data for the noun gender activity.
 * Stores and operates on the list containing nouns and their genders.
 */
@Singleton
public class NounGenderRepository {
    private MutableLiveData<String> accuracyText = new MutableLiveData<>();
    private LiveData<List<VocabModelA1>> vocabList;
    private MediatorLiveData<List<VocabModelA1>> mediatorVocabList = new MediatorLiveData<>();
    private Repository mRepository;
    private LiveData<VocabModelA1> currentNode;
    private boolean setAtBeginning = false;
    private double totalWords = 20;
    private double wordsCorrect =0;
    private double wordsTotal = 0;

    @Inject
    public NounGenderRepository(Repository repository){
        this.mRepository = repository;
        vocabList = mRepository.getmVocabDao().getNounQueue();
       // vocabList = mRepository.getmVocabDao().getVocabQueue();

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
                System.out.println(wordsCorrect + " / " + wordsTotal);
                //for (int i = 0;i<mediatorVocabList.getValue().size();i++){
               //     System.out.println(mediatorVocabList.getValue().get(i).getGerman());
               // }


                if (mediatorVocabList.getValue().size()>0){
                    //setupViews();
                    return mediatorVocabList.getValue().get(0);
                }else{
                    System.out.println("EMPTY");
                    //vocabList = mRepository.getmVocabDao().getNounQueue();
                    //return mediatorVocabList.getValue().get(0);
                    //mediatorVocabList.getValue().add(mRepository.getmVocabDao().getOneNoun());
                }
                return null;
            }else{
                return null;
            }
        });

        addSource();

    }



    public void addWordTotal(){
        this.wordsTotal++;
    }
    public void addWordCorrect(){
        this.wordsCorrect++;
    }

    public void addSource(){
        if (mediatorVocabList.getValue()==null || mediatorVocabList.getValue().isEmpty()){
            System.out.println("*\n*\nADDING SOURCE\n*\n*");
            try {
                mediatorVocabList.addSource(vocabList, value -> mediatorVocabList.setValue(value));
            }catch(Exception e){
                System.out.println("Error: " + e);

            }
        }
    }
    public void setMediatorVocabListValue(List<VocabModelA1> list) {
        this.mediatorVocabList.setValue(list);
    }
    public LiveData<List<VocabModelA1>> getMediatorVocabList(){
        return mediatorVocabList;
    }
    public LiveData<VocabModelA1> getCurrentNode(){
        return currentNode;
    }
    public LiveData<String> getAccuracyText() {
        return accuracyText;
    }


}
