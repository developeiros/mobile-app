package adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developeiros.ActDtlService;
import com.example.developeiros.R;

import java.util.ArrayList;
import java.util.List;

import database.EmployeeServiceDAO;
import models.EmployeeService;

public class AdpEmployeeServiceEdit extends ArrayAdapter<EmployeeService>
{
    private final List<EmployeeService> employeeServices = new ArrayList<>();
    private Context context;

    static class EmployeeServiceViewHolder
    {
        RelativeLayout lytBackground;
        TextView title;
        ImageButton btnDelete;
    }

    public AdpEmployeeServiceEdit(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
        this.context = context;
    }

    @Override
    public void add(EmployeeService object)
    {
        employeeServices.add(object);
        super.add(object);
    }

    @Override
    public int getCount()
    {
        return employeeServices.size();
    }

    @Override
    public EmployeeService getItem(int index)
    {
        return employeeServices.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        AdpEmployeeServiceEdit.EmployeeServiceViewHolder viewHolder;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_service_editable, parent, false);

            viewHolder = new AdpEmployeeServiceEdit.EmployeeServiceViewHolder();
            viewHolder.lytBackground = row.findViewById(R.id.lytBackground);
            viewHolder.title = row.findViewById(R.id.txvService);
            viewHolder.btnDelete = row.findViewById(R.id.btnDelete);

            row.setTag(viewHolder);
        }
        else
            viewHolder = (AdpEmployeeServiceEdit.EmployeeServiceViewHolder) row.getTag();

        EmployeeService employeeService = getItem(position);

        viewHolder.title.setText(employeeService.getService().getTitle());
        viewHolder.btnDelete.setOnClickListener(view ->
            new AlertDialog.Builder(parent.getContext())
                .setTitle("Deletar serviço do funcionário")
                .setMessage("Você tem certeza que deseja deletar o serviço selecionado?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Deletar", (dialog, whichButton) ->
                {
                    EmployeeServiceDAO.getInstance(parent.getContext())
                        .delete(
                            employeeService.getEmployee().getId(),
                            employeeService.getService().getId()
                        );

                    Toast.makeText(context, "Serviço deletado.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .create().show());

        viewHolder.lytBackground.setOnClickListener(view ->
        {
            Intent intent = new Intent(parent.getContext(), ActDtlService.class);
            intent.putExtra("serviceId", employeeService.getService().getId());

            parent.getContext().startActivity(intent);
        });

        return row;
    }
}
