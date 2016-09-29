package com.trivago.hackathon.spotrivagofy.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LastFmResponse
{
    private Artist artist;

    public Artist getArtist()
    {
        return artist;
    }

    public void setArtist(Artist artist)
    {
        this.artist = artist;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Artist
    {
        private Biography bio;

        public Biography getBio()
        {
            return bio;
        }

        public void setBio(Biography bio)
        {
            this.bio = bio;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Biography
    {
        private String summary;

        public String getSummary()
        {
            return summary;
        }

        public void setSummary(String summary)
        {
            this.summary = summary;
        }
    }
}
