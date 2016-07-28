package com.app.pratimaseva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class DonerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView username, useremail, userphone;
    CircleImageView userimage;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doner);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences DonerInfo = this.getSharedPreferences("DonerInfo", Context.MODE_PRIVATE);
        String ph_no = DonerInfo.getString("doner_ph", "doner_ph");

        UserData user = new UserData();
        user.execute(ph_no);

        Button btnreg = (Button) findViewById(R.id.bdhistory);
        Button btnfind = (Button) findViewById(R.id.findadonor);

        btnreg.setOnClickListener(this);
        btnfind.setOnClickListener(this);

        initUserNavigationDrawer();
    }

    public void initUserNavigationDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_viewuser);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.home:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.blooddonation:
                        drawerLayout.closeDrawers();
                        Intent bdonationtab = new Intent(DonerActivity.this, BloodDonationActivity.class);
                        startActivity(bdonationtab);
                        break;
                    case R.id.who:
                        drawerLayout.closeDrawers();
                        Intent whotab = new Intent(DonerActivity.this, WhocanActivity.class);
                        startActivity(whotab);
                        break;
                    case R.id.about:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.contact:
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });

        /*SharedPreferences DonerInfo = this.getSharedPreferences("DonerInfo", Context.MODE_PRIVATE);
        String name = DonerInfo.getString("doner_name", "doner_name");
        String ph_no = DonerInfo.getString("doner_ph", "doner_ph");
        String email = DonerInfo.getString("doner_email", "doner_email");
        String file = DonerInfo.getString("doner_file", "doner_file");  */

        View header = navigationView.getHeaderView(0);

        username = (TextView) header.findViewById(R.id.user_name);
        useremail = (TextView) header.findViewById(R.id.user_email);
        userphone = (TextView) header.findViewById(R.id.user_phone);
        userimage = (CircleImageView) header.findViewById(R.id.profile_image);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.userdrawer_open, R.string.userdrawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public class UserData extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(DonerActivity.this);
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
                data = "?ph_no=" + URLEncoder.encode(ph_no, "UTF-8");
                link = Constants.URL + "UserData.php" + data;
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
                String DONERID = user_data.getString("doner_id");
                String DONERIEMAIL = user_data.getString("email_id");
                String DONERPH = user_data.getString("ph_no");
                String DONERNAME = user_data.getString("name");
                String DONERFILE = user_data.getString("file");

                Picasso.with(DonerActivity.this)
                        .load(DONERFILE)
                        .into(userimage);
                username.setText(DONERNAME);
                useremail.setText(DONERIEMAIL);
                userphone.setText(DONERPH);
                drawerLayout = (DrawerLayout) findViewById(R.id.draweruser);

                SharedPreferences DonerInfo = getSharedPreferences("DonerInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor edDonerInfo = DonerInfo.edit();
                edDonerInfo.putString("doner_id", DONERID);
                edDonerInfo.putString("doner_name", DONERNAME);
                edDonerInfo.putString("doner_ph", DONERPH);
                edDonerInfo.putString("doner_email", DONERIEMAIL);
                edDonerInfo.putString("doner_file", DONERFILE);
                edDonerInfo.commit();

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
                Toast.makeText(DonerActivity.this, err, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_profile:
                Intent login = new Intent(DonerActivity.this, UserProfileActivity.class);
                startActivity(login);
                break;
            case R.id.Logout:
                logout();
                break;
            case R.id.exit:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences DonerInfo = getSharedPreferences("DonerInfo", MODE_PRIVATE);
                        SharedPreferences.Editor edDonerInfo = DonerInfo.edit();
                        edDonerInfo.clear();
                        edDonerInfo.commit();
                        String strPref = DonerInfo.getString("doner_id", null);
                        if (strPref == null) {
                            Intent Login = new Intent(DonerActivity.this, MainActivity.class);
                            startActivity(Login);
                        } else {
                            Intent intent = new Intent(DonerActivity.this, DonerActivity.class);
                            startActivity(intent);
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bdhistory:
                Intent his = new Intent(DonerActivity.this, DonorHistoryActivity.class);
                startActivity(his);
                break;
            case R.id.findadonor:
                Intent find = new Intent(DonerActivity.this, FindActivity.class);
                startActivity(find);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
