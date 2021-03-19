package com.example.myapp;

public class questions {
    private int answerResid;
    private  String answertrue;

    public int getAnswerResid() {
        return answerResid;
    }

    public void setAnswerResid(int answerResid) {
        this.answerResid = answerResid;
    }

    public String getAnswertrue() {
        return answertrue;
    }

    public void setAnswertrue(String answertrue) {
        this.answertrue = answertrue;
    }

    public questions(int answerResid, String answertrue) {
        this.answerResid = answerResid;
        this.answertrue = answertrue;
    }
}
