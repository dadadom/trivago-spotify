package com.trivago.hackathon.spotrivagofy.api;

import java.util.List;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class ToursRequest
{
    private List<Tour> tours;

    public List<Tour> getTours()
    {
        return tours;
    }

    public void setTours(List<Tour> tours)
    {
        this.tours = tours;
    }

    /**
     * Created by Dominik Sandjaja on 29/09/16.
     */
    public static class Tour
    {
        private String city;
        private String date;
        private String artist;

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
    }
}
