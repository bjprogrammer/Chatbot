package com.bobby.DoseFM.model;

//Restructured message data with time, sender name and message
public class Message{
    public Message(){}
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

    private String senderName;
    private String message;

    public Long getDate() {
        return Date;
    }

    public void setDate(Long date) {
        Date = date;
    }

    private Long Date;
}
