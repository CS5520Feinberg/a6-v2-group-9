package edu.northeastern.mainactivity.modals;

public class Message {
    private String sender;
    private String receiver;
    private long timestamp;
    private String imageUrl;

    public Message() {
        // Default construcreceiverr required for Firebase database
    }

    public Message(String sender, String receiver, long timestamp, String imageUrl) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }

    public String getsender() {
        return sender;
    }

    public void setsender(String sender) {
        this.sender = sender;
    }

    public String getreceiver() {
        return receiver;
    }

    public void setreceiver(String receiver) {
        this.receiver = receiver;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
