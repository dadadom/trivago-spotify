package com.trivago.hackathon.spotrivagofy.api;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class HotelRecommendation
{
    private int itemId;
    private String hotelName;

    public int getItemId()
    {
        return itemId;
    }

    public void setItemId(int itemId)
    {
        this.itemId = itemId;
    }

    public String getHotelName()
    {
        return hotelName;
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }
}
