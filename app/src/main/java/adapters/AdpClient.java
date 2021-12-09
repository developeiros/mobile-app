package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.developeiros.ActDtlClient;
import com.example.developeiros.R;

import java.util.ArrayList;
import java.util.List;

import models.Client;

public class AdpClient extends ArrayAdapter<Client> {
    private final List<Client> clientList = new ArrayList<>();

    static class ClientViewHolder {
        RelativeLayout lytBackground;
        TextView name;
        TextView telephone;
        TextView email;
    }

    public AdpClient(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Client object) {
        clientList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return clientList.size();
    }

    @Override
    public Client getItem(int index) {
        return clientList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ClientViewHolder viewHolder;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_client, parent, false);

            viewHolder = new ClientViewHolder();
            viewHolder.lytBackground = row.findViewById(R.id.lytBackground);
            viewHolder.name = row.findViewById(R.id.txvName);
            viewHolder.telephone = row.findViewById(R.id.txvTel);
            viewHolder.email = row.findViewById(R.id.txvEmail);

            row.setTag(viewHolder);
        }
        else
            viewHolder = (ClientViewHolder) row.getTag();

        Client client = getItem(position);

        viewHolder.lytBackground.setOnClickListener(view -> {
            Intent intent = new Intent(parent.getContext(), ActDtlClient.class);
            intent.putExtra("clientId", client.getId());

            parent.getContext().startActivity(intent);
        });

        viewHolder.name.setText(client.getNameContact());
        viewHolder.telephone.setText(client.getFormattedTelephone());
        viewHolder.email.setText(client.getEmail());

        return row;
    }
}
