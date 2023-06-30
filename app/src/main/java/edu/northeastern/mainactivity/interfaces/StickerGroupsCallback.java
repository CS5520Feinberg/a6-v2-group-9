package edu.northeastern.mainactivity.interfaces;

import com.google.firebase.database.DatabaseError;

import java.util.List;
import java.util.Map;

public interface StickerGroupsCallback {
    void onStickerGroupsLoaded(Map<String, List<String>> stickerGroups);
    void onStickerGroupsLoadFailed(DatabaseError databaseError);
}



