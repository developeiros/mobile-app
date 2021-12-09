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

import database.ClientServiceDAO;
import models.ClientService;

public class AdpClientServiceEdit extends ArrayAdapter<ClientService>
{
    private final List<ClientService> clientServiceList = new ArrayList<>();

    static class ClientServiceViewHolder
    {
        RelativeLayout lytBackground;
        TextView title;
        ImageButton btnDelete;
    }

    public AdpClientServiceEdit(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
    }

    @Override
    public void add(ClientService object)
    {
        clientServiceList.add(object);
        super.add(object);
    }

    @Override
    public int getCount()
    {
        return clientServiceList.size();
    }

    @Override
    public ClientService getItem(int index)
    {
        return clientServiceList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Context context = parent.getContext();
        View row = convertView;

        AdpClientServiceEdit.ClientServiceViewHolder viewHolder;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_service_editable, parent, false);

            viewHolder = new AdpClientServiceEdit.ClientServiceViewHolder();
            viewHolder.lytBackground = row.findViewById(R.id.lytBackground);
            viewHolder.title = row.findViewById(R.id.txvService);
            viewHolder.btnDelete = row.findViewById(R.id.btnDelete);

            row.setTag(viewHolder);
        }
        else
            viewHolder = (AdpClientServiceEdit.ClientServiceViewHolder) row.getTag();

        ClientService clientService = getItem(position);

        viewHolder.title.setText(clientService.getService().getTitle());
        viewHolder.btnDelete.setOnClickListener(view ->
            new AlertDialog.Builder(context)
                .setTitle("Deletar serviço do cliente")
                .setMessage("Você tem certeza que deseja deletar o serviço selecionado?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Deletar", (dialog, whichButton) ->
                {
                    ClientServiceDAO.getInstance(context)
                        .delete(
                            clientService.getClient().getId(),
                            clientService.getService().getId()
                        );

                    Toast.makeText(context, "Serviço deletado.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .create().show());

        viewHolder.lytBackground.setOnClickListener(view ->
        {
            Intent intent = new Intent(context, ActDtlService.class);
            intent.putExtra("serviceId", clientService.getService().getId());

            context.startActivity(intent);
        });

        return row;
    }
}
