package com.trivago.hackathon.spotrivagofy;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class SpotifyTrivagoApiConfiguration extends Configuration
{

    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();

    @NotNull
    private String accessId;

    @NotNull
    private String secretKey;

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
}
