package com.example.railsheba;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileShow extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userid;
    private TextView name, demail, number, header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_show);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("userprofile");
        userid = user.getUid();
        //////////////////////////////////////////
        ProgressDialog progressDialog = new ProgressDialog(UserProfileShow.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        /////////////////////////////////////////
        name = findViewById(R.id.disnameid);
        demail = findViewById(R.id.disemailid);
        number = findViewById(R.id.disnumberid);
        header = findViewById(R.id.header);

        databaseReference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                if (userProfile != null) {
                    String fullname = userProfile.name;
                    String email = userProfile.email;
                    String pnumber = userProfile.number;
                    header.setText("Hello " + fullname + "!");
                    name.setText(fullname);
                    demail.setText(email);
                    number.setText(pnumber);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileShow.this, "Something Happen ", Toast.LENGTH_SHORT).show();
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