package com.example.myapplication.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String App_ID = "418194c707960433ceaa705bdc47549e";
    public static Location current_location=null;

    public static String convertUnixTime(int dt){
        Date date=new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH : mm EEE MM yyyy");
        String formated=simpleDateFormat.format(date);
        return formated;
    }
}
