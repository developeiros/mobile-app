package com.example.developeiros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import adapters.AdpEmployeeServiceEdit;
import database.EmployeeServiceDAO;
import models.EmployeeService;

public class ActEditEmployeeServices extends AppCompatActivity
{
    Context here;
    long employeeId;
    ListView lsvEmployeeServices;
    ArrayList<EmployeeService> employeeServices;
    Class redirectTo;
    Button btnAdd, btnFinish;
    FrgAddService frgAddService;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    boolean hasFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee_services);

        here = getApplicationContext();
        employeeId = getIntent().getExtras().getLong("employeeId");
        redirectTo = (Class) getIntent().getExtras().get("redirectTo");
        hasFragment = false;

        // defining the ui elements
        lsvEmployeeServices = findViewById(R.id.lsvEmployeeServices);
        btnAdd = findViewById(R.id.btnAdd);
        btnFinish = findViewById(R.id.btnFinish);
        frgAddService = FrgAddService.newInstance(employeeId, false);

        ((SwipeRefreshLayout) findViewById(R.id.swpRefresh)).setOnRefreshListener(this::recreate);

        employeeServices = EmployeeServiceDAO.getInstance(here).getByEmployee(employeeId);

        if (employeeServices.size() > 0)
        {
            AdpEmployeeServiceEdit adpEmployeeService = new AdpEmployeeServiceEdit(here, R.layout.row_service_editable);

            for (EmployeeService employeeService : employeeServices)
                adpEmployeeService.add(employeeService);

            lsvEmployeeServices.setAdapter(adpEmployeeService);
        }

        btnAdd.setOnClickListener(view ->
        {
            fragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.bottom_to_top,
                    android.R.anim.fade_out,
                    R.anim.bottom_to_top,
                    android.R.anim.fade_out
                )
                .add(R.id.fragment_container_view, frgAddService, null)
                .addToBackStack(null)
                .commit();

            hasFragment = true;
        });

        btnFinish.setOnClickListener(view ->
        {
            Intent intent = new Intent(here, redirectTo);
            intent.putExtra("employeeId", employeeId);

            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        if (hasFragment)
        {
            fragmentManager.popBackStackImmediate();
            hasFragment = false;
        }
        else
            super.onBackPressed();
    }
}