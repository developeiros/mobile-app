package com.example.developeiros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import database.EmployeeDAO;
import models.Employee;

public class ActDtlEmployee extends AppCompatActivity
{
    Context here;
    long employeeId;
    TextView txvLogin, txvName, txvTelephone, txvAddress, txvEmail;
    Employee employee;
    Button btnServices, btnDelete, btnUpdate;
    EmployeeDAO employeeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtl_employee);

        here = getApplicationContext();
        employeeId = getIntent().getExtras().getLong("employeeId");
        employeeDAO = EmployeeDAO.getInstance(here);
        employee = employeeDAO.select(employeeId);

        // defining the ui elements
        txvLogin = findViewById(R.id.txvLogin);
        txvName = findViewById(R.id.txvName);
        txvTelephone = findViewById(R.id.txvTelephone);
        txvAddress = findViewById(R.id.txvAddress);
        txvEmail = findViewById(R.id.txvEmail);
        btnServices = findViewById(R.id.btnServices);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);

        ((SwipeRefreshLayout) findViewById(R.id.swpRefresh)).setOnRefreshListener(this::recreate);

        txvLogin.setText(employee.getLogin());
        txvName.setText(employee.getName());
        txvTelephone.setText(employee.getFormattedTelephone());
        txvAddress.setText(employee.getAddress());
        txvEmail.setText(employee.getEmail());

        btnServices.setOnClickListener(view ->
        {
            Intent intent = new Intent(here, ActEditEmployeeServices.class);
            intent.putExtra("employeeId", employeeId);
            intent.putExtra("redirectTo", ActDtlEmployee.class);

            startActivity(intent);
        });

        btnDelete.setOnClickListener(view ->
            new AlertDialog.Builder(this)
                .setTitle("Deletar funcionário")
                .setMessage("Você tem certeza que deseja deletar o funcionário selecionado?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Deletar", (dialog, whichButton) ->
                {
                    employeeDAO.delete(employeeId);
                    Toast.makeText(here, "Funcionário deletado.", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(here, ActEmployee.class));
                })
                .setNegativeButton("Cancelar", null)
                .create().show());


        btnUpdate.setOnClickListener(view ->
        {
            Intent intent = new Intent(here, ActFormEmployee.class);
            intent.putExtra("employeeId", employeeId);
            intent.putExtra("isCreate", false);

            startActivity(intent);
        });
    }
}