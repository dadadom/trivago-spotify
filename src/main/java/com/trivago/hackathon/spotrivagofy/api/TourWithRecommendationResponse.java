package com.trivago.hackathon.spotrivagofy.api;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class TourWithRecommendationResponse
{
    private String city;
    private String date;
    private String artist;
    private HotelRecommendation hotelRecommendation;
    private String artistInformation;

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public HotelRecommendation getHotelRecommendation()
    {
        return hotelRecommendation;
    }

    public void setHotelRecommendation(HotelRecommendation hotelRecommendation)
    {
        this.hotelRecommendation = hotelRecommendation;
    }

    public void setArtistInformation(String artistInformation)
    {
        this.artistInformation = artistInformation;
    }

    public String getArtistInformation()
    {
        return artistInformation;
    }

    /**
     * Created by Dominik Sandjaja on 29/09/16.
     */
    public static class HotelRecommendation
    {
        private HotelsResponse.Hotel hotel;
        private boolean error = true;
        private String errorMessage;

        public HotelsResponse.Hotel getHotel()
        {
            return hotel;
        }

        public void setHotel(HotelsResponse.Hotel hotel)
        {
            this.hotel = hotel;
        }

        public boolean isError()
        {
            return error;
        }

        public void setError(boolean error)
        {
            this.error = error;
        }


        public void setErrorMessage(String errorMessage)
        {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage()
        {
            return errorMessage;
        }
    }
}
