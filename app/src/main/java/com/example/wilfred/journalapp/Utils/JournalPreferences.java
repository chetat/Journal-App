package com.example.wilfred.journalapp.Utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by wilfred on 7/1/18.
 */

public class JournalPreferences {
    private int displayPrefValue;

    public void setDisplayPrefValue(int displayPrefValue) {
        this.displayPrefValue = displayPrefValue;
    }

    public int getDisplayPrefValue() {
        return displayPrefValue;
    }

    public void setLayout(Context context){
       if (getDisplayPrefValue() == 1){
       }
    }
}
