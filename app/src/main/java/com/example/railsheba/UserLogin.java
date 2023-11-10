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
import com.google.firebase.auth.FirebaseUser;

public class UserLogin extends AppCompatActivity {
    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signupRedirectText, forgotpass;
    private FirebaseAuth mAuth;
    private BroadcastReceiver broadcastReceiver1;
   private Switch showpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        broadcastReceiver1 = new Broadcaster();
        registerReceiver(broadcastReceiver1, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        loginEmail = findViewById(R.id.loginEmail);
        showpass=(Switch)findViewById(R.id.showpass);
        loginPassword = findViewById(R.id.login_password);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);
        forgotpass = (TextView) findViewById(R.id.forgotpassid);
        mAuth = FirebaseAuth.getInstance();

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLogin.this, UserResetPassword.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin();

            }
        });

        showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(b)
               {
                   loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
               }
               else {
                   loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
               }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLogin.this, UserSignUp.class);
                startActivity(intent);
            }
        });
    }

    private void userlogin() {
        ProgressDialog progressDialog = new ProgressDialog(UserLogin.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (email.isEmpty()) {
            loginEmail.setError("Enter Email ");
            loginEmail.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Enter a Valid Email ");
            loginEmail.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (password.isEmpty()) {
            loginPassword.setError("Enter Password ");
            loginPassword.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (password.length() < 6) {
            loginPassword.setError("Password must have minimum 6 digits");
            loginPassword.requestFocus();
            progressDialog.dismiss();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        Toast.makeText(getApplicationContext(), "Logged In Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserLogin.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                    user.sendEmailVerification();
                        Toast.makeText(UserLogin.this, "Check your email to verify your account", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Log In Unsuccessful", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(UserLogin.this,MainActivity.class));
        finish();
    }

}