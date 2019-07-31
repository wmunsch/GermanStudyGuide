package com.williammunsch.germanstudyguide;

public class SimpleWord {
    private String german,english;
    private int score;
    public SimpleWord(String german, String english, int score){
        this.german=german;
        this.english=english;
        this.score=score;
    }

    public String getScore(){
        return Integer.toString(score);
    }

    public String getGerman(){
        return german;
    }

    public String getEnglish(){
        return english;
    }
}
