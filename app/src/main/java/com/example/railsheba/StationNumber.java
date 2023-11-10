package com.example.railsheba;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StationNumber extends AppCompatActivity {
    private RecyclerView recyclerView;
    String[] stationname, stationnum;
    int call = R.drawable.ic_baseline_call_24;
    StationAdapter stationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_number);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.Stationnum);
        stationname = getResources().getStringArray(R.array.Station_name);
        stationnum = getResources().getStringArray(R.array.Numbers);
        stationAdapter = new StationAdapter(this, stationname, stationnum,call);
        recyclerView.setAdapter(stationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}