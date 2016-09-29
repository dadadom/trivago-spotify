package com.trivago.hackathon.spotrivagofy.api;

import java.util.List;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class HotelsResponse
{
    private Link _links;
    private List<Hotel> hotels;
    private SearchParams search_params;
    private List<FilterGroup> filter_groups;
    private ResultInfo result_info;

    public Link get_links()
    {
        return _links;
    }

    public void set_links(Link _links)
    {
        this._links = _links;
    }

    public List<Hotel> getHotels()
    {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels)
    {
        this.hotels = hotels;
    }

    public SearchParams getSearch_params()
    {
        return search_params;
    }

    public void setSearch_params(SearchParams search_params)
    {
        this.search_params = search_params;
    }

    public List<FilterGroup> getFilter_groups()
    {
        return filter_groups;
    }

    public void setFilter_groups(List<FilterGroup> filter_groups)
    {
        this.filter_groups = filter_groups;
    }

    public ResultInfo getResult_info()
    {
        return result_info;
    }

    public void setResult_info(ResultInfo result_info)
    {
        this.result_info = result_info;
    }

    /**
     * Created by Dominik Sandjaja on 29/09/16.
     */
    public static class SearchParams
    {
        private int path;
        private int item;
        private String start_date;
        private String end_date;
        private String currency;
        private int limit;
        private int offset;
        private int room_type;
        private String order;
        private List<Integer> category;
        private List<Integer> rating_class;
        private String hotel_name;
        private int max_price;

        public int getPath()
        {
            return path;
        }

        public void setPath(int path)
        {
            this.path = path;
        }

        public int getItem()
        {
            return item;
        }

        public void setItem(int item)
        {
            this.item = item;
        }

        public String getStart_date()
        {
            return start_date;
        }

        public void setStart_date(String start_date)
        {
            this.start_date = start_date;
        }

        public String getEnd_date()
        {
            return end_date;
        }

        public void setEnd_date(String end_date)
        {
            this.end_date = end_date;
        }

        public String getCurrency()
        {
            return currency;
        }

        public void setCurrency(String currency)
        {
            this.currency = currency;
        }

        public int getLimit()
        {
            return limit;
        }

        public void setLimit(int limit)
        {
            this.limit = limit;
        }

        public int getOffset()
        {
            return offset;
        }

        public void setOffset(int offset)
        {
            this.offset = offset;
        }

        public int getRoom_type()
        {
            return room_type;
        }

        public void setRoom_type(int room_type)
        {
            this.room_type = room_type;
        }

        public String getOrder()
        {
            return order;
        }

        public void setOrder(String order)
        {
            this.order = order;
        }

        public List<Integer> getCategory()
        {
            return category;
        }

        public void setCategory(List<Integer> category)
        {
            this.category = category;
        }

        public List<Integer> getRating_class()
        {
            return rating_class;
        }

        public void setRating_class(List<Integer> rating_class)
        {
            this.rating_class = rating_class;
        }

        public String getHotel_name()
        {
            return hotel_name;
        }

        public void setHotel_name(String hotel_name)
        {
            this.hotel_name = hotel_name;
        }

        public int getMax_price()
        {
            return max_price;
        }

        public void setMax_price(int max_price)
        {
            this.max_price = max_price;
        }
    }

    /**
     * Created by Dominik Sandjaja on 29/09/16.
     */
    public static class FilterGroup
    {
        private int group_id;
        private String type;
        private String name;
        private List<Field> fields;

        public int getGroup_id()
        {
            return group_id;
        }

        public void setGroup_id(int group_id)
        {
            this.group_id = group_id;
        }

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public List<Field> getFields()
        {
            return fields;
        }

        public void setFields(List<Field> fields)
        {
            this.fields = fields;
        }
    }

    /**
     * Created by Dominik Sandjaja on 29/09/16.
     */
    public static class ResultInfo
    {
        private Price price;

        public Price getPrice()
        {
            return price;
        }

        public void setPrice(Price price)
        {
            this.price = price;
        }
    }

    /**
     * Created by Dominik Sandjaja on 29/09/16.
     */
    public static class Hotel
    {
        private Link _links;
        private int id;
        private String name;
        private int category;
        private boolean superior;
        private String city;
        private double rating_value;
        private int rating_count;
        private MainImage main_image;
        private List<Deal> deals;

        public Link get_links()
        {
            return _links;
        }

        public void set_links(Link _links)
        {
            this._links = _links;
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getCategory()
        {
            return category;
        }

        public void setCategory(int category)
        {
            this.category = category;
        }

        public boolean isSuperior()
        {
            return superior;
        }

        public void setSuperior(boolean superior)
        {
            this.superior = superior;
        }

        public String getCity()
        {
            return city;
        }

        public void setCity(String city)
        {
            this.city = city;
        }

        public double getRating_value()
        {
            return rating_value;
        }

        public void setRating_value(double rating_value)
        {
            this.rating_value = rating_value;
        }

        public int getRating_count()
        {
            return rating_count;
        }

        public void setRating_count(int rating_count)
        {
            this.rating_count = rating_count;
        }

        public MainImage getMain_image()
        {
            return main_image;
        }

        public void setMain_image(MainImage main_image)
        {
            this.main_image = main_image;
        }

        public List<Deal> getDeals()
        {
            return deals;
        }

        public void setDeals(List<Deal> deals)
        {
            this.deals = deals;
        }

        /**
         * Created by Dominik Sandjaja on 29/09/16.
         */
        public static class Deal
        {
            private BookingSite booking_site;
            private String description;
            private Price price;
            private String booking_link;
            private List<RateAttribute> rate_attributes;

            public BookingSite getBooking_site()
            {
                return booking_site;
            }

            public void setBooking_site(BookingSite booking_site)
            {
                this.booking_site = booking_site;
            }

            public String getDescription()
            {
                return description;
            }

            public void setDescription(String description)
            {
                this.description = description;
            }

            public Price getPrice()
            {
                return price;
            }

            public void setPrice(Price price)
            {
                this.price = price;
            }

            public String getBooking_link()
            {
                return booking_link;
            }

            public void setBooking_link(String booking_link)
            {
                this.booking_link = booking_link;
            }

            public List<RateAttribute> getRate_attributes()
            {
                return rate_attributes;
            }

            public void setRate_attributes(List<RateAttribute> rate_attributes)
            {
                this.rate_attributes = rate_attributes;
            }

            /**
             * Created by Dominik Sandjaja on 29/09/16.
             */
            public static class BookingSite
            {
                private String name;
                private String logo;

                public String getName()
                {
                    return name;
                }

                public void setName(String name)
                {
                    this.name = name;
                }

                public String getLogo()
                {
                    return logo;
                }

                public void setLogo(String logo)
                {
                    this.logo = logo;
                }
            }

            /**
             * Created by Dominik Sandjaja on 29/09/16.
             */
            public static class RateAttribute
            {
                private String type;
                private String label;
                private boolean positive;

                public String getType()
                {
                    return type;
                }

                public void setType(String type)
                {
                    this.type = type;
                }

                public String getLabel()
                {
                    return label;
                }

                public void setLabel(String label)
                {
                    this.label = label;
                }

                public boolean isPositive()
                {
                    return positive;
                }

                public void setPositive(boolean positive)
                {
                    this.positive = positive;
                }
            }
        }
    }

    /**
     * Created by Dominik Sandjaja on 29/09/16.
     */
    public static class Field
    {
        private String field_id;
        private String name;
        private int count;

        public String getField_id()
        {
            return field_id;
        }

        public void setField_id(String field_id)
        {
            this.field_id = field_id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getCount()
        {
            return count;
        }

        public void setCount(int count)
        {
            this.count = count;
        }
    }

}
