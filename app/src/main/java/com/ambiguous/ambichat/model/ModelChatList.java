package com.ambiguous.ambichat.model;

public class ModelChatList {

    String name;
    String imageUrl;
    String msg;

    public ModelChatList(String name, String imageUrl, String msg) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
