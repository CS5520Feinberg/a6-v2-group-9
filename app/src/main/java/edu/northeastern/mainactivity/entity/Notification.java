package edu.northeastern.mainactivity.entity;

public class Notification {
    String sender;
    String receiver;
    String timeStamp;
    String stickerUrl;

    public Notification(String sender, String receiver, String timeStamp, String stickerUrl) {
        this.sender = sender;
        this.receiver = receiver;
        this.timeStamp = timeStamp;
        this.stickerUrl = stickerUrl;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStickerUrl() {
        return stickerUrl;
    }

    public void setStickerUrl(String stickerUrl) {
        this.stickerUrl = stickerUrl;
    }
}
