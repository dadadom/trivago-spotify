package com.trivago.hackathon.spotrivagofy.core;

import com.trivago.hackathon.spotrivagofy.SpotifyTrivagoApiConfiguration;
import com.trivago.hackathon.spotrivagofy.api.LastFmResponse;
import com.trivago.triava.tcache.TCacheFactory;
import com.trivago.triava.tcache.eviction.Cache;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientProperties;

import java.net.URLEncoder;
import java.util.concurrent.Callable;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class FindArtistInformationTask implements Callable<String>
{

    private final String artist;
    private final String lastFmApiKey;
    private final Client client;
    private final SpotifyTrivagoApiConfiguration configuration;

    private static Cache<String, String> descriptionsForArtist = TCacheFactory.standardFactory().<String, String>builder().setMaxCacheTime(60 * 60 * 24 * 180).build();


    public FindArtistInformationTask(String artist, Client client, SpotifyTrivagoApiConfiguration configuration)
    {
        this.artist = artist;
        this.lastFmApiKey = configuration.getLastFmApiKey();
        this.client = client;
        this.configuration = configuration;
    }

    @Override
    public String call() throws Exception
    {
        String description = descriptionsForArtist.get(artist);
        if (StringUtils.isNotEmpty(description))
        {
            return description;
        }
        if (StringUtils.isEmpty(lastFmApiKey))
        {
            return "";
        }
        String requestString = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo";
        requestString += "&artist=";
        requestString += URLEncoder.encode(artist, "UTF-8");
        requestString += "&api_key=" + lastFmApiKey;
        requestString += "&format=json";
        final Response response = client.target(requestString)
                .property(ClientProperties.CONNECT_TIMEOUT, configuration.getConnectTimeout())
                .property(ClientProperties.READ_TIMEOUT, configuration.getReadTimeout())
                .request().get();
        LastFmResponse lastFmResponse = response.readEntity(LastFmResponse.class);
        if (lastFmResponse == null)
        {
            return "";
        }
        else
        {
            description = lastFmResponse.getArtist().getBio().getSummary();
            descriptionsForArtist.put(artist, description);
            return description;
        }
    }
}
