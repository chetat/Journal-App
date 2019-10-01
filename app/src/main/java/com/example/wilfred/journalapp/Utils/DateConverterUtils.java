package com.example.wilfred.journalapp.Utils;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Created by wilfred on 6/26/18.
 */

public class DateConverterUtils {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


}
