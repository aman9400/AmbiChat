package com.ambiguous.ambichat.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInResponse extends BaseResponse {

    @SerializedName("result")
    @Expose
    private ResultBean results;

    public ResultBean getResults() {
        return results;
    }

    public void setResults(ResultBean results) {
        this.results = results;
    }

    public static class ResultBean {
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String message;

    }

}
