package com.trivago.hackathon.spotrivagofy.resources;

import com.trivago.hackathon.spotrivagofy.api.HotelRecommendation;
import com.trivago.hackathon.spotrivagofy.api.TourWithRecommendation;
import com.trivago.hackathon.spotrivagofy.api.ToursRequest;
import com.trivago.hackathon.spotrivagofy.core.FindHotelTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
    private final String accessId;
    private final String secretKey;
    private ExecutorService findHotelsExecutors;

    public FindHotelsResource(Client client, String accessId, String secretKey, ExecutorService findHotelsExecutors)
    {
        this.client = client;
        this.accessId = accessId;
        this.secretKey = secretKey;
        this.findHotelsExecutors = findHotelsExecutors;
    }

    @POST
    public List<TourWithRecommendation> findHotelsForTours(ToursRequest tours)
    {
        final List<TourWithRecommendation> toursWithRecommendations = new ArrayList<>(tours.getTours().size());

        final Map<Future<HotelRecommendation>, TourWithRecommendation> futuresForRequests = new HashMap<>();

        for (ToursRequest.Tour tour : tours.getTours())
        {
            final com.trivago.hackathon.spotrivagofy.api.TourWithRecommendation tourWithRecommendation = new com.trivago.hackathon.spotrivagofy.api.TourWithRecommendation();
            tourWithRecommendation.setArtist(tour.getArtist());
            tourWithRecommendation.setCity(tour.getCity());
            tourWithRecommendation.setDate(tour.getDate());
            final Future<HotelRecommendation> future = findHotelsExecutors.submit(new FindHotelTask(tour.getCity(), tour.getDate(), accessId, secretKey, client));
            futuresForRequests.put(future, tourWithRecommendation);
        }
        for (Map.Entry<Future<HotelRecommendation>, TourWithRecommendation> futureTourWithRecommendationEntry : futuresForRequests.entrySet())
        {
            final TourWithRecommendation tourWithRecommendation = futureTourWithRecommendationEntry.getValue();
            try
            {
                final HotelRecommendation hotelRecommendation = futureTourWithRecommendationEntry.getKey().get();
                tourWithRecommendation.setHotelRecommendation(hotelRecommendation);

            } catch (ExecutionException | InterruptedException e)
            {
                tourWithRecommendation.setHotelRecommendation(null);
            }
            toursWithRecommendations.add(tourWithRecommendation);
        }
        return toursWithRecommendations;
    }

}
