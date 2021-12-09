package models;

import java.time.LocalDateTime;

public class Event
{
    private long id;
    private String title;
    private String description;
    private Client client;
    private LocalDateTime datetime;

    // full constructor
    public Event(long id, String title, String description, Client client, LocalDateTime datetime)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.client = client;
        this.datetime = datetime;
    }

    // constructor without id
    public Event(String title, String description, Client client, LocalDateTime datetime)
    {
        this.title = title;
        this.description = description;
        this.client = client;
        this.datetime = datetime;
    }

    public long getId()
    {
        return this.id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Client getClient()
    {
        return this.client;
    }

    public LocalDateTime getDateTime()
    {
        return this.datetime;
    }

    public String getFormattedDateTime()
    {
        LocalDateTime datetime = this.getDateTime();
        return String.format("%d/%d/%d às %d:%d", datetime.getDayOfMonth(), datetime.getMonthValue(), datetime.getYear(), datetime.getHour(), datetime.getMinute());
    }
}
