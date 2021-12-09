package com.example.developeiros;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import database.ClientDAO;
import models.Client;
import utils.MaskWatcher;
import utils.Utils;

public class ActFormClient extends AppCompatActivity {
    Context here;
    TextView txvClientFormTitle;
    TextInputEditText edtNameContact, edtCnpj, edtTelephone, edtAddress, edtEmail, edtObservation;
    boolean isCreate;
    Button btnCadastrar;
    ClientDAO clientDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_client);

        here = getApplicationContext();
        clientDAO = ClientDAO.getInstance(here);
        isCreate = getIntent().getExtras().getBoolean("isCreate");

        // defining the ui elements
        txvClientFormTitle = findViewById(R.id.txvClientFormTitle);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        edtCnpj = findViewById(R.id.edtCnpj);
        edtNameContact = findViewById(R.id.edtNameContact);
        edtTelephone = findViewById(R.id.edtTelephone);
        edtAddress = findViewById(R.id.edtAddress);
        edtEmail = findViewById(R.id.edtEmail);
        edtObservation = findViewById(R.id.edtObservation);

        txvClientFormTitle.setText(isCreate ? "Cadastrar cliente" : "Atualizar cliente");
        btnCadastrar.setText(isCreate ? "Cadastrar" : "Atualizar");

        btnCadastrar.setOnClickListener(view ->
        {
            if (isValid())
            {
                // insert or update
                if (isCreate)
                {
                    long lastInsertId = clientDAO.insert(getInputClient());
                    Toast.makeText(this, "Cliente cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(here, ActEditClientServices.class);
                    intent.putExtra("clientId", lastInsertId);
                    intent.putExtra("redirectTo", ActDtlClient.class);

                    startActivity(intent);
                }
                else
                {
                    clientDAO.update(getInputClient());
                    Toast.makeText(this, "Cliente atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

        edtCnpj.addTextChangedListener(new MaskWatcher("##.###.###/####-##"));
        edtTelephone.addTextChangedListener(new MaskWatcher("(##) #####-####"));

        if (!isCreate)
        {
            Client client = clientDAO.select(getIntent().getExtras().getLong("clientId"));

            edtNameContact.setText(client.getNameContact());
            edtCnpj.setText(client.getFormattedCnpj());
            edtTelephone.setText(client.getFormattedTelephone());
            edtAddress.setText(client.getAddress());
            edtEmail.setText(client.getEmail());
            edtObservation.setText(client.getObservation());
        }
    }

    private Client getInputClient()
    {
        if (!isCreate)
            return new Client(
                getIntent().getExtras().getLong("clientId"),
                edtCnpj.getText().toString(),
                edtNameContact.getText().toString(),
                edtTelephone.getText().toString(),
                edtAddress.getText().toString(),
                edtEmail.getText().toString(),
                edtObservation.getText().toString()
            );

        return new Client(
            edtCnpj.getText().toString(),
            edtNameContact.getText().toString(),
            edtTelephone.getText().toString(),
            edtAddress.getText().toString(),
            edtEmail.getText().toString(),
            edtObservation.getText().toString()
        );
    }

    private boolean isValid()
    {
        String nameContact = edtNameContact.getText().toString();
        String cnpj = edtCnpj.getText().toString();
        String telephone = edtTelephone.getText().toString();
        String address = edtAddress.getText().toString();
        String email = edtEmail.getText().toString();

        if (Utils.isEmpty(nameContact))
        {
            edtNameContact.setError("Preencha esse campo");
            return false;
        }

        if (Utils.isEmpty(cnpj))
        {
            edtCnpj.setError("Preencha esse campo");
            return false;
        }

        if (cnpj.length() != 18)
        {
            edtCnpj.setError("Formato inválido");
            return false;
        }

        if (!Utils.isCnpjValid(cnpj))
        {
            edtCnpj.setError("CNPJ inválido");
            return false;
        }

        if (Utils.isEmpty(telephone))
        {
            edtTelephone.setError("Preencha esse campo");
            return false;
        }

        if (telephone.length() != 15)
        {
            edtTelephone.setError("Formato inválido");
            return false;
        }

        if (Utils.isEmpty(address))
        {
            edtAddress.setError("Preencha esse campo");
            return false;
        }

        if (Utils.isEmpty(email))
        {
            edtEmail.setError("Preencha esse campo");
            return false;
        }

        if (!Utils.isEmailValid(email))
        {
            edtEmail.setError("Insira um e-mail válido");
            return false;
        }

        return true;
    }
}