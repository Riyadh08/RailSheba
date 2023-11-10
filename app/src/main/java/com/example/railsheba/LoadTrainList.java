package com.example.railsheba;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoadTrainList extends AppCompatActivity {
    private ListView loadtrain;
    DatabaseReference databaseReference;
    private List<AddTrainclass> addTrainclassList;
    private TrainListAdapter trainListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_train_list);

        //change color of action bar and status bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin1)));
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.admin));
        ////////////////////////////////////////////////////////////
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadtrain = findViewById(R.id.loadtrain);
        databaseReference = FirebaseDatabase.getInstance().getReference("Trains");
        addTrainclassList = new ArrayList<>();
        trainListAdapter = new TrainListAdapter(LoadTrainList.this, addTrainclassList);
    }

    @Override
    protected void onStart() {
        ProgressDialog progressDialog = new ProgressDialog(LoadTrainList.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addTrainclassList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    AddTrainclass addTrainclass = snapshot1.getValue(AddTrainclass.class);
                    addTrainclassList.add(addTrainclass);
                }
                loadtrain.setAdapter(trainListAdapter);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        super.onStart();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}