package com.example.developeiros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import database.ClientDAO;
import models.Client;
import utils.Utils;

public class ActDtlClient extends AppCompatActivity
{
    Context here;
    long clientId;
    TextView txvNameContact, txvCnpj, txvTelephone, txvAddress, txvEmail, txvObservationLabel, txvObservation;
    Client client;
    Button btnServices, btnDelete, btnUpdate;
    ClientDAO clientDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtl_client);

        here = getApplicationContext();
        clientId = getIntent().getExtras().getLong("clientId");
        clientDAO = ClientDAO.getInstance(here);
        client = clientDAO.select(clientId);

        // defining the ui elements
        txvNameContact = findViewById(R.id.txvNameContact);
        txvCnpj = findViewById(R.id.txvCnpj);
        txvTelephone = findViewById(R.id.txvTelephone);
        txvAddress = findViewById(R.id.txvAddress);
        txvEmail = findViewById(R.id.txvEmail);
        txvObservationLabel = findViewById(R.id.txvObservationLabel);
        txvObservation = findViewById(R.id.txvObservation);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnServices = findViewById(R.id.btnServices);

        ((SwipeRefreshLayout) findViewById(R.id.swpRefresh)).setOnRefreshListener(this::recreate);

        txvNameContact.setText(client.getNameContact());
        txvCnpj.setText(client.getFormattedCnpj());
        txvTelephone.setText(client.getFormattedTelephone());
        txvAddress.setText(client.getAddress());
        txvEmail.setText(client.getEmail());
        txvObservation.setText(client.getObservation());

        btnServices.setOnClickListener(view ->
        {
            Intent intent = new Intent(here, ActEditClientServices.class);
            intent.putExtra("clientId", clientId);
            intent.putExtra("redirectTo", ActClient.class);

            startActivity(intent);
        });

        btnDelete.setOnClickListener(view ->
            new AlertDialog.Builder(this)
                .setTitle("Deletar cliente")
                .setMessage("Você tem certeza que deseja deletar o cliente selecionado?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Deletar", (dialog, whichButton) ->
                {
                    clientDAO.delete(clientId);
                    Toast.makeText(here, "Cliente deletado.", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(here, ActClient.class));
                })
                .setNegativeButton("Cancelar", null)
                .create().show());

        btnUpdate.setOnClickListener(view ->
        {
            Intent intent = new Intent(here, ActFormClient.class);
            intent.putExtra("clientId", clientId);
            intent.putExtra("isCreate", false);

            startActivity(intent);
        });

        if (Utils.isEmpty(client.getObservation()))
        {
            txvObservation.setVisibility(View.GONE);
            txvObservationLabel.setVisibility(View.GONE);
        }
    }
}