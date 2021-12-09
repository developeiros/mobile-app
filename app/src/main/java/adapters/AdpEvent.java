package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.developeiros.ActDtlEvent;
import com.example.developeiros.R;

import java.util.ArrayList;
import java.util.List;

import models.Event;

public class AdpEvent extends ArrayAdapter<Event> {
    private final List<Event> eventList = new ArrayList<>();

    static class EventViewHolder {
        RelativeLayout lytBackground;
        TextView title;
        TextView datetime;
        TextView client;
    }

    public AdpEvent(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Event object) {
        eventList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.eventList.size();
    }

    @Override
    public Event getItem(int index) {
        return this.eventList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        AdpEvent.EventViewHolder viewHolder;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_event, parent, false);

            viewHolder = new AdpEvent.EventViewHolder();
            viewHolder.lytBackground = row.findViewById(R.id.lytBackground);
            viewHolder.title = row.findViewById(R.id.txvTitle);
            viewHolder.datetime = row.findViewById(R.id.txvDatetime);
            viewHolder.client = row.findViewById(R.id.txvClient);

            row.setTag(viewHolder);
        }
        else
            viewHolder = (AdpEvent.EventViewHolder)row.getTag();

        Event event = getItem(position);
        viewHolder.title.setText(event.getTitle());
        viewHolder.datetime.setText(event.getFormattedDateTime());
        viewHolder.client.setText(event.getClient().getNameContact());

        viewHolder.lytBackground.setOnClickListener(view ->
        {
            Intent intent = new Intent(parent.getContext(), ActDtlEvent.class);
            intent.putExtra("eventId", event.getId());

            parent.getContext().startActivity(intent);
        });

        return row;
    }
}
