package com.example.aanandam.model;

public class Message_class {
    private String message,senderid,receiverid;
    private boolean isseen;

    public Message_class() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getisseen() {
        return isseen;
    }

    public void setis_seen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }
}
