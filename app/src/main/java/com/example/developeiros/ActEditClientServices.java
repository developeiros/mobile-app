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

import adapters.AdpClientServiceEdit;
import database.ClientServiceDAO;
import models.ClientService;

public class ActEditClientServices extends AppCompatActivity
{
    Context here;
    long clientId;
    ListView lsvClientServices;
    ArrayList<ClientService> clientServices;
    Class redirectTo;
    Button btnAdd, btnFinish;
    FrgAddService frgAddService;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    boolean hasFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client_services);

        here = getApplicationContext();
        clientId = getIntent().getExtras().getLong("clientId");
        redirectTo = (Class) getIntent().getExtras().get("redirectTo");
        hasFragment = false;

        // defining the ui elements
        lsvClientServices = findViewById(R.id.lsvClientServices);
        btnAdd = findViewById(R.id.btnAdd);
        btnFinish = findViewById(R.id.btnFinish);
        frgAddService = FrgAddService.newInstance(clientId, true);

        ((SwipeRefreshLayout) findViewById(R.id.swpRefresh)).setOnRefreshListener(this::recreate);

        clientServices = ClientServiceDAO.getInstance(here).getByClient(clientId);

        if (clientServices.size() > 0)
        {
            AdpClientServiceEdit adpClientService = new AdpClientServiceEdit(here, R.layout.row_service_editable);

            for (ClientService clientService : clientServices)
                adpClientService.add(clientService);

            lsvClientServices.setAdapter(adpClientService);
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
            intent.putExtra("clientId", clientId);

            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed()
    {
        if (hasFragment)
        {
            fragmentManager.popBackStackImmediate();
            hasFragment = false;
        }
        else
            super.onBackPressed();
    }
}