package com.app.pratimaseva;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Button btnreg,btnfind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnreg = (Button) findViewById(R.id.register);
        btnfind = (Button) findViewById(R.id.finddonor);

        btnreg.setOnClickListener(this);
        btnfind.setOnClickListener(this);
        initNavigationDrawer();
        SharedPreferences DonerInfo = getSharedPreferences("DonerInfo", MODE_PRIVATE);
        String strPref = DonerInfo.getString("doner_ph", null);
        if(strPref != null) {
            Intent Login = new Intent(MainActivity.this,DonerActivity.class);
            startActivity(Login);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register:
                Intent reg = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(reg);
                break;
            case R.id.finddonor:
                Intent find = new Intent(MainActivity.this,FindActivity.class);
                startActivity(find);
                break;
        }
    }

    // Navigation Drawer

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.blooddonation:
                        drawerLayout.closeDrawers();
                        Intent bdonationtab = new Intent(MainActivity.this,BloodDonationActivity.class);
                        startActivity(bdonationtab);
                        break;
                    case R.id.who:
                        drawerLayout.closeDrawers();
                        Intent whotab = new Intent(MainActivity.this,WhocanActivity.class);
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
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                Intent login = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(login);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
