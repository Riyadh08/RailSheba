package com.example.railsheba;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShowTicketAfterPayment extends AppCompatActivity {
    private TextView togo1, date1, time1, coach1, seatnum1, seats1, price1, ticknum1, username, numbers;
    String ticketnum, togo, date, time, coach, seatnum, totalid, seats;
    private Button down, ok;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userid;
     private String sms,name, fullname, pnumber;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final String TAG = "PERMISSION_TAG";
    Bitmap bmp, scaledbmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ticket_after_payment);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("userprofile");
        userid = user.getUid();
        seats = getIntent().getStringExtra("seats");
        togo = getIntent().getStringExtra("togo");
        time = getIntent().getStringExtra("time");
        date = getIntent().getStringExtra("date");
        coach = getIntent().getStringExtra("coach");
        totalid = getIntent().getStringExtra("total");
        seatnum = getIntent().getStringExtra("seatsnum");
        ticketnum = getIntent().getStringExtra("ticketnum");
        username = findViewById(R.id.username);
        numbers = findViewById(R.id.numbers);

        databaseReference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                if (userProfile != null) {
                    fullname = userProfile.name;
                    pnumber = userProfile.number;
                    username.setText(fullname);
                    numbers.setText(pnumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowTicketAfterPayment.this, "Something Happen ", Toast.LENGTH_SHORT).show();
            }
        });

        togo1 = findViewById(R.id.togo1);
        date1 = findViewById(R.id.date1);
        time1 = findViewById(R.id.time1);
        coach1 = findViewById(R.id.coach1);
        seatnum1 = findViewById(R.id.seatnum1);
        seats1 = findViewById(R.id.seats1);
        price1 = findViewById(R.id.price1);
        ticknum1 = findViewById(R.id.ticknum);

        ticknum1.setText(ticketnum);
        date1.setText(date);
        time1.setText(time);
        coach1.setText(coach);
        seatnum1.setText(seatnum);
        seats1.setText(seats);
        price1.setText(totalid);
        down = findViewById(R.id.down);
        ok = findViewById(R.id.ok);
        togo1.setText(togo);

        sms = "Passenger:" +name + "\nLocation:" + togo + "\n Date & Time:" + date + "/" + time + "\nCoach Seats:" + coach + "-" + seatnum + "\nFare:" + totalid;
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ShowTicketAfterPayment.this, "created", Toast.LENGTH_SHORT).show();
                if(checkPermission()){
                    Log.d(TAG,"onClick: Permission granted");
                    createfolder();
                }
                else{
                    Log.d(TAG,"onClick: Permission denied");
                    requestpermission();
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowTicketAfterPayment.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void createfolder() {
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.train1);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 70, 70, false);
        canvas.drawBitmap(scaledbmp, 5, 25, paint);

        paint.setColor(ContextCompat.getColor(this, R.color.main_color));
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Rail.BD", 170, 60, paint);

        paint.setColor(ContextCompat.getColor(this, R.color.main2_color));
        paint.setTextSize(15);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Online Ticket Booking System", 180, 80, paint);


        paint.setColor(ContextCompat.getColor(this, R.color.main2_color));
        canvas.drawLine(10, 100, 290, 100, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(15);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        canvas.drawText("Ticket Number:"+ticketnum, 50, 130, paint);
        canvas.drawText("Location:"+togo, 50, 170, paint);
        canvas.drawText("Date:"+date, 50, 210, paint);
        canvas.drawText("Time:"+time, 50, 250, paint);
        canvas.drawText("Coach:"+coach, 50, 290, paint);
        canvas.drawText("Seats:"+seatnum, 50, 330, paint);
        canvas.drawText("Total Seats:"+seats, 50, 370, paint);
        canvas.drawText("FARE:"+totalid, 50, 410, paint);
        canvas.drawText("Name:"+fullname, 50, 450, paint);
        //canvas.draw
        // finish the page


        document.finishPage(page);
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/RAIL.BD/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"Rail.BD"+System.currentTimeMillis()+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done check in RAIL.BD folder", Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    private void requestpermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Log.d(TAG, "requestpermission: try");
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);
            }
            catch (Exception e) {
                Log.e(TAG, "requestpermission: catch", e);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }
    }

    private ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult: ");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                            Log.d(TAG, "onActivityResult: Manage External Storage Permission is granted");
                            createfolder();
                        }
                        else {
                            Log.d(TAG, "onActivityResult: Manage External Storage Permission is granted");
                            Toast.makeText(ShowTicketAfterPayment.this, "External Storage Permission is granted", Toast.LENGTH_SHORT).show();
                        }


                    }


                }
            });


    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();

        }
        else {
            int write = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            return write == PackageManager.PERMISSION_GRANTED & read == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length>0){
                boolean write=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean read=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                if(write && read){
                    Log.d(TAG,"onRequestPermissionsResult: External Storage Permission is granted");
                    createfolder();
                }
                else {
                    Log.d(TAG,"onRequestPermissionsResult: External Storage Permission is denied");
                    Toast.makeText(this, "External Storage Permission is denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}