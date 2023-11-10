package com.example.railsheba;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private ProgressBar sp1;
    int progress;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAuth=FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        sp1=(ProgressBar) findViewById(R.id.splashscreen1);
        Thread thread = new Thread(() -> {

            dowork();
            startApp();
        });
        thread.start();
    }
    public void dowork()
    {
        for(progress=10;progress<=101;progress=progress+5) {
            try {
                Thread.sleep(100);
                sp1.setProgress(progress);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public void startApp()
    {
        Intent intent=new Intent(SplashScreen.this,MainActivity.class);
        startActivity(intent);
        finish();

    }
    @Override
    protected void onStart() {
        super.onStart();

    }
}