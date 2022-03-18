package com.v1.civiladvocacy;

import java.io.Serializable;
import java.util.ArrayList;

public class Official implements Serializable
{
    private String identity, position;
    private String office;
    private String side;
    private String numbers;
    private String websites;
    private String emails;
    private String imagewebsite;
    private ArrayList<Channel> channels;



    Official()
    {
        identity = "";
        position = "";
        office = "";
        side = "";
        numbers = "";
        websites = "";
        emails = "";
        imagewebsite = "";
        channels = new ArrayList<>();
    }

    public String getIdentity()
    {
        return identity;
    }

    String getPosition()
    {
        return position;
    }

    public String getOffice() {
        return office;
    }

    String getSide() {
        return side;
    }

    public String getNumbers()
    {
        return numbers;
    }

    public String getWebsites()
    {
        return websites;
    }

    public String getEmails()
    {
        return emails;
    }

    public String getImagewebsite()
    {
        return imagewebsite;
    }

    public ArrayList<Channel> getChannels()
    {
        return channels;
    }

    public void setIdentity(String identity)
    {
        this.identity = identity;
    }

    void setPosition(String position)
    {
        this.position = position;
    }

    void setOffice(String office) {
        this.office = office;
    }

    void setSide(String side)
    {
        this.side = side;
    }

    void setNumbers(String numbers)
    {
        this.numbers = numbers;
    }

    void setWebsites(String websites)
    {
        this.websites = websites;
    }

    void setEmails(String emails)
    {
        this.emails = emails;
    }

    void setImagewebsite(String imagewebsite)
    {
        this.imagewebsite = imagewebsite;
    }

    void setChannels(ArrayList<Channel> channels)
    {
        this.channels = channels;
    }
}
