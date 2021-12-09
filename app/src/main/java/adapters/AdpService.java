package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.developeiros.ActDtlService;
import com.example.developeiros.R;

import java.util.ArrayList;
import java.util.List;

import models.Service;

public class AdpService extends ArrayAdapter<Service> {
    private final List<Service> serviceList = new ArrayList<>();

    static class ServiceViewHolder {
        RelativeLayout lytBackground;
        TextView service;
    }

    public AdpService(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Service object) {
        serviceList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.serviceList.size();
    }

    @Override
    public Service getItem(int index) {
        return this.serviceList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        AdpService.ServiceViewHolder viewHolder;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_service, parent, false);

            viewHolder = new AdpService.ServiceViewHolder();
            viewHolder.service = row.findViewById(R.id.txvService);
            viewHolder.lytBackground = row.findViewById(R.id.lytBackground);

            row.setTag(viewHolder);
        }
        else
            viewHolder = (AdpService.ServiceViewHolder)row.getTag();

        Service service = getItem(position);
        viewHolder.service.setText(service.getTitle());

        viewHolder.lytBackground.setOnClickListener(view ->
        {
            Intent intent = new Intent(parent.getContext(), ActDtlService.class);
            intent.putExtra("serviceId", service.getId());

            parent.getContext().startActivity(intent);
        });

        return row;
    }
}
