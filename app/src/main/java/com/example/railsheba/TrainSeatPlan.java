package com.example.railsheba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrainSeatPlan extends AppCompatActivity {
    String togo, time, date, total, coachplan, check;
    private TextView showtrain, seats;
    GridLayout mainGrid;
    Double seatPrice = 500.00;
    Double totatCost = 0.0;
    int totalSeats = 0;
    TextView totalPrice;
    TextView totalBookedSeats;
    private Button buttonBook;
    List<Integer> list = new ArrayList<>();
    String listString, totalseatsget="";
    private DatabaseReference databaseReference;
    UserTicketDetails userTicketDetails;
    String[] strArray;
    boolean set = false;
    private TextView bookedseat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_seat_plan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        bookedseat = findViewById(R.id.bookedseat);
        databaseReference = FirebaseDatabase.getInstance().getReference("alltickets");
        check = databaseReference.push().getKey();
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        totalBookedSeats = (TextView) findViewById(R.id.total_seats);
        totalPrice = (TextView) findViewById(R.id.total_cost);
        seats = findViewById(R.id.seats);
        buttonBook = (Button) findViewById(R.id.btnBook);
        showtrain = findViewById(R.id.showtrain);


        showtrain.setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            togo = bundle.getString("loc");
            time = bundle.getString("timeshow");
            date = bundle.getString("date");
            coachplan = bundle.getString("coach");
            total = togo + date + time + coachplan;

        }
        showtrain.setVisibility(View.VISIBLE);
        showtrain.setText(togo + "\n" + date + " ( " + time + " BST )\n" + coachplan);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserTicketDetails userTicketDetails = dataSnapshot.getValue(UserTicketDetails.class);
                    if (userTicketDetails.getForcheck().equals(total)) {
                        totalseatsget = totalseatsget + "," + userTicketDetails.getSeats();

                    }
                    else {
                        // totalseatsget="0";
                        // Toast.makeText(TrainSeatPlan.this, "Not Added", Toast.LENGTH_SHORT).show();
                    }
                }
                set = true;
                strArray = new String[]{};
                if (totalseatsget == null || totalseatsget.isEmpty()) {
                    totalseatsget = "0";
                    bookedseat.setText(totalseatsget);
                }
                else {
                   strArray = totalseatsget.split(",");
                    bookedseat.setText(totalseatsget.substring(1));
                }


                for(String s : strArray)
                {
                    try{
                        int ind = Integer.parseInt(s.trim());

                        ((CardView)mainGrid.getChildAt(ind-1)).setCardBackgroundColor(Color.parseColor("#00d6a3"));

                    }
                    catch (Exception ignored){}
                }



                if (set) {
                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                    setToggleEvent(mainGrid);
                    buttonBook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String totalPriceI = totalPrice.getText().toString().trim();
                            String totalBookedSeatsI = totalBookedSeats.getText().toString().trim();
                            if (totalBookedSeatsI.equals("0")) {
                                Toast.makeText(TrainSeatPlan.this, "No Seat is selected", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intent = new Intent(TrainSeatPlan.this, GoForPayment.class);
                                intent.putExtra("TOTALCOST", totalPriceI);
                                intent.putExtra("TOTALSEAT", totalBookedSeatsI);
                                intent.putExtra("togo", togo);
                                intent.putExtra("time", time);
                                intent.putExtra("date", date);
                                intent.putExtra("coach", coachplan);
                                intent.putExtra("total", total);
                                intent.putExtra("seats", listString);


                                startActivity(intent);
                            }


                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void setToggleEvent(@NonNull GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                            int j = 0;
                            boolean booked = false;
                            if (strArray != null) {
                                try {
                                   // System.out.println(Arrays.asList(strArray));
                                    while (j < strArray.length) {
                                        if (strArray[j].trim().equals(String.valueOf(finalI + 1))) {
                                            Toast.makeText(TrainSeatPlan.this, "Seat is booked", Toast.LENGTH_SHORT).show();
                                            return;

                                        }
                                        j++;
                                    }
                                }
                                catch (Exception e) {
                                    Toast.makeText(TrainSeatPlan.this, "e=" + e, Toast.LENGTH_LONG).show();
                                }


                                if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {


                                    if (list.size() > 3) {
                                        Toast.makeText(TrainSeatPlan.this, "4 seats can only be booked in one purchase", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    cardView.setCardBackgroundColor(Color.parseColor("#00d6a3"));
                                    totatCost += seatPrice;
                                    ++totalSeats;
                                    list.add(finalI + 1);
                                }
                                else {
                                    //Change background color
                                    cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                                    totatCost -= seatPrice;
                                    --totalSeats;
                                    int idx = 0;
                                    while (idx < list.size()) {
                                        if (list.get(idx) == finalI + 1) {
                                            list.remove(idx);
                                        }
                                        else {
                                            idx++;
                                        }
                                    }
                                }

                            }

                    totalPrice.setText("" + totatCost + "0");
                    totalBookedSeats.setText("" + totalSeats);
                    listString = list.toString();
                    listString = listString.substring(1, listString.length() - 1);
                    seats.setText(listString);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}