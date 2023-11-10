package com.example.railsheba;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class FeedBackGet extends AppCompatActivity implements View.OnClickListener {
    private EditText  edf2;
    private Button bf1, bf2;
    private FirebaseUser user;
    private String userid,fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_get);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        edf2 = (EditText) findViewById(R.id.feedbackgive);
        bf1 = (Button) findViewById(R.id.sendfeedback);
        bf2 = (Button) findViewById(R.id.clearfeedback);
        user = FirebaseAuth.getInstance().getCurrentUser();
        // userid = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userprofile");
        bf1.setOnClickListener(this);
        bf2.setOnClickListener(this);

        databaseReference.child(String.valueOf(user.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                if (userProfile != null) {
                    fullname = userProfile.name;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FeedBackGet.this, "Something Happen ", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        try {
            String feedback = edf2.getText().toString();
            if (view.getId() == R.id.sendfeedback) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/email");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"robiulriyadh66@gmail.com", "ryad2007008@stud.kuet.ac.bd"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK FORM THE USER");
                intent.putExtra(Intent.EXTRA_TEXT, "Name= " + fullname + "\n Feedback= " + feedback);
                startActivity(Intent.createChooser(intent, "Feedback With"));
            }
            else if (view.getId() == R.id.clearfeedback) {
                edf2.setText("");

            }
        }
        catch (Exception e) {
            Toast.makeText(FeedBackGet.this, "INVALID", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Execption" + e, Toast.LENGTH_SHORT).show();
        }
    }
}