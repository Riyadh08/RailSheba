package com.example.railsheba;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GoForPayment extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Button buttonPay;
    TextView totalCost;
    TextView totalSeat;
    private CheckBox checkcard;
    private TextView a, b, c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_for_payment);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        getSupportActionBar().setTitle("You Can Pay");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        a = (TextView) findViewById(R.id.textView11);
        b = (TextView) findViewById(R.id.textView21);
        c = (TextView) findViewById(R.id.textView31);
        checkcard=findViewById(R.id.checkcard);

        totalCost = (TextView) findViewById(R.id.totalCostFinal);
        totalSeat = (TextView) findViewById(R.id.totalSeatsFinal);

        final String total = getIntent().getStringExtra("TOTALCOST");
        final String seats = getIntent().getStringExtra("TOTALSEAT");
        final String togo = getIntent().getStringExtra("togo");
        final String time = getIntent().getStringExtra("time");
        final String date = getIntent().getStringExtra("date");
        final String coach = getIntent().getStringExtra("coach");
        final String totalid = getIntent().getStringExtra("total");
        final String seatnum = getIntent().getStringExtra("seats");

        a.setText(togo);
        b.setText(date + " (" + time + " BST)");
        c.setText("Coach :" + coach);
        totalCost.setText("Payable: "+total+" Tk ( BDT )");
        totalSeat.setText("Seat Numbers : " + seatnum+"\n Number of seats:"+seats);

        buttonPay = (Button) findViewById(R.id.btnPay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // String totalPriceI=totalCost.getText().toString();

                String total_cost = totalCost.getText().toString().trim();
                String total_seats = totalSeat.getText().toString().trim();
                if(!checkcard.isChecked()){
                    Toast.makeText(GoForPayment.this, "Select a Payment Method", Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent intent = new Intent(GoForPayment.this, MakePayment.class);
                intent.putExtra("TOTALCOSTI", total);
                intent.putExtra("TOTALSEAT", seats);
                intent.putExtra("togo", togo);
                intent.putExtra("time", time);
                intent.putExtra("date", date);
                intent.putExtra("coach", coach);
                intent.putExtra("seats",seatnum);
                intent.putExtra("total", totalid);
                startActivity(intent);
                checkcard.setChecked(false);
                // startActivity(new Intent(getApplicationContext(), PayActivity.class));
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