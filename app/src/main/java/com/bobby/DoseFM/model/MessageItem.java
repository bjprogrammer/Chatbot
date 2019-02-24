package com.bobby.DoseFM.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;


//POJO class for network API call- send message response
@Generated("org.jsonschema2pojo")
public class MessageItem {

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public MessageData getData() {
        return data;
    }

    public void setData(MessageData data) {
        this.data = data;
    }

    @SerializedName("success")
    @Expose
    private int success;

    @SerializedName("errorMessage")
    @Expose
    private String error;

    @SerializedName("message")
    @Expose
    private MessageData data;

    public class MessageData{
        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @SerializedName("chatBotName")
        @Expose
        private String senderName;

        @SerializedName("message")
        @Expose
        private String message;
    }
}