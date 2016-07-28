package com.app.pratimaseva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class DonorHistoryActivity extends AppCompatActivity {

    private ListView lv;
    private List<String> classList;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv = (ListView) findViewById(R.id.listView);

        SharedPreferences DonerInfo = this.getSharedPreferences("DonerInfo", Context.MODE_PRIVATE);
        final String Doner_id = DonerInfo.getString("doner_id", "doner_id");
        new SignupActivity(this).execute(Doner_id);

        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register userd = new Register();
                userd.execute(Doner_id);
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public class Register extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(DonorHistoryActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String pd_phone = arg0[0];
            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?id=" + URLEncoder.encode(pd_phone, "UTF-8");
                link = Constants.URL + "donerhistory.php" + data;
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                return result;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (s.equals("unable")) {
                Toast.makeText(DonorHistoryActivity.this, "You are Not eligible for Blood Donate for 3 months.", Toast.LENGTH_SHORT).show();
            }
            if (s.equals("success")) {
                Toast.makeText(DonorHistoryActivity.this, "Success Donated.", Toast.LENGTH_LONG).show();
            }
            if (s.equals("error")) {
                Toast.makeText(DonorHistoryActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class SignupActivity extends AsyncTask<String, Void, String> {

        private Context context;

        public SignupActivity(Context context) {
            this.context = context;
        }

        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(DonorHistoryActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            String s_id = arg0[0];
            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?id=" + URLEncoder.encode(s_id, "UTF-8");
                link = Constants.URL + "getdonorhistory.php" + data;
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                return result;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            ClassAdapter(s);
        }
    }

    public void ClassAdapter(String json) {
        classList = JsonParser.Parse(json);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classList);
        lv.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), DonerActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), DonerActivity.class);
        startActivity(intent);
    }

}
