package edu.northeastern.mainactivity.modals;

public class UserInfo {
    private String email;
    private String userID;

    public UserInfo(String email, String userID) {
        this.email = email;
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public String getUserID() {
        return userID;
    }
}
