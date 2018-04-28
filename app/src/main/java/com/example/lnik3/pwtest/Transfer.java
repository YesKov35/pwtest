package com.example.lnik3.pwtest;

/**
 * Created by lnik3 on 21.04.2018.
 */

public class Transfer
{
    private String id_from;

    private String count;

    private String name_from;

    private String name_to;

    private String id_to;

    public String getId_from ()
    {
        return id_from;
    }

    public void setId_from (String id_from)
    {
        this.id_from = id_from;
    }

    public String getCount ()
    {
        return count;
    }

    public void setCount (String count)
    {
        this.count = count;
    }

    public String getName_from ()
    {
        return name_from;
    }

    public void setName_from (String name_from)
    {
        this.name_from = name_from;
    }

    public String getName_to ()
    {
        return name_to;
    }

    public void setName_to (String name_to)
    {
        this.name_to = name_to;
    }

    public String getId_to ()
    {
        return id_to;
    }

    public void setId_to (String id_to)
    {
        this.id_to = id_to;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id_from = "+id_from+", count = "+count+", name_from = "+name_from+", name_to = "+name_to+", id_to = "+id_to+"]";
    }
}

