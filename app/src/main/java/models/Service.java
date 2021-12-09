package models;

public class Service
{
    private long id;
    private String title;
    private String description;

    // full constructor
    public Service(long id, String title, String description)
    {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // constructor without id
    public Service(String title, String description)
    {
        this.title = title;
        this.description = description;
    }

    public long getId()
    {
        return this.id;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getTitle()
    {
        return title;
    }
}
