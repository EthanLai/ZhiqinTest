package com.example.myapplication;

import java.util.Date;

public class Entity {

    public Long mTimeL;
    public Date mDate;
    public String mTime, mPrice, mQuality;

    public Entity(Long timeL, Date date, String price, String quality) {
        mTimeL = timeL;
        mDate = date;
        mPrice = price;
        mQuality = quality;
    }
}