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


public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder> {
    Context context;
    String[] name, num;
    int image;

    public StationAdapter(Context context, String[] name, String[] num, int image) {
        this.context = context;
        this.name = name;
        this.num = num;
        this.image = image;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.stationnumber_sample, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.stationame.setText(name[position]);
        holder.stationnum.setText(num[position]);
        holder.station_call.setImageResource(image);
        String number = num[position];
        holder.station_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(number)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView stationame, stationnum;
        ImageView station_call;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stationame=itemView.findViewById(R.id.stationame);
            stationnum=itemView.findViewById(R.id.stationnum);
            station_call=itemView.findViewById(R.id.station_call);
        }
    }
}
