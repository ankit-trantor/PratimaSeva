package com.app.pratimaseva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import ca.gcastle.bottomnavigation.view.BottomNavigationTabView;
import de.hdodenhof.circleimageview.CircleImageView;

public class DonorPublicProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView name, email, phone, blood, address, city, state, pincode;
    CircleImageView user_photo;
    String DONERNAME = null, DONERPHONE = null, DONEREMAIL = null, DONERBLOOD = null, DONERADDRESS = null, DONERCITY = null, DONERSTATE = null, DONERPINCODE = null, DONERIMAGE = null;
    AlertDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_public_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        name = (TextView) findViewById(R.id.user_profile_name);
        email = (TextView) findViewById(R.id.user_profile_Email);
        phone = (TextView) findViewById(R.id.user_profile_Phone);
        blood = (TextView) findViewById(R.id.user_profile_blood);
        pincode = (TextView) findViewById(R.id.user_profile_pincode);
        user_photo = (CircleImageView) findViewById(R.id.user_profile_photo);

        String pd_phone = getIntent().getExtras().getString("pd_phone");
        UserDataProfile userd = new UserDataProfile();
        userd.execute(pd_phone);

        Button call = (Button) findViewById(R.id.call);
        Button report = (Button) findViewById(R.id.report);
        Button share = (Button) findViewById(R.id.share);
        call.setOnClickListener(this);
        report.setOnClickListener(this);
        share.setOnClickListener(this);

    }

   @Override
    public void onClick(View v) {
        switch (v.getId()) {
           case R.id.call:
                String pd_phone = getIntent().getExtras().getString("pd_phone");
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:" + pd_phone));
                startActivity(call);
                break;
            case R.id.report:
                final CharSequence[] items = {"Invalid Phone Number", "Didn't Pickup the Call", "Person is ill", "Not Available at Place", "Not able to come due to work", "Personal Reason"};

                AlertDialog.Builder builder = new AlertDialog.Builder(DonorPublicProfileActivity.this);
                builder.setTitle("Choose Report Type");
                builder.setIcon(R.drawable.ic_report);
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        String Name = String.valueOf(items[item]);
                        String Phone = DONERPHONE;
                        Report rep = new Report();
                        rep.execute(Name, Phone);
                    }
                });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                alert = builder.create();
                alert.show();
                break;
            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Hay, Check it out. \nHe/She Donating a Blood. Please Contact now \nName : " + DONERNAME + "\nContact Number : " + DONERPHONE + "\nBlood Group: " + DONERBLOOD + "\nPowered by PratimaSeva.in ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "PratimaSeva");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                break;
        }
    }

    public class UserDataProfile extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(DonorPublicProfileActivity.this);
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
                data = "?ph_no=" + URLEncoder.encode(pd_phone, "UTF-8");
                link = Constants.URL+"UserData.php" + data;
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
            String err = null;
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("doner_data");
                DONERNAME = user_data.getString("name");
                DONERPHONE = user_data.getString("ph_no");
                DONEREMAIL = user_data.getString("email_id");
                DONERBLOOD = user_data.getString("blood_gp");
                DONERADDRESS = user_data.getString("address_1");
                DONERCITY = user_data.getString("city");
                DONERSTATE = user_data.getString("state");
                DONERPINCODE = user_data.getString("pincode");
                DONERIMAGE = user_data.getString("file");
                name.setText(DONERNAME);
                phone.setText(DONERPHONE);
                email.setText(DONEREMAIL);
                blood.setText("Blood Group : " + DONERBLOOD);
                pincode.setText("Pincode : " + DONERPINCODE);
                Picasso.with(DonorPublicProfileActivity.this).load(DONERIMAGE).into(user_photo);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitle(DONERNAME);

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
                Toast.makeText(DonorPublicProfileActivity.this, err, Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String pd_blood = getIntent().getExtras().getString("pd_blood");
        String pd_time = getIntent().getExtras().getString("pd_time");
        String pd_pincode = getIntent().getExtras().getString("pd_pincode");
        Intent myIntent = new Intent(getApplicationContext(), DonorlistActivity.class);
        myIntent.putExtra("p_blood", pd_blood);
        myIntent.putExtra("p_time", pd_time);
        myIntent.putExtra("p_pincode", pd_pincode);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public class Report extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(DonorPublicProfileActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String r_name = params[0];
            String r_ph_no = params[1];

            String data = "";
            int tmp;

            try {
                URL url = new URL(Constants.URL+"report.php");
                String urlParams = "report=" + r_name + "&ph_no=" + r_ph_no;

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
                s = "Report Successfully Send.";
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                alert.dismiss();
            }
        }

    }

}