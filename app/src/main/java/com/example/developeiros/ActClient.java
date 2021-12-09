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

import adapters.AdpClient;
import models.Client;
import database.ClientDAO;

public class ActClient extends AppCompatActivity
{
    Context here;
    TextView txvNoneClient;
    AdpClient adpClient;
    ListView lsvClient;
    Button btnNewClient;

    ArrayList<Client> clients;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        here = getApplicationContext();
        clients = ClientDAO.getInstance(here).select();

        // defining the ui elements
        txvNoneClient = findViewById(R.id.txvNoneClient);
        lsvClient = findViewById(R.id.lsvClients);
        btnNewClient = findViewById(R.id.btnNewClient);

        ((SwipeRefreshLayout) findViewById(R.id.swpRefresh)).setOnRefreshListener(this::recreate);

        if (clients.size() == 0)
            noClients();
        else
        {
            adpClient = new AdpClient(here, R.layout.row_client);

            for (Client client : clients)
                adpClient.add(client);

            lsvClient.setAdapter(adpClient);
        }

        btnNewClient.setOnClickListener(view ->
        {
            Intent intent = new Intent(here, ActFormClient.class);
            intent.putExtra("isCreate", true);

            startActivity(intent);
        });
    }

    private void noClients()
    {
        txvNoneClient.setVisibility(View.VISIBLE);
    }
}