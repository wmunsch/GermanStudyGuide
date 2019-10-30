package com.williammunsch.germanstudyguide;

public class SaveDataResponse {
    private String freqList;
    private String scoreList;
    private String studyingList;

    public SaveDataResponse(String freqList, String scoreList, String studyingList) {
        this.freqList = freqList;
        this.scoreList = scoreList;
        this.studyingList = studyingList;
    }

    public String getFreqList() {
        return freqList;
    }

    public String getScoreList() {
        return scoreList;
    }

    public String getStudyingList() {
        return studyingList;
    }
}
