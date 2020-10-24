package com.plll.myapplication;

public class TestClass {
    private String mQuestion;
    private String mkey;

    public TestClass() {
    }

    public TestClass(String mQuestion, String mkey) {
        this.mQuestion = mQuestion;
        this.mkey = mkey;
    }

    public String getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }
}
