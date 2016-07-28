package com.app.pratimaseva;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class JsonParserCity {

    public static ArrayList<String> Parse(String json){
        try {
            ArrayList<String> CityList = new ArrayList<>();
            JSONObject jsonResponse = new JSONObject(json);
            JSONArray cast = jsonResponse.getJSONArray("city");
            for (int i=0; i<cast.length(); i++) {
                JSONObject actor = cast.getJSONObject(i);
                CityList.add(actor.getString("city_name"));
            }
            return CityList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
