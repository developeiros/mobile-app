package models;

public class Client
{
    private long id;
    private String cnpj;
    private String nameContact;
    private String telephone;
    private String address;
    private String email;
    private String observation;

    // full constructor
    public Client(long id, String cnpj, String nameContact, String telephone, String address, String email,
                  String observation)
    {
        this.id = id;
        this.cnpj = cnpj;
        this.nameContact = nameContact;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
        this.observation = observation;
    }

    public Client(long id)
    {
        this.id = id;
    }

    // constructor without id
    public Client(String cnpj, String nameContact, String telephone, String address, String email, String observation)
    {
        this.cnpj = cnpj;
        this.nameContact = nameContact;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
        this.observation = observation;
    }

    public long getId()
    {
        return this.id;
    }

    public String getCnpj()
    {
        return this.cnpj;
    }

    public String getNameContact()
    {
        return this.nameContact;
    }

    public String getTelephone()
    {
        return this.telephone;
    }

    public String getAddress()
    {
        return this.address;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getObservation()
    {
        return this.observation;
    }

    public String getFormattedTelephone()
    {
        return this.getTelephone().replaceFirst("(\\d{2})(\\d{5})(\\d+)", "($1) $2-$3");
    }

    public String getFormattedCnpj()
    {
        return this.getCnpj().replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }
}
