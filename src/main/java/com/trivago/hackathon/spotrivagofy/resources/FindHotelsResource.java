package com.trivago.hackathon.spotrivagofy.resources;

import com.codahale.metrics.annotation.Timed;
import com.trivago.hackathon.spotrivagofy.SpotifyTrivagoApiConfiguration;
import com.trivago.hackathon.spotrivagofy.api.TourWithRecommendationResponse;
import com.trivago.hackathon.spotrivagofy.api.ToursRequest;
import com.trivago.hackathon.spotrivagofy.core.FindArtistInformationTask;
import com.trivago.hackathon.spotrivagofy.core.FindHotelTask;
import com.trivago.triava.tcache.TCacheFactory;
import com.trivago.triava.tcache.eviction.Cache;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
@Path("/findHotels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FindHotelsResource
{
    private final Client client;
    private final SpotifyTrivagoApiConfiguration configuration;
    private ExecutorService findHotelsExecutors;
    private Cache<Integer, TourWithRecommendationResponse.HotelRecommendation> hotelRecommendationCache;

    public FindHotelsResource(Client client, SpotifyTrivagoApiConfiguration configuration, ExecutorService findHotelsExecutors)
    {
        this.client = client;
        this.configuration = configuration;
        this.findHotelsExecutors = findHotelsExecutors;

        hotelRecommendationCache = TCacheFactory.standardFactory()
                .<Integer, TourWithRecommendationResponse.HotelRecommendation>builder()
                // four hours caching time
                .setMaxCacheTime(60 * 60 * 4)
                .build();
    }

    @POST
    @Timed
    public List<TourWithRecommendationResponse> findHotelsForTours(ToursRequest tours)
    {
        final List<TourWithRecommendationResponse> toursWithRecommendations = new ArrayList<>(tours.getTours().size());

        final Map<Future<TourWithRecommendationResponse.HotelRecommendation>, TourWithRecommendationResponse> futuresForRequests = new HashMap<>();

        final Set<String> artists = tours.getTours().stream().map(ToursRequest.Tour::getArtist).collect(Collectors.toSet());
        Map<String, Future<String>> artistFutures = new HashMap<>(artists.size());
        for (String artist : artists)
        {
            final Future<String> artistFuture = findHotelsExecutors.submit(new FindArtistInformationTask(artist, client, configuration));
            artistFutures.put(artist, artistFuture);
        }

        for (ToursRequest.Tour tour : tours.getTours())
        {
            final TourWithRecommendationResponse tourWithRecommendationResponse = new TourWithRecommendationResponse();
            tourWithRecommendationResponse.setArtist(tour.getArtist());
            tourWithRecommendationResponse.setCity(tour.getCity());
            tourWithRecommendationResponse.setDate(tour.getDate());

            final int hashCode = new HashCodeBuilder().append(tour.getCity()).append(tour.getDate()).toHashCode();
            final TourWithRecommendationResponse.HotelRecommendation hotelRecommendation = hotelRecommendationCache.get(hashCode);
            if (hotelRecommendation == null)
            {
                final Future<TourWithRecommendationResponse.HotelRecommendation> future = findHotelsExecutors.submit(new FindHotelTask(tour.getCity(), tour.getDate(), client, configuration));
                futuresForRequests.put(future, tourWithRecommendationResponse);
            }
            else
            {
                tourWithRecommendationResponse.setHotelRecommendation(hotelRecommendation);
            }
            toursWithRecommendations.add(tourWithRecommendationResponse);
        }
        for (Map.Entry<Future<TourWithRecommendationResponse.HotelRecommendation>, TourWithRecommendationResponse> futureTourWithRecommendationEntry : futuresForRequests.entrySet())
        {
            final TourWithRecommendationResponse tourWithRecommendationResponse = futureTourWithRecommendationEntry.getValue();
            try
            {
                final TourWithRecommendationResponse.HotelRecommendation hotelRecommendation = futureTourWithRecommendationEntry.getKey().get();
                tourWithRecommendationResponse.setHotelRecommendation(hotelRecommendation);

                // make sure to not put error results into the cache so they are re-fetched every time
                if (!hotelRecommendation.isError())
                {
                    hotelRecommendationCache.put(new HashCodeBuilder().append(tourWithRecommendationResponse.getCity()).append(tourWithRecommendationResponse.getDate()).toHashCode(), hotelRecommendation);
                }

            } catch (ExecutionException | InterruptedException e)
            {
                tourWithRecommendationResponse.setHotelRecommendation(null);
            }
        }

        for (TourWithRecommendationResponse tour : toursWithRecommendations)
        {
            try
            {
                tour.setArtistInformation(artistFutures.get(tour.getArtist()).get(60, TimeUnit.SECONDS));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return toursWithRecommendations;
    }

}
