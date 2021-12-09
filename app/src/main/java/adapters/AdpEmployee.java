package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.developeiros.ActDtlEmployee;
import com.example.developeiros.R;

import java.util.ArrayList;
import java.util.List;

import models.Employee;

public class AdpEmployee extends ArrayAdapter<Employee>
{
    private final List<Employee> employeeList = new ArrayList<>();

    static class EmployeeViewHolder
    {
        RelativeLayout lytBackground;
        TextView name;
        TextView email;
    }

    public AdpEmployee(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Employee object)
    {
        employeeList.add(object);
        super.add(object);
    }

    @Override
    public int getCount()
    {
        return this.employeeList.size();
    }

    @Override
    public Employee getItem(int index)
    {
        return this.employeeList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        EmployeeViewHolder viewHolder;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_employee, parent, false);

            viewHolder = new EmployeeViewHolder();
            viewHolder.lytBackground = row.findViewById(R.id.lytBackground);
            viewHolder.name = row.findViewById(R.id.txvName);
            viewHolder.email = row.findViewById(R.id.txvEmail);

            row.setTag(viewHolder);
        }
        else
            viewHolder = (EmployeeViewHolder) row.getTag();

        Employee employee = getItem(position);

        viewHolder.lytBackground.setOnClickListener(view ->
        {
            Intent intent = new Intent(parent.getContext(), ActDtlEmployee.class);
            intent.putExtra("employeeId", employee.getId());

            parent.getContext().startActivity(intent);
        });

        viewHolder.name.setText(employee.getShortName());
        viewHolder.email.setText(employee.getEmail());

        return row;
    }
}
