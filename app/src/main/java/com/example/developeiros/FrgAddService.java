package com.example.developeiros;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import database.ClientServiceDAO;
import database.EmployeeServiceDAO;
import database.ServiceDAO;
import models.Client;
import models.ClientService;
import models.Employee;
import models.EmployeeService;
import models.Service;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FrgAddService#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrgAddService extends Fragment
{
    private static final String ENTITY_ID = "entityId";
    private static final String IS_CLIENT = "isClient";

    private long entityId;
    private boolean isClient;

    Context here;
    Button btnAdd;
    Spinner spnService;
    ClientServiceDAO clientServiceDAO;
    EmployeeServiceDAO employeeServiceDAO;

    public FrgAddService()
    { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param entityId id of the client or employee.
     * @param isClient boolean defining if it's a client or an employee.
     * @return A new instance of fragment FrgAddService.
     */
    public static FrgAddService newInstance(long entityId, boolean isClient)
    {
        FrgAddService fragment = new FrgAddService();
        Bundle args = new Bundle();
        args.putLong(ENTITY_ID, entityId);
        args.putBoolean(IS_CLIENT, isClient);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            entityId = getArguments().getLong(ENTITY_ID);
            isClient = getArguments().getBoolean(IS_CLIENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_service, container, false);

        here = getActivity().getApplicationContext();
        spnService = view.findViewById(R.id.spnService);
        btnAdd = view.findViewById(R.id.btnAddService);
        clientServiceDAO = ClientServiceDAO.getInstance(here);
        employeeServiceDAO = EmployeeServiceDAO.getInstance(here);

        fillSpinner();

        btnAdd.setOnClickListener(v ->
        {
            String selected = spnService.getSelectedItem().toString();

            Service service = ServiceDAO.getInstance(here).getByTitle(selected);

            if (isClient)
            {
                clientServiceDAO.insert(new ClientService(
                    new Client(entityId),
                    service
                ));
            }
            else
            {
                try {
                    EmployeeServiceDAO.getInstance(here).insert(new EmployeeService(
                        new Employee(entityId),
                        service
                    ));
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            leave();
        });

        return view;
    }

    private void fillSpinner()
    {
        String[] services =
            isClient ?
            clientServiceDAO.getServicesTitles(entityId) :
            employeeServiceDAO.getServicesTitles(entityId);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(here, R.layout.spinner_item, services);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnService.setAdapter(adapter);
    }

    private void leave()
    {
        Activity activity = getActivity();

        activity.onBackPressed();
        activity.recreate();
    }
}