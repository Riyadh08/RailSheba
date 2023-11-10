package com.example.railsheba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class UserTicketShowAdapter extends RecyclerView.Adapter<UserTicketShowAdapter.MyViewHolder> {
    Context context;
    ArrayList<UserTicketDetails> list;

    public UserTicketShowAdapter(Context context, ArrayList<UserTicketDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.user_ticketshow_sample, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserTicketDetails userTicketDetails = list.get(position);
        holder.ticketnumber.setText(userTicketDetails.getTicketnum());
        holder.togo.setText(userTicketDetails.getTogo());
        holder.datetime.setText(userTicketDetails.getDate()+"("+userTicketDetails.getTime()+" BST)");
        holder.seatnumber.setText(userTicketDetails.getSeats());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ticketnumber, togo, datetime, seatnumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketnumber = itemView.findViewById(R.id.ticketnumber);
            togo = itemView.findViewById(R.id.togo);
            datetime = itemView.findViewById(R.id.datetime);
            seatnumber = itemView.findViewById(R.id.seatnumber);
        }
    }

}
