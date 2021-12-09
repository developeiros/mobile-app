package com.example.developeiros;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import database.EmployeeDAO;
import models.Employee;
import utils.MaskWatcher;
import utils.Utils;

public class ActFormEmployee extends AppCompatActivity
{
    Context here;
    TextView txvEmployeeFormTitle;
    TextInputEditText edtLogin, edtName, edtTelephone, edtAddress, edtEmail, edtPassword;
    boolean isCreate;
    Button btnCadastrar;
    EmployeeDAO employeeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_employee);

        here = getApplicationContext();
        employeeDAO = EmployeeDAO.getInstance(here);
        isCreate = getIntent().getExtras().getBoolean("isCreate");

        // defining the ui elements
        txvEmployeeFormTitle = findViewById(R.id.txvEmployeeFormTitle);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        edtLogin = findViewById(R.id.edtLogin);
        edtName = findViewById(R.id.edtName);
        edtTelephone = findViewById(R.id.edtTelephone);
        edtAddress = findViewById(R.id.edtAddress);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        txvEmployeeFormTitle.setText(isCreate ? "Cadastrar funcionário" : "Atualizar funcionário");
        btnCadastrar.setText(isCreate ? "Cadastrar" : "Atualizar");

        btnCadastrar.setOnClickListener(view ->
        {
            if (isValid())
            {
                try
                {
                    // insert or update
                    if (isCreate)
                    {
                        long lastInsertId = employeeDAO.insert(getInputEmployee());
                        Toast.makeText(this, "Funcionário cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(here, ActEditEmployeeServices.class);
                        intent.putExtra("employeeId", lastInsertId);
                        intent.putExtra("redirectTo", ActDtlEmployee.class);

                        startActivity(intent);
                    }
                    else
                    {
                        employeeDAO.update(getInputEmployee());
                        Toast.makeText(this, "Funcionário atualizado com sucesso", Toast.LENGTH_SHORT).show();

                        onBackPressed();
                    }
                }
                catch (SQLiteConstraintException e)
                {
                    edtLogin.setError("Este login já está sendo utilizado.");
                }
            }
        });

        edtTelephone.addTextChangedListener(new MaskWatcher("(##) #####-####"));

        if (!isCreate)
        {
            Employee employee = employeeDAO.select(getIntent().getExtras().getLong("employeeId"));

            edtLogin.setText(employee.getLogin());
            edtName.setText(employee.getName());
            edtTelephone.setText(employee.getFormattedTelephone( ));
            edtAddress.setText(employee.getAddress());
            edtEmail.setText(employee.getEmail());
        }
    }

    private Employee getInputEmployee()
    {
        if (!isCreate)
        {
            return new Employee(
                getIntent().getExtras().getLong("employeeId"),
                edtName.getText().toString(),
                edtTelephone.getText().toString(),
                edtAddress.getText().toString(),
                edtEmail.getText().toString(),
                edtLogin.getText().toString(),
                edtPassword.getText().toString()
            );
        }

        return new Employee(
            edtName.getText().toString(),
            edtTelephone.getText().toString(),
            edtAddress.getText().toString(),
            edtEmail.getText().toString(),
            edtLogin.getText().toString(),
            edtPassword.getText().toString()
        );
    }

    private boolean isValid()
    {
        String login = edtLogin.getText().toString();
        String name = edtName.getText().toString();
        String telephone = edtTelephone.getText().toString();
        String address = edtAddress.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (Utils.isEmpty(login))
        {
            edtLogin.setError("Preencha esse campo");
            return false;
        }

        if (Utils.isEmpty(name))
        {
            edtName.setError("Preencha esse campo");
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

        if (Utils.isEmpty(password))
        {
            edtPassword.setError("Preencha esse campo");
            return false;
        }

        return true;
    }
}