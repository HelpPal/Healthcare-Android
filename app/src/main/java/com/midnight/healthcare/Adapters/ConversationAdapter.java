package com.midnight.healthcare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.midnight.healthcare.API.LastMessage;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUSK.ONE on 9/2/16.
 */
public class ConversationAdapter extends BaseAdapter {

    List<LastMessage> list = new ArrayList<>();
    Context context;

    LayoutInflater inflater;

    public ConversationAdapter(List<LastMessage> list, Context context) {
        this.list = list;
        this.context = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final LastMessage item  = (LastMessage) getItem(i);
        View rowView;
        if (item.getUser().equals(((Global)context.getApplicationContext()).getCurrentUser().getId())) {
            if (convertView == null) {
                rowView =  inflater.inflate(R.layout.to_cell, viewGroup, false);
            } else {
                rowView = convertView;
            }
        }else {
            if (convertView == null) {
                rowView =  inflater.inflate(R.layout.from_cell, viewGroup, false);
            } else {
                rowView = convertView;
            }

        }



        TextView text = (TextView) rowView.findViewById(R.id.mess_text);
        text.setText(item.getText());
        TextView time = (TextView) rowView.findViewById(R.id.mess_time);
       time.setText(item.getTime());



        return rowView;
    }
}
