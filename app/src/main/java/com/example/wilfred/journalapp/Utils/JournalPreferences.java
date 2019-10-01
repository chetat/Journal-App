package com.example.wilfred.journalapp.Utils;

import android.content.Context;

/**
 * Created by wilfred on 7/1/18.
 */

public class JournalPreferences {
    private int displayPrefValue;

    public void setDisplayPrefValue(int displayPrefValue) {
        this.displayPrefValue = displayPrefValue;
    }

    private int getDisplayPrefValue() {
        return displayPrefValue;
    }

    public void setLayout(Context context){
    }
}
