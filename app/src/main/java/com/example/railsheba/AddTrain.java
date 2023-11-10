package com.example.railsheba;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddTrain extends AppCompatActivity {
    private Spinner from, to;
    private EditText time;
    private Button addtrain;
    String[] location;
    String match;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_train);
        //change color of action bar and status bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin1)));
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.admin));
        /////////////////////////////////////////////////////
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        from = findViewById(R.id.fromid);
        to = findViewById(R.id.toid);
        time = findViewById(R.id.timeid);
        addtrain = findViewById(R.id.addtrainid);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Trains");
        location = getResources().getStringArray(R.array.Train_Location);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTrain.this, R.layout.location_spinner, R.id.txt2, location);
        from.setAdapter(adapter);
        to.setAdapter(adapter);
        addtrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    savetraindata();

                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error on Add Train", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void savetraindata() {
        String fromloc = from.getSelectedItem().toString().trim();
        String toloc = to.getSelectedItem().toString().trim();
        String timeloc = time.getText().toString().trim();
        String trainlocation = fromloc + "-" + toloc;
        String total = fromloc + toloc + timeloc;

        if(fromloc.equals("Select Location") || toloc.equals("Select Location")){
            Toast.makeText(this, "Please Select Location", Toast.LENGTH_SHORT).show();
         return;
        }


        if (timeloc.isEmpty()) {
            time.setError("Enter Time");
            time.requestFocus();
            return;
        }
        if (fromloc.equals(toloc)) {
            Toast.makeText(getApplicationContext(), "Both Location cannot be same", Toast.LENGTH_SHORT).show();
        }
        else {

            time.getText().clear();
            databaseReference.orderByChild("total").equalTo(total).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        //bus number exists in Database
                        Toast.makeText(getApplicationContext(), "Train Already Exist in This Time", Toast.LENGTH_SHORT).show();
                        time.setError("Train Already Exist in This Time");
                        time.requestFocus();
                        return;
                    }
                    else {
                        AddTrainclass addTrainclass1 = new AddTrainclass(timeloc, trainlocation, total);
                        databaseReference.child(total).setValue(addTrainclass1);
                        Toast.makeText(getApplicationContext(), "Train Added", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}