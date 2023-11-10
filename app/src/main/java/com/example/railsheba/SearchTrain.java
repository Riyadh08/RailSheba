package com.example.railsheba;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Calendar;

public class SearchTrain extends AppCompatActivity {
    private Spinner sfrom, sto;
    private Button searchid, sdate;
    String[] location;
    private DatePickerDialog datePickerDialog;
   //private String getdate;
    private Spinner coachselect;
    private String getdate;
    String[] coach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_train);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sfrom = findViewById(R.id.sfromid);
        coachselect = findViewById(R.id.pickcoach);
        sto = findViewById(R.id.stoid);
        sdate = findViewById(R.id.sdate);
        searchid = findViewById(R.id.searchtrainid);
        location = getResources().getStringArray(R.array.Train_Location);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchTrain.this, R.layout.location_spinner, R.id.txt2, location);
        sfrom.setAdapter(adapter);
        sto.setAdapter(adapter);
        coach = getResources().getStringArray(R.array.coachName);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(SearchTrain.this, R.layout.location_spinner, R.id.txt2, coach);
        coachselect.setAdapter(adapter1);

         initDatepicker();
        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog.show();
            }
        });
        searchid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Toast.makeText(getApplicationContext(), "Check Check", Toast.LENGTH_SHORT).show();
                    searchtrain();

                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error on Search Train", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sdate.setText(getTodaysDate());

    }

    private void searchtrain() {

        String sfromloc = sfrom.getSelectedItem().toString().trim();
        String stoloc = sto.getSelectedItem().toString().trim();
        String trainlocation = sfromloc + "-" + stoloc;
        String coachplan = coachselect.getSelectedItem().toString().trim();

        String total = sfromloc + stoloc + getTodaysDate();
       // Toast.makeText(getApplicationContext(), getTodaysDate(), Toast.LENGTH_SHORT).show();

        if (getTodaysDate().isEmpty()) {
            sdate.setError("Enter Time");
            sdate.requestFocus();
            return;
        }
        if (sfromloc.equals(stoloc)) {
            Toast.makeText(getApplicationContext(), "Please Select Correct Location", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), trainlocation, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SearchTrain.this, ShowAvaiableTrain.class);
            intent.putExtra("togo", trainlocation);
            intent.putExtra("total", total);
            intent.putExtra("date", getTodaysDate());
            intent.putExtra("coach",coachplan);
            startActivity(intent);

        }

    }

    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);

    }
    private void initDatepicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                sdate.setText(date);
                getdate = date;
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}