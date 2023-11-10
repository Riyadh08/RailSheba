package com.example.railsheba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserResetPassword extends AppCompatActivity {
    private EditText resetemail;
    private Button resetbutton;
    private ProgressBar resetprog;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reset_password);

        resetprog = findViewById(R.id.resetprog);
        resetbutton = findViewById(R.id.reset_button);
        resetemail = findViewById(R.id.reset_email);
        mauth = FirebaseAuth.getInstance();
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetpassword();
            }
        });
    }

    private void resetpassword() {
        ProgressDialog progressDialog=new ProgressDialog(UserResetPassword.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String email = resetemail.getText().toString().trim();

        if (email.isEmpty()) {
            resetemail.setError("Enter Email ");
            resetemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            resetemail.setError("Enter a Valid Email ");
            resetemail.requestFocus();
            return;
        }
        mauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(UserResetPassword.this, "An Email has been sent to you", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserResetPassword.this, UserLogin.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(UserResetPassword.this, "Error on sending email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}