package com.trivago.hackathon.spotrivagofy.api;

/**
 * Created by Dominik Sandjaja on 29/09/16.
 */
public class Link
{
    private Href self;
    private Href prev;
    private Href next;
    private Href deals;

    public Href getSelf()
    {
        return self;
    }

    public void setSelf(Href self)
    {
        this.self = self;
    }

    public Href getPrev()
    {
        return prev;
    }

    public void setPrev(Href prev)
    {
        this.prev = prev;
    }

    public Href getNext()
    {
        return next;
    }

    public void setNext(Href next)
    {
        this.next = next;
    }

    public Href getDeals()
    {
        return deals;
    }

    public void setDeals(Href deals)
    {
        this.deals = deals;
    }

    /**
     * Created by Dominik Sandjaja on 29/09/16.
     */
    public static class Href
    {
        private String href;

        public String getHref()
        {
            return href;
        }

        public void setHref(String href)
        {
            this.href = href;
        }
    }
}
