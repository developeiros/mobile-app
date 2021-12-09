package models;

public class ClientService
{
    private Client client;
    private Service service;

    public ClientService(Client client, Service service)
    {
        this.client = client;
        this.service = service;
    }

    public Service getService()
    {
        return service;
    }

    public Client getClient()
    {
        return client;
    }
}
