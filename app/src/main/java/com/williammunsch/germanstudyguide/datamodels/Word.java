package com.williammunsch.germanstudyguide.datamodels;

/**
 * Data model for the words used in the flashcard activity.
 */

public class Word {
    private int score, id, freq, type, studying; //type determines what type of activity. 0=show english, 1=test german->english, 2=test english->german
    private String german;
    private String english;
    private String gsentence;
    private String esentence;
    private Word next;
    private String[] englishStringsArray;

    public Word(int id, String german, String english, String gsent, String esent, int score, int freq, int studying){
        this.id = id;
        this.score = score;
        this.freq = freq;
        this.german = german;
        this.english = english;
        this.gsentence = gsent;
        this.esentence = esent;
        type = 0;
        this.studying = studying;
        if (english!=null){
            this.englishStringsArray = english.split(",");
        }
    }

    public int getStudying(){return studying;}

    public void setType(int type){this.type = type;}

    public int getType(){return type;}

    public Word getNext() {
        return next;
    }

    public void setNext(Word next) {
        this.next = next;
    }

    //public String getEnglish(){return english;}

    public void setEnglish(String e){this.english = e;}

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public String getGerman() {
        return german;
    }

    public void setGerman(String german) {
        this.german = german;
    }

    public String getGSentence() {
        return gsentence;
    }

    public void setGSentence(String gsentence) {
        this.gsentence = gsentence;
    }

    public String getEsentence() {
        return esentence;
    }

    public void setEsentence(String esentence) {
        this.esentence = esentence;
    }

    public String[] getEnglishStringsArray() {
        return englishStringsArray;
    }

    public void setEnglishStringsArray(String[] englishStringsArray) {
        this.englishStringsArray = englishStringsArray;
    }

    public String getEnglish(){
        String t = "";
        for (int i = 0 ; i < englishStringsArray.length; i++){
            t = t +englishStringsArray[i];
            if (englishStringsArray.length>1 && i<englishStringsArray.length-1){t = t + ", ";}
        }
        return t;
    }
}
