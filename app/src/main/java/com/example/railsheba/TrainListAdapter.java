package com.example.railsheba;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TrainListAdapter extends ArrayAdapter<AddTrainclass> {
    private Activity context;
    private List<AddTrainclass> trainclassList;

    public TrainListAdapter(Activity context,List<AddTrainclass> trainclassList) {
        super(context,R.layout.train_list_sample,trainclassList);
        this.context = context;
        this.trainclassList = trainclassList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View view= inflater.inflate(R.layout.train_list_sample,null,true);
        AddTrainclass addTrainclass=trainclassList.get(position);
        TextView t1=view.findViewById(R.id.locationid);
        TextView t2=view.findViewById(R.id.timelocid);
        t1.setText(addTrainclass.getTrainlocation());
        t2.setText(addTrainclass.getTimeloc());
        return view;
    }
}
