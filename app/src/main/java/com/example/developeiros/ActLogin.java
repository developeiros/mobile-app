package com.example.developeiros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import database.*;
import models.Employee;

public class ActLogin extends AppCompatActivity
{
    Context here;
    TextInputEditText edtLogin, edtPassword;
    Button btnSubmit;
    TextView txvForgotPassword;
    EmployeeDAO employees;
    FrgForgotPassword frgForgotPassword;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        here = getApplicationContext();
        employees = EmployeeDAO.getInstance(here);

        // defining the ui elements
        btnSubmit = findViewById(R.id.btnSubmit);
        edtLogin = findViewById(R.id.edtLogin);
        edtPassword = findViewById(R.id.edtPassword);
        txvForgotPassword = findViewById(R.id.txvForgotPassword);
        frgForgotPassword = FrgForgotPassword.newInstance();

        btnSubmit.setOnClickListener(view ->
        {
            Employee newComer = new Employee(
                edtLogin.getText().toString(),
                edtPassword.getText().toString()
            );

            if (newComer.isBlank())
            {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            long employeeId = employees.authenticate(newComer);

            if (employeeId > -1)
            {
                Intent intent = new Intent(here, ActMenu.class);
                intent.putExtra("employeeId", employeeId);

                startActivity(intent);
            }
            else
                Toast.makeText(here, "Usuário ou senha incorretos.", Toast.LENGTH_SHORT).show();
        });

        txvForgotPassword.setOnClickListener(view ->
            fragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.bottom_to_top,
                    android.R.anim.fade_out,
                    R.anim.bottom_to_top,
                    android.R.anim.fade_out
                )
                .add(R.id.fragment_container_view, frgForgotPassword, null)
                .addToBackStack(null)
                .commit());
    }

    @Override
    public void onBackPressed()
    {
        if (frgForgotPassword != null)
            fragmentManager.popBackStackImmediate();
    }
}
