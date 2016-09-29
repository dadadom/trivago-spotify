package com.trivago.hackathon.spotrivagofy;

import com.trivago.hackathon.spotrivagofy.resources.FindHotelsResource;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import java.util.concurrent.ExecutorService;

import javax.ws.rs.client.Client;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class SpotifyTrivagoApplication extends Application<SpotifyTrivagoApiConfiguration>
{

    public static void main(String[] args) throws Exception
    {
        new SpotifyTrivagoApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<SpotifyTrivagoApiConfiguration> bootstrap)
    {
        // nothing to do yet
    }

    public void run(SpotifyTrivagoApiConfiguration config, Environment environment) throws Exception
    {
        final ExecutorService findHotelsExecutors = environment.lifecycle().executorService("FindHotels").build();
        final ExecutorService findArtistsExecutors = environment.lifecycle().executorService("FindArtistsInformation").build();

        final Client client = new JerseyClientBuilder(environment).using(config.getJerseyClientConfiguration()).build(getName());
        environment.jersey().register(new FindHotelsResource(client, config, findHotelsExecutors, findArtistsExecutors));

        environment.jersey().register(MultiPartFeature.class);
    }
}
