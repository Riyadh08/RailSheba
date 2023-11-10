package com.example.railsheba;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteTrain extends AppCompatActivity {
    private Spinner dfrom, dto;
    private EditText dtime;
    private Button deletetrain;
    String[] location;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_train);
        //change color of action bar and status bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin1)));
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.admin));
        /////////////////////////////////////////////////////////////////////////////////////

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dfrom = findViewById(R.id.dfromid);
        dto = findViewById(R.id.dtoid);
        dtime = findViewById(R.id.dtimeid);
        deletetrain = findViewById(R.id.deletetrainid);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Trains");
        location = getResources().getStringArray(R.array.Train_Location);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(DeleteTrain.this, R.layout.location_spinner, R.id.txt2, location);
        dfrom.setAdapter(adapter);
        dto.setAdapter(adapter);
        deletetrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletetraindata();
            }
        });
    }

    private void deletetraindata() {
        ProgressDialog progressDialog = new ProgressDialog(DeleteTrain.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        String fromloc = dfrom.getSelectedItem().toString().trim();
        String toloc = dto.getSelectedItem().toString().trim();
        String timeloc = dtime.getText().toString().trim();
        String total = fromloc + toloc + timeloc;

        if (timeloc.isEmpty()) {
            dtime.setError("Enter Time");
            dtime.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (fromloc.equals(toloc)) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Both Location cannot be same", Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference.orderByChild("total").equalTo(total).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        databaseReference.child(total).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(DeleteTrain.this, fromloc + "-" + toloc + "'s Train is Deleted", Toast.LENGTH_SHORT).show();
                                    dtime.getText().clear();

                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(DeleteTrain.this, "Failed to Delete" + fromloc + "-" + toloc + " Train", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(DeleteTrain.this, "No " + fromloc + "-" + toloc + " Train Exist", Toast.LENGTH_SHORT).show();
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