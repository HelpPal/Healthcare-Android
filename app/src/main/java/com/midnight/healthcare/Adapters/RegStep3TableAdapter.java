package com.midnight.healthcare.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midnight.healthcare.API.Skill;
import com.midnight.healthcare.Global;
import com.midnight.healthcare.R;
import com.midnight.healthcare.Text;

import java.util.ArrayList;
import java.util.List;

public class RegStep3TableAdapter extends RecyclerView.Adapter<RegStep3TableAdapter.ViewHolder> implements View.OnClickListener {

    List<Skill> list;
    Context context;
    private int position;
    LayoutInflater inflater;

    public RegStep3TableAdapter(List<Skill> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.registration_step3_table_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (list.get(position).getId().equals("35") && !((Global) context.getApplicationContext()).getRegModel().getOtherSkills().contains(list.get(position).getName())) {
            ((Global) context.getApplicationContext()).getRegModel().getOtherSkills().add(list.get(position).getName());

//            MARK AS CHECKED
            holder.unchecked.setImageDrawable(context.getResources().getDrawable(R.drawable.checked_background));
            holder.checkSign.setVisibility(View.VISIBLE);

            ((Global) context.getApplicationContext()).getRegModel().getSkilss().remove(list.get(position).getName());
            ((Global) context.getApplicationContext()).getRegModel().getSkilssid().remove(list.get(position).getId());
        }


        for (String id : ((Global) context.getApplicationContext()).getRegModel().getSkilssid()) {
            if (list.get(position).getId().equals(id)) {
                holder.unchecked.setImageDrawable(context.getResources().getDrawable(R.drawable.checked_background));
                holder.checkSign.setVisibility(View.VISIBLE);
            }
        }

        holder.title.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {

    }

//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return list.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(final int i, View convertView, ViewGroup viewGroup) {
//        final ViewHolder holder;
//
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.registration_step3_table_cell, viewGroup, false);
//            holder = new ViewHolder();
//
//            holder.background = (LinearLayout) convertView.findViewById(R.id.background);
//            holder.checkSign = (ImageView) convertView.findViewById(R.id.checkSign);
//            holder.unchecked = (ImageView) convertView.findViewById(R.id.checkBox);
//            holder.title = (TextView) convertView.findViewById(R.id.skill_title);
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        final Skill item = (Skill) getItem(i);
//
//
//        holder.title.setText(item.getName());
//
//
//        if (item.getId().equals("35") && !((Global) context.getApplicationContext()).getRegModel().getOtherSkills().contains(item.getName())) {
//            ((Global) context.getApplicationContext()).getRegModel().getOtherSkills().add(item.getName());
//
////            MARK AS CHECKED
//            holder.unchecked.setImageDrawable(context.getResources().getDrawable(R.drawable.checked_background));
//            holder.checkSign.setVisibility(View.VISIBLE);
//
//            ((Global) context.getApplicationContext()).getRegModel().getSkilss().remove(item.getName());
//            ((Global) context.getApplicationContext()).getRegModel().getSkilssid().remove(item.getId());
//        }
//
//
//        for (String id : ((Global) context.getApplicationContext()).getRegModel().getSkilssid()) {
//            if (item.getId().equals(id)) {
//                holder.unchecked.setImageDrawable(context.getResources().getDrawable(R.drawable.checked_background));
//                holder.checkSign.setVisibility(View.VISIBLE);
//            }
//        }
//
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//        });
//
//        return convertView;
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView unchecked;
        ImageView checkSign;
        LinearLayout background;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            unchecked = (ImageView) itemView.findViewById(R.id.checkBox);
            checkSign = (ImageView) itemView.findViewById(R.id.checkSign);
            title = (TextView) itemView.findViewById(R.id.skill_title);
            background = (LinearLayout) itemView.findViewById(R.id.background);

            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkSign.getVisibility() == View.GONE) {

                        unchecked.setImageDrawable(context.getResources().getDrawable(R.drawable.checked_background));
                        checkSign.setVisibility(View.VISIBLE);

                        if (list.get(getAdapterPosition()).getId().equals("35")) {
                            addOtherSkill(list.get(getAdapterPosition()));
                        } else {
                            addItem(list.get(getAdapterPosition()));
                        }

                    } else {

                        unchecked.setImageDrawable(context.getResources().getDrawable(R.drawable.uncheck));
                        checkSign.setVisibility(View.GONE);

                        if (list.get(getAdapterPosition()).getId().equals("35")) {
                            Log.d("SKILL", "BEFORE ONCLICK: " + ((Global) context.getApplicationContext()).getRegModel().getOtherSkills().size());
                            removeOtherSkill(list.get(getAdapterPosition()));
                            Log.d("SKILL", "AFTER ONCLICK: " + ((Global) context.getApplicationContext()).getRegModel().getOtherSkills().size());

                        } else {
                            Log.d("SKILL", "BEFORE ONCLICK: " + (((Global) context.getApplicationContext()).getRegModel().getSkilss().size()));
                            Log.d("SKILL", "BEFORE ONCLICK: " + (((Global) context.getApplicationContext()).getRegModel().getSkilssid().size()));
                            removeItem(list.get(getAdapterPosition()));
                            Log.d("SKILL", "BEFORE ONCLICK: " + (((Global) context.getApplicationContext()).getRegModel().getSkilss().size()));
                            Log.d("SKILL", "BEFORE ONCLICK: " + (((Global) context.getApplicationContext()).getRegModel().getSkilssid().size()));
                        }
                    }
                }
            });
        }
    }


    private void addOtherSkill(Skill item) {
        if (!((Global) context.getApplicationContext()).getRegModel().getOtherSkills().contains(item.getName())) {
            ((Global) context.getApplicationContext()).getRegModel().getOtherSkills().add(item.getName());
        }
    }

    private void addItem(Skill item) {
        ((Global) context.getApplicationContext()).getRegModel().getSkilss().add(item.getName());
        ((Global) context.getApplicationContext()).getRegModel().getSkilssid().add(item.getId());

    }

    private void removeItem(Skill item) {
        ((Global) context.getApplicationContext()).getRegModel().getSkilss().remove(item.getName());
        ((Global) context.getApplicationContext()).getRegModel().getSkilssid().remove(item.getId());
    }

    private void removeOtherSkill(Skill item) {
        ((Global) context.getApplicationContext()).getRegModel().getOtherSkills().remove(item.getName());
    }

    public void addSkill(Skill skill) {
        list.add(skill);
    }
}
