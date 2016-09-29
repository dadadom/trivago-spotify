package com.trivago.hackathon.spotrivagofy;

import com.codahale.metrics.InstrumentedExecutorService;
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
public class SpotifyTrivagoApplication extends Application<SpotifyTrivagoConfiguration>
{

    public static void main(String[] args) throws Exception
    {
        new SpotifyTrivagoApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<SpotifyTrivagoConfiguration> bootstrap)
    {
        // nothing to do yet
    }

    public void run(SpotifyTrivagoConfiguration config, Environment environment) throws Exception
    {
        final ExecutorService findHotelsExecutors = new InstrumentedExecutorService(
                environment.lifecycle().executorService("FindHotels")
                        .minThreads(500)
                        .maxThreads(500)
                        .build(),
                environment.metrics(),
                "findHotelsExecutorService");

        final ExecutorService findArtistsExecutors = new InstrumentedExecutorService(
                environment.lifecycle().executorService("FindArtistsInformation")
                        .minThreads(10)
                        .maxThreads(10)
                        .build(),
                environment.metrics(),
                "findArtistsInformationExecutorService");

        final Client client = new JerseyClientBuilder(environment).using(config.getJerseyClientConfiguration()).build(getName());
        environment.jersey().register(new FindHotelsResource(client, config, findHotelsExecutors, findArtistsExecutors));

        environment.jersey().register(MultiPartFeature.class);
    }
}
