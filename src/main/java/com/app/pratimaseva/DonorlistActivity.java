package com.app.pratimaseva;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorlistActivity extends AppCompatActivity  {

    private RecyclerView myrcview;
    private AdapterData mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donorlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String blood = getIntent().getExtras().getString("p_blood");
        String time = getIntent().getExtras().getString("p_time");
        String pincode = getIntent().getExtras().getString("p_pincode");
        new AsyncLogin().execute(blood,time,pincode);
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(DonorlistActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("Loading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String blood = params[0];
            String time = params[1];
            String pincode = params[2];
            String link;
            String data1,data2,data3;
            try {
                data1 = "?blood=" + URLEncoder.encode(blood, "UTF-8");
                data2 = "&time=" + URLEncoder.encode(time, "UTF-8");
                data3 = "&pincode=" + URLEncoder.encode(pincode, "UTF-8");
                link = Constants.URL+"app_donorlist.php" + data1 + data2 +data3;
                url = new URL(link);
                conn = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                return (result.toString());

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            List<DataUser> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataUser UserData = new DataUser();
                    UserData.Image= json_data.getString("file");
                    UserData.Name= json_data.getString("name");
                    UserData.Blood= json_data.getString("blood_gp");
                    UserData.Location= json_data.getString("ph_no");
                    data.add(UserData);
                }

                // Setup and Handover data to recyclerview
                myrcview = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterData(DonorlistActivity.this, data);
                myrcview.setAdapter(mAdapter);
                myrcview.setLayoutManager(new LinearLayoutManager(DonorlistActivity.this));

            } catch (JSONException e) {
                Toast.makeText(DonorlistActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }



    }
    public class AdapterData extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private LayoutInflater inflater;
        List<DataUser> data= Collections.emptyList();

        // create constructor to innitilize context and data sent from MainActivity
        public AdapterData(Context context, List<DataUser> data){
            this.context=context;
            inflater= LayoutInflater.from(context);
            this.data=data;
        }

        // Inflate the layout when viewholder created
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=inflater.inflate(R.layout.donorlistitem, parent,false);
            MyHolder holder=new MyHolder(view);
            return holder;
        }

        // Bind data
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            MyHolder myHolder= (MyHolder) holder;
            DataUser current=data.get(position);
            myHolder.Name.setText(current.Name);
            myHolder.Blood.setText(current.Blood);
            myHolder.Location.setText(current.Location);

            Picasso.with(context).load(current.Image).into(myHolder.Image);


        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView Name;
            CircleImageView Image;
            TextView Blood;
            TextView Location;

            public MyHolder(View itemView) {
                super(itemView);
                CardView cardView = (CardView) itemView.findViewById(R.id.card_view);
                Name= (TextView) itemView.findViewById(R.id.DonorlName);
                Image= (CircleImageView) itemView.findViewById(R.id.DonorlImage);
                Blood = (TextView) itemView.findViewById(R.id.DonorlBlood);
                Location = (TextView) itemView.findViewById(R.id.DonorlLocation);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                String phone = Location.getText().toString();
                Intent intent = new Intent(DonorlistActivity.this,DonorPublicProfileActivity.class);
                String blood = getIntent().getExtras().getString("p_blood");
                String time = getIntent().getExtras().getString("p_time");
                String pincode = getIntent().getExtras().getString("p_pincode");
                intent.putExtra("pd_phone", phone);
                intent.putExtra("pd_blood", blood);
                intent.putExtra("pd_time", time);
                intent.putExtra("pd_pincode", pincode);
                startActivity(intent);
            }
        }
    }

    public class DataUser {

        public String Image;
        public String Name;
        public String Blood;
        public String Location;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}

