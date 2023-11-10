package com.example.railsheba;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakePayment extends AppCompatActivity {
    private TextView t1;
    Button buy, cancel;
    AlertDialog.Builder alertBuilder;
    UserTicketDetails userTicketDetails;
    DatabaseReference databaseReference;
    String forcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        t1 = findViewById(R.id.pay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final String total = getIntent().getStringExtra("TOTALCOSTI");
        final String seats = getIntent().getStringExtra("TOTALSEAT");
        final String togo = getIntent().getStringExtra("togo");
        final String time = getIntent().getStringExtra("time");
        final String date = getIntent().getStringExtra("date");
        final String coach = getIntent().getStringExtra("coach");

        final String totalid = getIntent().getStringExtra("total");
        final String seatnum = getIntent().getStringExtra("seats");
        forcheck = togo + date + time + coach;
        databaseReference = FirebaseDatabase.getInstance().getReference("userticket");
        t1.setText("From->To: " + togo + "\nJourney Date: " + date + "(" + time + "BST)\nCoach Name: " + coach + "\nSeat No: " + seatnum + "\nTotal Seats: " + seats + "\nFare: " + total + " TK(BDT)");
        buy = findViewById(R.id.btnBuy);
        cancel = findViewById(R.id.button05);
        CardForm cardForm = (CardForm) findViewById(R.id.card_form);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(MakePayment.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manage = getSystemService(NotificationManager.class);
            manage.createNotificationChannel(notificationChannel);
        }

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cardForm.isValid()) {
                    alertBuilder = new AlertDialog.Builder(MakePayment.this);
                    alertBuilder.setTitle("Confirm before purchase");
                    alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                            "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                            "Postal code: " + cardForm.getPostalCode() + "\n" +
                            "Phone number: " + cardForm.getMobileNumber() + "\nFare: " + total + " TK(BDT)");
                    String cardnumber = cardForm.getCardNumber();
                    String moblie = cardForm.getMobileNumber();
                    String cardname = cardForm.getCardholderName();
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            String ticketnum = databaseReference.push().getKey();
                            String ticketnumber = "RB-" + ticketnum.substring(15);
                            userTicketDetails = new UserTicketDetails(togo, date, time, coach, seatnum, total, cardnumber, moblie, cardname, ticketnumber, forcheck);
                            FirebaseDatabase.getInstance().getReference("userticket").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ticketnum)
                                    .setValue(userTicketDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {
                                            if (task1.isSuccessful()) {

                                                FirebaseDatabase.getInstance().getReference("alltickets").child(ticketnum).setValue(userTicketDetails);
                                                Toast.makeText(MakePayment.this, "Thank you for purchase", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(MakePayment.this, ShowTicketAfterPayment.class);

                                                intent.putExtra("togo", togo);
                                                intent.putExtra("time", time);
                                                intent.putExtra("date", date);
                                                intent.putExtra("coach", coach);
                                                intent.putExtra("total", total);
                                                intent.putExtra("seats", seats);
                                                intent.putExtra("seatsnum", seatnum);
                                                intent.putExtra("ticketnum", ticketnumber);
                                                startActivity(intent);

                                                finish();
                                            }
                                            else {
                                                Toast.makeText(MakePayment.this, "Unsuccessful Payment", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MakePayment.this, "My Notification");
                            builder.setContentTitle("Rail.BD");
                            builder.setContentText("Ticket Number= " + ticketnumber + "\nLocation= " + togo + "\nDate & Time= " + date + "-" + time);
                            builder.setSmallIcon(R.drawable.ic_launcher_background);
                            builder.setAutoCancel(true);
                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MakePayment.this);
                            if (ActivityCompat.checkSelfPermission(MakePayment.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            managerCompat.notify(1, builder.build());

                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                }
                else {
                    Toast.makeText(MakePayment.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakePayment.this, GoForPayment.class);
                startActivity(intent);
                finish();
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