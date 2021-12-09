package com.example.developeiros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.AdpEmployee;
import models.Employee;
import database.EmployeeDAO;

public class ActEmployee extends AppCompatActivity
{
    Context here;
    TextView txvNoneEmployee;
    AdpEmployee adpEmployee;
    ListView lsvEmployee;
    Button btnNewEmployee;

    ArrayList<Employee> employees;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        employees = EmployeeDAO.getInstance(here).select();

        // defining the ui elements
        here = getApplicationContext();
        txvNoneEmployee = findViewById(R.id.txvNoneEmployee);
        lsvEmployee = findViewById(R.id.lsvEmployees);
        btnNewEmployee = findViewById(R.id.btnNewEmployee);

        ((SwipeRefreshLayout) findViewById(R.id.swpRefresh)).setOnRefreshListener(this::recreate);

        if (employees.size() == 0)
            noEmployees();
        else
        {
            adpEmployee = new AdpEmployee(here, R.layout.row_employee);

            for (Employee employee : employees)
                adpEmployee.add(employee);

            lsvEmployee.setAdapter(adpEmployee);
        }

        btnNewEmployee.setOnClickListener(view ->
        {
            Intent intent = new Intent(here, ActFormEmployee.class);
            intent.putExtra("isCreate", true);

            startActivity(intent);
        });
    }

    private void noEmployees()
    {
        txvNoneEmployee.setVisibility(View.VISIBLE);
    }
}