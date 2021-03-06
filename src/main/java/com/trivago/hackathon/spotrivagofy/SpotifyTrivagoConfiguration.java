package com.trivago.hackathon.spotrivagofy;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class SpotifyTrivagoConfiguration extends Configuration
{

    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();

    @NotNull
    private String accessId;

    @NotNull
    private String secretKey;

    private int connectTimeout = 5000;

    private int readTimeout = 10000;

    private String lastFmApiKey;

    @JsonProperty("jerseyClientConfiguration")
    public JerseyClientConfiguration getJerseyClientConfiguration()
    {
        return jerseyClientConfiguration;
    }

    @JsonProperty("accessId")
    public String getAccessId()
    {
        return accessId;
    }

    @JsonProperty("secretKey")
    public String getSecretKey()
    {
        return secretKey;
    }

    @JsonProperty("connectTimeout")
    public int getConnectTimeout()
    {
        return connectTimeout;
    }

    @JsonProperty("readTimeout")
    public int getReadTimeout()
    {
        return readTimeout;
    }

    @JsonProperty("lastFmApiKey")
    public String getLastFmApiKey()
    {
        return lastFmApiKey;
    }

}
