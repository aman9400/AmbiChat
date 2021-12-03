package com.ambiguous.ambichat.model;

public class ModelChat {

    String msg;
    String time;
    String userWho;

    public ModelChat(String msg, String time, String userWho) {
        this.msg = msg;
        this.time = time;
        this.userWho = userWho;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserWho() {
        return userWho;
    }

    public void setUserWho(String userWho) {
        this.userWho = userWho;
    }
}
