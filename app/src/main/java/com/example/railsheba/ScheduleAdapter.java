package com.example.railsheba;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {
    Context context;
   List<Schedule> list;


    public ScheduleAdapter(Context context, List<Schedule> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.schedule_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Schedule item = list.get(position);

        holder.tvcity.setText(item.city);
        holder.tvtrain.setText(item.train);
        holder.tvcountry.setText(item.country);
        holder.tvfounded.setText(item.founded);
        holder.tvfromto.setText(item.fromto);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvcity,tvtrain,tvcountry,tvfounded,tvfromto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcity = itemView.findViewById(R.id.tvcity);
            tvtrain = itemView.findViewById(R.id.tvtrain);
            tvcountry = itemView.findViewById(R.id.tvcountry);
            tvfounded = itemView.findViewById(R.id.tvfounded);
            tvfromto= itemView.findViewById(R.id.tvfromto);
        }
    }
}
