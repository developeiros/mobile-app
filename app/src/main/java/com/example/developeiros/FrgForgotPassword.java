package com.example.developeiros;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import database.EmployeeDAO;
import models.Employee;
import utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FrgForgotPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrgForgotPassword extends Fragment
{
    Context here;
    TextInputEditText edtEmail;
    Button btnRecover;
    EmployeeDAO employees;
    Activity activity;

    public FrgForgotPassword()
    { }

    public static FrgForgotPassword newInstance()
    {
        FrgForgotPassword fragment = new FrgForgotPassword();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        activity = getActivity();
        here = activity.getApplicationContext();
        edtEmail = view.findViewById(R.id.edtEmail);
        btnRecover = view.findViewById(R.id.btnRecover);
        employees = EmployeeDAO.getInstance(here);

        btnRecover.setOnClickListener(buttonView ->
        {
            if (Utils.isEmpty(edtEmail.getText().toString()))
                edtEmail.setError("Preencha esse campo");
            else
            {
                String email = edtEmail.getText().toString();
                Employee employee = employees.selectEmail(email);

                if (employee != null)
                {
                    employees.sendRecoveryEmail(here, employee);
                    Toast.makeText(here, "Verifique seu e-mail :)", Toast.LENGTH_SHORT).show();
                    activity.onBackPressed();
                }
                else
                    Toast.makeText(here, "O e-mail inserido não foi encontrado.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}