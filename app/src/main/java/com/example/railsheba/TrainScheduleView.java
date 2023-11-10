package com.example.railsheba;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrainScheduleView extends AppCompatActivity {

    private RecyclerView listView;
    List<Schedule> list = new ArrayList<>();
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_schedule_view);
        progressDialog = new ProgressDialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        listView = findViewById(R.id.Stationlist);


        // Fetch data using AsyncTask
        new FetchData().execute();
    }

    private class FetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://api.myjson.online/v1/records/9c7db62a-0619-4b9a-9f70-638c0382a786");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                String jsonString = stringBuilder.toString();

                // Parsing JSON
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("data");


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dataObject = jsonArray.getJSONObject(i);
                    String city = dataObject.getString("city");
                    String train1 = dataObject.getString("Train_1");
                    String train2 = dataObject.getString("Train_2");
                    String country = dataObject.getString("country");
                    int founded = dataObject.getInt("founded");

                    JSONObject station1 = dataObject.getJSONObject("Station Name1");
                    String station1Name = station1.getString("name");
                    int station1Capacity = station1.getInt("capacity");

                    JSONArray station2Array = dataObject.getJSONArray("Station Name2");
                    JSONObject station2 = station2Array.getJSONObject(0);
                    String station2Name = station2.getString("name");
                    int station2Capacity = station2.getInt("capacity");

                    String data = "City: " + city +
                            "\nTrain 1: " + train1 +
                            "\nTrain 2: " + train2 +
                            "\nCountry: " + country +
                            "\nFounded: " + founded +
                            "\nStation 1: " + station1Name + " (Capacity: " + station1Capacity + ")" +
                            "\nStation 2: " + station2Name + " (Capacity: " + station2Capacity + ")";

                    list.add(
                            new Schedule(city,train1+"\n"+train2,country,founded+"",station1Name+"\n"+station2Name)

                    );
                }

                inputStream.close();
                urlConnection.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Set up an ArrayAdapter for the ListView
          ScheduleAdapter adapter = new ScheduleAdapter(TrainScheduleView.this,list);
          progressDialog.dismiss();
            listView.setAdapter(adapter);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}