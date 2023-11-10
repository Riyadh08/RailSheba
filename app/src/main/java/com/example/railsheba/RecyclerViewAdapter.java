package com.example.railsheba;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<AddTrainclass> list;
    String getdate,coach;

    public RecyclerViewAdapter(Context context, ArrayList<AddTrainclass> list,String getdate,String coach) {
        this.context = context;
        this.list = list;
        this.getdate=getdate;
        this.coach=coach;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.available_train,parent,false);
        return  new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    AddTrainclass trainclass = list.get(position);
    holder.loc.setText(trainclass.getTrainlocation());
    holder.timeshow.setText(trainclass.getTimeloc());
    holder.v.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(context,TrainSeatPlan.class);
            intent.putExtra("loc",trainclass.getTrainlocation());
            intent.putExtra("timeshow",trainclass.getTimeloc());
            intent.putExtra("date",getdate);
            intent.putExtra("coach",coach);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView loc,timeshow;
        View v;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            loc=itemView.findViewById(R.id.locationshow);
            timeshow=itemView.findViewById(R.id.timeshow);
            v=itemView;
        }


    }

}
