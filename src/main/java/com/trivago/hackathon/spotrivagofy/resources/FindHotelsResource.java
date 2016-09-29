package com.trivago.hackathon.spotrivagofy.resources;

import com.trivago.hackathon.spotrivagofy.api.HotelRecommendation;
import com.trivago.hackathon.spotrivagofy.api.HotelsResponse;
import com.trivago.hackathon.spotrivagofy.api.LocationsResponse;
import com.trivago.hackathon.spotrivagofy.api.TourWithRecommendation;
import com.trivago.hackathon.spotrivagofy.api.ToursRequest;
import com.trivago.hackathon.spotrivagofy.core.TrivagoRequestBuilder;

import org.glassfish.jersey.client.ClientProperties;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    public FindHotelsResource(Client client, String accessId, String secretKey)
    {
        this.client = client;
        this.accessId = accessId;
        this.secretKey = secretKey;
    }

    @POST
    public List<TourWithRecommendation> findHotelsForTours(ToursRequest tours)
    {
        final List<TourWithRecommendation> toursWithRecommendations = new ArrayList<>(tours.getTours().size());

        for (ToursRequest.Tour tour : tours.getTours())
        {
            final com.trivago.hackathon.spotrivagofy.api.TourWithRecommendation tourWithRecommendation = new com.trivago.hackathon.spotrivagofy.api.TourWithRecommendation();
            tourWithRecommendation.setArtist(tour.getArtist());
            tourWithRecommendation.setCity(tour.getCity());
            tourWithRecommendation.setDate(tour.getDate());
            try
            {
                final HotelRecommendation hotelRecommendation = findHotel(tour.getCity(), tour.getDate());
                tourWithRecommendation.setHotelRecommendation(hotelRecommendation);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException | InterruptedException e)
            {
                tourWithRecommendation.setHotelRecommendation(null);
            }
            toursWithRecommendations.add(tourWithRecommendation);
        }
        return toursWithRecommendations;
    }

    private HotelRecommendation findHotel(String queryLocation, String date) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, InterruptedException
    {
        int pathId = findBestPathId(queryLocation);

        final HotelRecommendation hotelRecommendation = new HotelRecommendation();

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
            hotelRecommendation.setItemId(hotel.getId());
            hotelRecommendation.setHotelName(hotel.getName());
        }

        return hotelRecommendation;
    }

    private int findBestPathId(String queryLocation) throws UnsupportedEncodingException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException
    {
        final TrivagoRequestBuilder requestBuilder = new TrivagoRequestBuilder(accessId, secretKey);
        requestBuilder.setPath("/webservice/tas/locations");
        requestBuilder.setQuery(queryLocation);

        Response response = queryAndGetResponse(requestBuilder);

        final LocationsResponse responseEntity = response.readEntity(LocationsResponse.class);

        if (responseEntity.get_embedded() == null || responseEntity.get_embedded().getLocations() == null)
        {
            return -1;
        }
        final Map<Integer, Long> collectedPaths = responseEntity.get_embedded().getLocations().stream().map(LocationsResponse.Locations.Location::getPath).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (collectedPaths.isEmpty())
        {
            return -1;
        }

        int pathId = -1;
        long maxCount = -1;

        for (Map.Entry<Integer, Long> entry : collectedPaths.entrySet())
        {
            if (entry.getValue() > maxCount)
            {
                pathId = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        return pathId;
    }

    private Response queryAndGetResponse(TrivagoRequestBuilder requestBuilder) throws UnsupportedEncodingException, InterruptedException
    {
        Response response = null;
        int retryCount = 0;

        while (retryCount < 5 && (response == null || 200 != response.getStatus()))
        {
            try
            {
                response = client.target(requestBuilder.build())
                        .property(ClientProperties.CONNECT_TIMEOUT, 25_000)
                        .property(ClientProperties.READ_TIMEOUT, 25_000)
                        .request()
                        .acceptLanguage("en-GB")
                        .accept("application/vnd.trivago.affiliate.hal+json;version=1")
                        .get();
            } catch (Exception e)
            {
                TimeUnit.SECONDS.sleep(1);
                retryCount++;
                continue;
            }
            if (200 != response.getStatus())
            {
                TimeUnit.SECONDS.sleep(1);
                retryCount++;
            }
        }
        return response;
    }
}
