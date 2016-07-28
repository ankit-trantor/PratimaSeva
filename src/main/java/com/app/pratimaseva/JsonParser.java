package com.app.pratimaseva;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dharmik on 10-07-2016.
 */
public class JsonParser {

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
        }

        return outputDate;

    }

    public static List<String> Parse(String json){
        try {
            List<String> classList = new ArrayList<>();
            JSONObject jsonResponse = new JSONObject(json);
            JSONArray cast = jsonResponse.getJSONArray("city");
            for (int i=0; i<cast.length(); i++) {
                JSONObject actor = cast.getJSONObject(i);
                String date = actor.getString("donated_date");
                String date_after = formateDateFromstring("yyyy-MM-dd", "dd, MMM yyyy", date);
                classList.add(date_after);
            }
            return classList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

