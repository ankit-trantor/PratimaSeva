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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.ganfra.materialspinner.MaterialSpinner;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    EditText etDate;
    int Valor;
    private int mYear, mMonth, mDay;
    EditText Naame, Pphone, Eemail, Ppincode, Aaddress, etDaate;
    String Name, Phone, Email, Birth, State, Address, City, Blood, Time, Pincode, Id, Bllood, Tiime, Sttate, Ciity, Country;
    private Spinner state_Spinner, city_Spinner, Blood_Spinner, Time_Spinner, country_Spinner;
    CircleImageView imgView;
    private ArrayList<String> countryList;
    private JSONArray resultstates;
    private ArrayList<String> CityList;
    private ArrayList<String> StateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        etDaate = (EditText) findViewById(R.id.updatebirth_date);
        etDaate.setOnFocusChangeListener(this);

        countryList = new ArrayList<String>();
        StateList = new ArrayList<String>();
        CityList = new ArrayList<String>();

        imgView = (CircleImageView) findViewById(R.id.imgViewupdate);

        Naame = (EditText) findViewById(R.id.updatename);
        Pphone = (EditText) findViewById(R.id.updateph_no);
        Eemail = (EditText) findViewById(R.id.updateemail);
        Ppincode = (EditText) findViewById(R.id.updatepincode);
        Aaddress = (EditText) findViewById(R.id.updateaddress);
        state_Spinner = (Spinner) findViewById(R.id.updatestate);
        city_Spinner = (Spinner) findViewById(R.id.updatecity);
        Blood_Spinner = (Spinner) findViewById(R.id.upadteblood_gp);
        Time_Spinner = (Spinner) findViewById(R.id.updatetime);
        country_Spinner = (Spinner) findViewById(R.id.updatecountry);

        SharedPreferences DonerInfo = this.getSharedPreferences("DonerInfo", Context.MODE_PRIVATE);
        String name = DonerInfo.getString("doner_name", "doner_name");
        String ph_no = DonerInfo.getString("doner_ph", "doner_ph");
        String email = DonerInfo.getString("doner_email", "doner_email");
        String birth = DonerInfo.getString("doner_birth", "doner_birth");
        String address = DonerInfo.getString("doner_add", "doner_add");
        String pincode = DonerInfo.getString("doner_pin", "doner_pin");
        String file = DonerInfo.getString("doner_file", "doner_file");

        Naame.setText(name);
        Pphone.setText(ph_no);
        Eemail.setText(email);
        Ppincode.setText(pincode);
        Aaddress.setText(address);
        etDaate.setText(birth);
        Picasso.with(UpdateProfileActivity.this)
                .load(file)
                .into(imgView);

        String[] Time = {"AnyTime", "Morning", "Afternoon", "Evening"};
        String[] Blood = {"A Positive", "A Negative", "B Positive", "B Negative", "AB Positive", "AB Negative", "O Positive", "O Negative"};
        ArrayAdapter<String> Timeadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Time);

        Timeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Time_Spinner.setAdapter(Timeadapter);

        ArrayAdapter<String> Bloodadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Blood);
        Bloodadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Blood_Spinner.setAdapter(Bloodadapter);

        getCountry();

        country_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String country_name = (String) country_Spinner.getSelectedItem();
                StatesActivity sg = new StatesActivity(UpdateProfileActivity.this);
                sg.execute(country_name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        state_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String state_name = (String) state_Spinner.getSelectedItem();
                CityActivity sga = new CityActivity(UpdateProfileActivity.this);
                sga.execute(state_name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.updateprofile);
        fab.setOnClickListener(this);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image = new Intent(UpdateProfileActivity.this, ProfileUploadActivity.class);
                startActivity(image);
            }
        });
    }

    private void getCountry() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Constants.URL + "country.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            resultstates = j.getJSONArray("country");

                            //Calling method getStudents to get the students from the JSON Array
                            getStudents(resultstates);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                countryList.add(json.getString("country_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateProfileActivity.this, android.R.layout.simple_spinner_item, countryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_Spinner.setAdapter(adapter);
    }

    public class StatesActivity extends AsyncTask<String, Void, String> {

        private Context context;

        public StatesActivity(Context context) {
            this.context = context;
        }

        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(UpdateProfileActivity.this);
            progressDialog.setMessage("Loading States...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            String state = arg0[0];
            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?country=" + URLEncoder.encode(state, "UTF-8");
                link = Constants.URL + "states.php" + data;
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
            StateAdapter(s);
        }
    }

    public void StateAdapter(String json) {
        StateList = JsonParserState.Parse(json);
        ArrayAdapter<String> aaadapter = new ArrayAdapter<String>(UpdateProfileActivity.this, android.R.layout.simple_spinner_item, StateList);
        aaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_Spinner.setAdapter(aaadapter);
    }

    public class CityActivity extends AsyncTask<String, Void, String> {

        private Context context;

        public CityActivity(Context context) {
            this.context = context;
        }

        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(UpdateProfileActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            String state = arg0[0];
            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?state=" + URLEncoder.encode(state, "UTF-8");
                link = Constants.URL + "city.php" + data;
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
            CityAdapter(s);
        }
    }

    public void CityAdapter(String json) {
        CityList = JsonParserCity.Parse(json);
        ArrayAdapter<String> aadapter = new ArrayAdapter<String>(UpdateProfileActivity.this, android.R.layout.simple_spinner_item, CityList);
        aadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_Spinner.setAdapter(aadapter);
    }


    @Override
    public void onClick(View v) {

        SharedPreferences DonerInfo = this.getSharedPreferences("DonerInfo", Context.MODE_PRIVATE);
        String blood = DonerInfo.getString("doner_blood", "doner_blood");
        String time = DonerInfo.getString("doner_time", "doner_time");
        String city = DonerInfo.getString("doner_city", "doner_city");
        String state = DonerInfo.getString("doner_state", "doner_state");

        Name = Naame.getText().toString();
        Phone = Pphone.getText().toString();
        Email = Eemail.getText().toString();
        Birth = etDaate.getText().toString();
        Address = Aaddress.getText().toString();
        Bllood = Blood_Spinner.getSelectedItem().toString();
        Country = country_Spinner.getSelectedItem().toString();
        if ("Blood Group".equals(Bllood)) {
            Blood = blood;
        } else {
            Blood = Bllood;
        }
        Tiime = Time_Spinner.getSelectedItem().toString();
        if ("Available Time".equals(Tiime)) {
            Time = time;
        } else {
            Time = Tiime;
        }
        Pincode = Ppincode.getText().toString();
        Sttate = state_Spinner.getSelectedItem().toString();
        if ("State".equals(Sttate)) {
            State = state;
        } else {
            State = Sttate;
        }
        Ciity = city_Spinner.getSelectedItem().toString();
        if ("City".equals(Ciity)) {
            City = city;
        } else {
            City = Ciity;
        }
        Id = DonerInfo.getString("doner_id", "doner_id");

        Register reg = new Register();
        reg.execute(Name, Phone, Email, Birth, Blood, Time, Address, Pincode, State, City, Id, Country);
    }

    public class Register extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(UpdateProfileActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String r_name = params[0];
            String r_ph_no = params[1];
            String r_email = params[2];
            String r_birth = params[3];
            String r_blood = params[4];
            String r_time = params[5];
            String r_address = params[6];
            String r_pincode = params[7];
            String r_state = params[8];
            String r_city = params[9];
            String r_id = params[10];
            String r_country = params[11];

            String data = "";
            int tmp;

            try {
                URL url = new URL(Constants.URL + "updateprofile.php");
                String urlParams = "name=" + r_name + "&phno=" + r_ph_no + "&email=" + r_email + "&dob=" + r_birth + "&blgp=" + r_blood + "&timing=" + r_time + "&address=" + r_address + "&pin=" + r_pincode + "&state=" + r_state + "&city=" + r_city + "&id=" + r_id + "&country=" + r_country;

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
                s = "Profile Updated Successfully.";
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent intentreg = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intentreg);
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

                        if (month < 10) {

                            formattedMonth = "0" + month;
                        }
                        if (dayOfMonth < 10) {

                            formattedDayOfMonth = "0" + dayOfMonth;
                        }
                        etDaate.setText(year + "-" + formattedMonth + "-" + formattedDayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        if (hasFocus) {
            dpd.show();
        } else {
            dpd.hide();
        }
    }

}
