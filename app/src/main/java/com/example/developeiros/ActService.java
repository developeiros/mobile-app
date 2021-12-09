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

import adapters.AdpService;
import models.Service;
import database.ServiceDAO;

public class ActService extends AppCompatActivity
{
    Context here;
    TextView txvNoneService;
    AdpService adpService;
    ListView lsvService;
    Button btnNewService;

    ArrayList<Service> services;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        here = getApplicationContext();

        // defining the ui elements
        txvNoneService = findViewById(R.id.txvNoneService);
        lsvService = findViewById(R.id.lsvService);
        btnNewService = findViewById(R.id.btnNewService);

        ((SwipeRefreshLayout) findViewById(R.id.swpRefresh)).setOnRefreshListener(this::recreate);

        services = ServiceDAO.getInstance(here).select();

        if (services.size() == 0)
            noServices();
        else
        {
            adpService = new AdpService(here, R.layout.row_service);

            for (Service service : services)
                adpService.add(service);

            lsvService.setAdapter(adpService);
        }

        btnNewService.setOnClickListener(view ->
        {
            Intent intent = new Intent(here, ActFormService.class);
            intent.putExtra("isCreate", true);

            startActivity(intent);
        });
    }

    private void noServices()
    {
        txvNoneService.setVisibility(View.VISIBLE);
    }
}