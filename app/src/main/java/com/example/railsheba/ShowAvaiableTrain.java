package com.example.railsheba;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowAvaiableTrain extends AppCompatActivity {
    private TextView showtrain;
    String togo, total, date,coachplan;
    private RecyclerView availabletrain;
    DatabaseReference databaseReference;
    RecyclerViewAdapter viewAdapter;
    ArrayList<AddTrainclass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_avaiable_train);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        availabletrain = findViewById(R.id.availabletrain);

        showtrain = findViewById(R.id.showtrain);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            togo = bundle.getString("togo");
            total = bundle.getString("total");
            date = bundle.getString("date");
            coachplan=bundle.getString("coach");
            showtrain.setText(togo + "\n" + date+"\n"+coachplan );
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Trains");
        availabletrain.setHasFixedSize(true);
        availabletrain.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        viewAdapter = new RecyclerViewAdapter(getApplicationContext(), list,date,coachplan);
        availabletrain.setAdapter(viewAdapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    AddTrainclass addTrainclass = dataSnapshot.getValue(AddTrainclass.class);
                    if (addTrainclass.getTrainlocation().equals(togo)) {
                        list.add(addTrainclass);
                    }

                }
                viewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}