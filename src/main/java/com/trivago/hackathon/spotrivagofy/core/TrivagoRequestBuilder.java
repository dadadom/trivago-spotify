package com.trivago.hackathon.spotrivagofy.core;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class TrivagoRequestBuilder
{
    private final Mac mac;
    private final String accessId;

    private Integer pathId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String path;
    private String query;
    private Integer limit;

    public TrivagoRequestBuilder(String accessId, String key) throws NoSuchAlgorithmException, InvalidKeyException
    {
        String algorithm = "HmacSHA256";
        mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key.getBytes(), algorithm));

        this.accessId = accessId;
    }

    public void setPathId(Integer pathId)
    {
        this.pathId = pathId;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public void setLimit(Integer limit)
    {
        this.limit = limit;
    }

    public String build() throws UnsupportedEncodingException
    {
        StringBuilder requestBuilder = new StringBuilder("GET\napi.trivago.com\n").append(path).append("\n");

        StringBuilder requestParametersBuilder = new StringBuilder();


        requestParametersBuilder.append("&access_id=").append(URLEncoder.encode(accessId, "UTF-8"));

        if (endDate != null)
        {
            requestParametersBuilder.append("&end_date=").append(URLEncoder.encode(getCorrectDateString(endDate), "UTF-8"));
        }

        if (limit != null)
        {
            requestParametersBuilder.append("&limit=").append(limit);
        }

        if (pathId != null)
        {
            requestParametersBuilder.append("&path=").append(pathId);
        }

        if (StringUtils.isNotEmpty(query))
        {
            requestParametersBuilder.append("&query=").append(URLEncoder.encode(query, "UTF-8"));
        }

        if (startDate != null)
        {
            requestParametersBuilder.append("&start_date=").append(URLEncoder.encode(getCorrectDateString(startDate), "UTF-8"));
        }

        String formattedLocalDateInUtc = getCorrectDateString(LocalDateTime.now(ZoneId.of("Z")));
        requestParametersBuilder.append("&timestamp=").append(URLEncoder.encode(formattedLocalDateInUtc, "UTF-8"));

        final String request = requestBuilder.append(requestParametersBuilder.substring(1)).toString();

        final byte[] signature = mac.doFinal(request.getBytes("UTF-8"));

        final String encodedSignature = Base64.encodeBase64String(signature);

        return new StringBuilder("https://api.trivago.com")
                .append(path)
                .append("?").append(requestParametersBuilder.substring(1))
                .append("&signature=").append(encodedSignature)
                .toString();
    }

    private String getCorrectDateString(LocalDateTime localDateTimeInUtc)
    {
        String formattedLocalDateInUtc = localDateTimeInUtc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        formattedLocalDateInUtc += "T";
        formattedLocalDateInUtc += localDateTimeInUtc.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        formattedLocalDateInUtc += "+00:00";
        return formattedLocalDateInUtc;
    }

    private String getCorrectDateString(LocalDate localDate)
    {
        String formattedLocalDateInUtc = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        formattedLocalDateInUtc += "T12:00:00+00:00";
        return formattedLocalDateInUtc;
    }

}
