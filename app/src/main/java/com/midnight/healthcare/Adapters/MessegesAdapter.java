package com.midnight.healthcare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midnight.healthcare.API.Messegess;
import com.midnight.healthcare.Activity.ConversationActivity;
import com.midnight.healthcare.Activity.MessagesActivity;
import com.midnight.healthcare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUSK.ONE on 9/2/16.
 */
public class MessegesAdapter extends BaseAdapter {

    List<Messegess> list = new ArrayList<>();
    Context context;
    List<Integer> deletePositions = new ArrayList<>();

    LayoutInflater inflater;

    int typeGlobal;

    public MessegesAdapter(List<Messegess> list, Context context, int type) {
        this.list = list;
        this.context = context;

        typeGlobal = type;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(list == null) return 0;
        else return list.size();
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
        View rowView;
        if (convertView == null) {
            rowView =  inflater.inflate(R.layout.messages_cell, viewGroup, false);
        } else {
            rowView = convertView;
        }

        final Messegess item  = (Messegess) getItem(i);

        final int finalI = i;

        final LinearLayout messageTypeOne = (LinearLayout) rowView.findViewById(R.id.messageTypeOne);
        final LinearLayout messageTypeTwo = (LinearLayout) rowView.findViewById(R.id.messageTypeTwo);
        final ImageView checkBox = (ImageView) rowView.findViewById(R.id.checkBox);
        checkBox.setImageDrawable(context.getResources().getDrawable(R.drawable.uncheck));


        if(typeGlobal == 0) {

            messageTypeOne.setVisibility(View.VISIBLE);
            messageTypeTwo.setVisibility(View.GONE);

            RelativeLayout unread = (RelativeLayout) rowView.findViewById(R.id.unreadMessage);
            TextView from = (TextView) rowView.findViewById(R.id.mess_from);
            from.setText(item.getPartner_name());
            TextView last = (TextView) rowView.findViewById(R.id.mess_last);
            last.setText(item.getLastMessage().getText());
            TextView time = (TextView) rowView.findViewById(R.id.mess_time);
            time.setText(item.getLastMessage().getTime());

            unread.setVisibility(View.GONE);

            Boolean check = false;
            List<String> messageId = ((MessagesActivity)context).unreadMessages;
            for(int j = 0; j < messageId.size(); j++){
                if(item.getPartner().equals(messageId.get(j))) check = true;
            }
            if(check) unread.setVisibility(View.VISIBLE);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean check = false;
                    List<String> messageId = ((MessagesActivity)context).unreadMessages;
                    for(int j = 0; j < messageId.size(); j++){
                        if(item.getPartner().equals(messageId.get(j))) check = true;
                    }

                    if(check){
                        if(messageId.size() == 1){
                            ((MessagesActivity)context).clearPreferences();
                        }
                        else{
                            ((MessagesActivity)context).deleteUser(item.getPartner());
                        }
                    }
                    Intent intent = new Intent(context, ConversationActivity.class);
                    intent.putExtra("mess", item);
                    intent.putExtra("partnerName", item.getPartner_name());
                    context.startActivity(intent);
                }
            });
        }
        else{
            messageTypeOne.setVisibility(View.GONE);
            messageTypeTwo.setVisibility(View.VISIBLE);

            TextView from = (TextView) rowView.findViewById(R.id.message_from);
            from.setText(item.getPartner_name());
            TextView last = (TextView) rowView.findViewById(R.id.message_last);
            last.setText(item.getLastMessage().getText());
            TextView time = (TextView) rowView.findViewById(R.id.message_time);
            time.setText(item.getLastMessage().getTime());

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!deletePositions.contains(finalI)){
                        deletePositions.add(finalI);
                        checkBox.setImageDrawable(context.getResources().getDrawable(R.drawable.checked_time));
                        ((MessagesActivity) context).addToDeleteList(item);
                    }
                    else{
                        for(int j = 0; j < deletePositions.size(); j++){
                            if(deletePositions.get(j) == finalI) deletePositions.remove(j);
                        }
                        checkBox.setImageDrawable(context.getResources().getDrawable(R.drawable.uncheck));
                        ((MessagesActivity) context).deleteFromDeleteList(item);
                    }
                }
            });
        }


        return rowView;
    }
}
