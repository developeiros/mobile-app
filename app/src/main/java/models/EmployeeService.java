package models;

public class EmployeeService
{
    private Employee employee;
    private Service service;

    public EmployeeService(Employee employee, Service service)
    {
        this.employee = employee;
        this.service = service;
    }

    public Employee getEmployee()
    {
        return employee;
    }

    public Service getService()
    {
        return service;
    }
}
