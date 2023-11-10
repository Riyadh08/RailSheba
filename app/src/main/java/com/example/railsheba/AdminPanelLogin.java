package com.example.railsheba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class AdminPanelLogin extends AppCompatActivity {
    private Button adminlogin;
    private EditText admincode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //change color of action bar and status bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin1)));
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.admin));
        ////////////////////////////////////////////////////////////////////

        sharedPreferences = getSharedPreferences("adminlogin", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        admincode = findViewById(R.id.admincode);
        adminlogin = findViewById(R.id.adminlogin);

        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = admincode.getText().toString().trim();

                editor.putString("save_code", code);
                editor.commit();
                if (code.equals("admin123")) {
                    Intent intent = new Intent(AdminPanelLogin.this, AdminOptions.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(AdminPanelLogin.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                }
                else if (code.isEmpty()) {
                    admincode.setError("Enter you code");
                    admincode.requestFocus();
                    return;
                }
                else {
                    Toast.makeText(AdminPanelLogin.this, "Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            startActivity(new Intent(AdminPanelLogin.this,MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}