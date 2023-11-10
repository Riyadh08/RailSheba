package com.example.railsheba;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.FirebaseDatabase;

public class UserSignUp extends AppCompatActivity {
    EditText signupEmail, signupPassword, signupname, signupnumber;
    TextView loginRedirectText;
    Button signupButton;
    private FirebaseAuth mAuth;
    private BroadcastReceiver broadcastReceiver1;
    private Switch showpass1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        broadcastReceiver1 = new Broadcaster();
        registerReceiver(broadcastReceiver1, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        showpass1=(Switch)findViewById(R.id.showpass1);

        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupname = findViewById(R.id.signup_name);
        signupnumber = findViewById(R.id.signup_num);
        mAuth = FirebaseAuth.getInstance();
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userregister();
            }
        });
        showpass1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    signupPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    signupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSignUp.this, UserLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void userregister() {
        ProgressDialog progressDialog = new ProgressDialog(UserSignUp.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String name = signupname.getText().toString().trim();
        String number = signupnumber.getText().toString().trim();

        if (name.isEmpty()) {
            signupname.setError("Enter a Name");
            signupname.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (number.isEmpty()) {
            signupnumber.setError("Enter Phone Number");
            signupnumber.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (email.isEmpty()) {
            signupEmail.setError("Enter Email ");
            signupEmail.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("Enter a Valid Email ");
            signupEmail.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (password.isEmpty()) {
            signupPassword.setError("Enter Password ");
            signupPassword.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (password.length() < 6) {
            signupPassword.setError("Password must have minimum 6 digits");
            signupPassword.requestFocus();
            progressDialog.dismiss();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserProfile userProfile = new UserProfile(name, email, number);
                    FirebaseDatabase.getInstance().getReference("userprofile").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task1) {
                                    if (task1.isSuccessful()) {

                                        Toast.makeText(getApplicationContext(), "SignUp Successfully done", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UserSignUp.this, UserLogin.class);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                        finish();
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        Toast.makeText(UserSignUp.this, "SignUp is Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else {
                    progressDialog.dismiss();
                    if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(getApplicationContext(), "User is already taken", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "SignUp is Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver1);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserSignUp.this,MainActivity.class));
        finish();
    }
}