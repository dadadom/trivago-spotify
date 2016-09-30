package com.trivago.hackathon.spotrivagofy.resources;

import com.codahale.metrics.annotation.Timed;
import com.trivago.hackathon.spotrivagofy.SpotifyTrivagoConfiguration;
import com.trivago.hackathon.spotrivagofy.api.TourWithRecommendationResponse;
import com.trivago.hackathon.spotrivagofy.api.ToursRequest;
import com.trivago.hackathon.spotrivagofy.core.FindArtistInformationTask;
import com.trivago.hackathon.spotrivagofy.core.FindHotelTask;
import com.trivago.triava.tcache.TCacheFactory;
import com.trivago.triava.tcache.eviction.Cache;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

@Path("/findHotels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FindHotelsResource
{

    private static final Logger logger = LoggerFactory.getLogger(FindHotelsResource.class);

    private final Client client;
    private final SpotifyTrivagoConfiguration configuration;
    private final ExecutorService findHotelsExecutors;
    private final ExecutorService findArtistInformationExecutors;
    private Cache<Integer, TourWithRecommendationResponse.HotelRecommendation> hotelRecommendationCache;

    public FindHotelsResource(Client client, SpotifyTrivagoConfiguration configuration, ExecutorService findHotelsExecutors, ExecutorService findArtistInformationExecutors)
    {
        this.client = client;
        this.configuration = configuration;
        this.findHotelsExecutors = findHotelsExecutors;
        this.findArtistInformationExecutors = findArtistInformationExecutors;

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
            final Future<String> artistFuture = findArtistInformationExecutors.submit(new FindArtistInformationTask(artist, client, configuration));
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
            TourWithRecommendationResponse.HotelRecommendation hotelRecommendation = new TourWithRecommendationResponse.HotelRecommendation();
            final TourWithRecommendationResponse tourWithRecommendationResponse = futureTourWithRecommendationEntry.getValue();
            try
            {
                hotelRecommendation = futureTourWithRecommendationEntry.getKey().get(15, TimeUnit.SECONDS);

                // make sure to not put error results into the cache so they are re-fetched every time
                if (!hotelRecommendation.isError())
                {
                    hotelRecommendationCache.put(new HashCodeBuilder().append(tourWithRecommendationResponse.getCity()).append(tourWithRecommendationResponse.getDate()).toHashCode(), hotelRecommendation);
                }

            } catch (ExecutionException | InterruptedException e)
            {
                logger.warn("Exception when trying to get result from findHotel future.", e);
                hotelRecommendation.setErrorMessage("Unspecified error: " + e.getMessage());
            } catch (TimeoutException e)
            {
                hotelRecommendation.setErrorMessage("Timeout while querying the hotels.");
            }
            tourWithRecommendationResponse.setHotelRecommendation(hotelRecommendation);
        }

        for (TourWithRecommendationResponse tour : toursWithRecommendations)
        {

            String artistInformation = "";
            try
            {
                artistInformation = artistFutures.get(tour.getArtist()).get(10, TimeUnit.SECONDS);
            } catch (TimeoutException e)
            {
                logger.warn("Timeout of 10 seconds reached when waiting for artist information future.");
            } catch (InterruptedException | ExecutionException e)
            {
                logger.warn("Exception when trying to get artist information future.", e);
            }
            tour.setArtistInformation(artistInformation);
        }
        return toursWithRecommendations;
    }

}
