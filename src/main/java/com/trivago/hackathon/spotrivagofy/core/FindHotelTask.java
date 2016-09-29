package com.trivago.hackathon.spotrivagofy.core;

import com.trivago.hackathon.spotrivagofy.SpotifyTrivagoConfiguration;
import com.trivago.hackathon.spotrivagofy.api.HotelsResponse;
import com.trivago.hackathon.spotrivagofy.api.LocationsResponse;
import com.trivago.hackathon.spotrivagofy.api.TourWithRecommendationResponse;
import com.trivago.triava.tcache.TCacheFactory;
import com.trivago.triava.tcache.eviction.Cache;

import org.glassfish.jersey.client.ClientProperties;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class FindHotelTask implements Callable<TourWithRecommendationResponse.HotelRecommendation>
{
    private String city;
    private String date;
    private String accessId;
    private String secretKey;
    private int connectTimeout;
    private int readTimeout;
    private Client client;

    private static Cache<String, Integer> pathsForCityCache = TCacheFactory.standardFactory().<String, Integer>builder().setMaxCacheTime(60 * 60 * 24).build();

    public FindHotelTask(String city, String date, Client client, SpotifyTrivagoConfiguration configuration)
    {
        this.city = city;
        this.date = date;
        this.accessId = configuration.getAccessId();
        this.secretKey = configuration.getSecretKey();
        this.connectTimeout = configuration.getConnectTimeout();
        this.readTimeout = configuration.getReadTimeout();
        this.client = client;
    }

    @Override
    public TourWithRecommendationResponse.HotelRecommendation call() throws Exception
    {
        return findHotel(city, date);
    }


    private TourWithRecommendationResponse.HotelRecommendation findHotel(String queryLocation, String date) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, InterruptedException
    {
        int pathId = findBestPathId(queryLocation);

        final TourWithRecommendationResponse.HotelRecommendation hotelRecommendation = new TourWithRecommendationResponse.HotelRecommendation();

        if (pathId < 0)
        {
            return hotelRecommendation;
        }
        // now we have the PathId with the maximum responses
        final TrivagoRequestBuilder hotelsRequestBuilder = new TrivagoRequestBuilder(accessId, secretKey);
        hotelsRequestBuilder.setPath("/webservice/tas/hotels");
        hotelsRequestBuilder.setPathId(pathId);
        final LocalDate parsedStartDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        hotelsRequestBuilder.setStartDate(parsedStartDate);
        hotelsRequestBuilder.setEndDate(parsedStartDate.plus(1, ChronoUnit.DAYS));
        hotelsRequestBuilder.setLimit(1);

        Response hotelsResponse = queryAndGetResponse(hotelsRequestBuilder);

        if (hotelsResponse != null && hotelsResponse.getStatus() == 200)
        {
            final HotelsResponse hotelsResponseEntity = hotelsResponse.readEntity(HotelsResponse.class);
            final HotelsResponse.Hotel hotel = hotelsResponseEntity.getHotels().get(0);
            hotelRecommendation.setHotel(hotel);
            hotelRecommendation.setError(false);
        }
        else
        {
            // just to have a breakpoint
            hotelRecommendation.setError(true);
        }

        return hotelRecommendation;
    }

    private int findBestPathId(String queryLocation) throws UnsupportedEncodingException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException
    {
        String normalizedQueryLocation = queryLocation.trim().toLowerCase();

        final Integer pathIdFromCache = pathsForCityCache.get(normalizedQueryLocation);
        if (pathIdFromCache != null)
        {
            return pathIdFromCache;
        }

        final TrivagoRequestBuilder requestBuilder = new TrivagoRequestBuilder(accessId, secretKey);
        requestBuilder.setPath("/webservice/tas/locations");
        requestBuilder.setQuery(queryLocation);

        Response response = queryAndGetResponse(requestBuilder);

        final LocationsResponse responseEntity = response.readEntity(LocationsResponse.class);

        if (responseEntity.get_embedded() == null || responseEntity.get_embedded().getLocations() == null)
        {
            return -1;
        }

        final Optional<LocationsResponse.Locations.Location> actualLocationOptional = responseEntity.get_embedded().getLocations().stream().sorted((l1, l2) -> Integer.compare(l1.getCount(), l2.getCount())).findFirst();
        if (!actualLocationOptional.isPresent())
        {
            return -1;
        }
        final LocationsResponse.Locations.Location actualLocation = actualLocationOptional.get();
        int pathId = actualLocation.getPath();
        pathsForCityCache.put(normalizedQueryLocation, pathId);
        return pathId;
    }

    private Response queryAndGetResponse(TrivagoRequestBuilder requestBuilder) throws UnsupportedEncodingException, InterruptedException
    {
        Response response = null;
        int retryCount = 0;

        while (!Thread.interrupted() && retryCount < 5 && (response == null || 200 != response.getStatus()))
        {
            try
            {
                retryCount++;
                response = client.target(requestBuilder.build())
                        .property(ClientProperties.CONNECT_TIMEOUT, connectTimeout)
                        .property(ClientProperties.READ_TIMEOUT, readTimeout)
                        .request()
                        .acceptLanguage("de-DE")
                        .accept("application/vnd.trivago.affiliate.hal+json;version=1")
                        .get();
            } catch (Exception e)
            {
                TimeUnit.SECONDS.sleep(1);
                continue;
            }
            if (200 != response.getStatus())
            {
                TimeUnit.SECONDS.sleep(1);
            }
        }
        return response;
    }

}
