package com.example.developeiros;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;

import database.ClientDAO;
import database.EventDAO;
import models.Event;
import utils.Utils;

public class ActFormEvent extends AppCompatActivity {
    Context here;
    TextView txvEventFormTitle;
    TextInputEditText edtTitle, edtDate, edtTime, edtDescription;
    Spinner spnClient;
    boolean isCreate;
    Button btnCadastrar;
    EventDAO eventDAO;
    Calendar calendar;
    String[] clients;
    ClientDAO clientDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_event);

        here = getApplicationContext();
        eventDAO = EventDAO.getInstance(here);
        isCreate = getIntent().getExtras().getBoolean("isCreate");
        calendar = Calendar.getInstance();
        clientDAO = ClientDAO.getInstance(here);

        // defining the ui elements
        txvEventFormTitle = findViewById(R.id.txvEventFormTitle);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        edtTitle = findViewById(R.id.edtTitle);
        spnClient = findViewById(R.id.spnClient);
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);
        edtDescription = findViewById(R.id.edtDescription);

        txvEventFormTitle.setText(isCreate ? "Cadastrar evento" : "Atualizar evento");
        btnCadastrar.setText(isCreate ? "Cadastrar" : "Atualizar");

        // setting the spinner
        clients = clientDAO.getClientsNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(here, R.layout.spinner_item, clients);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnClient.setAdapter(adapter);

        // setting the datepicker
        DatePickerDialog.OnDateSetListener datePicker = (date, year, month, day) ->
        {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateDateLabel();
        };

        edtDate.setOnClickListener(view ->
            new DatePickerDialog(
                ActFormEvent.this,
                datePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show());

        // setting the time picker
        TimePickerDialog.OnTimeSetListener timePicker = (time, hour, minute) ->
        {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        };

        edtTime.setOnClickListener(view ->
            new TimePickerDialog(
                ActFormEvent.this,
                timePicker,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show());

        // submit button
        btnCadastrar.setOnClickListener(view ->
        {
            if (isValid())
            {
                // insert or update
                if (isCreate)
                {
                    eventDAO.insert(getInputEvent());
                    Toast.makeText(this, "Evento cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    eventDAO.update(getInputEvent());
                    Toast.makeText(this, "Evento atualizado com sucesso", Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
            }
        });

        // filling the inputs if it's an update
        if (!isCreate)
        {
            Event event = eventDAO.select(getIntent().getExtras().getLong("eventId"));

            edtTitle.setText(event.getTitle());
            edtDescription.setText((event.getDescription()));
            spnClient.setSelection(adapter.getPosition(event.getClient().getNameContact()));

            calendar.set(Calendar.DAY_OF_MONTH, event.getDateTime().getDayOfMonth());
            calendar.set(Calendar.MONTH, event.getDateTime().getMonthValue() - 1);
            calendar.set(Calendar.YEAR, event.getDateTime().getYear());
            updateDateLabel();

            calendar.set(Calendar.HOUR_OF_DAY, event.getDateTime().getHour());
            calendar.set(Calendar.MINUTE, event.getDateTime().getMinute());
            updateTimeLabel();
        }
    }

    private Event getInputEvent()
    {
        if (!isCreate)
            return new Event(
                getIntent().getExtras().getLong("eventId"),
                edtTitle.getText().toString(),
                edtDescription.getText().toString(),
                clientDAO.getByName(spnClient.getSelectedItem().toString()),
                getDateTime()
            );

        return new Event(
            edtTitle.getText().toString(),
            edtDescription.getText().toString(),
            clientDAO.getByName(spnClient.getSelectedItem().toString()),
            getDateTime()
        );
    }

    private void updateDateLabel()
    {
        String format = "dd/MM/y";
        SimpleDateFormat sdf = new SimpleDateFormat(format, new Locale("pt", "BR"));

        edtDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateTimeLabel()
    {
        edtTime.setText(String.format("%d:%d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
    }

    private LocalDateTime getDateTime()
    {
        LocalDateTime result = LocalDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        );

        // returns null if the date has already passed
        if (result.isBefore(LocalDateTime.now()))
            return null;

        return result;
    }

    private boolean isValid()
    {
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        String date = edtDate.getText().toString();
        String time = edtTime.getText().toString();

        if (Utils.isEmpty(title))
        {
            edtTitle.setError("Preencha esse campo");
            return false;
        }

        if (Utils.isEmpty(description))
        {
            edtDescription.setError("Preencha esse campo");
            return false;
        }

        if (Utils.isEmpty(date))
        {
            edtDate.setError("Preencha esse campo");
            return false;
        }

        if (Utils.isEmpty(time))
        {
            edtTime.setError("Preencha esse campo");
            return false;
        }

        if (getDateTime() == null)
        {
            edtTime.setError("A data selecionada já passou");
            return false;
        }

        return true;
    }
}