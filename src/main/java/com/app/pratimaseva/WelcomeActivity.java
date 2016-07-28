package com.app.pratimaseva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (Connectionstatus.getInstance(this).isOnline())
        {
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    SharedPreferences DonerInfo = getSharedPreferences("DonerInfo", MODE_PRIVATE);
                    String strPref = DonerInfo.getString("doner_id", null);
                    if(strPref == null) {
                        Intent Login = new Intent(WelcomeActivity.this,MainActivity.class);
                        startActivity(Login);
                    }
                    else{
                        Intent intent = new Intent(WelcomeActivity.this,DonerActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        timerThread.start();
        }
        else
        {
            Toast.makeText(this,"Your Internet Connection is not working,\nPlease Check Internet Connection...!!!",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
