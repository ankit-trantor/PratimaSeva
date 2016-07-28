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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    String name,ph_no,email,birth,blood,address,city,state,pincode,time,file;
    TextView Name, Email, Phone, Birthdate, Time, Blood, Address, City, State, Pincode , Country;
    String DONERID=null,DONERNAME=null,DONERPHONE=null,DONEREMAIL=null,DONERBIRTH=null,DONERBLOOD=null,DONERTIME=null,DONERADDRESS=null,DONERCITY=null,DONERPINCODE=null,DONERSTATE=null,DONERFILE=null,DONERCOUNTRY=null;
    CircleImageView user_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Name = (TextView) findViewById(R.id.profile_name);
        Email = (TextView) findViewById(R.id.profile_Email);
        Phone = (TextView) findViewById(R.id.profile_Phone);
        Birthdate = (TextView) findViewById(R.id.profile_Birthdate);
        Time = (TextView) findViewById(R.id.profile_Time);
        Blood = (TextView) findViewById(R.id.profile_blood);
        Address = (TextView) findViewById(R.id.profile_address);
        City = (TextView) findViewById(R.id.profile_city);
        State = (TextView) findViewById(R.id.profile_state);
        Pincode = (TextView) findViewById(R.id.profile_pincode);
        Country = (TextView) findViewById(R.id.profile_country);
        user_photo = (CircleImageView) findViewById(R.id.profile_photo);

        SharedPreferences DonerInfo = this.getSharedPreferences("DonerInfo", Context.MODE_PRIVATE);
        String Doner_id = DonerInfo.getString("doner_id","doner_id");
        UserData user = new UserData();
        user.execute(Doner_id);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentreg = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                startActivity(intentreg);
            }
        });

    }

    public class UserData extends AsyncTask<String, String, String>
    {
        private ProgressDialog progressDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(UserProfileActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            String ph_no = arg0[0];
            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?id=" + URLEncoder.encode(ph_no, "UTF-8");
                link = Constants.URL+"User_Data_id.php" + data;
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
            String err=null;
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("doner_data");
                DONERNAME = user_data.getString("name");
                DONERPHONE = user_data.getString("ph_no");
                DONEREMAIL = user_data.getString("email_id");
                DONERBLOOD = user_data.getString("blood_gp");
                DONERBIRTH = user_data.getString("birth_date");
                DONERTIME = user_data.getString("timing");
                DONERADDRESS = user_data.getString("address_1");
                DONERCITY = user_data.getString("city");
                DONERPINCODE = user_data.getString("pincode");
                DONERSTATE = user_data.getString("state");
                DONERCOUNTRY = user_data.getString("country");
                DONERFILE = user_data.getString("file");

                Name.setText(DONERNAME);
                Phone.setText(DONERPHONE);
                Email.setText(DONEREMAIL);
                Blood.setText("Blood Group : " + DONERBLOOD);
                Address.setText("Address : " + DONERADDRESS);
                City.setText("City : " + DONERCITY);
                State.setText("State : " + DONERSTATE);
                Pincode.setText("Pincode : " + DONERPINCODE);
                Birthdate.setText("Birth Date : " + DONERBIRTH);
                Time.setText("Available Time : " + DONERTIME);
                Country.setText("Country :" +DONERCOUNTRY);
                Picasso.with(UserProfileActivity.this).load(DONERFILE).into(user_photo);

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
                Toast.makeText(UserProfileActivity.this, err, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), DonerActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), DonerActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
