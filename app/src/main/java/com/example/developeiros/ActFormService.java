package com.example.developeiros;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import database.ServiceDAO;
import models.Service;
import utils.Utils;

public class ActFormService extends AppCompatActivity {
    TextView txvServiceFormTitle;
    TextInputEditText edtTitle, edtDescription;
    boolean isCreate;
    Button btnCadastrar;
    ServiceDAO serviceDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_service);

        serviceDAO = ServiceDAO.getInstance(getApplicationContext());
        isCreate = getIntent().getExtras().getBoolean("isCreate");

        // defining the ui elements
        txvServiceFormTitle = findViewById(R.id.txvServiceFormTitle);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);

        txvServiceFormTitle.setText(isCreate ? "Cadastrar serviço" : "Atualizar serviço");
        btnCadastrar.setText(isCreate ? "Cadastrar" : "Atualizar");

        btnCadastrar.setOnClickListener(view ->
        {
            if (isValid())
            {
                // insert or update
                if (isCreate)
                {
                    serviceDAO.insert(getInputService());
                    Toast.makeText(this, "Serviço cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    serviceDAO.update(getInputService());
                    Toast.makeText(this, "Serviço atualizado com sucesso", Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
            }
        });

        // filling the inputs if it's an update
        if (!isCreate)
        {
            Service service = serviceDAO.select(getIntent().getExtras().getLong("serviceId"));

            edtTitle.setText(service.getTitle());
            edtDescription.setText(service.getDescription());
        }
    }

    private Service getInputService()
    {
        if (!isCreate)
            return new Service(
                getIntent().getExtras().getLong("serviceId"),
                edtTitle.getText().toString(),
                edtDescription.getText().toString()
            );

        return new Service(
            edtTitle.getText().toString(),
            edtDescription.getText().toString()
        );
    }

    private boolean isValid()
    {
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();

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

        return true;
    }
}