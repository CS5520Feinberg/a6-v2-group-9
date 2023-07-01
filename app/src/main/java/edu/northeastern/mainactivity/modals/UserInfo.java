package edu.northeastern.mainactivity.modals;

public class UserInfo {
    private String email;
    private String userID;
    private String deviceToken;

    public UserInfo(String email, String userID, String deviceToken) {
        this.email = email;
        this.userID = userID;
        this.deviceToken = deviceToken;
    }

    public String getEmail() {
        return email;
    }

    public String getUserID() {
        return userID;
    }

    public String getDeviceToken() {
        return deviceToken;
    }
}
