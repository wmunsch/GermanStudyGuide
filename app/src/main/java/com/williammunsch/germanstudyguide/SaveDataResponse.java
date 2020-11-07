package com.williammunsch.germanstudyguide;

public class SaveDataResponse {
    private String error;
    private String tablename;
    private String freq;
    private String score;
    private String studying;

    public SaveDataResponse(String tablename, String score, String freq, String studying) {
        this.tablename = tablename;
        this.freq = freq;
        this.score = score;
        this.studying = studying;

    }

    public String getFreq() {
        return freq;
    }

    public String getScore() {
        return score;
    }

    public String getStudying() {
        return studying;
    }

    public String toString() {return tablename + "\n" + score + "\n" + freq + "\n" + studying;}
}
