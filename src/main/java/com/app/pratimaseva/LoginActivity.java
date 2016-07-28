package com.app.pratimaseva;

import android.app.DatePickerDialog;
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
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    EditText etBDate,etPhno;
    Button btnlogin;
    String ph_no,birth_date;
    Context ctx=this;
    String DONERID=null,DONERNAME=null,DONERPHONE=null,DONEREMAIL=null,DONERBIRTH=null,DONERBLOOD=null,DONERTIME=null,DONERADDRESS=null,DONERCITY=null,DONERPINCODE=null,DONERSTATE=null,DONERFILE=null,DONERCOUNTRY=null;
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        etBDate = (EditText) findViewById(R.id.oldbirth_date);
        etPhno = (EditText) findViewById(R.id.oldph_no);
        btnlogin = (Button) findViewById(R.id.donerlogin);
        etBDate.setOnFocusChangeListener(this);
        btnlogin.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.donerlogin:
                String ph_no = etPhno.getText().toString();
                String birth_date = etBDate.getText().toString();
                if(TextUtils.isEmpty(ph_no)) {
                    Toast.makeText(getApplicationContext(),"Phone Number cannot be Empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!isValidMobile(ph_no)) {
                    Toast.makeText(getApplicationContext(),"Phone Number is Invalid",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(birth_date)) {
                    Toast.makeText(getApplicationContext(),"Birth Date Cannot be Empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    LoginBackGround login = new LoginBackGround();
                    login.execute(ph_no, birth_date);
                }
                break;
        }
    }

    private boolean isValidMobile(String Phone)
    {
        if (Phone.length() < 10 || Phone.length() > 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(Phone).matches();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // Process to get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        String formattedMonth = "" + month;
                        String formattedDayOfMonth = "" + dayOfMonth;

                        if(month < 10){

                            formattedMonth = "0" + month;
                        }
                        if(dayOfMonth < 10){

                            formattedDayOfMonth = "0" + dayOfMonth;
                        }
                        etBDate.setText(year + "-" + formattedMonth + "-" + formattedDayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        if(hasFocus) {
            dpd.show();
        }
        else {
            dpd.hide();
        }
    }

    public class LoginBackGround extends AsyncTask<String, String, String>
    {
        private ProgressDialog progressDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            String ph_no = params[0];
            String birth_date = params[1];
            String data1="",data2="";
            int tmp;
            String link;
            BufferedReader bufferedReader;
            String result;

            try {
                data1 = "?ph_no=" + URLEncoder.encode(ph_no, "UTF-8");
                data2 = "&birth_date=" + URLEncoder.encode(birth_date, "UTF-8");
                link = Constants.URL+"login.php"+data1+data2;
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
                DONERID = user_data.getString("doner_id");
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

                SharedPreferences DonerInfo = getSharedPreferences("DonerInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor edDonerInfo = DonerInfo.edit();
                edDonerInfo.putString("doner_id", DONERID);
                edDonerInfo.putString("doner_name", DONERNAME);
                edDonerInfo.putString("doner_ph", DONERPHONE);
          /*      edDonerInfo.putString("doner_email", DONEREMAIL);
                edDonerInfo.putString("doner_blood", DONERBLOOD);
                edDonerInfo.putString("doner_birth", DONERBIRTH);
                edDonerInfo.putString("doner_time", DONERTIME);
                edDonerInfo.putString("doner_add", DONERADDRESS);
                edDonerInfo.putString("doner_city", DONERCITY);
                edDonerInfo.putString("doner_pin", DONERPINCODE);
                edDonerInfo.putString("doner_state", DONERSTATE);
                edDonerInfo.putString("doner_country", DONERCOUNTRY);
                edDonerInfo.putString("doner_file", DONERFILE);  */
                edDonerInfo.commit();
                Toast.makeText(ctx, "Successfully Login...", Toast.LENGTH_LONG).show();
                Intent Login = new Intent(LoginActivity.this,DonerActivity.class);
                startActivity(Login);
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
                Toast.makeText(ctx, err, Toast.LENGTH_LONG).show();
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
