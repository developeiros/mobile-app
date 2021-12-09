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

import database.EventDAO;
import models.Event;

public class ActDtlEvent extends AppCompatActivity
{
    Context here;
    long eventId;
    TextView txvEvent, txvClient, txvDatetime, txvDescription;
    Event event;
    Button btnDelete, btnUpdate;
    EventDAO eventDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtl_event);

        here = getApplicationContext();
        eventId = getIntent().getExtras().getLong("eventId");
        eventDAO = EventDAO.getInstance(here);

        // defining the ui elements
        txvEvent = findViewById(R.id.txvEvent);
        txvClient = findViewById(R.id.txvClient);
        txvDatetime = findViewById(R.id.txvDatetime);
        txvDescription = findViewById(R.id.txvDescription);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);

        ((SwipeRefreshLayout) findViewById(R.id.swpRefresh)).setOnRefreshListener(this::recreate);

        event = eventDAO.select(eventId);

        txvEvent.setText(String.format("%s.", event.getTitle()));
        txvClient.setText(String.format("%s.", event.getClient().getNameContact()));
        txvDatetime.setText(String.format("%s.", event.getFormattedDateTime()));
        txvDescription.setText(event.getDescription());

        btnDelete.setOnClickListener(view ->
            new AlertDialog.Builder(this)
                .setTitle("Deletar evento")
                .setMessage("Você tem certeza que deseja deletar o evento selecionado?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Deletar", (dialog, whichButton) ->
                {
                    eventDAO.delete(eventId);
                    Toast.makeText(here, "Evento deletado.", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(here, ActEvent.class));
                })
                .setNegativeButton("Cancelar", null)
                .create().show());

        btnUpdate.setOnClickListener(view ->
        {
            Intent intent = new Intent(here, ActFormEvent.class);
            intent.putExtra("eventId", eventId);
            intent.putExtra("isCreate", false);

            startActivity(intent);
        });
    }
}