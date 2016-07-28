package com.app.pratimaseva;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import fr.ganfra.materialspinner.MaterialSpinner;

public class FindActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner Blood_Spinner, Time_Spinner;
    String Name, Phone, Blood, Time, Pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String[] blood = {"Select Blood Group", "A Positive", "A Negative", "B Positive", "B Negative", "AB Positive", "AB Negative", "O Positive", "O Negative"};
        String[] Time = {"Select Available Time", "AnyTime", "Morning", "Afternoon", "Evening"};

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Blood_Spinner = (Spinner) findViewById(R.id.findblood_gp);
        Time_Spinner = (Spinner) findViewById(R.id.findtime);

        ArrayAdapter<String> Timeadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Time);
        Timeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Time_Spinner.setAdapter(Timeadapter);
        ArrayAdapter<String> Bloodadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, blood);
        Bloodadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Blood_Spinner.setAdapter(Bloodadapter);

        Button btnfind = (Button) findViewById(R.id.finddonorreg);
        btnfind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        EditText findname = (EditText) findViewById(R.id.findname);
        EditText findphno = (EditText) findViewById(R.id.findphno);
        EditText findpincode = (EditText) findViewById(R.id.findpincode);
        Blood_Spinner = (Spinner) findViewById(R.id.findblood_gp);
        Time_Spinner = (Spinner) findViewById(R.id.findtime);

        Name = findname.getText().toString();
        Phone = findphno.getText().toString();
        Blood = Blood_Spinner.getSelectedItem().toString();
        Time = Time_Spinner.getSelectedItem().toString();
        Pincode = findpincode.getText().toString();

        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(getApplicationContext(), "Name cannot be Empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Phone)) {
            Toast.makeText(getApplicationContext(), "Phone Number cannot be Empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isValidMobile(Phone)) {
            Toast.makeText(getApplicationContext(), "Phone Number is Invalid", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Pincode)) {
            Toast.makeText(getApplicationContext(), "Pincode Cannot be Empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (Blood_Spinner.getSelectedItem().toString().trim().equals("Blood Group")) {
            Toast.makeText(getApplicationContext(), "Please Select Blood Group", Toast.LENGTH_SHORT).show();
            return;
        } else if (Time_Spinner.getSelectedItem().toString().trim().equals("Available Time")) {
            Toast.makeText(getApplicationContext(), "Please Select Available Time", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Find reg = new Find();
            reg.execute(Name, Phone, Blood, Time, Pincode);
        }
    }

    private boolean isValidMobile(String Phone) {
        if (Phone.length() < 10 || Phone.length() > 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(Phone).matches();
        }
    }

    public class Find extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(FindActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String r_name = params[0];
            String r_ph_no = params[1];
            String r_blood = params[2];
            String r_time = params[3];
            String r_pincode = params[4];

            String data = "";
            int tmp;

            try {
                URL url = new URL(Constants.URL+"finddonorreg.php");
                String urlParams = "name=" + r_name + "&phno=" + r_ph_no + "&blgp=" + r_blood + "&timing=" + r_time + "&pin=" + r_pincode;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (s.equals("")) {
                s = "Please Wait,While We Will Finding Donors...";
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FindActivity.this,DonorlistActivity.class);
                intent.putExtra("p_blood", Blood);
                intent.putExtra("p_time", Time);
                intent.putExtra("p_pincode", Pincode);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}