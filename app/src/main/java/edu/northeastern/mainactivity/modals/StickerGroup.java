package edu.northeastern.mainactivity.modals;

import java.util.List;

public class StickerGroup {
    private String groupName;
    private List<String> imageUrls;

    public StickerGroup() {
        // Default constructor required for Firebase database
    }

    public StickerGroup(String groupName, List<String> imageUrls) {
        this.groupName = groupName;
        this.imageUrls = imageUrls;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}