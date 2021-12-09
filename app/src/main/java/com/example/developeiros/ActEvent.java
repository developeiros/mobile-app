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

import adapters.AdpEvent;
import models.Event;
import database.EventDAO;

public class ActEvent extends AppCompatActivity
{
    Context here;
    TextView txvNoneEvent;
    AdpEvent adpEvent;
    ListView lsvEvent;
    Button btnNewEvent;

    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // defining the ui elements
        here = getApplicationContext();
        txvNoneEvent = findViewById(R.id.txvNoneEvent);
        lsvEvent = findViewById(R.id.lsvEvent);
        btnNewEvent = findViewById(R.id.btnNewEvent);

        events = EventDAO.getInstance(here).select();

        ((SwipeRefreshLayout) findViewById(R.id.swpRefresh)).setOnRefreshListener(this::recreate);

        if (events.size() == 0)
            noEvents();
        else
        {
            adpEvent = new AdpEvent(here, R.layout.row_event);

            for (Event event : events)
                adpEvent.add(event);

            lsvEvent.setAdapter(adpEvent);
        }

        btnNewEvent.setOnClickListener(view ->
        {
            Intent intent = new Intent(here, ActFormEvent.class);
            intent.putExtra("isCreate", true);

            startActivity(intent);
        });
    }

    private void noEvents()
    {
        txvNoneEvent.setVisibility(View.VISIBLE);
    }
}