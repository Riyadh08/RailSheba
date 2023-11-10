package com.example.railsheba;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminOptions extends AppCompatActivity implements View.OnClickListener {
    private CardView adminlogout, addtrain, trainlist, see_ticket;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);
        //change color of action bar and status bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin1)));
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.admin));
        ///////////////////////////////////////////////////////////
        sharedPreferences = getSharedPreferences("adminlogin", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        adminlogout = findViewById(R.id.deletetrain);
        see_ticket = findViewById(R.id.see_ticket);
        addtrain = findViewById(R.id.addtrain);
        trainlist = findViewById(R.id.trainlist);
        adminlogout.setOnClickListener(this);
        addtrain.setOnClickListener(this);
        see_ticket.setOnClickListener(this);
        trainlist.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alart = new AlertDialog.Builder(AdminOptions.this);
        alart.setTitle("ALERT");
        alart.setMessage("Go to the Home or Exit?");
        alart.setIcon(R.drawable.interrogation);
        alart.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alart.setNegativeButton("Home", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "Welcome Home", Toast.LENGTH_SHORT).show();
            }
        });
        alart.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AdminOptions.this, "Continue your work", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog al = alart.create();
        al.show();


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.deletetrain){
            Intent intent = new Intent(AdminOptions.this, DeleteTrain.class);
            startActivity(intent);
        }
        if(id== R.id.addtrain){
            Intent intent1 = new Intent(AdminOptions.this, AddTrain.class);
            startActivity(intent1);
        }
        if(id==R.id.see_ticket){
            Intent intent3 = new Intent(AdminOptions.this, AdminSeeAllTickets.class);
            startActivity(intent3);
            Toast.makeText(getApplicationContext(), "See Ticket", Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.trainlist){
            Intent intent2 = new Intent(AdminOptions.this, LoadTrainList.class);
            startActivity(intent2);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.admin_signout) {
            editor.clear();
            editor.commit();
            Intent intent = new Intent(AdminOptions.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(item.getItemId() == R.id.govt){
            Uri web=Uri.parse("https://railway.gov.bd/");
            Intent webintent=new Intent(Intent.ACTION_VIEW,web);
            startActivity(webintent);
        }

        return super.onOptionsItemSelected(item);
    }
}