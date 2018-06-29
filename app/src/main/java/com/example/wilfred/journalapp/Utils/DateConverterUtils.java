package com.example.wilfred.journalapp.Utils;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by wilfred on 6/26/18.
 */

public class DateConverterUtils {
    @TypeConverter
    public static Date toDate(Long timestamp){
        if (timestamp == null){
            return null;
        }else {
            return new Date(timestamp);
        }
    }
    @TypeConverter
    public static Long toTimeStamp(Date date){
        if (date == null){
            return null;
        }else {
            return date.getTime();
        }
    }
}
