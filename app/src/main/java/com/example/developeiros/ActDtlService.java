package com.example.developeiros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import adapters.AdpEmployee;
import database.EmployeeServiceDAO;
import database.ServiceDAO;
import models.EmployeeService;
import models.Service;

public class ActDtlService extends AppCompatActivity
{
    Context here;
    long serviceId;
    TextView txvService, txvDescription;
    Service service;
    ListView lsvServiceEmployees;
    AdpEmployee adpEmployee;
    ArrayList<EmployeeService> serviceEmployees;
    Button btnDelete, btnUpdate;
    ServiceDAO serviceDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtl_service);

        here = getApplicationContext();
        serviceId = getIntent().getExtras().getLong("serviceId");
        serviceDAO = ServiceDAO.getInstance(here);
        service = serviceDAO.select(serviceId);

        // defining the ui elements
        txvService = findViewById(R.id.txvService);
        txvDescription = findViewById(R.id.txvDescription);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);

        ((SwipeRefreshLayout) findViewById(R.id.swpRefresh)).setOnRefreshListener(this::recreate);

        txvService.setText(service.getTitle());
        txvDescription.setText(service.getDescription());

        btnDelete.setOnClickListener(view ->
            new AlertDialog.Builder(this)
                .setTitle("Deletar serviço")
                .setMessage("Você tem certeza que deseja deletar o serviço selecionado?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Deletar", (dialog, whichButton) ->
                {
                    serviceDAO.delete(serviceId);
                    Toast.makeText(here, "Serviço deletado.", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(here, ActService.class));
                })
                .setNegativeButton("Cancelar", null)
                .create().show());

        serviceEmployees = EmployeeServiceDAO.getInstance(here).getByService(serviceId);
        lsvServiceEmployees = findViewById(R.id.lsvServiceEmployees);
        adpEmployee = new AdpEmployee(here, R.layout.row_employee);

        for (EmployeeService serviceEmployee : serviceEmployees)
            adpEmployee.add(serviceEmployee.getEmployee());

        lsvServiceEmployees.setAdapter(adpEmployee);

        lsvServiceEmployees.setOnItemClickListener((adapterView, view, i, l) ->
        {
            Intent intent = new Intent(here, ActDtlEmployee.class);
            intent.putExtra("employeeId", serviceEmployees.get(i).getEmployee().getId());

            startActivity(intent);
        });

        btnUpdate.setOnClickListener(view ->
        {
            Intent intent = new Intent(here, ActFormService.class);
            intent.putExtra("serviceId", serviceId);
            intent.putExtra("isCreate", false);

            startActivity(intent);
        });
    }
}