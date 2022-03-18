package com.v1.civiladvocacy;

import java.io.Serializable;

public class Channel implements Serializable
{
    private String id, name;

    public Channel(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }


}
