package com.williammunsch.germanstudyguide;

public class Word {
    private int score, id, freq, type; //type determines what type of activity. 0=show english, 1=test german->english, 2=test english->german
    private String german;
    private String english;
    private String gsentence;
    private String esentence;
    private Word next;

    public Word(int id, int score, int freq, String german, String english, String gsentence, String esentence){
        this.id = id;
        this.score = score;
        this.freq = freq;
        this.german = german;
        this.english = english;
        this.gsentence = gsentence;
        this.esentence = esentence;
        type = 0;
    }

    public void setType(int type){this.type = type;}

    public int getType(){return type;}

    public Word getNext() {
        return next;
    }

    public void setNext(Word next) {
        this.next = next;
    }

    public String getEnglish(){return english;}

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

}