package com.trivago.hackathon.spotrivagofy.api;

import java.util.List;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class LocationsResponse
{
    private String code;
    private String detail;
    private String title;
    private String type;
    private String query;
    private boolean query_corrected;
    private Locations _embedded;
    private Link _links;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public boolean isQuery_corrected()
    {
        return query_corrected;
    }

    public void setQuery_corrected(boolean query_corrected)
    {
        this.query_corrected = query_corrected;
    }

    public Locations get_embedded()
    {
        return _embedded;
    }

    public void set_embedded(Locations _embedded)
    {
        this._embedded = _embedded;
    }

    public Link get_links()
    {
        return _links;
    }

    public void set_links(Link _links)
    {
        this._links = _links;
    }

    /**
     * Created by Dominik Sandjaja on 29/09/16.
     */
    public static class Locations
    {
        private List<Location> locations;

        public List<Location> getLocations()
        {
            return locations;
        }

        public void setLocations(List<Location> locations)
        {
            this.locations = locations;
        }

        /**
         * Created by Dominik Sandjaja on 29/09/16.
         */
        public static class Location
        {
            private int count;
            private int path;
            private Integer item;
            private String name;
            private String path_name;
            private String type;

            public int getCount()
            {
                return count;
            }

            public void setCount(int count)
            {
                this.count = count;
            }

            public int getPath()
            {
                return path;
            }

            public void setPath(int path)
            {
                this.path = path;
            }

            public Integer getItem()
            {
                return item;
            }

            public void setItem(Integer item)
            {
                this.item = item;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public String getPath_name()
            {
                return path_name;
            }

            public void setPath_name(String path_name)
            {
                this.path_name = path_name;
            }

            public String getType()
            {
                return type;
            }

            public void setType(String type)
            {
                this.type = type;
            }
        }
    }
}
