package com.trivago.hackathon.spotrivagofy.api;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class HotelRecommendation
{
    private int itemId;
    private String hotelName;
    private boolean error = true;

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

    public boolean isError()
    {
        return error;
    }

    public void setError(boolean error)
    {
        this.error = error;
    }
}
