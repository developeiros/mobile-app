package com.example.developeiros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.MessageFormat;

import database.EmployeeDAO;

public class ActMenu extends AppCompatActivity
{
    Context here;
    RelativeLayout btnEmployee, btnService, btnClient, btnEvent;
    long idEmployee;
    TextView txvWelcome;
    EmployeeDAO employees;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        here = getApplicationContext();
        employees = EmployeeDAO.getInstance(here);
        idEmployee = getIntent().getExtras().getLong("employeeId");

        // defining the ui elements
        txvWelcome = findViewById(R.id.txvWelcome);
        btnClient = findViewById(R.id.btnClient);
        btnEmployee = findViewById(R.id.btnEmployee);
        btnService = findViewById(R.id.btnService);
        btnEvent = findViewById(R.id.btnEvent);

        setWelcomeMessage();

        btnClient.setOnClickListener(view -> startActivity(new Intent(here, ActClient.class)));
        btnEmployee.setOnClickListener(view -> startActivity(new Intent(here, ActEmployee.class)));
        btnService.setOnClickListener(view -> startActivity(new Intent(here, ActService.class)));
        btnEvent.setOnClickListener(view -> startActivity(new Intent(here, ActEvent.class)));
    }

    private void setWelcomeMessage()
    {
        txvWelcome.setText(
            MessageFormat.format("{0} {1}.", txvWelcome.getText(), employees.select(idEmployee).getFirstName())
        );
    }
}