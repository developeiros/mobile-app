package models;

public class Employee
{
    private long id;
    private String name;
    private String telephone;
    private String address;
    private String email;
    private String login;
    private String password;

    // full constructor
    public Employee(long id, String name, String telephone, String address, String email, String login, String password)
    {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    // constructor without id
    public Employee(String name, String telephone, String address, String email, String login, String password)
    {
        this.name = name;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public Employee(long id)
    {
        this.id = id;
    }

    // login constructor
    public Employee(String login, String password)
    {
        this.login = login;
        this.password = password;
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public String getAddress()
    {
        return address;
    }

    public String getEmail()
    {
        return email;
    }

    public String getLogin()
    {
        return login;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFirstName()
    {
        int endName = this.getName().indexOf(' ');

        if (endName != -1)
            return this.getName().substring(0, endName);

        return this.getName();
    }

    public String getShortName()
    {
        String[] split = this.getName().split(" ");

        if (split.length == 1)
            return split[0];

        return split[0] + " " + split[split.length - 1];
    }

    public boolean isBlank()
    {
        return
            getLogin().replace(" ", "").equals("") ||
            getPassword().replace(" ", "").equals("");
    }

    public String getFormattedTelephone()
    {
        return this.getTelephone().replaceFirst("(\\d{2})(\\d{5})(\\d+)", "($1) $2-$3");
    }
}