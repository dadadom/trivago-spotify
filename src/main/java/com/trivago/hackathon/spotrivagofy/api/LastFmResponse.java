package com.trivago.hackathon.spotrivagofy.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LastFmResponse
{
    private Integer error;
    private Artist artist;
    private String message;

    public Integer getError()
    {
        return error;
    }

    public void setError(Integer error)
    {
        this.error = error;
    }

    public Artist getArtist()
    {
        return artist;
    }

    public void setArtist(Artist artist)
    {
        this.artist = artist;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
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
