package com.example.railsheba;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BroadcastReceiver broadcastReceiver1;
    private FirebaseAuth mAuth;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView imageMenu;
    DatabaseReference databaseReference;
    private String userid;
    private TextView usershowinnavigation;
    private CardView buyTicket, verifyticket, tickethistory, ticketprice, trainshedule, stationnumber;
    SliderView sliderView;
    int[] images = {
            R.drawable.rail1,
            R.drawable.rail5,
            R.drawable.rail4,
            R.drawable.rail6,
            R.drawable.rail3,
            R.drawable.rail2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        broadcastReceiver1 = new Broadcaster();
        mAuth = FirebaseAuth.getInstance();

        registerReceiver(broadcastReceiver1, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        stationnumber = findViewById(R.id.stationnumber);
        buyTicket = findViewById(R.id.buyTicket);
        verifyticket = findViewById(R.id.verifyticket);
        tickethistory = findViewById(R.id.tickethistory);
        ticketprice = findViewById(R.id.ticketprice);
        trainshedule = findViewById(R.id.trainshedule);
        sliderView = findViewById(R.id.image_slider);

        //For Slider view

        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
        // Navagation Drawar------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);
        View header = navigationView.getHeaderView(0);
        usershowinnavigation = (TextView) header.findViewById(R.id.usershow);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("userprofile");

        if (firebaseUser != null) {
            userid=firebaseUser.getUid();
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.msignin).setVisible(false);
            nav_Menu.findItem(R.id.msignup).setVisible(false);
            usershowinnavigation.setVisibility(View.VISIBLE);
            databaseReference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserProfile userProfile = snapshot.getValue(UserProfile.class);
                    if (userProfile != null) {
                        String fullname = userProfile.name;
                        usershowinnavigation.setText("@"+fullname);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        imageMenu = findViewById(R.id.imageMenu);
        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if(id == R.id.mHome) {
                    Toast.makeText(MainActivity.this, "You Are in Home", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                }
                else if(id == R.id.Profile) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        Intent intent = new Intent(MainActivity.this, UserProfileShow.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                    }
                    else {
                        dialog();
                    }
                }
                else if(id == R.id.msignout) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent1 = new Intent(getApplicationContext(), UserLogin.class);
                    startActivity(intent1);
                }
                else if(id == R.id.msignin) {
                    Intent intent2 = new Intent(MainActivity.this, UserLogin.class);
                    startActivity(intent2);
                    drawerLayout.closeDrawers();
                }
                else if(id == R.id.msignup) {
                    Intent intent3 = new Intent(MainActivity.this, UserSignUp.class);
                    startActivity(intent3);
                    drawerLayout.closeDrawers();
                }
                else if(id == R.id.aboutus) {
                    Toast.makeText(MainActivity.this, "About uS", Toast.LENGTH_SHORT).show();
                    Intent intentabout = new Intent(MainActivity.this, AboutUs.class);
                    startActivity(intentabout);
                    drawerLayout.closeDrawers();
                }
                else if(id == R.id.shareapp) {
                    Intent intents = new Intent(Intent.ACTION_SEND);
                    intents.setType("text/plain");
                    String subject = "Rail.BD";
                    String body = "https://drive.google.com/drive/folders/1DiEQaU2pNEyjsiwbHjxtGu62cZDDzHTB?usp=drive_link";
                    intents.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intents.putExtra(Intent.EXTRA_TEXT, body);
                    startActivity(Intent.createChooser(intents, "Share With"));

                    drawerLayout.closeDrawers();
                }
                else if(id == R.id.feedback) {
                    FirebaseUser firebaseUser1 = mAuth.getCurrentUser();
                    if (firebaseUser1 != null) {
                        Toast.makeText(MainActivity.this, "Feedback", Toast.LENGTH_SHORT).show();
                        Intent intent4 = new Intent(MainActivity.this, FeedBackGet.class);
                        startActivity(intent4);
                        drawerLayout.closeDrawers();
                    }
                    else {
                        dialog();
                    }
                }
                else if(id == R.id.admin){
                    FirebaseAuth.getInstance().signOut();
                    Intent intentadmin = new Intent(MainActivity.this, AdminPanelLogin.class);
                    startActivity(intentadmin);
                }

                return false;
            }
        });

        imageMenu = findViewById(R.id.imageMenu);
        imageMenu.setOnClickListener(this);
        buyTicket.setOnClickListener(this);
        verifyticket.setOnClickListener(this);
        tickethistory.setOnClickListener(this);
        ticketprice.setOnClickListener(this);
        trainshedule.setOnClickListener(this);
        stationnumber.setOnClickListener(this);


    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alart = new AlertDialog.Builder(MainActivity.this);
        alart.setTitle("ALERT");
        alart.setMessage("Are you sure exit?");
        alart.setIcon(R.drawable.interrogation);
        alart.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alart.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "App not exit yet", Toast.LENGTH_SHORT).show();
            }
        });
        alart.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Continue your work", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog al = alart.create();
        al.show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("adminlogin", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.contains("save_code")) {
            Intent intentadmingo = new Intent(MainActivity.this, AdminOptions.class);
            startActivity(intentadmingo);
            finish();
        }
        else {

        }

    }

    @Override
    public void onClick(View view) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (view.getId() == R.id.buyTicket) {
            if (firebaseUser != null) {
                Intent intent=new Intent(MainActivity.this, SearchTrain.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Search Train", Toast.LENGTH_SHORT).show();
            }
            else {
                dialog();
            }

        }
        if (view.getId() == R.id.verifyticket) {
            if (firebaseUser != null) {
                Intent intent=new Intent(MainActivity.this, VerifyTickets.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Verify", Toast.LENGTH_SHORT).show();
            }
            else {
                dialog();
            }

        }
        if (view.getId() == R.id.tickethistory) {
            if (firebaseUser != null) {
                startActivity(new Intent(MainActivity.this, UserTicketBookingHistory.class));
                Toast.makeText(getApplicationContext(), "History", Toast.LENGTH_SHORT).show();
            }
            else {
                dialog();
            }

        }
        if (view.getId() == R.id.ticketprice) {
            if (firebaseUser != null) {
                startActivity(new Intent(MainActivity.this, TicketPrice.class));
                Toast.makeText(getApplicationContext(), "Price", Toast.LENGTH_SHORT).show();
            }
            else {
                dialog();
            }

        }
        if (view.getId() == R.id.trainshedule) {
            if (firebaseUser != null) {
                Intent intent=new Intent(MainActivity.this, TrainScheduleView.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Train Schedule", Toast.LENGTH_SHORT).show();
            }
            else {
                dialog();
            }

        }
        if (view.getId() == R.id.stationnumber) {
            if (firebaseUser != null) {
                //user log in
                Intent intent=new Intent(MainActivity.this, StationNumber.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Station Number", Toast.LENGTH_SHORT).show();
            }
            else {
                dialog();
            }

        }
        if (view.getId() == R.id.imageMenu) {

            drawerLayout.openDrawer(GravityCompat.START);
        }

    }

    public void dialog() {
        AlertDialog.Builder alart = new AlertDialog.Builder(MainActivity.this);
        alart.setTitle("ALERT");
        alart.setMessage("To Continue ,You Need to Log In.");
        alart.setIcon(R.drawable.interrogation);
        alart.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this, UserLogin.class);
                startActivity(intent);
            }
        });
        alart.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Have to log in", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog al = alart.create();
        al.show();
    }
}